package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T22_ChannelManagement extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T22_ChannelManagement positionReport = new T22_ChannelManagement();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public T22_ChannelManagement() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 22"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(40, 51, 12, "Channel A", "channel_a", "u", "Channel number"));
		getMessageBlocks().add(new AISPayloadBlock(52, 63, 12, "Channel B", "channel_b", "u", "Channel number"));
		getMessageBlocks().add(new AISPayloadBlock(64, 67, 4, "Tx/Rx mode", "txrx", "u", "Transmit/receive mode"));
		getMessageBlocks().add(new AISPayloadBlock(68, 68, 1, "Power", "power", "b", "Low=0, high=1"));
		getMessageBlocks()
				.add(new AISPayloadBlock(69, 86, 18, "NE Longitude", "ne_lon", "I1", "NE longitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new AISPayloadBlock(87, 103, 17, "NE Latitude", "ne_lat", "I1", "NE latitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new AISPayloadBlock(104, 121, 18, "SW Longitude", "sw_lon", "I1", "SW longitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new AISPayloadBlock(122, 138, 17, "SW Latitude", "sw_lat", "I1", "SW latitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new AISPayloadBlock(139, 139, 1, "Addressed", "addressed", "b", "0=Broadcast, 1=Addressed"));
		getMessageBlocks().add(new AISPayloadBlock(140, 140, 1, "Channel A Band", "band_a", "b", "0=Default, 1=12.5kHz"));
		getMessageBlocks().add(new AISPayloadBlock(141, 141, 1, "Channel B Band", "band_b", "b", "0=Default, 1=12.5kHz"));
		getMessageBlocks()
				.add(new AISPayloadBlock(142, 144, 3, "Zone size", "zonesize", "u", "Size of transitional zone"));
		// getMessageBlocks().add(new PayloadBlock(145, 167, 23, "Spare", "", "x", "Reserved for future use"));
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
			setChannelA(parseUINT(block.getBits()));
			break;
		case 52:
			setChannelB(parseUINT(block.getBits()));
			break;
		case 64:
			setTxRx(parseUINT(block.getBits()));
			break;
		case 68:
			setPower(parseBOOLEAN(block.getBits()));
			break;
		case 69:
			setNELongitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 87:
			setNELatitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 104:
			setSWLongitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 122:
			setSWLatitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 139:
			setAddressed(parseBOOLEAN(block.getBits()));
			break;
		case 140:
			setBandA(parseBOOLEAN(block.getBits()));
			break;
		case 141:
			setBandB(parseBOOLEAN(block.getBits()));
			break;
		case 142:
			setZoneSize(parseUINT(block.getBits()));
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
			
			node.put("channelA", getChannelA());
			node.put("channelB", getChannelB());
			node.put("TxRx", getTxRx());
			node.put("power", isPower());
			node.put("neLongitude", getNELongitude());
			node.put("neLatitude", getNELatitude());
			node.put("swLongitude", getSWLongitude());
			node.put("swLatitude", getSWLatitude());
			node.put("addressed", isAddressed());
			node.put("bandA", isBandA());
			node.put("bandB", isBandB());
			node.put("zoneSize", getZoneSize());

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

	public int getChannelA() {
		return channelA;
	}

	public void setChannelA(int channelA) {
		this.channelA = channelA;
	}

	public int getChannelB() {
		return channelB;
	}

	public void setChannelB(int channelB) {
		this.channelB = channelB;
	}

	public int getTxRx() {
		return txrx;
	}

	public void setTxRx(int txrx) {
		this.txrx = txrx;
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public float getNELongitude() {
		return neLongitude;
	}

	public void setNELongitude(float longitude) {
		this.neLongitude = longitude;
	}

	public float getNELatitude() {
		return neLatitude;
	}

	public void setNELatitude(float latitude) {
		this.neLatitude = latitude;
	}

	public float getSWLongitude() {
		return swLongitude;
	}

	public void setSWLongitude(float longitude) {
		this.swLongitude = longitude;
	}

	public float getSWLatitude() {
		return swLatitude;
	}

	public void setSWLatitude(float latitude) {
		this.swLatitude = latitude;
	}

	public boolean isAddressed() {
		return addressed;
	}

	public void setAddressed(boolean addressed) {
		this.addressed = addressed;
	}

	public boolean isBandA() {
		return bandA;
	}

	public void setBandA(boolean band) {
		this.bandA = band;
	}

	public boolean isBandB() {
		return bandB;
	}

	public void setBandB(boolean band) {
		this.bandB = band;
	}

	public int getZoneSize() {
		return zoneSize;
	}

	public void setZoneSize(int zoneSize) {
		this.zoneSize = zoneSize;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int channelA = 0;
	private int channelB = 0;
	private int txrx = 0;
	private boolean power = false;
	private float neLongitude = 181f;
	private float neLatitude = 91f;
	private float swLongitude = 0f;
	private float swLatitude = 0f;
	private boolean addressed = false;
	private boolean bandA = false;
	private boolean bandB = false;
	private int zoneSize = 0;

}
