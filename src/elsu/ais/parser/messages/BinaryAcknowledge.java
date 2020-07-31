package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class BinaryAcknowledge extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		BinaryAcknowledge binaryMessage = new BinaryAcknowledge();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public BinaryAcknowledge() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 7"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "MMSI number 1", "mmsi1", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(70, 71, 2, "Sequence for MMSI 1", "mmsiseq1", "u", "Not used"));
		getMessageBlocks().add(new PayloadBlock(72, 101, 30, "MMSI number 2", "mmsi2", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(102, 103, 2, "Sequence for MMSI 2", "mmsiseq2", "u", "Not used"));
		getMessageBlocks().add(new PayloadBlock(104, 133, 30, "MMSI number 3", "mmsi3", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(134, 135, 2, "Sequence for MMSI 3", "mmsiseq3", "u", "Not used"));
		getMessageBlocks().add(new PayloadBlock(136, 165, 30, "MMSI number 4", "mmsi4", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(166, 167, 2, "Sequence for MMSI 4", "mmsiseq4", "u", "Not used"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			try {
				if (block.getEnd() == -1) {
					block.setBits(message.substring(block.getStart(), message.length()));
				} else {
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				}
			} catch (IndexOutOfBoundsException iobe) {
				if (block.getStart() >= 72) {
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
				setMmsi1(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setMmsi1Seq(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 72:
				setMmsi1(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 102:
				setMmsi1Seq(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 104:
				setMmsi1(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 134:
				setMmsi1Seq(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 136:
				setMmsi1(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 166:
				setMmsi1Seq(AISMessage.unsigned_integer_decoder(block.getBits()));
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
		buffer.append(", \"mmsi1\":" + getMmsi1());
		buffer.append(", \"mmsi1Seq\":" + getMmsi1Seq());
		buffer.append(", \"mmsi2\":" + getMmsi2());
		buffer.append(", \"mmsi2Seq\":" + getMmsi2Seq());
		buffer.append(", \"mmsi3\":" + getMmsi3());
		buffer.append(", \"mmsi3Seq\":" + getMmsi3Seq());
		buffer.append(", \"mmsi4\":" + getMmsi4());
		buffer.append(", \"mmsi4Seq\":" + getMmsi4Seq());
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

	public int getMmsi1() {
		return mmsi1;
	}

	public void setMmsi1(int mmsi1) {
		this.mmsi1 = mmsi1;
	}

	public int getMmsi1Seq() {
		return mmsi1Seq;
	}

	public void setMmsi1Seq(int mmsi1Seq) {
		this.mmsi1Seq = mmsi1Seq;
	}

	public int getMmsi2() {
		return mmsi2;
	}

	public void setMmsi2(int mmsi2) {
		this.mmsi2 = mmsi2;
	}

	public int getMmsi2Seq() {
		return mmsi2Seq;
	}

	public void setMmsi2Seq(int mmsi2Seq) {
		this.mmsi2Seq = mmsi2Seq;
	}

	public int getMmsi3() {
		return mmsi3;
	}

	public void setMmsi3(int mmsi3) {
		this.mmsi3 = mmsi3;
	}

	public int getMmsi3Seq() {
		return mmsi3Seq;
	}

	public void setMmsi3Seq(int mmsi3Seq) {
		this.mmsi3Seq = mmsi3Seq;
	}

	public int getMmsi4() {
		return mmsi4;
	}

	public void setMmsi4(int mmsi4) {
		this.mmsi4 = mmsi4;
	}

	public int getMmsi4Seq() {
		return mmsi4Seq;
	}

	public void setMmsi4Seq(int mmsi4Seq) {
		this.mmsi4Seq = mmsi4Seq;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int mmsi1 = 0;
	private int mmsi1Seq = 0;
	private int mmsi2 = 0;
	private int mmsi2Seq = 0;
	private int mmsi3 = 0;
	private int mmsi3Seq = 0;
	private int mmsi4 = 0;
	private int mmsi4Seq = 0;
}
