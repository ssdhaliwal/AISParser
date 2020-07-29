package elsu.ais.parser;

import java.io.*;
import java.util.*;

import elsu.common.*;
import elsu.support.*;

public class AISParser {
	private void parseMessageFile(ConfigLoader config, InputStream stream, String fileOut) {
		ArrayList<String> result = new ArrayList();

		BufferedReader reader = null;
		try {

			// InputStream (is) must be set and initialized before
			reader = new BufferedReader(new InputStreamReader(stream));

			String line;
			AISSentence message = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}

				try {
					if (message != null) {
						message = AISSentence.fromString(line, message);
					} else {
						message = AISSentence.fromString(line);
					}
					
					System.out.println(message);
					if (message.isComplete() && message.isValid()) {
						message = null;
					}
				} catch (Exception ex) {
					System.out.println("!ERROR!; " + ex.getMessage() + " \n" + line);
				}
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
				FileUtils.writeFile(fileOut, result, false);
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
}
