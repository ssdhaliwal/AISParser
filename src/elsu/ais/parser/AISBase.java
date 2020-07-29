package elsu.ais.parser;

import java.util.regex.Pattern;

public class AISBase {

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

	public static final String headerRegex = "^.*\\!";
	public static final Pattern headerPattern = Pattern.compile(headerRegex);
	public static final String messageRegex = "^!..VD[OM]\\,\\d+\\,\\d+,\\d*,[12AB]\\,[0-9:;<=>?@A-W`a-w]{1,}\\,[0-9]\\*[0-9a-zA-Z]{2}$";
	public static final Pattern messagePattern = Pattern.compile(messageRegex);

	public static String decodeMessage(String message) {
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

	public static String encodeMessagePayload(String bitArray) {
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

	public static int calculateChecksum(String message) {
		int checksum = 0;
	
		char c = 0;
		
		for (int i = 1; i < message.length(); i++) {
			c = message.charAt(i);
			
			// if delimiter is not at the start; return 0
			if ((i > 1) && (c == '!' || c == '*')) {
				return checksum;
			}
			
			// compute checksum
			checksum ^= c;
		}
		
		// return 0; invalid
		return 0;
	}
	
	public static int unsigned_integer_decoder(String bits) {
		return Integer.valueOf(bits, 2);
	}

	public static int integer_decoder(String bits) {
		int result = 0;

		if (bits.substring(0, 1).equals("1")) {
			String value = bits.substring(1).replace("0", "x").replace("1", "0").replace("x", "1");
			result = -1 - unsigned_integer_decoder(value);
		} else {
			result = unsigned_integer_decoder(bits.substring(1));
		}

		return result;
	}

	public static float unsigned_float_decoder(String bits) {
		return Float.valueOf(unsigned_integer_decoder(bits));
	}

	public static float float_decoder(String bits) {
		return Float.valueOf(integer_decoder(bits));
	}

	public static long unsigned_long_decoder(String bits) {
		return Long.parseLong(bits, 2);
	}

	public static boolean boolean_decoder(String bits) {
		if (bits.substring(0, 1).equals("1")) {
			return true;
		}

		return false;
	}

	public static String time_decoder(String bits) {
		int month = unsigned_integer_decoder(bits.substring(0, 4));
		int day = unsigned_integer_decoder(bits.substring(4, 9));
		int hour = unsigned_integer_decoder(bits.substring(9, 14));
		int minute = unsigned_integer_decoder(bits.substring(14, 20));

		return ((day < 10) ? "0" + day : day) + "-" + ((month < 10) ? "0" + month : month) + " "
				+ ((hour < 10) ? "0" + hour : hour) + ":" + ((minute < 10) ? "0" + minute : minute);
	}

	public static String text_decoder(String bits) {
		StringBuffer buffer = new StringBuffer();

		int len = bits.length(), index = 0;
		String bit = "";
		for (int i = 0; i < len; i += 6) {
			if (len < i + 6) {
				bit = String.format("%-6s", bits.substring(i, len)).replace(" ", "0");
			} else {
				bit = bits.substring(i, i + 6);
			}
			index = unsigned_integer_decoder(bit);

			buffer.append(sixbitAscii[index]);
		}

		return buffer.toString();
	}

	public static String text_decoder_8bit(String bits) {
		StringBuffer buffer = new StringBuffer();

		int len = bits.length(), index = 0;
		String bit = "";
		for (int i = 0; i < len; i += 8) {
			if (len < i + 8) {
				bit = String.format("%-8s", bits.substring(i, len)).replace(" ", "0");
			} else {
				bit = bits.substring(i, i + 8);
			}
			index = unsigned_integer_decoder(bit);

			buffer.append(Integer.toHexString(0x100 | index).substring(1));
		}

		return buffer.toString();
	}

	public static String bit_decoder(String bits) {
		return bits;
	}
}
