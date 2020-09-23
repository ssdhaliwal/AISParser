package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T15_Interrogation extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T15_Interrogation binaryMessage = new T15_Interrogation();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T15_Interrogation() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 15"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(40, 69, 30, "Interrogated MMSI", "mmsi1", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(70, 75, 6, "First message type", "type1_1", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(76, 87, 12, "First slot offset", "offset1_1", "u", "Unsigned integer"));
		// getMessageBlocks().add(new PayloadBlock(88, 89, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(90, 95, 6, "Second message type", "type1_2", "u", "Unsigned integer"));
		getMessageBlocks()
				.add(new AISPayloadBlock(96, 107, 12, "Second slot offset", "offset1_2", "u", "Unsigned integer"));
		// getMessageBlocks().add(new PayloadBlock(108, 109, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(110, 139, 30, "Interrogated MMSI", "mmsi2", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(140, 145, 6, "First message type", "type2_1", "u", "Unsigned integer"));
		getMessageBlocks()
				.add(new AISPayloadBlock(146, 157, 12, "First slot offset", "offset2_1", "u", "Unsigned integer"));
		// getMessageBlocks().add(new PayloadBlock(158, 159, 2, "Spare", "", "x", "Not used"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 76) {
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
			setType11(parseUINT(block.getBits()));
			break;
		case 76:
			setOffset11(parseUINT(block.getBits()));
			break;
		case 90:
			setType12(parseUINT(block.getBits()));
			break;
		case 96:
			setOffset12(parseUINT(block.getBits()));
			break;
		case 110:
			setMmsi2(parseUINT(block.getBits()));
			break;
		case 140:
			setType21(parseUINT(block.getBits()));
			break;
		case 146:
			setOffset21(parseUINT(block.getBits()));
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
			node.put("type11", getType11());
			node.put("offset11", getOffset11());
			node.put("type12", getType12());
			node.put("offset12", getOffset12());
			node.put("mmsi2", getMmsi2());
			node.put("type21", getType21());
			node.put("offset21", getOffset21());

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

	public int getType11() {
		return type11;
	}

	public void setType11(int type11) {
		this.type11 = type11;
	}

	public int getOffset11() {
		return offset11;
	}

	public void setOffset11(int offset11) {
		this.offset11 = offset11;
	}

	public int getType12() {
		return type12;
	}

	public void setType12(int type12) {
		this.type12 = type12;
	}

	public int getOffset12() {
		return offset12;
	}

	public void setOffset12(int offset12) {
		this.offset12 = offset12;
	}

	public int getMmsi2() {
		return mmsi2;
	}

	public void setMmsi2(int mmsi2) {
		this.mmsi2 = mmsi2;
	}

	public int getType21() {
		return type21;
	}

	public void setType21(int type21) {
		this.type21 = type21;
	}

	public int getOffset21() {
		return offset21;
	}

	public void setOffset21(int offset21) {
		this.offset21 = offset21;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int mmsi1 = 0;
	private int type11 = 0;
	private int offset11 = 0;
	private int type12 = 0;
	private int offset12 = 0;
	private int mmsi2 = 0;
	private int type21 = 0;
	private int offset21 = 0;
}
