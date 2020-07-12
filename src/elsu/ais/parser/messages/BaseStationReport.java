package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.lookups.LookupValues;

public class BaseStationReport extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		BaseStationReport stationReport = new BaseStationReport();
		
		stationReport.setRawMessage(aisMessage.getRawMessage());
		stationReport.setBinaryMessage(aisMessage.getBinaryMessage());
		stationReport.setEncodedMessage(aisMessage.getEncodedMessage());
		stationReport.setErrorMessage(aisMessage.getErrorMessage());

		stationReport.parseMessage(messageBits);
		
		return stationReport;
	}
	
	public BaseStationReport() {
		initialize();
	}

	private void initialize() {
		ArrayList<_PayloadBlock> messageBlocks = getMessageBlock();
		
		messageBlocks.add(new _PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 4"));
		messageBlocks.add(new _PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		messageBlocks.add(new _PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		messageBlocks.add(new _PayloadBlock(38, 51, 14, "Year (UTC)", "year", "u", "UTC, 1-9999, 0 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(52, 55, 4, "Month (UTC)", "month", "u", "1-12; 0 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(56, 60, 5, "Day (UTC)", "day", "u", "1-31; 0 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(61, 65, 5, "Hour (UTC)", "hour", "u", "0-23; 24 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(66, 71, 6, "Minute (UTC)", "minute", "u", "0-59; 60 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(72, 77, 6, "Second (UTC)", "second", "u", "0-59; 60 = N/A (default)"));
		messageBlocks.add(new _PayloadBlock(78, 78, 1, "Fix quality", "accuracy", "b", "As in Common Navigation Block"));
		messageBlocks.add(new _PayloadBlock(79, 106, 28, "Longitude", "lon", "I4", "As in Common Navigation Block"));
		messageBlocks.add(new _PayloadBlock(107, 133, 27, "Latitude", "lat", "I4", "As in Common Navigation Block"));
		messageBlocks.add(new _PayloadBlock(134, 137, 4, "Type of EPFD", "epfd", "e", "See \"EPFD Fix Types\""));
		messageBlocks.add(new _PayloadBlock(138, 147, 10, "Spare", "", "x", "Not used"));
		messageBlocks.add(new _PayloadBlock(148, 148, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		messageBlocks.add(new _PayloadBlock(149, 167, 19, "SOTDMA state", "radio", "u", "As in same bits for Type 1"));
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
				setYear(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 52:
				setMonth(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 56:
				setDay(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 61:
				setHour(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 66:
				setMinute(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 72:
				setSecond(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 78:
				setAccuracy(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 79:
				setLon(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 107:
				setLat(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 134:
				setEpfd(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 138:
				break;
			case 148:
				setRaim(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 149:
				setRadio(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("{ \"BaseStationReport\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"year\":" + getYear());
		buffer.append(", \"month\":" + getMonth());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"lon\":" + getLon());
		buffer.append(", \"lat\":" + getLat());
		buffer.append(", \"epfd\":\"" + getEpfd() + "/" + LookupValues.getEPFDFixType(getEpfd()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"radio\":" + getRadio());
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public boolean isAccuracy() {
		return accuracy;
	}

	public void setAccuracy(boolean accuracy) {
		this.accuracy = accuracy;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public int getEpfd() {
		return epfd;
	}

	public void setEpfd(int epfd) {
		this.epfd = epfd;
	}

	public boolean isRaim() {
		return raim;
	}

	public void setRaim(boolean raim) {
		this.raim = raim;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private boolean accuracy = false;
	private float lon = 0.0f;
	private float lat = 0.0f;
	private int epfd = 0;
	private boolean raim = false;
	private int radio = 0;
}