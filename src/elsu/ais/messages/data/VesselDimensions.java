package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class VesselDimensions extends AISMessageBase {

	public static VesselDimensions fromPayload(String messageBits) throws Exception {
		VesselDimensions dimensions = new VesselDimensions();

		dimensions.parseMessage(messageBits);

		return dimensions;
	}

	public VesselDimensions() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 8, 9, "Dimension to Bow", "toBow", "u", "Meters"));
		getMessageBlocks().add(new AISPayloadBlock(9, 17, 9, "Dimension to Stern", "toStern", "u", "Meters"));
		getMessageBlocks().add(new AISPayloadBlock(18, 23, 6, "Dimension to Port", "toPort", "u", "Meters"));
		getMessageBlocks().add(new AISPayloadBlock(24, 29, 6, "Dimension to Starboard", "toStarboard", "u", "Meters"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 0:
			setToBow(parseUINT(block.getBits()));
			break;
		case 9:
			setToStern(parseUINT(block.getBits()));
			break;
		case 18:
			setToPort(parseUINT(block.getBits()));
			break;
		case 24:
			setToStarboard(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("toBow", getToBow());
			node.put("toStern", getToStern());
			node.put("toPort", getToPort());
			node.put("toStarboard", getToStarboard());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public int getRepeat() {
		return 0;
	}

	@Override
	public int getMmsi() {
		return 0;
	}

	public int getToBow() {
		return toBow;
	}

	public void setToBow(int toBow) {
		this.toBow = toBow;
	}

	public int getToStern() {
		return toStern;
	}

	public void setToStern(int toStern) {
		this.toStern = toStern;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}

	public int getToStarboard() {
		return toStarboard;
	}

	public void setToStarboard(int toStarboard) {
		this.toStarboard = toStarboard;
	}

	private int toBow = 0;
	private int toStern = 0;
	private int toPort = 0;
	private int toStarboard = 0;
}
