package elsu.ais.messages.data;

import java.util.ArrayList;

import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.Sentence;

public class CommunicationState_SOTDMA {

	public static CommunicationState_SOTDMA fromPayload(String messageBits) {
		CommunicationState_SOTDMA commState = new CommunicationState_SOTDMA();

		commState.parseMessage(messageBits);

		return commState;
	}

	public CommunicationState_SOTDMA() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new AISPayloadBlock(0, 2, 3, "Slot time-out", "timeout", "u",
				"Specifies frames remaining until a new slot is selected"));
		messageBlocks.add(new AISPayloadBlock(4, 16, 14, "Sub message", "message", "u", "9 decimal digits"));
	}

	public void parseMessage(String message) {
		for (AISPayloadBlock block : messageBlocks) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setTimeout(Sentence.parseUINT(block.getBits()));
				break;
			case 4:
				switch (getTimeout()) {
				case 1:
					int hour = Sentence.parseUINT(block.getBits().substring(0, 10));
					int minute = Sentence.parseUINT(block.getBits().substring(10, 12));
					setUtcHourMinute(hour + ":" + minute);
					break;
				case 2:
				case 4:
				case 6:
					setSlotNumber(Sentence.parseUINT(block.getBits()));
					break;
				case 3:
				case 5:
				case 7:
					setReceivedStations(Sentence.parseUINT(block.getBits()));
					break;
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"accessScheme\":\"SOTDMA\"");
		buffer.append(", \"timeout\":" + getTimeout());
		switch (getTimeout()) {
		case 0:
			buffer.append(", \"slotOffset\":" + getSlotOffset());
			break;
		case 1:
			buffer.append(", \"UTCHourMin\":" + getUtcHourMinute());
			break;
		case 2:
		case 4:
		case 6:
			buffer.append(", \"slotNumber\":" + getSlotNumber());
			break;
		case 3:
		case 5:
		case 7:
			buffer.append(", \"receivedStations\":" + getReceivedStations());
			break;
		}
		buffer.append("}");

		return buffer.toString();
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getReceivedStations() {
		return receivedStations;
	}

	public void setReceivedStations(int receivedStations) {
		this.receivedStations = receivedStations;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getUtcHourMinute() {
		return utcHourMinute;
	}

	public void setUtcHourMinute(String utcHourMinute) {
		this.utcHourMinute = utcHourMinute;
	}

	public int getSlotOffset() {
		return slotOffset;
	}

	public void setSlotOffset(int slotOffset) {
		this.slotOffset = slotOffset;
	}

	private int timeout = 0;
	private int receivedStations = 0;
	private int slotNumber = 0;
	private String utcHourMinute = "";
	private int slotOffset = 0;

	private ArrayList<AISPayloadBlock> messageBlocks = new ArrayList<>();
}
