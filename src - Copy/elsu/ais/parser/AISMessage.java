package elsu.ais.parser;

import java.util.ArrayList;

import elsu.ais.parser.resources.PayloadBlock;

public class AISMessage extends AISMessageBase {

	public ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

	private ArrayList<String> _message = new ArrayList<String>();
	private ArrayList<AISMessage> _messageFragments = new ArrayList<AISMessage>();
	private String _binaryMessage = "";

	public static AISMessage fromString(String message) throws Exception {
		return new AISMessage(message);
	}

	public AISMessage() {
	}

	public AISMessage(String message) throws Exception {
		setRawMessage(message);

		validateMessage(message);
	}

	private void validateMessage(String message) throws Exception {
		// validate single or multiple messages
		String[] messageArray = message.split(",");
		
		// check message fields
		if (!message.matches(sentenceRegex)) {
			throw new Exception("message field length is < 7; " + message);
		}
		
		// check message type
		try {
			setType(!messageArray[1].isEmpty() ? messageArray[0].replace("!", "") : "");
			
			if ((getType() == null) || (getType().length() != 5)) {
				setValid(false);
				throw new Exception("message type is invalid; length < 5; " + message);
			}
			
			if ((!getType().endsWith("VDM")) && (!getType().endsWith("VDO"))) {
				throw new Exception("message type is invalid; not VDM/VDO format; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message type is invalid; " + message);
		}
		
		// check fragment count
		try {
			setFragments(!messageArray[1].isEmpty() ? Integer.valueOf(messageArray[1]) : 0);
			
			if (getFragments() <= 0) {
				throw new Exception("message fragment count is invalid; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message fragment count is invalid; " + message);
		}
		
		// check fragment number
		try {
			setFragmentNumber(!messageArray[2].isEmpty() ? Integer.valueOf(messageArray[1]) : 0);
			
			if ((getFragmentNumber() <= 0) || (getFragmentNumber() > getFragments())) {
				throw new Exception("message fragment number is invalid; " + message);
			}
			
			if (getFragmentNumber() < getFragments()) {
				setComplete(false);
			}
		} catch (Exception ex) {
			throw new Exception("message fragment number is invalid; " + message);
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
			setChecksum(!messageArray[6].isEmpty() ? Integer.valueOf(messageArray[6], 16) : 0);

			int checksum = calculateChecksum(message);
			if (checksum != getChecksum()) {
				throw new Exception("message checksum does not match; " + message);
			}
		} catch (Exception ex) {
			throw new Exception("message checksum is invalid; " + message);
		}
		
		setValid(true);
	}
	
	public ArrayList<String> getMessage() {
		return this._message;
	}

	protected void setMessage(String message) {
		this._message.add(message);
	}

	public ArrayList<AISMessage> getMessageFragments() {
		return this._messageFragments;
	}

	public String getBinaryMessage() {
		return this._binaryMessage;
	}

	public void setBinaryMessage(String message) {
		this._binaryMessage = message;
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

	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		this.payload = payload;
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

	private boolean complete = false;
	private boolean valid = false;
	private int fieldCount = 0;
	private String[] header = null;
	private String type = "";
	private int fragments = 0;
	private int fragmentNumber = 0;
	private int sequenceNumber = 0;
	private String radioChannelCode = "";
	private String payload = "";
	private int checksum = 0;
}
