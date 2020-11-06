package elsu.parser.connector;

import java.util.*;
import java.util.regex.Matcher;

import elsu.common.CollectionUtils;
import elsu.common.FileUtils;
import elsu.sentence.SentenceBase;
import elsu.support.ConfigLoader;

public class StreamFileConnector extends ConnectorBase {

	public StreamFileConnector(ConfigLoader config, String connName) throws Exception {
		super();
		initialize(config, connName);
	}

	private void initialize(ConfigLoader config, String connName) throws Exception {
		// load the config params else override from constructor
		max_threads = 10;
		max_threads = Integer.parseInt(config.getProperty("application.services.key.processing.threads").toString());

		// initialize the threadpool from bass class
		initializeThreadPool(max_threads);

		// load the config params else override from constructor
		this.filename = config.getProperty("application.services.service." + connName + ".attributes.key.filename")
				.toString();
		this.interval = Integer.parseInt(
				config.getProperty("application.services.service." + connName + ".attributes.key.interval").toString());
		if (config.getProperty("application.services.service." + connName + ".attributes.key.loop").toString()
				.equals("true")) {
			this.loop = true;
		}

		System.out.println(getClass().toString() + ", StreamFileConnector(), " + "client config loaded, " + "filename: "
				+ this.filename + ", " + "speed: " + this.interval);
	}

	public void sendMessage(ArrayList<String> messages) throws Exception {
		recordCounter++;
		super.sendMessage(messages);
	}

	@Override
	public void run() {
		try {
			// read the file in memory
			ArrayList<String> fileData = FileUtils.readFileToList(filename);

			// until shutdown
			String line = null, sentence = "", message[] = null;
			ArrayList<String> messages = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			Matcher hMatch = null;
			int fileSize = fileData.size(), i = 0, systemGCCounter = 0, lineSize = 0;
			while (!Thread.currentThread().isInterrupted() && !isShutdown) {
				// loop and send file data
				if (i < fileSize) {
					if (lineSize == 0) {
						line = fileData.get(i);
					} else {
						line = lines.remove(0);
						lineSize--;
					}

					// if line is an array; separate it and parse it
					if (line.startsWith("[") && line.endsWith("]")) {
						line = line.substring(1, line.length() - 1);

						lines.clear();
						for (String l : line.split(" ")) {
							if (!l.isEmpty()) {
								if (l.endsWith(",")) {
									l = l.substring(0, l.length());
								}
								lines.add(l);
							}
						}

						lineSize = lines.size();
					} else if ((line != null) && (!line.isEmpty())) {
						// increment record trackers
						lifetimeCounter++;

						// process the message and fire the events
						try {
							int totalMsgNbr = 0, fragmentNbr = 0;
							if (line.matches("(?s).*!..VD[OM].*")) {
								hMatch = SentenceBase.messageVDOPattern.matcher(line);
								while (hMatch.find()) {
									sentence = hMatch.group(0);

									// if complete message
									message = sentence.split(",");
									totalMsgNbr = Integer.valueOf(message[1]);
									fragmentNbr = Integer.valueOf(message[2]);
									if (fragmentNbr <= totalMsgNbr) {
										messages.add(sentence);

										if (messages.size() == totalMsgNbr) {
											sendMessage(messages);
											messages = new ArrayList<String>();
										} else if (fragmentNbr > messages.size()) {
											sendError("partial fragment, pending queue cleared, ["
													+ CollectionUtils.ArrayListToString(messages) + "]");
											messages = new ArrayList<String>();
										}
									} else if (fragmentNbr == 1) {
										if (messages.size() > 0) {
											sendError("partial fragment, pending queue cleared, ["
													+ CollectionUtils.ArrayListToString(messages) + "]");
											messages = new ArrayList<String>();
										}
										messages.add(sentence);
									}
								}
							}

							systemGCCounter++;
							if (systemGCCounter >= 50000) {
								systemGCCounter = 0;
								System.gc();

								System.out.println(">> queue count / " + getMessageQueue().size() + " of "
										+ recordCounter + " (" + lifetimeCounter + ") << / " + getDateTimeUTC());
							}

							Thread.yield();
						} catch (Exception exi) {
							sendError("client collector error, sending message, (" + line + "), " + exi.getMessage());
						}
					}
				} else {
					if (loop) {
						i = -1;
					} else {
						isShutdown = true;
					}
				}

				Thread.sleep(interval);
				if (lineSize == 0) {
					i++;
				}
			}
		} catch (Exception ex) {
			// log error for tracking
			try {
				sendError("file, " + ex.getMessage());
			} catch (Exception exi) {
				System.out.println(getClass().toString() + ", run(), " + "file connector, " + exi.getMessage());
			}
		} finally {
			isShutdown = true;
			sendTermination();

			// log message
			try {
				sendMessage("file closed - shutdown.");
			} catch (Exception ex2) {
				System.out.println(getClass().toString() + ", run(), " + "file connector-2, " + ex2.getMessage());
			}
		}
	}

	private int max_threads = 10;

	public String filename = "";
	public int interval = 100;
	public boolean loop = false;

	private long recordCounter = 0L;
	private long lifetimeCounter = 0L;

	public boolean isShutdown = false;
}
