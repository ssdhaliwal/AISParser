package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.CommunicationState;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class PositionReportClassA extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		PositionReportClassA positionReport = new PositionReportClassA();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public PositionReportClassA() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 1-3"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "Message repeat count"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks()
				.add(new PayloadBlock(38, 41, 4, "Navigation Status", "status", "e", "See Navigation Status"));
		getMessageBlocks().add(new PayloadBlock(42, 49, 8, "Rate of Turn (ROT)", "turn", "I3", "See below"));
		getMessageBlocks().add(new PayloadBlock(50, 59, 10, "Speed Over Ground (SOG)", "speed", "U1", "See below"));
		getMessageBlocks().add(new PayloadBlock(60, 60, 1, "Position Accuracy", "accuracy", "b", "See below"));
		getMessageBlocks().add(new PayloadBlock(61, 88, 28, "Longitude", "lon", "I4", "Minutes/10000 (see below)"));
		getMessageBlocks().add(new PayloadBlock(89, 115, 27, "Latitude", "lat", "I4", "Minutes/10000 (see below)"));
		getMessageBlocks().add(new PayloadBlock(116, 127, 12, "Course Over Ground (COG)", "course", "U1",
				"Relative to true north, to 0.1 degree precision"));
		getMessageBlocks().add(new PayloadBlock(128, 136, 9, "True Heading (HDG)", "heading", "u",
				"0 to 359 degrees, 511 = not available."));
		getMessageBlocks().add(new PayloadBlock(137, 142, 6, "Time Stamp", "second", "u", "Second of UTC timestamp"));
		getMessageBlocks()
				.add(new PayloadBlock(143, 144, 2, "Maneuver Indicator", "maneuver", "e", "See Maneuver Indicator"));
		getMessageBlocks().add(new PayloadBlock(145, 147, 3, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(148, 148, 1, "RAIM flag", "raim", "b", "See below"));
		getMessageBlocks().add(new PayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See below"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if (block.getEnd() == -1) {
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
			case 38:
				setStatus(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 42:
				setTurn(AISBase.integer_decoder(block.getBits()));
				break;
			case 50:
				setSpeed(AISBase.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 60:
				setAccuracy(AISBase.boolean_decoder(block.getBits()));
				break;
			case 61:
				setLongitude(AISBase.float_decoder(block.getBits()) / 600000f);
				break;
			case 89:
				setLatitude(AISBase.float_decoder(block.getBits()) / 600000f);
				break;
			case 116:
				setCourse(AISBase.unsigned_float_decoder(block.getBits()) / 10f);
				break;
			case 128:
				setHeading(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 137:
				setSecond(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 143:
				setManeuver(AISBase.unsigned_integer_decoder(block.getBits()));
				break;
			case 148:
				setRaim(AISBase.boolean_decoder(block.getBits()));
				break;
			case 149:
				setRadio(AISBase.unsigned_integer_decoder(block.getBits()));
				setCommState(block.getBits());
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"transponder\":\"" + getTransponderType() + "\"");
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"status\":" + getStatus());
		buffer.append(", \"statusText\":\"" + LookupValues.getNavigationStatus(getStatus()) + "\"");
		buffer.append(", \"turn\":" + getTurn());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"heading\":" + getHeading());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(
				", \"maneuver\":" + getManeuver());
		buffer.append(
				", \"maneuverText\":\"" + LookupValues.getManeuverIndicator(getManeuver()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState().toString());
		buffer.append(", \"commtech\":\"" + LookupValues.getCommunicationTechnology(getType()) + "\"");
		buffer.append("}");

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

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
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

	public CommunicationState getCommState() {
		return commState;
	}

	private void setCommState(String bits) {
		commState = CommunicationState.fromPayload(bits, getType());
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int status = 0;
	private int turn = 0;
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float longitude = 0f;
	private float latitude = 0f;
	private float course = 0.0f;
	private int heading = 0;
	private int second = 0;
	private int maneuver = 0;
	private boolean raim = false;
	private int radio = 0;
	private CommunicationState commState = null;
}
