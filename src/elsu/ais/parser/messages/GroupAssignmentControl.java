package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.CommunicationState;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class GroupAssignmentControl extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		GroupAssignmentControl positionReport = new GroupAssignmentControl();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public GroupAssignmentControl() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Unsigned Integer: 23"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "Unsigned Integer: 9 digits"));
		getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(40, 57, 18, "NE Longitude", "ne_lon", "u", "Same as broadcast type 22"));
		getMessageBlocks().add(new PayloadBlock(58, 74, 17, "NE Latitude", "ne_lat", "u", "Same as broadcast type 22"));
		getMessageBlocks().add(new PayloadBlock(75, 92, 18, "SW Longitude", "sw_lon", "u", "Same as broadcast type 22"));
		getMessageBlocks().add(new PayloadBlock(93, 109, 17, "SW Latitude", "sw_lat", "u", "Same as broadcast type 22"));
		getMessageBlocks().add(new PayloadBlock(110, 113, 4, "Station Type", "station_type", "e", "See \"Station Types\""));
		getMessageBlocks().add(new PayloadBlock(114, 121, 8, "Ship Type", "ship_type", "e", "See \"Ship Types\""));
		getMessageBlocks().add(new PayloadBlock(122, 143, 22, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(144, 145, 2, "Tx/Rx Mode", "txrx", "u", "See \"Transmit/Receive Modes\""));
		getMessageBlocks().add(new PayloadBlock(146, 149, 4, "Report Interval", "interval", "e", "See \"Station Intervals\""));
		getMessageBlocks().add(new PayloadBlock(150, 153, 4, "Quiet Time", "quiet", "u", "0 = none, 1-15 quiet time in minutes"));
		getMessageBlocks().add(new PayloadBlock(154, 159, 6, "Spare", "", "x", "Not used"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setType(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 6:
				setRepeat(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 8:
				setMmsi(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 40:
				setNELongitude(AISBase.float_decoder(block.getBits()) / 600f);
				break;
			case 58:
				setNELatitude(AISBase.float_decoder(block.getBits()) / 600f);
				break;
			case 75:
				setSWLongitude(AISBase.float_decoder(block.getBits()) / 600f);
				break;
			case 93:
				setSWLatitude(AISBase.float_decoder(block.getBits()) / 600f);
				break;
			case 110:
				setStationType(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 114:
				setShipType(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 144:
				setTxRx(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 146:
				setInterval(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 150:
				setQuiteTime(AISBase.unsigned_integer_decoder(block.getBits()));
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
		buffer.append(", \"neLongitude\":" + getNELongitude());
		buffer.append(", \"neLatitude\":" + getNELatitude());
		buffer.append(", \"swLongitude\":" + getSWLongitude());
		buffer.append(", \"swLatitude\":" + getSWLatitude());
		buffer.append(", \"stationType\":" + getStationType());
		buffer.append(", \"stationTypeText\":\"" + LookupValues.getStationType(getStationType()) + "\"");
		buffer.append(", \"shipType\":" + getShipType());
		buffer.append(", \"shipTypeText\":\"" + LookupValues.getShipType(getShipType()) + "\"");
		buffer.append(", \"TxRx\":" + getTxRx());
		buffer.append(", \"interval\":" + getInterval());
		buffer.append(", \"intervalText\":\"" + LookupValues.getReportingInterval(getInterval()) + "\"");
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
