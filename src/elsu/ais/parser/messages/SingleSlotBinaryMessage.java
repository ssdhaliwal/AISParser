package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class SingleSlotBinaryMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		SingleSlotBinaryMessage binaryMessage = new SingleSlotBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public SingleSlotBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 25"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(
				new PayloadBlock(38, 38, 1, "Destination indicator", "addressed", "b", "0=broadcast, 1=addressed."));
		getMessageBlocks().add(new PayloadBlock(39, 39, 1, "Binary data flag", "structured", "b", "See below"));

		// w/+Destination +ApplicationId
		getMessageBlocks()
				.add(new PayloadBlock(40, 69, 30, 0, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 0, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new PayloadBlock(72, 81, 10, 0, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(82, 87, 6, 0, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(88, 167, 112, 0, "Data", "data", "d", "Binary data"));

		// w/Destination -ApplicationId
		getMessageBlocks()
				.add(new PayloadBlock(40, 69, 30, 1, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 1, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new PayloadBlock(72, 167, 96, 1, "Data", "data", "d", "Binary data"));

		// w/-Destination +ApplicationId
		getMessageBlocks().add(new PayloadBlock(40, 49, 10, 2, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(50, 55, 6, 2, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(56, 167, 112, 2, "Data", "data", "d", "Binary data"));

		// w/-Destination -ApplicationId
		getMessageBlocks().add(new PayloadBlock(40, 167, 128, 3, "Data", "data", "d", "Binary data"));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
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
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"addressed\":" + isAddressed());
		buffer.append(", \"strictured\":" + isStructured());
		buffer.append(", \"destinationMmsi\":" + getDestinationMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"data\":\"" + getData() + "\"");
		if (AISBase.debug) {
			buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		}
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
		return data;
	}

	public void setData(String bits) {
		this.data = bits;
		this.dataRaw = parseTEXT8BIT(bits);
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
}
