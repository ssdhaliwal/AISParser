package elsu.ais.parser.messages.dataparser;

import java.util.ArrayList;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.messages.PositionReportClassA;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class CommunicationState {

	public static CommunicationState fromPayload(String messageBits) {
		CommunicationState commState = new CommunicationState();

		commState.parseMessage(messageBits);

		return commState;
	}

	public ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

	public CommunicationState() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 1, 2, "Sync State", "state", "u", "see Sync State values"));
		messageBlocks.add(new PayloadBlock(2, 4, 3, "Slot time-out", "timeout", "u",
				"Specifies frames remaining until a new slot is selected"));
		messageBlocks.add(new PayloadBlock(5, 18, 14, "Sub message", "message", "u", "9 decimal digits"));
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
				setState(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 2:
				setTimeout(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 5:
				switch (getTimeout()) {
				case 0:
					setMsgSlotOffset(AISMessage.unsigned_integer_decoder(block.getBits()));
					break;
				case 1:
					int hour = AISMessage.unsigned_integer_decoder(block.getBits().substring(0, 10));
					int minute = AISMessage.unsigned_integer_decoder(block.getBits().substring(10, 12));
					setMsgUTCHourMinute(hour + ":" + minute);
					break;
				case 2:
				case 4:
				case 6:
					setMsgSlotNumber(AISMessage.unsigned_integer_decoder(block.getBits()));
					break;
				case 3:
				case 5:
				case 7:
					setMsgReceivedStations(AISMessage.unsigned_integer_decoder(block.getBits()));
					break;
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"CommunicationState\": {");
		buffer.append("\"syncstate\":\"" + getState() + "/" + LookupValues.getCommunicationSyncState(getState()) + "\"");
		buffer.append(", \"timeout\":" + getTimeout());
		buffer.append(", \"message\":" + getMessage());
		switch(getTimeout()) {
		case 0:
			buffer.append(", \"slotoffset\":" + getMsgSlotOffset());
			break;
		case 1:
			buffer.append(", \"UTChourmin\":" + getMsgUTCHourMinute());
			break;
		case 2:
		case 4:
		case 6:
			buffer.append(", \"slotnumber\":" + getMsgSlotNumber());
			break;
		case 3:
		case 5:
		case 7:
			buffer.append(", \"receivedstations\":" + getMsgReceivedStations());
			break;
		}
		buffer.append("}}");

		return buffer.toString();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public int getMsgReceivedStations() {
		return msgReceivedStations;
	}

	public void setMsgReceivedStations(int msgReceivedStations) {
		this.msgReceivedStations = msgReceivedStations;
	}

	public int getMsgSlotNumber() {
		return msgSlotNumber;
	}

	public void setMsgSlotNumber(int msgSlotNumber) {
		this.msgSlotNumber = msgSlotNumber;
	}

	public String getMsgUTCHourMinute() {
		return msgUTCHourMinute;
	}

	public void setMsgUTCHourMinute(String msgUTCHourMinute) {
		this.msgUTCHourMinute = msgUTCHourMinute;
	}

	public int getMsgSlotOffset() {
		return msgSlotOffset;
	}

	public void setMsgSlotOffset(int msgSlotOffset) {
		this.msgSlotOffset = msgSlotOffset;
	}

	private int state = 0;
	private int timeout = 0;
	private int message = 0;
	private int msgReceivedStations = 0;
	private int msgSlotNumber = 0;
	private String msgUTCHourMinute = "";
	private int msgSlotOffset = 0;

}
