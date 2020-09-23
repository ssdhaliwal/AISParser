package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T24_StaticDataReport extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T24_StaticDataReport staticReport = new T24_StaticDataReport();
		staticReport.parseMessage(messageBits);

		return staticReport;
	}

	public T24_StaticDataReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 24"));
		getMessageBlocks().add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(new AISPayloadBlock(38, 39, 2, "Part Number", "partno", "u", "0-1"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 0:
			setType(parseUINT(block.getBits()));
			break;
		case 6:
			setRepeat(parseUINT(block.getBits()));
			break;
		case 8:
			setMmsi(parseUINT(block.getBits()));
			break;
		case 38:
			setPartNumber(parseUINT(block.getBits()));
			break;
		}
	}

	public void parseMessage(T24_StaticDataReport message) {
		this.type = message.getType();
		this.repeat = message.getRepeat();
		this.mmsi = message.getMmsi();
		this.partNumber = message.getPartNumber();
	}

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
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partNumber\":" + getPartNumber());
		buffer.append("}");

		return buffer.toString();
		*/
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getMmsi() {
		return mmsi;
	}

	public void setMmsi(int mmsi) {
		this.mmsi = mmsi;
		this.setAuxilizary();
	}

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public boolean isAuxiliary() {
		return auxiliary;
	}

	private void setAuxilizary() {
		if ((getMmsi() + "").substring(0, 2) == "98") {
			this.auxiliary = true;
		}
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int partNumber = 0;
	private boolean auxiliary = false;
}
