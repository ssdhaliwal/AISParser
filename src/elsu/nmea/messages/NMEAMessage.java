package elsu.nmea.messages;

import java.util.*;

import elsu.nmea.base.NMEAMessageBase;
import elsu.sentence.SentenceBase;

public class NMEAMessage {
	
	public static NMEAMessage fromString(String message) throws Exception {
		NMEAMessage nmeaMessage = new NMEAMessage();
		nmeaMessage.parseMessage(message);

		return nmeaMessage;
	}

	public NMEAMessage() {
	}

	public void parseMessage(String message) throws Exception {
		String[] params = message.split(",");

		// extract and update last field of checksum
		String[] cValues = params[params.length - 1].split("\\*");
		int checksum = 0;

		// remove * from last field in params
		params[params.length - 1] = params[params.length - 1].replaceAll("\\*.*", "");

		try {
			setChecksum(cValues[1]);
			checksum = Integer.valueOf(cValues[1], 16);

			int calcChecksum = SentenceBase.calculateChecksum(message);
			if (calcChecksum != checksum) {
				setChecksumError(true);
			}
		} catch (Exception exi) {
			setChecksumError(true);
		}
		
		// call super method to continue process of local message
		if (params.length > 0) {
			setType(params[0]);
		}
		parseMessage(params);
	}

	public void parseMessage(String[] params) throws Exception {
		// get the definition of the message and parse it
		String messageType = params[0].substring(params[0].length() - 3);
		Map<Integer, String> definitions = NMEAMessageBase.params.get(messageType);

		if (definitions == null) {
			throw new Exception("invalid nmea messageType; " + params[0]);
		}
		
		setAttributes("typeName", definitions.get(0));
		for(int i = 1; i < definitions.size(); i++) {
			setAttributes(definitions.get(i), params[i]);
		}
	};

	@Override
	public String toString() {
		String result = "";
		
		try {
			result = SentenceBase.objectMapper.writeValueAsString(this);
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
		/*
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\": \"" + getType() + "\"");
		
		for (String key : getAttributes().keySet())  
		{
		   buffer.append(", \"" + key + "\": \"" + getAttributes().get(key) + "\"");
		}
		
		buffer.append(", \"checksum\": \"" + getChecksum() + "\"");
		buffer.append(", \"checksumError\": " + isChecksumError());
		buffer.append(", \"exceptions\": \"" + getExceptions() + "\"");
		buffer.append("}");

		return buffer.toString();
		*/
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public boolean isChecksumError() {
		return checksumError;
	}

	public void setChecksumError(boolean error) {
		this.checksumError = error;
	}

	public String getExceptions() {
		return this.exceptions;
	}

	public void setExceptions(String error) {
		this.exceptions += (this.exceptions.isEmpty() ? "" : ", ") + error;
	}

	private String type = "";
	private Map<String, String> attributes = new HashMap<String, String>();
	private String checksum = "";
	private boolean checksumError = false;
	private String exceptions = "";
}
