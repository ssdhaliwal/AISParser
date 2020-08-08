package elsu.ais.messages;

import elsu.ais.base.AISMessage;
import elsu.ais.message.data.CommunicationState;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;
import elsu.sentence.SentenceBase;

public class PositionReportClassA extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
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
		// getMessageBlocks().add(new PayloadBlock(145, 147, 3, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(148, 148, 1, "RAIM flag", "raim", "b", "See below"));
		getMessageBlocks().add(new PayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See below"));
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
			setStatus(parseUINT(block.getBits()));
			break;
		case 42:
			setRateOfTurn(parseINT(block.getBits()));
			break;
		case 50:
			setSpeed(parseUFLOAT(block.getBits()) / 10f);
			break;
		case 60:
			setAccuracy(parseBOOLEAN(block.getBits()));
			break;
		case 61:
			setLongitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 89:
			setLatitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 116:
			setCourse(parseUFLOAT(block.getBits()) / 10f);
			break;
		case 128:
			setHeading(parseUINT(block.getBits()));
			break;
		case 137:
			setSecond(parseUINT(block.getBits()));
			break;
		case 143:
			setManeuver(parseUINT(block.getBits()));
			break;
		case 148:
			setRaim(parseBOOLEAN(block.getBits()));
			break;
		case 149:
			setRadio(parseUINT(block.getBits()));
			setCommState(block.getBits());
			break;
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
		buffer.append(", \"rateOfTurn\":" + getRateOfTurn());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"heading\":" + getHeading());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"maneuver\":" + getManeuver());
		buffer.append(", \"maneuverText\":\"" + LookupValues.getManeuverIndicator(getManeuver()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState());
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

	public double getRateOfTurn() {
		return rateOfTurn;
	}

	public void setRateOfTurn(int turnRate) {
		double rateOfTurn = 0;

		if ((turnRate == 127) || (turnRate == -127) || (turnRate == -128)) {
			rateOfTurn = turnRate;
		} else {
			if (turnRate < 0) {
				rateOfTurn = -1 * Math.sqrt((-1 * turnRate) / 4.733);
			} else {
				rateOfTurn = Math.sqrt(turnRate / 4.733);
			}
		}
		
		this.rateOfTurn = rateOfTurn;
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
	private double rateOfTurn = 0;
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