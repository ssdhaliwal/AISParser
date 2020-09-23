package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.T17_DGNSSCorrectionData;
import elsu.sentence.SentenceBase;

public class T17_DGNSSBroadcastBinaryMessage extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T17_DGNSSBroadcastBinaryMessage binaryMessage = new T17_DGNSSBroadcastBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T17_DGNSSBroadcastBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 17"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(40, 57, 18, "Longitude", "lon", "I1", "Signed: minutes/10"));
		getMessageBlocks().add(new AISPayloadBlock(58, 74, 17, "Latitude", "lat", "I1", "Signed: minutes/10"));
		// getMessageBlocks().add(new PayloadBlock(75, 79, 5, "Spare", "", "x", "Not used - reserved"));
		getMessageBlocks().add(new AISPayloadBlock(80, -1, 736, "Payload", "data", "d", "DGNSS correction data"));
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
		case 40:
			setLongitude(SentenceBase.parseFLOAT(block.getBits()) / 600f);
			break;
		case 58:
			setLatitude(SentenceBase.parseFLOAT(block.getBits()) / 600f);
			break;
		case 80:
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
			
			node.put("longitude", getLongitude());
			node.put("latitude", getLatitude());
			node.set("gnssMessage", SentenceBase.objectMapper.readTree(((getGNSSMessage() != null) ? getGNSSMessage().toString() : "")));

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

	public T17_DGNSSCorrectionData getGNSSMessage() {
		return gnssMessage;
	}

	public void setGNSSMessage(T17_DGNSSCorrectionData message) {
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
		this.dataRaw = parseTEXT8BIT(data);

		gnssMessage = T17_DGNSSCorrectionData.fromPayload(getData());
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private float longitude = 181f;
	private float latitude = 91f;
	private T17_DGNSSCorrectionData gnssMessage = null;
	private String data = "";
	private String dataRaw = "";
}
