package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.CommunicationState;
import elsu.sentence.SentenceBase;

public class T26_MultipleSlotBinaryMessage extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T26_MultipleSlotBinaryMessage binaryMessage = new T26_MultipleSlotBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T26_MultipleSlotBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 25"));
		getMessageBlocks().add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(
				new AISPayloadBlock(38, 38, 1, "Destination indicator", "addressed", "b", "0=broadcast, 1=addressed."));
		getMessageBlocks().add(new AISPayloadBlock(39, 39, 1, "Binary data flag", "structured", "b", "See below"));

		// w/+Destination +ApplicationId
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 69, 30, 0, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 0, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new AISPayloadBlock(72, 81, 10, 0, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(82, 87, 6, 0, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(88, -20, 952, 0, "Data", "data", "d", "Binary data"));

		// w/Destination -ApplicationId
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 69, 30, 1, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 1, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new AISPayloadBlock(72, -20, 968, 1, "Data", "data", "d", "Binary data"));

		// w/-Destination +ApplicationId
		getMessageBlocks().add(new AISPayloadBlock(40, 49, 10, 2, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(50, 55, 6, 2, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(56, -20, 984, 2, "Data", "data", "d", "Binary data"));

		// w/-Destination -ApplicationId
		getMessageBlocks().add(new AISPayloadBlock(40, -20, 1000, 3, "Data", "data", "d", "Binary data"));

		// comm state (start offset start/end needs to be adjusted after data
		// parsing)
		// getMessageBlocks().add(new PayloadBlock(1040, 1043, 4, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new AISPayloadBlock(-20, 0, 1, "Comm State Selector", "commflag", "d",
				"Communication state selector flag"));
		getMessageBlocks().add(new AISPayloadBlock(-19, 0, 19, "Radio status", "radio", "u", "See [IALA] for details."));
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
			setAddressed(parseBOOLEAN(block.getBits()));
			break;
		case 39:
			setStructured(parseBOOLEAN(block.getBits()));
			break;
		case 40:
		case 72:
		case 82:
		case 88:
		case 50:
		case 56:
			if (isAddressed() && isStructured() && (block.getGroup() == 0)) {
				switch (block.getStart()) {
				case 40:
					setDestinationMmsi(parseUINT(block.getBits()));
					break;
				case 72:
					setDac(parseUINT(block.getBits()));
					break;
				case 82:
					setFid(parseUINT(block.getBits()));
					break;
				case 88:
					setData(parseBITS(block.getBits()));
					return;
				}
			} else if (isAddressed() && !isStructured() && (block.getGroup() == 1)) {
				switch (block.getStart()) {
				case 40:
					setDestinationMmsi(parseUINT(block.getBits()));
					break;
				case 72:
					setData(parseBITS(block.getBits()));
					return;
				}
			} else if (!isAddressed() && isStructured() && (block.getGroup() == 2)) {
				switch (block.getStart()) {
				case 40:
					setDac(parseUINT(block.getBits()));
					break;
				case 50:
					setFid(parseUINT(block.getBits()));
					break;
				case 56:
					setData(parseBITS(block.getBits()));
					return;
				}
			} else if (!isAddressed() && !isStructured() && (block.getGroup() == 3)) {
				switch (block.getStart()) {
				case 40:
					setData(parseBITS(block.getBits()));
					return;
				}
			}
			break;
		default:
			if (block.getName().equals("commflag")) {
				setCommFlag(parseUINT(block.getBits()));
			} else if (block.getName().equals("radio")) {
				setRadio(parseUINT(block.getBits()));
				setCommState(block.getBits());
			}
			break;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"addressed\":" + isAddressed());
		buffer.append(", \"strictured\":" + isStructured());
		buffer.append(", \"destinationMmsi\":" + getDestinationMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		if (SentenceBase.logLevel >= 2) {
			buffer.append(", \"dataBits\":\"" + getData() + "\"");
		}
		buffer.append(", \"data\":\"" + getDataRaw() + "\"");
		buffer.append(", \"commFlag\":" + getCommFlag());
		buffer.append(", \"commFlagText\":\"" + AISLookupValues.getCommunicationFlag(getCommFlag()) + "\"");
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState());
		buffer.append(", \"commtech\":\"" + AISLookupValues.getCommunicationTechnology(getType()) + "\"");
		buffer.append("}");

		return buffer.toString();
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
	}

	public boolean isAddressed() {
		return addressed;
	}

	public void setAddressed(boolean addressed) {
		this.addressed = addressed;
	}

	public boolean isStructured() {
		return structured;
	}

	public void setStructured(boolean structured) {
		this.structured = structured;
	}

	public int getDestinationMmsi() {
		return destinationMmsi;
	}

	public void setDestinationMmsi(int mmsi) {
		this.destinationMmsi = mmsi;
	}

	public int getDac() {
		return dac;
	}

	public void setDac(int dac) {
		this.dac = dac;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getData() {
		return data;
	}

	public String getDataRaw() {
		return dataRaw;
	}

	public void setData(String bits) {
		this.data = bits;
		this.dataRaw = parseTEXT8BIT(bits);
	}

	public int getCommFlag() {
		return commFlag;
	}

	public void setCommFlag(int commFlag) {
		this.commFlag = commFlag;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	public CommunicationState getCommState() {
		return commState;
	}

	private void setCommState(String bits) {
		commState = CommunicationState.fromPayload(bits, getType());
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private boolean addressed = false;
	private boolean structured = false;
	private int destinationMmsi = 0;
	private int dac = 0;
	private int fid = 0;
	private String data = "";
	private String dataRaw = "";
	private int commFlag = 0;
	private int radio = 0;
	private CommunicationState commState = null;
}
