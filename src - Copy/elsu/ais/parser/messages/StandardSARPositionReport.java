package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.messages.dataparser.CommunicationState;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StandardSARPositionReport extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StandardSARPositionReport positionReport = new StandardSARPositionReport();

		positionReport.setRawMessage(aisMessage.getRawMessage());
		positionReport.setBinaryMessage(aisMessage.getBinaryMessage());
		positionReport.setEncodedMessage(aisMessage.getEncodedMessage());
		positionReport.setErrorMessage(aisMessage.getErrorMessage());

		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public StandardSARPositionReport() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 9"));
		messageBlocks.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		messageBlocks.add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		messageBlocks.add(new PayloadBlock(38, 49, 12, "Altitude", "alt", "u", "See below"));
		messageBlocks.add(new PayloadBlock(50, 59, 10, "SOG", "speed", "u", "See below"));
		messageBlocks.add(new PayloadBlock(60, 60, 1, "Position Accuracy", "accuracy", "u", "See below"));
		messageBlocks.add(new PayloadBlock(61, 88, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		messageBlocks.add(new PayloadBlock(89, 115, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		messageBlocks.add(new PayloadBlock(116, 127, 12, "Course Over Ground", "course", "U1", "True bearing, 0.1 degree units"));
		messageBlocks.add(new PayloadBlock(128, 133, 6, "Time Stamp", "second", "u", "UTC second."));
		messageBlocks.add(new PayloadBlock(134, 134, 1, "Altitude Sensor", "altsensor", "u", "0=GNSS;1=barometric source"));
		messageBlocks.add(new PayloadBlock(135, 141, 7, "Regional reserved", "regional", "x", "Reserved"));
		messageBlocks.add(new PayloadBlock(142, 142, 1, "DTE", "dte", "u", "0=Data terminal ready, 1=Data terminal not ready (default)"));
		messageBlocks.add(new PayloadBlock(143, 145, 3, "Spare", "", "x", "Not used"));
		messageBlocks.add(new PayloadBlock(146, 146, 1, "Assigned", "assigned", "b", "Assigned-mode flag"));
		messageBlocks.add(new PayloadBlock(147, 147, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		messageBlocks.add(new PayloadBlock(148, 148, 1, "Comm State Selector", "commflag", "u", "0=SOTDMA, 1=ITDMA"));
		messageBlocks.add(new PayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See [IALA] for details."));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : messageBlocks) {
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
				setAltitude(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 50:
				setSpeed(AISMessage.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 60:
				setAccuracy(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 61:
				setLon(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 89:
				setLat(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 116:
				setCourse(AISMessage.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 128:
				setSecond(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 134:
				setAltitudesensor(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 142:
				setDte(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 146:
				setAssigned(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 147:
				setRaim(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 148:
				setCommflag(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 149:
				setRadio(AISMessage.unsigned_integer_decoder(block.getBits()));
				setCommState(block.getBits());
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"PositionReportClassA\": {");
		buffer.append("\"transponder\":" + getTransponderType());
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"altitude\":\"" + getAltitude());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"lon\":" + getLon());
		buffer.append(", \"lat\":" + getLat());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(
				", \"altitudesensor\":\"" + getAltitudesensor() + "/" + LookupValues.getAltitudeSensor(getAltitudesensor()) + "\"");
		buffer.append(", \"assigned\":" + isAssigned());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"commflag\":\"" + getCommflag() + "/" + LookupValues.getCommunicationFlag(getCommflag()) + "\"");
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState().toString());
		buffer.append(", \"commtech\":\"" + LookupValues.getCommunicationTechnology(getType()) + "\"");
		buffer.append("}}");

		return buffer.toString();
	}

	public String getTransponderType() {
		return "A";
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

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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

	public float getCourse() {
		return course;
	}

	public void setCourse(float course) {
		this.course = course;
	}

	public int getAltitudesensor() {
		return altitudesensor;
	}

	public void setAltitudesensor(int altitudesensor) {
		this.altitudesensor = altitudesensor;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getDte() {
		return dte;
	}

	public void setDte(int dte) {
		this.dte = dte;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public boolean isRaim() {
		return raim;
	}

	public void setRaim(boolean raim) {
		this.raim = raim;
	}

	public int getCommflag() {
		return commflag;
	}

	public void setCommflag(int commflag) {
		this.commflag = commflag;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	public CommunicationState getCommState() {
		return commState;
	}
	
	private void setCommState(String bits) {
		commState = CommunicationState.fromPayload(bits, getType());
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int altitude = 0;
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float lon = 0.0f;
	private float lat = 0.0f;
	private float course = 0.0f;
	private int second = 0;
	private int altitudesensor = 0;
	private int dte = 1;
	private boolean assigned = false;
	private boolean raim = false;
	private int commflag = 0;
	private int radio = 0;
	private CommunicationState commState = null;
}
