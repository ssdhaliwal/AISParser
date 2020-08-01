package elsu.ais.parser.message.data;

import java.util.ArrayList;

import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;
import elsu.ais.parser.sentence.AISSentence;

public class CommunicationState_ITDMA {

	public static CommunicationState_ITDMA fromPayload(String messageBits) {
		CommunicationState_ITDMA commState = new CommunicationState_ITDMA();

		commState.parseMessage(messageBits);

		return commState;
	}

	public ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

	public CommunicationState_ITDMA() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 12, 13, "Slot Increment", "increment", "u",
				"Offset to next slot to be used, or zero (0) if no more transmissio"));
		messageBlocks.add(new PayloadBlock(13, 15, 3, "Number of Slots", "slots", "u", "Number of slots to allocate"));
		messageBlocks.add(new PayloadBlock(16, 16, 1, "Keep flag", "keep", "b", "if slot remains allocated for additional frame"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : messageBlocks) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setIncrement(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			case 14:
				setSlots(AISSentence.unsigned_integer_decoder(block.getBits()));
				break;
			case 17:
				setKeep(AISSentence.boolean_decoder(block.getBits()));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"accessScheme\":\"ITDMA\"");
		buffer.append(", \"increment\":" + getIncrement());
		buffer.append(", \"slots\":" + getSlots());
		buffer.append(", \"keep\":" + isKeep());
		buffer.append("}");

		return buffer.toString();
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	private int increment = 0;
	private int slots = 0;
	private boolean keep = false;
}
