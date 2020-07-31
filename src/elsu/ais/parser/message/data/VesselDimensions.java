package elsu.ais.parser.message.data;

import java.util.ArrayList;

import elsu.ais.parser.resources.PayloadBlock;
import elsu.ais.parser.sentence.AISSentence;

public class VesselDimensions {

	public static VesselDimensions fromPayload(String messageBits, int messageType) {
		VesselDimensions dimensions = new VesselDimensions();

		dimensions.parseMessage(messageBits, messageType);

		return dimensions;
	}

	public ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

	public VesselDimensions() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 8, 9, "Dimension to Bow", "to_bow", "u", "Meters"));
		messageBlocks.add(new PayloadBlock(9, 17, 9, "Dimension to Stern", "to_stern", "u", "Meters"));
		messageBlocks.add(new PayloadBlock(18, 23, 6, "Dimension to Port", "to_port", "u", "Meters"));
		messageBlocks.add(new PayloadBlock(24, 29, 6, "Dimension to Starboard", "to_starboard", "u", "Meters"));
	}

	public void parseMessage(String message, int messageType) {
		for (PayloadBlock block : messageBlocks) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setToBow(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			case 9:
				setToStern(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			case 18:
				setToPort(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			case 24:
				setToStarboard(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"to_bow\":" + getToBow());
		buffer.append(", \"to_stern\":" + getToStern());
		buffer.append(", \"to_port\":" + getToPort());
		buffer.append(", \"to_starboard\":" + getToStarboard());
		buffer.append("}");

		return buffer.toString();
	}


	public int getToBow() {
		return to_bow;
	}

	public void setToBow(int to_bow) {
		this.to_bow = to_bow;
	}

	public int getToStern() {
		return to_stern;
	}

	public void setToStern(int to_stern) {
		this.to_stern = to_stern;
	}

	public int getToPort() {
		return to_port;
	}

	public void setToPort(int to_port) {
		this.to_port = to_port;
	}

	public int getToStarboard() {
		return to_starboard;
	}

	public void setToStarboard(int to_starboard) {
		this.to_starboard = to_starboard;
	}

	private int to_bow = 0;
	private int to_stern = 0;
	private int to_port = 0;
	private int to_starboard = 0;
}
