package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.lookups.LookupValues;

public class PositionReportClassA extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		PositionReportClassA positionReport = new PositionReportClassA();
		
		positionReport.setRawMessage(aisMessage.getRawMessage());
		positionReport.setBinaryMessage(aisMessage.getBinaryMessage());
		positionReport.setEncodedMessage(aisMessage.getEncodedMessage());
		positionReport.setErrorMessage(aisMessage.getErrorMessage());

		positionReport.parseMessage(messageBits);
		
		return positionReport;
	}
	
	public PositionReportClassA() {
		initialize();
	}

	private void initialize() {
		ArrayList<_PayloadBlock> messageBlocks = getMessageBlock();
		
		messageBlocks.add(new _PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 1-3"));
		messageBlocks
				.add(new _PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "Message repeat count"));
		messageBlocks.add(new _PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		messageBlocks.add(
				new _PayloadBlock(38, 41, 4, "Navigation Status", "status", "e", "See Navigation Status"));
		messageBlocks.add(new _PayloadBlock(42, 49, 8, "Rate of Turn (ROT)", "turn", "I3", "See below"));
		messageBlocks
				.add(new _PayloadBlock(50, 59, 10, "Speed Over Ground (SOG)", "speed", "U1", "See below"));
		messageBlocks.add(new _PayloadBlock(60, 60, 1, "Position Accuracy", "accuracy", "b", "See below"));
		messageBlocks
				.add(new _PayloadBlock(61, 88, 28, "Longitude", "lon", "I4", "Minutes/10000 (see below)"));
		messageBlocks
				.add(new _PayloadBlock(89, 115, 27, "Latitude", "lat", "I4", "Minutes/10000 (see below)"));
		messageBlocks.add(new _PayloadBlock(116, 127, 12, "Course Over Ground (COG)", "course", "U1",
				"Relative to true north, to 0.1 degree precision"));
		messageBlocks.add(new _PayloadBlock(128, 136, 9, "True Heading (HDG)", "heading", "u",
				"0 to 359 degrees, 511 = not available."));
		messageBlocks
				.add(new _PayloadBlock(137, 142, 6, "Time Stamp", "second", "u", "Second of UTC timestamp"));
		messageBlocks.add(new _PayloadBlock(143, 144, 2, "Maneuver Indicator", "maneuver", "e",
				"See Maneuver Indicator"));
		messageBlocks.add(new _PayloadBlock(145, 147, 3, "Spare", "", "x", "Not used"));
		messageBlocks.add(new _PayloadBlock(148, 148, 1, "RAIM flag", "raim", "b", "See below"));
		messageBlocks.add(new _PayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See below"));
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
				setStatus(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 42:
				setTurn(AISMessage.integer_decoder(block.getBits()));
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
				setHeading(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 137:
				setSecond(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 143:
				setManeuver(AISMessage.unsigned_integer_decoder(block.getBits()));
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
		
		buffer.append("{ \"CLASSAPositionReport\": {");
		buffer.append("\"transponder\":" + getTransponderType());
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"status\":\"" + getStatus() + "/" + LookupValues.getNavigationStatus(getStatus()) + "\"");
		buffer.append(", \"turn\":" + getTurn());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"lon\":" + getLon());
		buffer.append(", \"lat\":" + getLat());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"heading\":" + getHeading());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"maneuver\":\"" + getManeuver() + "/" + LookupValues.getManeuverIndicator(getManeuver()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"radio\":" + getRadio());
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
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

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getManeuver() {
		return maneuver;
	}

	public void setManeuver(int maneuver) {
		this.maneuver = maneuver;
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
	private int status = 0;
	private int turn = 0;
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float lon = 0.0f;
	private float lat = 0.0f;
	private float course = 0.0f;
	private int heading = 0;
	private int second = 0;
	private int maneuver = 0;
	private boolean raim = false;
	private int radio = 0;
}
