package elsu.ais.messages.data;

import java.util.ArrayList;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.Sentence;
import elsu.sentence.SentenceBase;

public class CommunicationState {

	public static CommunicationState fromPayload(String messageBits, int messageType) {
		CommunicationState commState = new CommunicationState();

		commState.parseMessage(messageBits, messageType);

		return commState;
	}

	public CommunicationState() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new AISPayloadBlock(0, 1, 2, "Sync State", "state", "u", "see Sync State values"));
		messageBlocks.add(new AISPayloadBlock(2, 18, 17, "message", "message", "m",
				"Specifies frames remaining until a new slot is selected"));
	}

	public void parseMessage(String message, int messageType) {
		for (AISPayloadBlock block : messageBlocks) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setState(Sentence.parseUINT(block.getBits()));
				break;
			case 2:
				setMessage(Sentence.parseUINT(block.getBits()));

				if ((messageType == 1) || (messageType == 2)) { // SOTDMA
					setCommState_SOTDMA(block.getBits());
				} else { // ITDMA
					setCommState_ITDMA(block.getBits());
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"syncState\":" + getState());
		buffer.append(", \"syncStateText\":\"" + AISLookupValues.getCommunicationSyncState(getState()) + "\"");
		if (SentenceBase.logLevel >= 2) {
			buffer.append(", \"message\":" + getMessage());
		}
		if (getCommStateSOTDMA() != null) { // SOTDMA
			buffer.append(", \"csSOTDMA\":" + getCommStateSOTDMA().toString());
		} else if (getCommStateITDMA() != null) { // ITDMA
			buffer.append(", \"csITDMA\":" + getCommStateITDMA().toString());
		}
		buffer.append("}");

		return buffer.toString();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public CommunicationState_SOTDMA getCommStateSOTDMA() {
		return csSOTDMA;
	}

	public void setCommState_SOTDMA(String bits) {
		this.csSOTDMA = CommunicationState_SOTDMA.fromPayload(bits);
	}

	public CommunicationState_ITDMA getCommStateITDMA() {
		return csITDMA;
	}

	public void setCommState_ITDMA(String bits) {
		this.csITDMA = CommunicationState_ITDMA.fromPayload(bits);
	}

	private int state = 0;
	private int message = 0;
	private CommunicationState_SOTDMA csSOTDMA = null;
	private CommunicationState_ITDMA csITDMA = null;

	private ArrayList<AISPayloadBlock> messageBlocks = new ArrayList<>();
}
