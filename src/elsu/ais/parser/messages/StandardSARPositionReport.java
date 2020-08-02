package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.CommunicationState;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StandardSARPositionReport extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		StandardSARPositionReport positionReport = new StandardSARPositionReport();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public StandardSARPositionReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 9"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 49, 12, "Altitude", "alt", "u", "See below"));
		getMessageBlocks().add(new PayloadBlock(50, 59, 10, "SOG", "speed", "u", "See below"));
		getMessageBlocks().add(new PayloadBlock(60, 60, 1, "Position Accuracy", "accuracy", "u", "See below"));
		getMessageBlocks().add(new PayloadBlock(61, 88, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(89, 115, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(
				new PayloadBlock(116, 127, 12, "Course Over Ground", "course", "U1", "True bearing, 0.1 degree units"));
		getMessageBlocks().add(new PayloadBlock(128, 133, 6, "Time Stamp", "second", "u", "UTC second."));
		getMessageBlocks()
				.add(new PayloadBlock(134, 134, 1, "Altitude Sensor", "altsensor", "u", "0=GNSS;1=barometric source"));
		getMessageBlocks().add(new PayloadBlock(135, 141, 7, "Regional reserved", "regional", "x", "Reserved"));
		getMessageBlocks().add(new PayloadBlock(142, 142, 1, "DTE", "dte", "u",
				"0=Data terminal ready, 1=Data terminal not ready (default)"));
		getMessageBlocks().add(new PayloadBlock(143, 145, 3, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(146, 146, 1, "Assigned", "assigned", "b", "Assigned-mode flag"));
		getMessageBlocks()
				.add(new PayloadBlock(147, 147, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		getMessageBlocks()
				.add(new PayloadBlock(148, 148, 1, "Comm State Selector", "commflag", "u", "0=SOTDMA, 1=ITDMA"));
		getMessageBlocks().add(new PayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See [IALA] for details."));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 0:
			setType(unsigned_integer_decoder(block.getBits()));
			break;
		case 6:
			setRepeat(unsigned_integer_decoder(block.getBits()));
			break;
		case 8:
			setMmsi(unsigned_integer_decoder(block.getBits()));
			break;
		case 38:
			setAltitude(unsigned_integer_decoder(block.getBits()));
			break;
		case 50:
			setSpeed(unsigned_float_decoder(block.getBits()));
			break;
		case 60:
			setAccuracy(boolean_decoder(block.getBits()));
			break;
		case 61:
			setLongitude(float_decoder(block.getBits()) / 600000f);
			break;
		case 89:
			setLatitude(float_decoder(block.getBits()) / 600000f);
			break;
		case 116:
			setCourse(unsigned_float_decoder(block.getBits()) / 10f);
			break;
		case 128:
			setSecond(unsigned_integer_decoder(block.getBits()));
			break;
		case 134:
			setAltitudeSensor(unsigned_integer_decoder(block.getBits()));
			break;
		case 142:
			setDte(unsigned_integer_decoder(block.getBits()));
			break;
		case 146:
			setAssigned(boolean_decoder(block.getBits()));
			break;
		case 147:
			setRaim(boolean_decoder(block.getBits()));
			break;
		case 148:
			setCommFlag(unsigned_integer_decoder(block.getBits()));
			break;
		case 149:
			setRadio(unsigned_integer_decoder(block.getBits()));
			setCommState(block.getBits());
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
		buffer.append(", \"altitude\":\"" + getAltitude());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"altitudeSensor\":" + getAltitudeSensor());
		buffer.append(", \"altitudeSensorText\":\"" + LookupValues.getAltitudeSensor(getAltitudeSensor()) + "\"");
		buffer.append(", \"assigned\":" + isAssigned());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"commFlag\":" + getCommFlag());
		buffer.append(", \"commFlagText\":\"" + LookupValues.getCommunicationFlag(getCommFlag()) + "\"");
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState());
		buffer.append(", \"commtech\":\"" + LookupValues.getCommunicationTechnology(getType()) + "\"");
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

	public int getAltitudeSensor() {
		return altitudeSensor;
	}

	public void setAltitudeSensor(int altitudeSensor) {
		this.altitudeSensor = altitudeSensor;
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

	public int getCommFlag() {
		return commFlag;
	}

	public void setCommFlag(int commFlag) {
		this.commFlag = commFlag;
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
	private float longitude = 0f;
	private float latitude = 0f;
	private float course = 0.0f;
	private int second = 0;
	private int altitudeSensor = 0;
	private int dte = 1;
	private boolean assigned = false;
	private boolean raim = false;
	private int commFlag = 0;
	private int radio = 0;
	private CommunicationState commState = null;
}
