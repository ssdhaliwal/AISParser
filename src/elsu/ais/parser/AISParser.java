package elsu.ais.parser;

import java.io.*;
import java.util.*;

import elsu.ais.parser.sentence.AISSentenceFactory;
import elsu.ais.parser.sentence.IEventListener;
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
		result.add("error, " + ex.getMessage() + ", " + o + ", " + message);
	}

	@Override
	public void onComplete(Object o) {
		result.add("complete, " + o);
	}

	@Override
	public void onUpdate(Object o) {
		result.add("update, " + o);
	}

	public AISSentenceFactory getSentenceFactory() {
		return sentenceFactory;
	}
	
	private AISSentenceFactory sentenceFactory = new AISSentenceFactory();
}
