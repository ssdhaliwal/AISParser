package elsu.parser.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;

import elsu.common.CollectionUtils;
import elsu.sentence.SentenceBase;
import elsu.support.ConfigLoader;
import elsu.support.Log4JManager;

public class StreamSocketConnector extends ConnectorBase {

	public String hostUri = "";
	public int hostPort = 0;
	public int noDataTimeout = 30000;
	public int retryWaitTime = 5000;
	public String siteId = "306";
	public String siteName = "SITESVR1";
	public boolean isShutdown = false;
	
	private boolean isRunning = false;
	private boolean isMonitorRunning = false;
	private long recordCounter = 0L;
	private long lifetimeCounter = 0L;
	private long monitorRecordCounter = 0L;
	private Socket clientSocket = null;

	public String logConfig = "";
	public String logPath = "";
	public String logClass = "";
	private Log4JManager log4JManager = null;
	
	private int max_threads = 10;
	
	public StreamSocketConnector(ConfigLoader config, String connName) throws Exception {
		super();
		
		// load the config params else override from constructor
		max_threads = 10;
		max_threads = Integer.parseInt(config.getProperty("application.services.key.processing.threads").toString());
		
		// initialize the threadpool from bass class
		initializeThreadPool(max_threads);
		
		hostUri = config.getProperty("application.services.service." + connName + ".attributes.key.site.host").toString();
		hostPort = Integer.parseInt(config.getProperty("application.services.service." + connName + ".attributes.key.site.port").toString());
		noDataTimeout = Integer.parseInt(config.getProperty("application.services.service." + connName + ".attributes.key.monitor.noDataTimeout").toString());
		retryWaitTime = Integer.parseInt(config.getProperty("application.services.service." + connName + ".attributes.key.monitor.idleTimeout").toString());
		logPath = config.getProperty("application.framework.attributes.key.log.path").toString();
		siteId = config.getProperty("application.services.service." + connName + ".attributes.key.site.id").toString();
		siteName = config.getProperty("application.services.service." + connName + ".attributes.key.site.name").toString();
		
		System.out.println(getClass().toString() + ", StreamNetworkConnector(), " + "client config loaded, " +
			"hostUri: " + hostUri + ", " +
			"hostPort: " + hostPort + ", " +
			"noDataTimeout: " + noDataTimeout + ", " +
			"retryWaitTime: " + retryWaitTime + ", " +
			"logPath: " + logPath + ", " +
			"siteId: " + siteId + ", " +
			"siteName: " + siteName );

		// initialize logger for connector
		String logConfig = config.getProperty("application.framework.attributes.key.log.config").toString();
		logClass = config.getProperty("application.services.service." + connName + ".attributes.key.log.class").toString();

		log4JManager = new Log4JManager(logConfig, 
				logClass, logPath + siteId + "_" + siteName + ".log");
	}

	public void sendError(String error) throws Exception {
		log4JManager.getLogger().error(error);
		super.sendError(error);
	}

	public void sendMessage(ArrayList<String> messages) throws Exception {
		recordCounter++;

		log4JManager.getLogger().info(messages);
		super.sendMessage(messages);
	}

	@Override
	public void run() {
		try {
			Thread tMonitor = null;

			while (!Thread.currentThread().isInterrupted() && !isShutdown) {
				// if socket is not running, try to start it
				if (!isRunning) {
					try {
						// create socket to the equipment
						clientSocket = new Socket(hostUri, hostPort);
						isRunning = true;
					} catch (Exception exi) {
						sendError("client socket error, " + exi.getMessage());
					} finally {
						if (!isRunning && !isShutdown) {
							try {
								Thread.sleep(retryWaitTime);
							} catch (Exception exi) {
							}
						}
					}
				}

				// if thread monitor is not running, try to start it
				if (!isMonitorRunning && !isShutdown) {
					monitorRecordCounter = 0L;

					// start no data monitor thread
					tMonitor = new Thread(new Runnable() {
						@Override
						public void run() {
							isMonitorRunning = true;
							
							System.out.println("client monitor started...");
							while (isRunning && isMonitorRunning && !isShutdown) {
								try {
									// yield processing to other threads
									// for specified
									// time, any exceptions are ignored
									try {
										monitorRecordCounter = 0L;
										Thread.sleep(noDataTimeout);
									} catch (Exception exi) {
									}

									// check the data count
									if (monitorRecordCounter == 0L) {
										// force close the connections and
										// restart
										try {
											sendError("client monitor error, no data received, resetting connection...");
											clientSocket.close();
										} catch (Exception exi) {
										} finally {
											clientSocket = null;
											isRunning = false;
										}
									}
								} catch (Exception exi) {
									isMonitorRunning = false;
									try {
										sendError("client monitor error, " + exi.getMessage());
									} catch (Exception ex) {
										System.out.println(getClass().toString() + ", run(), " + "network monitor-2, " + ex.getMessage());
									}
								}
							}
							
							System.out.println("client monitor stopped...");
							isMonitorRunning = false;
						}
					});

					// start the thread to create connection for the service.
					tMonitor.start();
				}
				
				// start the connection data collection
				if (isRunning && !isShutdown) {
					// local parameter for reader thread access, passes the
					// socket out
					// stream
					final PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

					// this is to prevent socket to stay open after error
					String line = null, sentence = "", message[] = null;
					ArrayList<String> messages = new ArrayList<String>();
					Matcher hMatch = null;
					int systemGCCounter = 0;
					try (BufferedReader in = new BufferedReader(
							  new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {
						
						while (isRunning && !isShutdown) {
							// read line from the socket stream
							line = in.readLine();

							if ((line != null) && (!line.isEmpty())) {
								// increment record trackers
								lifetimeCounter++;
								monitorRecordCounter++;
								
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
										
										System.out.println(">> queue count / " + getMessageQueue().size() + " of " + recordCounter + " (" + lifetimeCounter + ") <<");
									}

									Thread.yield();
								} catch (Exception exi) {
									sendError("client collector error, sending message, (" + line + "), " + exi.getMessage());
								}
							}
							
							line = null;
						}
					} catch (Exception ex) {
						// log error for tracking
						isRunning = false;
						try {
							sendError("client collector error, " + ex.getMessage());
						} catch (Exception exi) {
							sendError("client collector error, sending error, " + exi.getMessage());
						}
					} finally {
						// close out all open in/out streams.
						try {
							try {
								out.flush();
							} catch (Exception exi) {
							}
							out.close();
						} catch (Exception exi) {
						}
					}
				}
			}
		} catch (Exception ex) {
			// log error for tracking
			try {
				sendError("client socket error, " + ex.getMessage());
			} catch (Exception ex2) {
				System.out.println(getClass().toString() + ", run(), " + "network connector, " + ex2.getMessage());
			}
		} finally {
			isShutdown = true;
			isRunning = false;

			// log message
			sendTermination();
			try {
				sendMessage("client socket closed - shutdown.");
			} catch (Exception ex2) {
				System.out.println(getClass().toString() + ", run(), " + "network connector-2, " + ex2.getMessage());
			}
		}
	}
}
