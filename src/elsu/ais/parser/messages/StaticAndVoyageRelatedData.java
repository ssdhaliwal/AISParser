package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.VesselDimensions;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StaticAndVoyageRelatedData extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		StaticAndVoyageRelatedData voyageReport = new StaticAndVoyageRelatedData();
		voyageReport.parseMessage(messageBits);

		return voyageReport;
	}

	public StaticAndVoyageRelatedData() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 5"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "Message repeat count"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(
				new PayloadBlock(38, 39, 2, "AIS Version", "ais_version", "u", "0=[ITU1371], 1-3 = future editions"));
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "IMO Number", "imo", "u", "IMO ship ID number"));
		getMessageBlocks().add(new PayloadBlock(70, 111, 42, "Call Sign", "callsign", "t", "7 six-bit characters"));
		getMessageBlocks()
				.add(new PayloadBlock(112, 231, 120, "Vessel Name", "shipname", "t", "20 six-bit characters"));
		getMessageBlocks()
				.add(new PayloadBlock(232, 239, 8, "Ship Type", "shiptype", "e", "See \"Codes for Ship Type\""));
		getMessageBlocks().add(new PayloadBlock(240, 269, 30, "Vessel Dimensions", "dimension", "u", "Meters"));
		getMessageBlocks()
				.add(new PayloadBlock(270, 273, 4, "Position Fix Type", "epfd", "e", "See \"EPFD Fix Types\""));
		getMessageBlocks().add(new PayloadBlock(274, 277, 4, "ETA month (UTC)", "month", "u", "1-12, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(278, 282, 5, "ETA day (UTC)", "day", "u", "1-31, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(283, 287, 5, "ETA hour (UTC)", "hour", "u", "0-23, 24=N/A (default)"));
		getMessageBlocks()
				.add(new PayloadBlock(288, 293, 6, "ETA minute (UTC)", "minute", "u", "0-59, 60=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(294, 301, 8, "Draught", "draught", "U1", "Meters/10"));
		getMessageBlocks()
				.add(new PayloadBlock(302, 421, 120, "Destination", "destination", "t", "20 6-bit characters"));
		getMessageBlocks()
				.add(new PayloadBlock(422, 422, 1, "DTE", "dte", "u", "0=Data terminal ready, 1=Not ready (default)."));
		// getMessageBlocks().add(new PayloadBlock(423, 423, 1, "Spare", "", "x", "Not used"));
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
		case 38:
			setAisVersion(parseUINT(block.getBits()));
			break;
		case 40:
			setImo(parseUINT(block.getBits()));
			break;
		case 70:
			setCallSign(parseTEXT(block.getBits()));
			break;
		case 112:
			setShipName(parseTEXT(block.getBits()));
			break;
		case 232:
			setShipType(parseUINT(block.getBits()));
			break;
		case 240:
			setDimension(block.getBits());
			break;
		case 270:
			setEpfd(parseUINT(block.getBits()));
			break;
		case 274:
			setMonth(parseUINT(block.getBits()));
			break;
		case 278:
			setDay(parseUINT(block.getBits()));
			break;
		case 283:
			setHour(parseUINT(block.getBits()));
			break;
		case 288:
			setMinute(parseUINT(block.getBits()));
			break;
		case 294:
			setDraught(parseUFLOAT(block.getBits()) / 10f);
			break;
		case 302:
			setDestination(parseTEXT(block.getBits()));
			break;
		case 422:
			setDte(parseUINT(block.getBits()));
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
		buffer.append(", \"aisVersion\":" + getAisVersion());
		buffer.append(", \"imo\":" + getImo());
		buffer.append(", \"callSign\":\"" + getCallSign().trim() + "\"");
		buffer.append(", \"shipName\":\"" + getShipName().trim() + "\"");
		buffer.append(", \"shipType\":" + getShipType());
		buffer.append(", \"shipTypeText\":\"" + LookupValues.getShipType(getShipType()) + "\"");
		buffer.append(", \"dimension\":" + getDimension());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + LookupValues.getEPFDFixType(getEpfd()) + "\"");
		buffer.append(", \"month\":" + getMonth());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"draught\":" + getDraught());
		buffer.append(", \"destination\":\"" + getDestination().trim() + "\"");
		buffer.append(", \"dte\":" + getDte());
		buffer.append(", \"dteText\":\"" + LookupValues.getDte(getDte()) + "\"");
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

	public int getAisVersion() {
		return aisVersion;
	}

	public void setAisVersion(int aisVersion) {
		this.aisVersion = aisVersion;
	}

	public int getImo() {
		return imo;
	}

	public void setImo(int imo) {
		this.imo = imo;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign.replace("@", "");
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName.replace("@", "");
	}

	public int getShipType() {
		return shipType;
	}

	public void setShipType(int shipType) {
		this.shipType = shipType;
	}

	public VesselDimensions getDimension() {
		return dimension;
	}

	public void setDimension(String bits) {
		try {
			this.dimension = VesselDimensions.fromPayload(bits);
		} catch (Exception exi) {}
	}

	public int getEpfd() {
		return epfd;
	}

	public void setEpfd(int epfd) {
		this.epfd = epfd;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public float getDraught() {
		return draught;
	}

	public void setDraught(float draught) {
		this.draught = draught;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination.replace("@", "");
	}

	public int getDte() {
		return dte;
	}

	public void setDte(int dte) {
		this.dte = dte;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int aisVersion = 0;
	private int imo = 0;
	private String callSign = "";
	private String shipName = "";
	private int shipType = 0;
	private VesselDimensions dimension = null;
	private int epfd = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private float draught = 0;
	private String destination = "";
	private int dte = 1;
}