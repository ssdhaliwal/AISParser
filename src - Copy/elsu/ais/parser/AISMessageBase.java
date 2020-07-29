package elsu.ais.parser;

import java.util.zip.Checksum;

import elsu.ais.parser.resources.AISMessageDecoders;

public class AISMessageBase {

	public static final String sentenceRegex = "^!..VD[OM]\\,\\d+\\,\\d+,\\d*,[12AB]\\,[0-9:;<=>?@A-W`a-w]{1,}\\,[0-9]\\*[0-9a-zA-Z]{2}$";
	public static final String messageRegex = "^!..VD[OM]\\,\\d+\\,\\d+,\\d*,[12AB]\\,[0-9:;<=>?@A-W`a-w]{1,}\\,[0-9]\\*[0-9a-zA-Z]{2}$";

	public static int calculateChecksum(String message) {
		int checksum = 0;
		
		char c = 0;
		
		for (int i = 1; i < message.length(); i++) {
			c = message.charAt(i);
			
			// if delimiter is not at the start; return 0
			if ((i > 1) && (c == '!' || c == '$')) {
				return 0;
			}
			
			// only return valid value if end delimiter is found
			if (c == '*') {
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

			buffer.append(AISMessageDecoders.sixbitAscii[index]);
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
