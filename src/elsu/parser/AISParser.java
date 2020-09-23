package elsu.parser;

import java.io.*;
import java.util.*;

import elsu.base.IAISEventListener;
import elsu.common.*;
import elsu.nmea.messages.NMEAMessage;
import elsu.sentence.SentenceFactory;
import elsu.sentence.Sentence;
import elsu.sentence.SentenceBase;
import elsu.support.*;

public class AISParser implements IAISEventListener {
	ArrayList<Object> result = new ArrayList<Object>();
	ArrayList<Integer> resultType = new ArrayList<Integer>();

	public AISParser() {
		getSentenceFactory().addEventListener(this);
	}

	private void parseMessageFile(ConfigLoader config, InputStream stream, String fileOut, boolean collate) {

		BufferedReader reader = null;
		try {
			System.out.println(".. parser start .." + (new Date()));

			// InputStream (is) must be set and initialized before
			reader = new BufferedReader(new InputStreamReader(stream));

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}

				getSentenceFactory().parseSentence(line);
			}
		} catch (Exception ex) {
			// Do something
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				// Do something
			}

			// save the output to file
			try {
				System.out.println(".. parser complete .." + (new Date()));
				saveRecordsToFile(fileOut, collate);
				System.out.println("Total Records: " + result.size());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private void saveRecordsToFile(String fileOut, boolean collate) throws Exception {
		System.out.println(".. file output start .." + (new Date()));

		String fileExt = "";
		int index = fileOut.lastIndexOf(".");
		if (index != -1) {
			fileExt = fileOut.substring(index);
		}
		fileOut = fileOut.replaceAll(fileExt, "");

		System.out.println(".. file output msg x start .." + (new Date()));
		if (!collate) {
			FileUtils.writeFile(fileOut + fileExt, result, false);
		} else {
			for (int i = 0; i < result.size(); i++) {
				FileUtils.writeFile(fileOut + "_" + resultType.get(i) + fileExt, (String)result.get(i) + "\n", false);
			}
		}
		System.out.println(".. file output msg x complete .." + (new Date()));
	}

	@Override
	public void onAISError(Exception ex, Object o, String message) {
		try {
			if (SentenceBase.logLevel >= 2) {
				System.out.println("error, " + ex.getMessage() + ", " + o + ", " + message);
			}
			result.add(ex.getMessage() + ", " + o + ", " + message);
			resultType.add(0);
		} catch (Exception exi) {
			System.out.println("message error notification exception; " + message);
		}
	}

	@Override
	public void onAISComplete(Object o) {
		try {
			if (SentenceBase.logLevel >= 4) {
				System.out.println("complete, " + o);
			}
			result.add(o.toString());
			if (o instanceof Sentence) {
				resultType.add(((Sentence) o).getAISMessage().getType());
			} else {
				resultType.add(99);
			}
		} catch (Exception exi) {
			System.out.println("message complete notification exception; ");
		}
	}

	@Override
	public void onAISUpdate(Object o) {
		try {
			if (SentenceBase.logLevel >= 4) {
				System.out.println("update, " + o);
			}
			result.add(o.toString());
			if (o instanceof Sentence) {
				resultType.add(((Sentence) o).getAISMessage().getType());
			} else {
				resultType.add(99);
			}
		} catch (Exception exi) {
			System.out.println("message update notification exception; ");
		}
	}

	public SentenceFactory getSentenceFactory() {
		return sentenceFactory;
	}

	private SentenceFactory sentenceFactory = new SentenceFactory();

	public static void main(String[] args) throws Exception {
		class parseArgs {
			boolean parseArg(String arg) {
				if (arg.startsWith("debug")) {
					parseDebugArg(arg);
				} else 
				if (arg.startsWith("collate")) {
					return parseCollateArg(arg);
				}

				return false;
			}

			void parseDebugArg(String arg) {
				if (arg.equals("debug") || arg.equals("debug=1") || arg.equals("debug=on") || arg.equals("debug=true")) {
					SentenceBase.logLevel = 6;
				} else if (arg.equals("debuglevel=all")) {
					SentenceBase.logLevel = 6;
				} else if (arg.equals("debuglevel=debug")) {
					SentenceBase.logLevel = 5;
				} else if (arg.equals("debuglevel=info")) {
					SentenceBase.logLevel = 4;
				} else if (arg.equals("debuglevel=warn")) {
					SentenceBase.logLevel = 3;
				} else if (arg.equals("debuglevel=error")) {
					SentenceBase.logLevel = 2;
				} else if (arg.equals("debuglevel=fatal")) {
					SentenceBase.logLevel = 1;
				} else if (arg.equals("debuglevel=off")) {
					SentenceBase.logLevel = 0;
				}
			}

			boolean parseCollateArg(String arg) {
				if (arg.equals("collate=true") || arg.equals("collate=on") || arg.equals("collate")) {
					return true;
				}
				
				return false;
			}
		}
		
		ConfigLoader config = null;
		try {
			config = new ConfigLoader("config/app.config", null);
		} catch (Exception ex) {
			throw new Exception("config resource not found: config/app.config");
		}

		AISParser aisparser = new AISParser();
		String path, fileIn, fileOut, tArg;
		boolean collate = false;

		if (args.length < 3) {
			throw new Exception(
					"invalid arguments: java -jar AISParser.jar path file-in file-out [debug/debug=on/debug=true/debug=1/debugLevel=all/debug/info/warn/error/fatal/off] [collate/collate=on/1/true]");
		} else {
			path = args[0];
			fileIn = args[1];
			fileOut = args[2];

			System.out.println(".. start .." + (new Date()));
			for (int i = 3; i < args.length; i++){
				tArg = args[i].toLowerCase();
				boolean result = new parseArgs().parseArg(tArg);
				
				if (tArg.startsWith("collate")) {
					collate = result;
				}
			}
		}

		try {
			File initialFile = new File(path + "/" + fileIn);
			InputStream targetStream = new FileInputStream(initialFile);

			aisparser.parseMessageFile(config, targetStream, path + "/" + fileOut, collate);
			System.out.println(".. complete .." + (new Date()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
