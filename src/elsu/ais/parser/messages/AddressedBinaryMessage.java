package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class AddressedBinaryMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		AddressedBinaryMessage binaryMessage = new AddressedBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public AddressedBinaryMessage() {
		initialize();
	}

	public void parseMessage(AddressedBinaryMessage message) {
		this.type = message.getType();
		this.repeat = message.getRepeat();
		this.mmsi = message.getMmsi();
		this.seqno = message.getSeqno();
		this.destinationMmsi = message.getDestinationMmsi();
		this.retransmit = message.isRetransmit();
		this.dac = message.getDac();
		this.fid = message.getFid();
		this.data = message.getData();
		this.dataRaw = message.getDataRaw();
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

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 0:
			setType(unsigned_integer_decoder(block.getBits()));
			break;
		case 6:
			setRepeat(unsigned_integer_decoder(block.getBits()));
			break;
		case 8:
			setMmsi(unsigned_integer_decoder(block.getBits()));
			break;
		case 38:
			setSeqno(unsigned_integer_decoder(block.getBits()));
			break;
		case 40:
			setDestinationMmsi(unsigned_integer_decoder(block.getBits()));
			break;
		case 70:
			setRetransmit(boolean_decoder(block.getBits()));
			break;
		case 72:
			setDac(unsigned_integer_decoder(block.getBits()));
			break;
		case 82:
			setFid(unsigned_integer_decoder(block.getBits()));
			break;
		case 88:
			setData(bit_decoder(block.getBits()));
			break;
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
		buffer.append(", \"retransmit\":" + isRetransmit());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		if (AISBase.debug) {
			buffer.append(", \"data\":\"" + getData() + "\"");
			buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		}
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

	public void setDestinationMmsi(int mmsi) {
		this.destinationMmsi = mmsi;
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
		return data;
	}

	public void setData(String bits) {
		this.data = bits;
		this.dataRaw = text_decoder_8bit(bits);
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int seqno = 0;
	private int destinationMmsi = 0;
	private boolean retransmit = false;
	private int dac = 0;
	private int fid = 0;
	private String data = "";
	private String dataRaw = "";
}
