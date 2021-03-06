package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T6_BinaryAddressedMessage extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T6_BinaryAddressedMessage binaryMessage = new T6_BinaryAddressedMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_BinaryAddressedMessage() {
		initialize();
	}

	public void parseMessage(T6_BinaryAddressedMessage message) {
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
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 6"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(38, 39, 2, "Sequence Number", "seqno", "u", "Unsigned integer 0-3"));
		getMessageBlocks().add(new AISPayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(70, 70, 1, "Retransmit flag", "retransmit", "b",
				"0 = no retransmit (default) 1 = retransmitted"));
		// getMessageBlocks().add(new PayloadBlock(71, 71, 1, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(72, 81, 10, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(82, 87, 6, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks()
				.add(new AISPayloadBlock(88, -1, 920, "Data", "data", "d", "Binary data May be shorter than 920 bits."));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 0:
			setType(parseUINT(block.getBits()));
			break;
		case 6:
			setRepeat(parseUINT(block.getBits()));
			break;
		case 8:
			setMmsi(parseUINT(block.getBits()));
			break;
		case 38:
			setSeqno(parseUINT(block.getBits()));
			break;
		case 40:
			setDestinationMmsi(parseUINT(block.getBits()));
			break;
		case 70:
			setRetransmit(parseBOOLEAN(block.getBits()));
			break;
		case 72:
			setDac(parseUINT(block.getBits()));
			break;
		case 82:
			setFid(parseUINT(block.getBits()));
			break;
		case 88:
			setData(parseBITS(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("type", getType());
			node.put("typeText", AISLookupValues.getMessageType(getType()));
			node.put("repeat", getRepeat());
			node.put("mmsi", getMmsi());
			
			node.put("seqno", getSeqno());
			node.put("destinationMmsi", getDestinationMmsi());
			node.put("retransmit", isRetransmit());
			node.put("dac", getDac());
			node.put("fid", getFid());

			if (SentenceBase.logLevel >= 2) {
				node.put("dataBits", getData());
				node.put("dataRaw", getDataRaw());
			}

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
		return dataRaw;
	}

	public void setData(String bits) {
		this.data = bits;
		this.dataRaw = parseTEXT8BIT(bits);
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
