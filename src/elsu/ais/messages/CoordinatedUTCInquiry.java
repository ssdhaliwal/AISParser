package elsu.ais.messages;

import elsu.ais.base.AISMessage;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;

public class CoordinatedUTCInquiry extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		CoordinatedUTCInquiry binaryMessage = new CoordinatedUTCInquiry();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public CoordinatedUTCInquiry() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 10"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, "Spare", "", "x", "Not used"));
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
		case 40:
			setDestinationMmsi(parseUINT(block.getBits()));
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
		buffer.append(", \"destinationMmsi\":" + getDestinationMmsi());
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

	public int getDestinationMmsi() {
		return destinationMmsi;
	}

	public void setDestinationMmsi(int destinationMmsi) {
		this.destinationMmsi = destinationMmsi;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int destinationMmsi = 0;
}
