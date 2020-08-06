package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class ChannelManagement extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		ChannelManagement positionReport = new ChannelManagement();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public ChannelManagement() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 22"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 51, 12, "Channel A", "channel_a", "u", "Channel number"));
		getMessageBlocks().add(new PayloadBlock(52, 63, 12, "Channel B", "channel_b", "u", "Channel number"));
		getMessageBlocks().add(new PayloadBlock(64, 67, 4, "Tx/Rx mode", "txrx", "u", "Transmit/receive mode"));
		getMessageBlocks().add(new PayloadBlock(68, 68, 1, "Power", "power", "b", "Low=0, high=1"));
		getMessageBlocks()
				.add(new PayloadBlock(69, 86, 18, "NE Longitude", "ne_lon", "I1", "NE longitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new PayloadBlock(87, 103, 17, "NE Latitude", "ne_lat", "I1", "NE latitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new PayloadBlock(104, 121, 18, "SW Longitude", "sw_lon", "I1", "SW longitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new PayloadBlock(122, 138, 17, "SW Latitude", "sw_lat", "I1", "SW latitude to 0.1 minutes"));
		getMessageBlocks()
				.add(new PayloadBlock(139, 139, 1, "Addressed", "addressed", "b", "0=Broadcast, 1=Addressed"));
		getMessageBlocks().add(new PayloadBlock(140, 140, 1, "Channel A Band", "band_a", "b", "0=Default, 1=12.5kHz"));
		getMessageBlocks().add(new PayloadBlock(141, 141, 1, "Channel B Band", "band_b", "b", "0=Default, 1=12.5kHz"));
		getMessageBlocks()
				.add(new PayloadBlock(142, 144, 3, "Zone size", "zonesize", "u", "Size of transitional zone"));
		// getMessageBlocks().add(new PayloadBlock(145, 167, 23, "Spare", "", "x", "Reserved for future use"));
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
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"channelA\":" + getChannelA());
		buffer.append(", \"channelB\":" + getChannelB());
		buffer.append(", \"TxRx\":" + getTxRx());
		buffer.append(", \"power\":" + isPower());
		buffer.append(", \"neLongitude\":" + getNELongitude());
		buffer.append(", \"neLatitude\":" + getNELatitude());
		buffer.append(", \"swLongitude\":" + getSWLongitude());
		buffer.append(", \"swLatitude\":" + getSWLatitude());
		buffer.append(", \"addressed\":" + isAddressed());
		buffer.append(", \"bandA\":" + isBandA());
		buffer.append(", \"bandB\":" + isBandB());
		buffer.append(", \"zoneSize\":" + getZoneSize());
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
	private float neLongitude = 0f;
	private float neLatitude = 0f;
	private float swLongitude = 0f;
	private float swLatitude = 0f;
	private boolean addressed = false;
	private boolean bandA = false;
	private boolean bandB = false;
	private int zoneSize = 0;

}
