package elsu.ais.parser;

import java.io.*;
import java.util.*;

import elsu.ais.parser.messages.*;
import elsu.ais.parser.messages.dataparser.Type8_Dac1_Fid11;
import elsu.ais.parser.resources.AISMessageDecoders;
import elsu.common.*;
import elsu.support.*;

public class AISParser {
	private ArrayList<AISMessage> messageFragments = new ArrayList<>();

	private void parseMessageFile(ConfigLoader config, InputStream stream, String fileOut) {
		ArrayList<String> result = new ArrayList();

		BufferedReader reader = null;
		try {

			// InputStream (is) must be set and initialized before
			reader = new BufferedReader(new InputStreamReader(stream));

			int msgNumber;
			String line, messageBits;
			AISMessage decodedMessage;
			while ((line = reader.readLine()) != null) {
				if (line.contains("SAVDM") || line.contains("AIVDM") || line.contains("AIVDO")) {
					line = line.replaceFirst("^.*SAVDM", "!AIVDM");
					// System.out.println(line);
				} else {
					continue;
				}

				try {
					AISMessage message = AISMessage.fromString(line);
					message = processMessages("BCS1", message);
					messageBits = "";

					if (message != null) {
						messageBits = message.getBinaryMessage();
					}

					if (messageBits.length() > 0) {
						msgNumber = AISMessageDecoders.getMessageNumber(messageBits);
						result.add(message.getRawMessage() + "/" + msgNumber);
						System.out.println(message.getRawMessage() + "/" + msgNumber);
						// System.out.println(message.getBinaryMessage() + "/" + msgNumber);
						// String encoded = AISDecoder.encodeMessagePayload(aisMessage);
						switch (msgNumber) {
						case 1:
						case 2:
						case 3:
							decodedMessage = PositionReportClassA.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 4:
							decodedMessage = BaseStationReport.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 5:
							decodedMessage = StaticAndVoyageRelatedData.fromAISMessage((AISMessage) message,
									messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 6:
							decodedMessage = AddressedBinaryMessage.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 8:
							decodedMessage = BinaryBroadCastMessage.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());

							if (((BinaryBroadCastMessage) decodedMessage).getDac() == 1
									&& ((BinaryBroadCastMessage) decodedMessage).getFid() == 11) {
								Type8_Dac1_Fid11 tdf = (Type8_Dac1_Fid11) Type8_Dac1_Fid11
										.fromAISMessage(decodedMessage, messageBits);
								result.add(decodedMessage.toString());
							 	System.out.println(tdf.toString());
							}
							break;
						case 9:
							decodedMessage = StandardSARPositionReport.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 11:
							decodedMessage = CoordinatedUniversalTimeDateResponse.fromAISMessage((AISMessage) message,
									messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 18:
							decodedMessage = StandardClassBCSPositionReport.fromAISMessage((AISMessage) message,
									messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 19:
							decodedMessage = ExtendedClassBCSPositionReport.fromAISMessage((AISMessage) message,
									messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 21:
							decodedMessage = AidToNavigationReport.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							break;
						case 24:
							decodedMessage = StaticDataReport.fromAISMessage((AISMessage) message, messageBits);
							result.add(decodedMessage.toString());
							System.out.println(decodedMessage.toString());
							
							if (((StaticDataReport) decodedMessage).getPartno() == 0) {
								StaticDataReportPartA sdrPartA = (StaticDataReportPartA) StaticDataReportPartA
										.fromAISMessage(decodedMessage, messageBits);
								result.add(sdrPartA.toString());
								System.out.println(sdrPartA.toString());
							} else if (((StaticDataReport) decodedMessage).getPartno() == 1) {
								StaticDataReportPartB sdrPartB = (StaticDataReportPartB) StaticDataReportPartB
										.fromAISMessage(decodedMessage, messageBits);
								result.add(sdrPartB.toString());
								System.out.println(sdrPartB.toString());
							}
							break;
						default:
							System.out.println(message.getRawMessage() + "/" + msgNumber);
						}
					}
				} catch (Exception ex) {
					messageFragments.clear();
					System.out.println("!ERROR!; " + ex.getMessage() + ", \n" + line);
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

	private AISMessage processMessages(String source, AISMessage message) throws Exception {
		AISMessage result = null;

		int numberOfFragments = message.getNumberOfFragments();
		if (numberOfFragments <= 0) {
			messageFragments.clear();
			throw new Exception("number of fragments is invalid; " + message.getRawMessage());
		} else if (numberOfFragments == 1) {
			// System.out.println("unfragmented message; " +
			// message.getRawMessage());
			messageFragments.add(message);
			result = AISMessageDecoders.decodeMessagePayload(messageFragments);
			messageFragments.clear();

			return result;
		} else {
			int fragmentNumber = message.getFragmentNumber();

			if (fragmentNumber < 0) {
				messageFragments.clear();
				throw new Exception("fragmentNumber is invalid; " + message.getRawMessage());
			} else if (fragmentNumber > numberOfFragments) {
				messageFragments.clear();
				throw new Exception("fragmentNumber is higher than numberOfFragments; " + message.getRawMessage());
			} else {
				if (fragmentNumber != (messageFragments.size() + 1)) {
					messageFragments.clear();
					throw new Exception("fragmentNumber is not in sequence; " + message.getRawMessage());
				} else {
					messageFragments.add(message);

					if (numberOfFragments == messageFragments.size()) {
						// System.out.println("fragmented message; ");
						// for(AISMessage fragment : messageFragments) {
						// System.out.println(" == " +
						// fragment.getRawMessage());
						// }

						result = AISMessageDecoders.decodeMessagePayload(messageFragments);
						messageFragments.clear();

						return result;
					} else {
						return result;
					}
				}
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
