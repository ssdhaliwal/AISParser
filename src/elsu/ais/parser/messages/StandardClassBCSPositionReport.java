package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.resources.PayloadBlock;

public class StandardClassBCSPositionReport extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StandardClassBCSPositionReport positionReport = new StandardClassBCSPositionReport();

		positionReport.setRawMessage(aisMessage.getRawMessage());
		positionReport.setBinaryMessage(aisMessage.getBinaryMessage());
		positionReport.setEncodedMessage(aisMessage.getEncodedMessage());
		positionReport.setErrorMessage(aisMessage.getErrorMessage());

		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public StandardClassBCSPositionReport() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 18"));
		messageBlocks
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		messageBlocks.add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		messageBlocks.add(new PayloadBlock(38, 45, 8, "Regional Reserved", "reserved", "x", "Not used"));
		messageBlocks.add(
				new PayloadBlock(46, 55, 10, "Speed Over Ground", "speed", "U1", "As in common navigation block"));
		messageBlocks.add(new PayloadBlock(56, 56, 1, "Position Accuracy", "accuracy", "b", "See below"));
		messageBlocks.add(new PayloadBlock(57, 84, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		messageBlocks.add(new PayloadBlock(85, 111, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		messageBlocks.add(
				new PayloadBlock(112, 123, 12, "Course Over Ground", "course", "U1", "0.1 degrees from true north"));
		messageBlocks
				.add(new PayloadBlock(124, 132, 9, "True Heading", "heading", "u", "0 to 359 degrees, 511 = N/A"));
		messageBlocks.add(new PayloadBlock(133, 138, 6, "Time Stamp", "second", "u", "Second of UTC timestamp."));
		messageBlocks.add(new PayloadBlock(139, 140, 2, "Regional reserved", "regional", "u", "Uninterpreted"));
		messageBlocks.add(new PayloadBlock(141, 141, 1, "CS Unit", "cs", "b",
				"0=Class B SOTDMA unit 1=Class B CS (Carrier Sense) unit"));
		messageBlocks.add(new PayloadBlock(142, 142, 1, "Display flag", "display", "b",
				"0=No visual display, 1=Has display, (Probably not reliable)."));
		messageBlocks.add(new PayloadBlock(143, 143, 1, "DSC Flag", "dsc", "b",
				"If 1, unit is attached to a VHF voice radio with DSC capability."));
		messageBlocks.add(new PayloadBlock(144, 144, 1, "Band flag", "band", "b",
				"Base stations can command units to switch frequency. If this flag is 1, the unit can use any part of the marine channel."));
		messageBlocks.add(new PayloadBlock(145, 145, 1, "Message 22 flag", "msg22", "b",
				"If 1, unit can accept a channel assignment via Message Type 22."));
		messageBlocks.add(new PayloadBlock(146, 146, 1, "Assigned", "assigned", "b",
				"Assigned-mode flag: 0 = autonomous mode (default), 1 = assigned mode."));
		messageBlocks.add(new PayloadBlock(147, 147, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		messageBlocks.add(new PayloadBlock(148, 167, 20, "Radio status", "radio", "u", "See [IALA] for details."));
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
			case 46:
				setSpeed(AISMessage.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 56:
				setAccuracy(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 57:
				setLon(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 85:
				setLat(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 112:
				setCourse(AISMessage.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 124:
				setHeading(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 133:
				setSecond(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 139:
				setRegional(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 141:
				setCs(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 142:
				setDisplay(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 143:
				setDsc(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 144:
				setBand(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 145:
				setMsg22(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 146:
				setAssigned(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 147:
				setRaim(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 148:
				setRadio(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"StandardClassBCSPositionReport\": {");
		buffer.append("\"transponder\":" + getTransponderType());
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"lon\":" + getLon());
		buffer.append(", \"lat\":" + getLat());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"heading\":" + getHeading());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"regional\":" + getRegional());
		buffer.append(", \"cs\":" + isCs());
		buffer.append(", \"display\":" + isDisplay());
		buffer.append(", \"dsc\":" + isDsc());
		buffer.append(", \"band\":" + isBand());
		buffer.append(", \"msg22\":" + isMsg22());
		buffer.append(", \"assigned\":" + isAssigned());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"radio\":" + getRadio());
		buffer.append("}}");

		return buffer.toString();
	}

	public String getTransponderType() {
		return "B";
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

	public int getRegional() {
		return regional;
	}

	public void setRegional(int regional) {
		this.regional = regional;
	}

	public boolean isCs() {
		return cs;
	}

	public void setCs(boolean cs) {
		this.cs = cs;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isDsc() {
		return dsc;
	}

	public void setDsc(boolean dsc) {
		this.dsc = dsc;
	}

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
	}

	public boolean isMsg22() {
		return msg22;
	}

	public void setMsg22(boolean msg22) {
		this.msg22 = msg22;
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

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float lon = 0.0f;
	private float lat = 0.0f;
	private float course = 0.0f;
	private int heading = 0;
	private int second = 0;
	private int regional = 0;
	private boolean cs = false;
	private boolean display = false;
	private boolean dsc = false;
	private boolean band = false;
	private boolean msg22 = false;
	private boolean assigned = false;
	private boolean raim = false;
	private int radio = 0;
}
