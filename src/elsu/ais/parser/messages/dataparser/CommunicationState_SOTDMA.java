package elsu.ais.parser.messages.dataparser;

import java.util.ArrayList;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class CommunicationState_SOTDMA {

	public static CommunicationState_SOTDMA fromPayload(String messageBits) {
		CommunicationState_SOTDMA commState = new CommunicationState_SOTDMA();

		commState.parseMessage(messageBits);

		return commState;
	}

	public ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

	public CommunicationState_SOTDMA() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 2, 3, "Slot time-out", "timeout", "u",
				"Specifies frames remaining until a new slot is selected"));
		messageBlocks.add(new PayloadBlock(4, 16, 14, "Sub message", "message", "u", "9 decimal digits"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : messageBlocks) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setTimeout(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 4:
			switch (getTimeout()) {
			case 1:
				int hour = AISMessage.unsigned_integer_decoder(block.getBits().substring(0, 10));
				int minute = AISMessage.unsigned_integer_decoder(block.getBits().substring(10, 12));
				setUtcHourMinute(hour + ":" + minute);
				break;
			case 2:
			case 4:
			case 6:
				setSlotNumber(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 3:
			case 5:
			case 7:
				setReceivedStations(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
			break;
		}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"CommunicationState_SOTDMA\": {");
		buffer.append("\"timeout\":" + getTimeout());
		switch(getTimeout()) {
		case 0:
			buffer.append(", \"slotoffset\":" + getSlotOffset());
			break;
		case 1:
			buffer.append(", \"UTChourmin\":" + getUtcHourMinute());
			break;
		case 2:
		case 4:
		case 6:
			buffer.append(", \"slotnumber\":" + getSlotNumber());
			break;
		case 3:
		case 5:
		case 7:
			buffer.append(", \"receivedstations\":" + getReceivedStations());
			break;
		}
		buffer.append("}}");

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
}
