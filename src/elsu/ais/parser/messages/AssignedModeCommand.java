package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class AssignedModeCommand extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		AssignedModeCommand binaryMessage = new AssignedModeCommand();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public AssignedModeCommand() {
		initialize();
	}
	
	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 16"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "Destination A MMSI", "mmsi1", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(70, 81, 12, "Offset A", "offset1", "u", "See [IALA]"));
		getMessageBlocks().add(new PayloadBlock(82, 91, 10, "Increment A", "increment1", "u", "See [IALA]"));
		getMessageBlocks().add(new PayloadBlock(92, 121, 30, "Destination B MMSI", "mmsi2", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(122, 133, 12, "Offset B", "offset2", "u", "See [IALA]"));
		getMessageBlocks().add(new PayloadBlock(134, 143, 10, "Increment B", "increment2", "u", "See [IALA]"));
		getMessageBlocks().add(new PayloadBlock(144, 147, 4, "Spare", "", "x", "Spare is used for byte boundary to 96 bits"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			try {
				if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
					block.setBits(message.substring(block.getStart(), message.length()));
				} else {
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				}
			} catch (IndexOutOfBoundsException iobe) {
				if (block.getStart() >= 92) {
					continue;
				}
			}

			switch (block.getStart()) {
			case 0:
				setType(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 6:
				setRepeat(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 8:
				setMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 40:
				setDestinationMmsiA(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setOffsetA(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 82:
				setIncrementA(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 92:
				setDestinationMmsiB(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 122:
				setOffsetB(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 134:
				setIncrementB(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
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
		buffer.append(", \"destinationMmsiA\":" + getDestinationMmsiA());
		buffer.append(", \"offsetA\":" + getOffsetA());
		buffer.append(", \"incrementA\":" + getIncrementA());
		buffer.append(", \"destinationMmsiB\":" + getDestinationMmsiB());
		buffer.append(", \"offsetB\":" + getOffsetB());
		buffer.append(", \"incrementB\":" + getIncrementB());
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