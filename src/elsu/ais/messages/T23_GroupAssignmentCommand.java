package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T23_GroupAssignmentCommand extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T23_GroupAssignmentCommand positionReport = new T23_GroupAssignmentCommand();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public T23_GroupAssignmentCommand() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Unsigned Integer: 23"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "Unsigned Integer: 9 digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 57, 18, "NE Longitude", "ne_lon", "u", "Same as broadcast type 22"));
		getMessageBlocks().add(new AISPayloadBlock(58, 74, 17, "NE Latitude", "ne_lat", "u", "Same as broadcast type 22"));
		getMessageBlocks()
				.add(new AISPayloadBlock(75, 92, 18, "SW Longitude", "sw_lon", "u", "Same as broadcast type 22"));
		getMessageBlocks()
				.add(new AISPayloadBlock(93, 109, 17, "SW Latitude", "sw_lat", "u", "Same as broadcast type 22"));
		getMessageBlocks()
				.add(new AISPayloadBlock(110, 113, 4, "Station Type", "station_type", "e", "See \"Station Types\""));
		getMessageBlocks().add(new AISPayloadBlock(114, 121, 8, "Ship Type", "ship_type", "e", "See \"Ship Types\""));
		// getMessageBlocks().add(new PayloadBlock(122, 143, 22, "Spare", "", "x", "Not used"));
		getMessageBlocks()
				.add(new AISPayloadBlock(144, 145, 2, "Tx/Rx Mode", "txrx", "u", "See \"Transmit/Receive Modes\""));
		getMessageBlocks()
				.add(new AISPayloadBlock(146, 149, 4, "Report Interval", "interval", "e", "See \"Station Intervals\""));
		getMessageBlocks()
				.add(new AISPayloadBlock(150, 153, 4, "Quiet Time", "quiet", "u", "0 = none, 1-15 quiet time in minutes"));
		// getMessageBlocks().add(new PayloadBlock(154, 159, 6, "Spare", "", "x", "Not used"));
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
			setNELongitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 58:
			setNELatitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 75:
			setSWLongitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 93:
			setSWLatitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 110:
			setStationType(parseUINT(block.getBits()));
			break;
		case 114:
			setShipType(parseUINT(block.getBits()));
			break;
		case 144:
			setTxRx(parseUINT(block.getBits()));
			break;
		case 146:
			setInterval(parseUINT(block.getBits()));
			break;
		case 150:
			setQuiteTime(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"neLongitude\":" + getNELongitude());
		buffer.append(", \"neLatitude\":" + getNELatitude());
		buffer.append(", \"swLongitude\":" + getSWLongitude());
		buffer.append(", \"swLatitude\":" + getSWLatitude());
		buffer.append(", \"stationType\":" + getStationType());
		buffer.append(", \"stationTypeText\":\"" + AISLookupValues.getStationType(getStationType()) + "\"");
		buffer.append(", \"shipType\":" + getShipType());
		buffer.append(", \"shipTypeText\":\"" + AISLookupValues.getShipType(getShipType()) + "\"");
		buffer.append(", \"TxRx\":" + getTxRx());
		buffer.append(", \"interval\":" + getInterval());
		buffer.append(", \"intervalText\":\"" + AISLookupValues.getReportingInterval(getInterval()) + "\"");
		buffer.append(", \"quiteTime\":" + getQuiteTime());
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

	public int getStationType() {
		return stationType;
	}

	public void setStationType(int stationType) {
		this.stationType = stationType;
	}

	public int getShipType() {
		return shipType;
	}

	public void setShipType(int shipType) {
		this.shipType = shipType;
	}

	public int getTxRx() {
		return txrx;
	}

	public void setTxRx(int txrx) {
		this.txrx = txrx;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getQuiteTime() {
		return quiteTime;
	}

	public void setQuiteTime(int quiteTime) {
		this.quiteTime = quiteTime;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private float neLongitude = 0f;
	private float neLatitude = 0f;
	private float swLongitude = 0f;
	private float swLatitude = 0f;
	private int stationType = 0;
	private int shipType = 0;
	private int txrx = 0;
	private int interval = 0;
	private int quiteTime = 0;
}
