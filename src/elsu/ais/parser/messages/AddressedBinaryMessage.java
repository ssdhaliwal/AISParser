package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class AddressedBinaryMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		AddressedBinaryMessage binaryMessage = new AddressedBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public AddressedBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 6"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Sequence Number", "seqno", "u", "Unsigned integer 0-3"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(70, 70, 1, "Retransmit flag", "retransmit", "b",
				"0 = no retransmit (default) 1 = retransmitted"));
		getMessageBlocks().add(new PayloadBlock(71, 71, 1, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(72, 81, 10, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(82, 87, 6, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks()
				.add(new PayloadBlock(88, -1, 920, "Data", "data", "d", "Binary data May be shorter than 920 bits."));
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
				setDestMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setRetransmit(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 72:
				setDac(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 82:
				setFid(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 88:
				setData(AISMessage.bit_decoder(block.getBits()));
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
		buffer.append(", \"dest_mmsi\":" + getDestMmsi());
		buffer.append(", \"retransmit\":" + isRetransmit());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
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

	public int getDestMmsi() {
		return dest_mmsi;
	}

	public void setDestMmsi(int dest_mmsi) {
		this.dest_mmsi = dest_mmsi;
	}

	public boolean isRetransmit() {
		return retransmit;
	}

	public void setRetransmit(boolean retransmit) {
		this.retransmit = retransmit;
	}

	public int getDac() {
		return dac;
	}

	public void setDac(int dac) {
		this.dac = dac;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getData() {
		return data;
	}

	public String getDataRaw() {
		return dataRaw;
	}

	public void setData(String data) {
		this.data = data;
		this.dataRaw = text_decoder_8bit(data);
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int seqno = 0;
	private int dest_mmsi = 0;
	private boolean retransmit = false;
	private int dac = 0;
	private int fid = 0;
	private String data = "";
	private String dataRaw = "";
}
