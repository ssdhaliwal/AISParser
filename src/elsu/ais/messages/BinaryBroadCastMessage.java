package elsu.ais.messages;

import elsu.ais.base.AISMessage;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;
import elsu.sentence.SentenceBase;

public class BinaryBroadCastMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		BinaryBroadCastMessage binaryMessage = new BinaryBroadCastMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public BinaryBroadCastMessage() {
		initialize();
	}

	public void parseMessage(BinaryBroadCastMessage message) {
		this.type = message.getType();
		this.repeat = message.getRepeat();
		this.mmsi = message.getMmsi();
		this.dac = message.getDac();
		this.fid = message.getFid();
		this.data = message.getData();
		this.dataRaw = message.getDataRaw();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 8"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 49, 10, "Designated Area Code", "dac", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(50, 55, 6, "Functional ID", "fid", "u", "Unsigned integer"));
		getMessageBlocks()
				.add(new PayloadBlock(56, -1, 952, "Data", "data", "d", "Binary data, May be shorter than 952 bits."));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
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
		case 40:
			setDac(parseUINT(block.getBits()));
			break;
		case 50:
			setFid(parseUINT(block.getBits()));
			break;
		case 56:
			setData(parseBITS(block.getBits()));
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
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		if (SentenceBase.logLevel >= 2) {
			buffer.append(", \"dataBits\":\"" + getData() + "\"");
		}
		buffer.append(", \"data\":\"" + getDataRaw() + "\"");
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
		this.dataRaw = parseTEXT8BIT(data);
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int dac = 0;
	private int fid = 0;
	private String data = "";
	private String dataRaw = "";
}
