package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.GNSSMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class GNSSBroadcastBinaryMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		GNSSBroadcastBinaryMessage binaryMessage = new GNSSBroadcastBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public GNSSBroadcastBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 17"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 57, 18, "Longitude", "lon", "I1", "Signed: minutes/10"));
		getMessageBlocks().add(new PayloadBlock(58, 74, 17, "Latitude", "lat", "I1", "Signed: minutes/10"));
		getMessageBlocks().add(new PayloadBlock(75, 79, 5, "Spare", "", "x", "Not used - reserved"));
		getMessageBlocks().add(new PayloadBlock(80, -1, 736, "Payload", "data", "d", "DGNSS correction data"));
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
		case 40:
			setLongitude(AISBase.float_decoder(block.getBits()) / 600f);
			break;
		case 58:
			setLatitude(AISBase.float_decoder(block.getBits()) / 600f);
			break;
		case 80:
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
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"gnssMessage\":\"" + getGNSSMessage() + "\"");
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

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public GNSSMessage getGNSSMessage() {
		return gnssMessage;
	}

	public void setGNSSMessage(GNSSMessage message) {
		this.gnssMessage = message;
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

		gnssMessage = GNSSMessage.fromPayload(getData());
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private float longitude = 0f;
	private float latitude = 0f;
	private GNSSMessage gnssMessage = null;
	private String data = "";
	private String dataRaw = "";
}
