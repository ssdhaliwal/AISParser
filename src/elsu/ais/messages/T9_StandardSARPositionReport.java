package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.CommunicationState;

public class T9_StandardSARPositionReport extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T9_StandardSARPositionReport positionReport = new T9_StandardSARPositionReport();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public T9_StandardSARPositionReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 9"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(38, 49, 12, "Altitude", "alt", "u", "See below"));
		getMessageBlocks().add(new AISPayloadBlock(50, 59, 10, "SOG", "speed", "u", "See below"));
		getMessageBlocks().add(new AISPayloadBlock(60, 60, 1, "Position Accuracy", "accuracy", "u", "See below"));
		getMessageBlocks().add(new AISPayloadBlock(61, 88, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new AISPayloadBlock(89, 115, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(
				new AISPayloadBlock(116, 127, 12, "Course Over Ground", "course", "U1", "True bearing, 0.1 degree units"));
		getMessageBlocks().add(new AISPayloadBlock(128, 133, 6, "Time Stamp", "second", "u", "UTC second."));
		getMessageBlocks()
				.add(new AISPayloadBlock(134, 134, 1, "Altitude Sensor", "altsensor", "u", "0=GNSS;1=barometric source"));
		getMessageBlocks().add(new AISPayloadBlock(135, 141, 7, "Regional reserved", "regional", "x", "Reserved"));
		getMessageBlocks().add(new AISPayloadBlock(142, 142, 1, "DTE", "dte", "u",
				"0=Data terminal ready, 1=Data terminal not ready (default)"));
		// getMessageBlocks().add(new PayloadBlock(143, 145, 3, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(146, 146, 1, "Assigned", "assigned", "b", "Assigned-mode flag"));
		getMessageBlocks()
				.add(new AISPayloadBlock(147, 147, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		getMessageBlocks()
				.add(new AISPayloadBlock(148, 148, 1, "Comm State Selector", "commflag", "u", "0=SOTDMA, 1=ITDMA"));
		getMessageBlocks().add(new AISPayloadBlock(149, 167, 19, "Radio status", "radio", "u", "See [IALA] for details."));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
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
			setAltitude(parseUINT(block.getBits()));
			break;
		case 50:
			setSpeed(parseUFLOAT(block.getBits()));
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
			setSecond(parseUINT(block.getBits()));
			break;
		case 134:
			setAltitudeSensor(parseUINT(block.getBits()));
			break;
		case 142:
			setDte(parseUINT(block.getBits()));
			break;
		case 146:
			setAssigned(parseBOOLEAN(block.getBits()));
			break;
		case 147:
			setRaim(parseBOOLEAN(block.getBits()));
			break;
		case 148:
			setCommFlag(parseUINT(block.getBits()));
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
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
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
		buffer.append(", \"altitudeSensorText\":\"" + AISLookupValues.getAltitudeSensor(getAltitudeSensor()) + "\"");
		buffer.append(", \"assigned\":" + isAssigned());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"commFlag\":" + getCommFlag());
		buffer.append(", \"commFlagText\":\"" + AISLookupValues.getCommunicationFlag(getCommFlag()) + "\"");
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState());
		buffer.append(", \"commtech\":\"" + AISLookupValues.getCommunicationTechnology(getType()) + "\"");
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
	private int altitude = 4095;
	private float speed = 1023.0f;
	private boolean accuracy = false;
	private float longitude = 181f;
	private float latitude = 91f;
	private float course = 360.0f;
	private int second = 60;
	private int altitudeSensor = 0;
	private int dte = 1;
	private boolean assigned = false;
	private boolean raim = false;
	private int commFlag = 0;
	private int radio = 0;
	private CommunicationState commState = null;
}
