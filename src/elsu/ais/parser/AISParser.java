package elsu.ais.parser;

import java.io.*;
import java.util.*;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.IEventListener;
import elsu.ais.parser.sentence.AISSentenceFactory;
import elsu.common.*;
import elsu.support.*;

public class AISParser implements IEventListener {
	ArrayList<String> result = new ArrayList();

	public AISParser() {
		getSentenceFactory().addEventListener(this);
	}
	
	private void parseMessageFile(ConfigLoader config, InputStream stream, String fileOut) {

		BufferedReader reader = null;
		try {

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
				FileUtils.writeFile(fileOut, result, true);
				System.out.println("Total Records: " + result.size());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigLoader config = null;
		try {
			config = new ConfigLoader("config/app.config", null);
		} catch (Exception ex) {
			throw new Exception("config resource not found: config/app.config");
		}

		AISParser aisparser = new AISParser();
		String path, fileIn, fileOut;

		if (args.length != 3) {
			throw new Exception("invalid arguments: java -jar AISParser.jar path file-in file-out");
		} else {
			path = args[0];
			fileIn = args[1];
			fileOut = args[2];
			
			if (args.length == 4) {
				if (args[4].toLowerCase().equals("debug") || args[4].toLowerCase().equals("true")) {
					AISBase.debug = true;
				}
			}
		}

		try {
			File initialFile = new File(path + "/" + fileIn);
			InputStream targetStream = new FileInputStream(initialFile);
			
			aisparser.parseMessageFile(config, targetStream, path + "/" + fileOut);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void onError(Exception ex, Object o, String message) {
		try {
			System.out.println("error, " + ex.getMessage() + ", " + o + ", " + message);
			result.add("error, " + ex.getMessage() + ", " + o + ", " + message);
		} catch (Exception exi) {
			System.out.println("message error notification exception; " + message);
		}
	}

	@Override
	public void onComplete(Object o) {
		try {
			System.out.println("complete, " + o);
			result.add("complete, " + o);
		} catch (Exception exi) {
			System.out.println("message complete notification exception; ");
		}
	}

	@Override
	public void onUpdate(Object o) {
		try {
			System.out.println("update, " + o);
			result.add("update, " + o);
		} catch (Exception exi) {
			System.out.println("message update notification exception; ");
		}
	}

	public AISSentenceFactory getSentenceFactory() {
		return sentenceFactory;
	}
	
	private AISSentenceFactory sentenceFactory = new AISSentenceFactory();
}
