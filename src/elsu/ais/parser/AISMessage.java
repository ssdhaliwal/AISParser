package elsu.ais.parser;

public class AISMessage {

	private String _rawMessage = "";
	private String[] _rawMessageArray = null;
	private String _binaryMessage = "";
	private String _encodedMessage = "";
	private String _errorMessage = "";

	public static AISMessage fromString(String message) throws Exception {
		return new AISMessage(message);
	}

	public AISMessage() {
	}
	
	public AISMessage(String message) throws Exception {
		setRawMessage(message);

		if ((getRawMessageArray() != null) && (getRawMessageArray().length == 7)) {
			isValid();
		} else {
			setErrorMessage("message length is < 7; " + message);
			throw new Exception(getErrorMessage());
		}
	}

	protected boolean isValid() throws Exception {
		boolean result = true;

		isMessageValid();
		return result;
	}

	public String getRawMessage() {
		return this._rawMessage;
	}

	protected void setRawMessage(String message) {
		this._rawMessage = message;

		this._rawMessageArray = this._rawMessage.split(",");
	}

	protected String[] getRawMessageArray() {
		return this._rawMessageArray;
	}

	public String getBinaryMessage() {
		return this._binaryMessage;
	}

	protected void setBinaryMessage(String message) {
		this._binaryMessage = message;
	}

	public String getEncodedMessage() {
		return this._encodedMessage;
	}

	protected void setEncodedMessage(String message) {
		this._encodedMessage = message;
	}

	public String getErrorMessage() {
		return this._errorMessage;
	}

	protected void setErrorMessage(String message) {
		this._errorMessage = message;
	}

	public String getMessageType() {
		return getRawMessageArray()[0].replace("!", "");
	}

	public Integer getNumberOfFragments() {
		return (!getRawMessageArray()[1].isEmpty()) ? Integer.valueOf(getRawMessageArray()[1]) : null;
	}

	public Integer getFragmentNumber() {
		return (!getRawMessageArray()[2].isEmpty()) ? Integer.valueOf(getRawMessageArray()[2]) : null;
	}

	public Integer getSequenceNumber() {
		return (!getRawMessageArray()[3].isEmpty()) ? Integer.valueOf(getRawMessageArray()[3]) : null;
	}

	public String getRadioChannelCode() {
		return getRawMessageArray()[4];
	}

	public String getPayload() {
		return getRawMessageArray()[5];
	}

	public Integer getChecksum() {
		return (!getRawMessageArray()[6].isEmpty()) ? Integer.valueOf(getRawMessageArray()[6].substring(2), 16) : null;
	}

	public boolean isMessageTypeValid() throws Exception {
		String messageType = getMessageType();

		if (messageType == null || messageType.length() != 5) {
			setErrorMessage("messageType length != 5; " + getRawMessage());
			throw new Exception(getErrorMessage());
		}

		if (messageType.endsWith("VDM") || messageType.endsWith("VDO")) {
			return true;
		}

		setErrorMessage("messageType != ??VDM or ??VDO; " + getRawMessage());
		throw new Exception(getErrorMessage());
	}

	public boolean isMessageValid() throws Exception {
		String message = getRawMessage();
		String regexp = "^!..VD[OM]\\,\\d+\\,\\d+,\\d*,[12AB]\\,[0-9:;<=>?@A-W`a-w]{1,}\\,[0-9]\\*[0-9a-zA-Z]{2}$";

		if (!message.matches(regexp)) {
			setErrorMessage("message format invalid (7 fields w/checksum expected); " + getRawMessage());
			throw new Exception(getErrorMessage());
		}

		isMessageTypeValid();

		return true;
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
		for(int i = 0; i < len; i += 6) {
			if (len < i+6) {
				bit = String.format("%-6s", bits.substring(i, len)).replace(" ", "0");
			} else {
				bit = bits.substring(i, i+6);
			}
			index = unsigned_integer_decoder(bit);

			buffer.append(AISDecoder.sixbitAscii[index]);
		}
		
		return buffer.toString();
	}

	public static String text_decoder_8bit(String bits) {
		StringBuffer buffer = new StringBuffer();
		
		int len = bits.length(), index = 0;
		String bit = "";
		for(int i = 0; i < len; i += 8) {
			if (len < i+8) {
				bit = String.format("%-8s", bits.substring(i, len)).replace(" ", "0");
			} else {
				bit = bits.substring(i, i+8);
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
