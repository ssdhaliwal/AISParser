package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T7_BinaryAcknowledgement extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T7_BinaryAcknowledgement binaryMessage = new T7_BinaryAcknowledgement();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T7_BinaryAcknowledgement() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 7"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(40, 69, 30, "MMSI number 1", "mmsi1", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(70, 71, 2, "Sequence for MMSI 1", "mmsiseq1", "u", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(72, 101, 30, "MMSI number 2", "mmsi2", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(102, 103, 2, "Sequence for MMSI 2", "mmsiseq2", "u", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(104, 133, 30, "MMSI number 3", "mmsi3", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(134, 135, 2, "Sequence for MMSI 3", "mmsiseq3", "u", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(136, 165, 30, "MMSI number 4", "mmsi4", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(166, 167, 2, "Sequence for MMSI 4", "mmsiseq4", "u", "Not used"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 72) {
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
			setMmsi1(parseUINT(block.getBits()));
			break;
		case 70:
			setMmsi1Seq(parseUINT(block.getBits()));
			break;
		case 72:
			setMmsi1(parseUINT(block.getBits()));
			break;
		case 102:
			setMmsi1Seq(parseUINT(block.getBits()));
			break;
		case 104:
			setMmsi1(parseUINT(block.getBits()));
			break;
		case 134:
			setMmsi1Seq(parseUINT(block.getBits()));
			break;
		case 136:
			setMmsi1(parseUINT(block.getBits()));
			break;
		case 166:
			setMmsi1Seq(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("type", getType());
			node.put("typeText", AISLookupValues.getMessageType(getType()));
			node.put("repeat", getRepeat());
			node.put("mmsi", getMmsi());
			
			node.put("mmsi1", getMmsi1());
			node.put("mmsi1Seq", getMmsi1Seq());
			node.put("mmsi2", getMmsi2());
			node.put("mmsi2Seq", getMmsi2Seq());
			node.put("mmsi3", getMmsi3());
			node.put("mmsi3Seq", getMmsi3Seq());
			node.put("mmsi4", getMmsi4());
			node.put("mmsi4Seq", getMmsi4Seq());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
