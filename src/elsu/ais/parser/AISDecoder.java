package elsu.ais.parser;

import java.util.*;

public class AISDecoder {
	static public String[] payloadBits = new String[] {
			/*
			 * numeric values "0","1","2","3","4","5","6","7","8","9" ASCII
			 * 48-57
			 */
			"000000", "000001", "000010", "000011", "000100", "000101", "000110", "000111", "001000", "001001",
			/* special chars ":",";","<","=",">","?","@" ASCII 58-64 */
			"001010", "001011", "001100", "001101", "001110", "001111", "010000",
			/*
			 * alpha CAPS "A","B","C","D","E","F","G","H","I","J",
			 * "K","L","M","N","O","P","Q","R","S","T", "U","V","W" ASCII 65-87
			 */
			"010001", "010010", "010011", "010100", "010101", "010110", "010111", "011000", "011001", "011010",
			"011011", "011100", "011101", "011110", "011111", "100000", "100001", "100010", "100011", "100100",
			"100101", "100110", "100111",
			/*
			 * special char "`" ASCII 96
			 */
			"101000",
			/*
			 * alpha LOWER
			 * "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p",
			 * "q","r","s","t","u","v","w" ASCII 97-119
			 */
			"101001", "101010", "101011", "101100", "101101", "101110", "101111", "110000", "110001", "110010",
			"110011", "110100", "110101", "110110", "110111", "111000", "111001", "111010", "111011", "111100",
			"111101", "111110", "111111" };
	static public String[] payloadChars = new String[] {
			/* numeric values ASCII 48-57 */
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			/* special chars ASCII 58-64 */
			":", ";", "<", "=", ">", "?", "@",
			/* alpha CAPS ASCII 65-87 */
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W",
			/* special char ASCII 96 */
			"`",
			/* alpha LOWER ASCII 97-119 */
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w" };
	static public String[] sixbitAscii = new String[] { "@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", " ", "!",
			"\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", ":", ";", "<", "=", ">", "?" };

	static public String decodeMessagePayload(ArrayList<AISMessage> messages) {
		String result = "";

		if (messages.size() > 0) {
			AISMessage firstMessage = messages.get(0);

			if (!firstMessage.getEncodedMessage().isEmpty()) {
				result = firstMessage.getBinaryMessage();
			} else {
				// encoded merger
				StringBuilder builder = new StringBuilder();

				for (AISMessage message : messages) {
					builder.append(message.getPayload());
				}

				firstMessage.setEncodedMessage(builder.toString());
				result = decodeMessage(firstMessage.getEncodedMessage());
				firstMessage.setBinaryMessage(result);
			}
		}

		return result;
	}
	
	static public String decodeMessage(String message) {
		StringBuilder builder = new StringBuilder();

		// encoded to bit string
		builder = new StringBuilder();
		byte[] encodedBytes = message.getBytes();
		byte index = 0;
		for (byte eb : encodedBytes) {
			index = (eb <= 87) ? (byte) (eb - 48) : (byte) (eb - 56);
			builder.append(payloadBits[index]);
		}

		return builder.toString();
	}

	static public String encodeMessagePayload(String bitArray) {
		String result = "";

		if (bitArray.length() > 0) {
			int length = bitArray.length(), bitValue = 0;
			String bits = "";
			for (int start = 0; start < length; start += 6) {
				bits = bitArray.substring(start, Math.min(length, start + 6));

				bitValue = Integer.parseInt(bits, 2);
				result += payloadChars[bitValue];
			}
		}

		return result;
	}

	static public int getMessageNumber(String message) {
		int result = 0;

		result = Integer.parseInt(message.substring(0, 6), 2);
		return result;
	}
}
