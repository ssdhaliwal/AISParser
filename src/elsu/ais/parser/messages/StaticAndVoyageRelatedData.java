package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.lookups.LookupValues;

public class StaticAndVoyageRelatedData extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StaticAndVoyageRelatedData voyageReport = new StaticAndVoyageRelatedData();
		
		voyageReport.setRawMessage(aisMessage.getRawMessage());
		voyageReport.setBinaryMessage(aisMessage.getBinaryMessage());
		voyageReport.setEncodedMessage(aisMessage.getEncodedMessage());
		voyageReport.setErrorMessage(aisMessage.getErrorMessage());

		voyageReport.parseMessage(messageBits);
		
		return voyageReport;
	}
	
	public StaticAndVoyageRelatedData() {
		initialize();
	}

	private void initialize() {
		ArrayList<_PayloadBlock> messageBlocks = getMessageBlock();
		
		messageBlocks.add(new _PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 5"));
		messageBlocks.add(new _PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "Message repeat count"));
		messageBlocks.add(new _PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		messageBlocks.add(new _PayloadBlock(38, 39, 2, "AIS Version", "ais_version", "u", "0=[ITU1371], 1-3 = future editions"));
		messageBlocks.add(new _PayloadBlock(40, 69, 30, "IMO Number", "imo", "u", "IMO ship ID number"));
		messageBlocks.add(new _PayloadBlock(70, 111, 42, "Call Sign", "callsign", "t", "7 six-bit characters"));
		messageBlocks.add(new _PayloadBlock(112, 231, 120, "Vessel Name", "shipname", "t", "20 six-bit characters"));
		messageBlocks.add(new _PayloadBlock(232, 239, 8, "Ship Type", "shiptype", "e", "See \"Codes for Ship Type\""));
		messageBlocks.add(new _PayloadBlock(240, 248, 9, "Dimension to Bow", "to_bow", "u", "Meters"));
		messageBlocks.add(new _PayloadBlock(249, 257, 9, "Dimension to Stern", "to_stern", "u", "Meters"));
		messageBlocks.add(new _PayloadBlock(258, 263, 6, "Dimension to Port", "to_port", "u", "Meters"));
		messageBlocks.add(new _PayloadBlock(264, 269, 6, "Dimension to Starboard", "to_starboard", "u", "Meters"));
		messageBlocks.add(new _PayloadBlock(270, 273, 4, "Position Fix Type", "epfd", "e", "See \"EPFD Fix Types\""));
		messageBlocks.add(new _PayloadBlock(274, 277, 4, "ETA month (UTC)", "month", "u", "1-12, 0=N/A (default)"));
		messageBlocks.add(new _PayloadBlock(278, 282, 5, "ETA day (UTC)", "day", "u", "1-31, 0=N/A (default)"));
		messageBlocks.add(new _PayloadBlock(283, 287, 5, "ETA hour (UTC)", "hour", "u", "0-23, 24=N/A (default)"));
		messageBlocks.add(new _PayloadBlock(288, 293, 6, "ETA minute (UTC)", "minute", "u", "0-59, 60=N/A (default)"));
		messageBlocks.add(new _PayloadBlock(294, 301, 8, "Draught", "draught", "U1", "Meters/10"));
		messageBlocks.add(new _PayloadBlock(302, 421, 120, "Destination", "destination", "t", "20 6-bit characters"));
		messageBlocks.add(new _PayloadBlock(422, 422, 1, "DTE", "dte", "b", "0=Data terminal ready, 1=Not ready (default)."));
		messageBlocks.add(new _PayloadBlock(423, 423, 1, "Spare", "", "x", "Not used"));
	}

	public void parseMessage(String message) {
		for (_PayloadBlock block : getMessageBlock()) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}
			
			switch (block.getStart()) {
			case 0:
				setType(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 6:
				setRepeat(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 8:
				setMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 38:
				setAisVersion(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 40:
				setImo(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setCallsign(AISMessage.text_decoder(block.getBits()));
				break;
			case 112:
				setShipname(AISMessage.text_decoder(block.getBits()));
				break;
			case 232:
				setShiptype(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 240:
				setToBow(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 249:
				setToStern(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 258:
				setToPort(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 264:
				setToStarboard(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 270:
				setEpfd(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 274:
				setMonth(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 278:
				setDay(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 283:
				setHour(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 288:
				setMinute(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 294:
				setDraught(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 302:
				setDestination(AISMessage.text_decoder(block.getBits()));
				break;
			case 422:
				setDte(AISMessage.boolean_decoder(block.getBits()));
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("{ \"StaticAndVoyageRelatedData\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"aisVersion\":" + getAisVersion());
		buffer.append(", \"imo\":" + getImo());
		buffer.append(", \"callSign\":\"" + getCallsign().trim() + "\"");
		buffer.append(", \"shipName\":\"" + getShipname().trim() + "\"");
		buffer.append(", \"shipType\":\"" + getShiptype() + "/" + LookupValues.getShipType(getShiptype()) + "\"");
		buffer.append(", \"to_bow\":" + getToBow());
		buffer.append(", \"to_stern\":" + getToStern());
		buffer.append(", \"to_port\":" + getToPort());
		buffer.append(", \"to_starboard\":" + getToStarboard());
		buffer.append(", \"epfd\":\"" + getEpfd() + "/" + LookupValues.getEPFDFixType(getEpfd())+ "\"");
		buffer.append(", \"month\":" + getMonth());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"draught\":" + getDraught());
		buffer.append(", \"destination\":\"" + getDestination().trim() + "\"");
		buffer.append(", \"dte\":" + isDte());
		buffer.append("}}");
		
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
		return ais_version;
	}

	public void setAisVersion(int ais_version) {
		this.ais_version = ais_version;
	}

	public int getImo() {
		return imo;
	}

	public void setImo(int imo) {
		this.imo = imo;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign.replace("@", "");
	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname.replace("@", "");
	}

	public int getShiptype() {
		return shiptype;
	}

	public void setShiptype(int shiptype) {
		this.shiptype = shiptype;
	}

	public int getToBow() {
		return to_bow;
	}

	public void setToBow(int to_bow) {
		this.to_bow = to_bow;
	}

	public int getToStern() {
		return to_stern;
	}

	public void setToStern(int to_stern) {
		this.to_stern = to_stern;
	}

	public int getToPort() {
		return to_port;
	}

	public void setToPort(int to_port) {
		this.to_port = to_port;
	}

	public int getToStarboard() {
		return to_starboard;
	}

	public void setToStarboard(int to_starboard) {
		this.to_starboard = to_starboard;
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

	public boolean isDte() {
		return dte;
	}

	public void setDte(boolean dte) {
		this.dte = dte;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int ais_version = 0;
	private int imo = 0;
	private String callsign = "";
	private String shipname = "";
	private int shiptype = 0;
	private int to_bow = 0;
	private int to_stern = 0;
	private int to_port = 0;
	private int to_starboard = 0;
	private int epfd = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private float draught = 0;
	private String destination = "";
	private boolean dte = false;
}