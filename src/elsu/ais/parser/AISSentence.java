package elsu.ais.parser;

import java.util.ArrayList;

import elsu.ais.parser.messages.BinaryBroadCastMessage;
import elsu.ais.parser.messages.PositionReportClassA;
import elsu.ais.parser.messages.data.Type8_Dac1_Fid11;

public class AISSentence extends AISBase {

	public static AISSentence fromString(String message) throws Exception {
		return new AISSentence(message);
	}

	public static AISSentence fromString(String message, AISSentence fragment) throws Exception {
		fragment.setMessage(message);
		fragment.validateMessage(message);

		return fragment;
	}

	public AISSentence() {
	}

	public AISSentence(String message) throws Exception {
		setMessage(message);
		validateMessage(message);
	}

	private void validateMessage(String message) throws Exception {
		// validate single or multiple messages
		String[] messageArray = message.split(",");

		// check message fields
		if (!message.matches(messageRegex)) {
			throw new Exception("message field length is < 7; " + message);
		}

		// check message type
		try {
			setProtocol(!messageArray[1].isEmpty() ? messageArray[0].replaceAll("[\\!\\$]", "") : "");

			if ((getProtocol() == null) || (getProtocol().length() != 5)) {
				setValid(false);
				throw new Exception("message type is invalid; length < 5; " + message);
			}

			if ((!getProtocol().endsWith("VDM")) && (!getProtocol().endsWith("VDO"))) {
				throw new Exception("message type is invalid; not VDM/VDO format; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message type is invalid; " + ex.getMessage() + "; " + message);
		}

		// check fragment count
		try {
			setFragments(!messageArray[1].isEmpty() ? Integer.valueOf(messageArray[1]) : 0);

			if (getFragments() <= 0) {
				throw new Exception("message fragment count less than equal to 0; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message fragment count is invalid; " + ex.getMessage() + "; " + message);
		}

		// check fragment number
		try {
			setFragmentNumber(!messageArray[2].isEmpty() ? Integer.valueOf(messageArray[2]) : 0);

			if ((getFragmentNumber() == getFragments()) && (getMessages().size() == getFragments())) {
				setComplete(true);
			} else {
				if ((getFragmentNumber() == 1) && (getFragmentNumber() == getFragments())) {
					if (getMessages().size() != getFragments()) {
						setPriorError(true);
					}

					clearMessages();
					setMessage(message);

					setComplete(true);
				} else if (getFragmentNumber() <= 0) {
					throw new Exception("message fragment number is 0; " + message);
				} else if (getFragmentNumber() > getFragments()) {
					throw new Exception("message fragment number greater then fragments; " + message);
				} else if (getFragmentNumber() > getMessages().size()) {
					throw new Exception("message fragment number greater than fragments; " + message);
				} else if (getFragmentNumber() < getMessages().size()) {
					throw new Exception("message fragment number less than received fragments; " + message);
				} else {
					setComplete(false);
				}
			}
		} catch (Exception ex) {
			throw new Exception("message fragment number is invalid; " + ex.getMessage() + "; " + message);
		}

		// check sequence number
		try {
			setSequenceNumber(!messageArray[3].isEmpty() ? Integer.valueOf(messageArray[3]) : 0);
		} catch (Exception ex) {
			throw new Exception("message sequence number is invalid; " + message);
		}

		// check radio channel code
		try {
			setRadioChannelCode(messageArray[4]);
		} catch (Exception ex) {
			throw new Exception("message radio channel code is invalid; " + message);
		}

		// check payload
		try {
			setPayload(messageArray[5]);

			if (getPayload().isEmpty()) {
				throw new Exception("message payload is empty; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message payload is invalid; " + message);
		}

		// check checksum
		try {
			setChecksum(!messageArray[6].isEmpty() ? Integer.valueOf(messageArray[6].replaceAll("^.*\\*", ""), 16) : 0);

			int checksum = calculateChecksum(message);
			if (checksum != getChecksum()) {
				throw new Exception("message checksum does not match; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message checksum is invalid; " + message);
		}

		// decode the message
		if (isComplete()) {
			setBitString();
			parseBinaryMessage();
		}
		setValid(true);
	}

	private void clearMessages() {
		this.messages.clear();
		this.payload = "";
	}

	public ArrayList<String> getMessages() {
		return this.messages;
	}

	protected void setMessage(String message) {
		this.messages.add(message);
	}

	public String getBinaryMessage() {
		return this.binaryMessage;
	}

	public void setBinaryMessage(String message) {
		this.binaryMessage = message;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isPriorError() {
		return priorError;
	}

	public void setPriorError(boolean priorError) {
		this.priorError = priorError;
	}

	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getFragments() {
		return fragments;
	}

	public void setFragments(int fragments) {
		this.fragments = fragments;
	}

	public int getFragmentNumber() {
		return fragmentNumber;
	}

	public void setFragmentNumber(int fragmentNumber) {
		this.fragmentNumber = fragmentNumber;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getRadioChannelCode() {
		return radioChannelCode;
	}

	public void setRadioChannelCode(String radioChannelCode) {
		this.radioChannelCode = radioChannelCode;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload += payload;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getBitString() {
		return bitString;
	}

	private void setBitString() {
		this.bitString = decodeMessage(payload);
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	private void setMessageNumber() {
		this.messageNumber = AISBase.unsigned_integer_decoder(this.bitString.substring(0, 6));
	}

	public AISMessage getAISMessage() {
		return this.aisMessage;
	}

	private void parseBinaryMessage() {
		setMessageNumber();

		switch (getMessageNumber()) {
		case 1:
		case 2:
		case 3:
			aisMessage = PositionReportClassA.fromAISMessage(getBitString());
			break;
		case 8:
			aisMessage = BinaryBroadCastMessage.fromAISMessage(getBitString());
			if (((BinaryBroadCastMessage) aisMessage).getDac() == 1
					&& ((BinaryBroadCastMessage) aisMessage).getFid() == 11) {
				aisMessage = (Type8_Dac1_Fid11) Type8_Dac1_Fid11.fromAISMessage(getBitString());
			}
			break;
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int counter = 0;

		result.append("{AISSentence: {");
		result.append("complete: " + isComplete());
		result.append(", valid: " + isValid());
		result.append(", priorError: " + isPriorError());
		result.append(", messages: [");

		counter = 0;
		for (String message : getMessages()) {
			if (counter > 0) {
				result.append(",");
			}
			result.append("\"" + message + "\"");
			counter++;
		}

		result.append("], fieldCount: " + getFieldCount());
		result.append(", header: \"" + getHeader() + "\"");
		result.append(", protocol: " + getProtocol());
		result.append(", fragments: " + getFragments());
		result.append(", fragmentNumber: " + getFragmentNumber());
		result.append(", sequenceNumber: " + getSequenceNumber());
		result.append(", radioChannelCode: " + getRadioChannelCode());
		result.append(", payload: \"" + getPayload() + "\"");
		result.append(", checksum: " + getChecksum());
		result.append(", bitString: \"" + getBitString() + "\"");
		result.append(", messageNumber: " + getMessageNumber());
		result.append(", aisMessage: " + getAISMessage());
		result.append("}}");

		return result.toString();
	}

	private ArrayList<String> messages = new ArrayList<String>();
	private String binaryMessage = "";

	private boolean complete = false;
	private boolean valid = false;
	private boolean priorError = false;
	private int fieldCount = 0;
	private String header = "";
	private String protocol = "";
	private int fragments = 0;
	private int fragmentNumber = 0;
	private int sequenceNumber = 0;
	private String radioChannelCode = "";
	private String payload = "";
	private int checksum = 0;
	private String bitString = "";
	private int messageNumber = 0;
	private AISMessage aisMessage = null;
}
