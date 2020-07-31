package elsu.ais.parser.sentence;

import java.util.ArrayList;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.exceptions.IncompleteFragmentException;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.sentence.tags.SentenceTagBlock;
import elsu.ais.parser.sentence.tags.VDLSignalInformation;

public class AISSentence extends AISBase {

	public static AISSentence fromString(String message) throws Exception {
		return new AISSentence(message);
	}

	public static AISSentence appendString(String message, AISSentence sentence) throws Exception {
		sentence.validateMessage(message);
		sentence.setMessage(message);

		return sentence;
	}

	public AISSentence() {
	}

	public AISSentence(String message) throws Exception {
		validateMessage(message);
		setMessage(message);
	}

	private void validateMessage(String message) throws Exception {
		// validate single or multiple messages
		String[] params = message.split(",");
		boolean valid = false, complete = false;
		String protocol = "", radioChannelCode = "", payload = "";
		int fragments = 0, fragmentNumber = 0, sequenceNumber = 0, checksum = 0;

		// check message fields
		if (!message.matches(messageVDORegex)) {
			throw new Exception("message field length is < 7; " + message);
		}

		// extract and update last field of checksum
		String[] cValues = params[params.length - 1].split("\\*");

		// remove * from last field in params
		params[params.length - 1] = params[params.length - 1].replaceAll("\\*.*", "");

		try {
			setChecksum(cValues[1]);
			checksum = Integer.valueOf(cValues[1], 16);

			int calcChecksum = AISBase.calculateChecksum(message);
			if (calcChecksum != checksum) {
				setChecksumError(true);
			}
		} catch (Exception exi) {
			setChecksumError(true);
		}

		// check message type
		try {
			protocol = !params[1].isEmpty() ? params[0].replaceAll("[\\!\\$]", "") : "";

			if ((protocol == null) || (protocol.length() != 5)) {
				valid = false;
				throw new Exception("message type is invalid; length < 5; " + message);
			}

			if ((!protocol.endsWith("VDM")) && (!protocol.endsWith("VDO"))) {
				throw new Exception("message type is invalid; not VDM/VDO format; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message type is invalid; " + ex.getMessage() + "; " + message);
		}

		// check fragment count
		try {
			fragments = !params[1].isEmpty() ? Integer.valueOf(params[1]) : 0;

			if (fragments <= 0) {
				throw new Exception("message fragment count less than equal to 0; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message fragment count is invalid; " + ex.getMessage() + "; " + message);
		}

		// check fragment number
		try {
			fragmentNumber = !params[2].isEmpty() ? Integer.valueOf(params[2]) : 0;

			// check if partial fragment is invalid?
			if ((fragmentNumber == 1) && (fragments == 1)) {
				if (getMessages().size() != 0) {
					throw new IncompleteFragmentException("message fragment missing; ");
				}

				complete = true;
			} else if ((fragmentNumber == fragments) && (getMessages().size() + 1 == fragments)) {
				if (getFragments() != fragments) {
					throw new IncompleteFragmentException("message fragment missing; ");
				}

				complete = true;
			} else {
				if (fragmentNumber < getFragmentNumber()) {
					throw new IncompleteFragmentException("message fragment out of sequence; ");
				} else if (fragmentNumber != getMessages().size() + 1) {
					throw new IncompleteFragmentException("message fragment out of sequence; ");
				}

				complete = false;
			}
		} catch (IncompleteFragmentException ife) {
			throw ife;
		} catch (Exception ex) {
			throw new Exception("message fragment number is invalid; " + ex.getMessage() + "; " + message);
		}

		// check sequence number
		try {
			sequenceNumber = !params[3].isEmpty() ? Integer.valueOf(params[3]) : 0;
		} catch (Exception ex) {
			throw new Exception("message sequence number is invalid; " + message);
		}

		// check radio channel code
		try {
			radioChannelCode = params[4];
		} catch (Exception ex) {
			throw new Exception("message radio channel code is invalid; " + message);
		}

		// check payload
		try {
			payload = params[5];

			if (payload.isEmpty()) {
				throw new Exception("message payload is empty; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message payload is invalid; " + message);
		}

		// update class variables and decode the message
		setProtocol(protocol);
		setFragments(fragments);
		setFragmentNumber(fragmentNumber);
		setSequenceNumber(sequenceNumber);
		setRadioChannelCode(radioChannelCode);
		setPayload(payload);

		if (complete) {
			setComplete(true);
			setBitString();

			setType();
			AISMessage.fromSentence(this);
		}

		setValid(true);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int counter = 0;

		result.append("{");
		result.append("\"complete\": " + isComplete());
		result.append(", \"valid\": " + isValid());
		result.append(", \"messages\": [");

		counter = 0;
		for (String message : getMessages()) {
			if (counter > 0) {
				result.append(",");
			}
			result.append("\"" + message + "\"");
			counter++;
		}

		result.append("], \"fieldCount\": " + getFieldCount());
		result.append(", \"header\": \"" + getHeader() + "\"");
		result.append(", \"protocol\": \"" + getProtocol() + "\"");
		result.append(", \"fragments\": " + getFragments());
		result.append(", \"fragmentNumber\": " + getFragmentNumber());
		result.append(", \"sequenceNumber\": " + getSequenceNumber());
		result.append(", \"radioChannelCode\": \"" + getRadioChannelCode() + "\"");
		result.append(", \"payload\": \"" + getPayload() + "\"");
		result.append(", \"checksum\": \"" + getChecksum() + "\"");
		result.append(", \"checksumError\": " + isChecksumError());
		result.append(", \"bitString\": \"" + getBitString() + "\"");
		result.append(", \"type\":" + getType());
		result.append(", \"typeText\":\"" + LookupValues.getMessageType(getType())  + "\"");
		result.append(", \"aisMessage\": " + getAISMessage());
		result.append(", \"tagBlock\": " + getTagBlock());
		result.append(", \"vdlInfo\": " + getVDLInfo());
		result.append("}");

		return result.toString();
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

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
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

	public int getType() {
		return type;
	}

	private void setType() {
		this.type = AISBase.unsigned_integer_decoder(this.bitString.substring(0, 6));
	}

	public AISMessage getAISMessage() {
		return this.aisMessage;
	}

	public void setAISMessage(AISMessage message) {
		this.aisMessage = message;
	}

	public SentenceTagBlock getTagBlock() {
		return this.tagBlock;
	}

	public void setTagBlock(SentenceTagBlock tagBlock) {
		this.tagBlock = tagBlock;
	}

	public VDLSignalInformation getVDLInfo() {
		return this.vdlInfo;
	}

	public void setVDLInfo(VDLSignalInformation vdlInfo) {
		this.vdlInfo = vdlInfo;
	}

	public boolean isChecksumError() {
		return checksumError;
	}

	public void setChecksumError(boolean error) {
		this.checksumError = error;
	}

	private ArrayList<String> messages = new ArrayList<String>();
	private String binaryMessage = "";

	private boolean complete = false;
	private boolean valid = false;
	private int fieldCount = 0;
	private String header = "";
	private String protocol = "";
	private int fragments = 0;
	private int fragmentNumber = 0;
	private int sequenceNumber = 0;
	private String radioChannelCode = "";
	private String payload = "";
	private String checksum = "";
	private boolean checksumError = false;
	private String bitString = "";
	private int type = 0;

	private SentenceTagBlock tagBlock = null;
	private VDLSignalInformation vdlInfo = null;
	private AISMessage aisMessage = null;
}
