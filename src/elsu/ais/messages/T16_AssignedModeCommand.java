package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T16_AssignedModeCommand extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T16_AssignedModeCommand binaryMessage = new T16_AssignedModeCommand();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T16_AssignedModeCommand() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 16"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(40, 69, 30, "Destination A MMSI", "mmsi1", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(70, 81, 12, "Offset A", "offset1", "u", "See [IALA]"));
		getMessageBlocks().add(new AISPayloadBlock(82, 91, 10, "Increment A", "increment1", "u", "See [IALA]"));
		getMessageBlocks().add(new AISPayloadBlock(92, 121, 30, "Destination B MMSI", "mmsi2", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(122, 133, 12, "Offset B", "offset2", "u", "See [IALA]"));
		getMessageBlocks().add(new AISPayloadBlock(134, 143, 10, "Increment B", "increment2", "u", "See [IALA]"));
		// getMessageBlocks().add(new PayloadBlock(144, 147, 4, "Spare", "", "x", "Spare is used for byte boundary to 96 bits"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 92) {
				return;
			} else {
				throw new Exception("parsing error; " + block);
			}
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
			setDestinationMmsiA(parseUINT(block.getBits()));
			break;
		case 70:
			setOffsetA(parseUINT(block.getBits()));
			break;
		case 82:
			setIncrementA(parseUINT(block.getBits()));
			break;
		case 92:
			setDestinationMmsiB(parseUINT(block.getBits()));
			break;
		case 122:
			setOffsetB(parseUINT(block.getBits()));
			break;
		case 134:
			setIncrementB(parseUINT(block.getBits()));
			break;
		}
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
		buffer.append(", \"destinationMmsiA\":" + getDestinationMmsiA());
		buffer.append(", \"offsetA\":" + getOffsetA());
		buffer.append(", \"incrementA\":" + getIncrementA());
		buffer.append(", \"destinationMmsiB\":" + getDestinationMmsiB());
		buffer.append(", \"offsetB\":" + getOffsetB());
		buffer.append(", \"incrementB\":" + getIncrementB());
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
	}

	public int getDestinationMmsiA() {
		return destinationMmsiA;
	}

	public void setDestinationMmsiA(int destinationMmsiA) {
		this.destinationMmsiA = destinationMmsiA;
	}

	public int getOffsetA() {
		return offsetA;
	}

	public void setOffsetA(int offsetA) {
		this.offsetA = offsetA;
	}

	public int getIncrementA() {
		return incrementA;
	}

	public void setIncrementA(int incrementA) {
		this.incrementA = incrementA;
	}

	public int getDestinationMmsiB() {
		return destinationMmsiB;
	}

	public void setDestinationMmsiB(int destinationMmsiB) {
		this.destinationMmsiB = destinationMmsiB;
	}

	public int getOffsetB() {
		return offsetB;
	}

	public void setOffsetB(int offsetB) {
		this.offsetB = offsetB;
	}

	public int getIncrementB() {
		return incrementB;
	}

	public void setIncrementB(int incrementB) {
		this.incrementB = incrementB;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int destinationMmsiA = 0;
	private int offsetA = 0;
	private int incrementA = 0;
	private int destinationMmsiB = 0;
	private int offsetB = 0;
	private int incrementB = 0;
}
