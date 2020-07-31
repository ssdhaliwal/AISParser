package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class AddressedSafetyRelatedMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		AddressedSafetyRelatedMessage binaryMessage = new AddressedSafetyRelatedMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public AddressedSafetyRelatedMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 12"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Sequence Number", "seqno", "u", "Unsigned integer 0-3"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(70, 70, 1, "Retransmit flag", "retransmit", "b", "0 = no retransmit (default), 1 = retransmitted"));
		getMessageBlocks().add(new PayloadBlock(71, 71, 1, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(72, -1, 936, "Text", "text", "t", "1-156 chars of six-bit text. May be shorter than 936 bits."));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
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
			case 38:
				setSeqno(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 40:
				setDestinationMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setRetransmit(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 72:
				setText(AISMessage.text_decoder(block.getBits()));
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
		buffer.append(", \"seqno\":" + getSeqno());
		buffer.append(", \"destinationMmsi\":" + getDestinationMmsi());
		buffer.append(", \"retransmit\":" + getRetransmit());
		buffer.append(", \"text\":" + getText());
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

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public int getDestinationMmsi() {
		return destinationMmsi;
	}

	public void setDestinationMmsi(int destinationMmsi) {
		this.destinationMmsi = destinationMmsi;
	}

	public boolean getRetransmit() {
		return retransmit;
	}

	public void setRetransmit(boolean retransmit) {
		this.retransmit = retransmit;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int seqno = 0;
	private int destinationMmsi = 0;
	private boolean retransmit = false;
	private String text = "";
}
