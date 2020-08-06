package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.CommunicationState;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StandardClassBCSPositionReport extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		StandardClassBCSPositionReport positionReport = new StandardClassBCSPositionReport();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public StandardClassBCSPositionReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 18"));
		getMessageBlocks()
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new PayloadBlock(38, 45, 8, "Regional Reserved", "reserved", "x", "Not used"));
		getMessageBlocks()
				.add(new PayloadBlock(46, 55, 10, "Speed Over Ground", "speed", "U1", "As in common navigation block"));
		getMessageBlocks().add(new PayloadBlock(56, 56, 1, "Position Accuracy", "accuracy", "b", "See below"));
		getMessageBlocks().add(new PayloadBlock(57, 84, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(85, 111, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(
				new PayloadBlock(112, 123, 12, "Course Over Ground", "course", "U1", "0.1 degrees from true north"));
		getMessageBlocks()
				.add(new PayloadBlock(124, 132, 9, "True Heading", "heading", "u", "0 to 359 degrees, 511 = N/A"));
		getMessageBlocks().add(new PayloadBlock(133, 138, 6, "Time Stamp", "second", "u", "Second of UTC timestamp."));
		getMessageBlocks().add(new PayloadBlock(139, 140, 2, "Regional reserved", "regional", "u", "Uninterpreted"));
		getMessageBlocks().add(new PayloadBlock(141, 141, 1, "CS Unit", "cs", "b",
				"0=Class B SOTDMA unit 1=Class B CS (Carrier Sense) unit"));
		getMessageBlocks().add(new PayloadBlock(142, 142, 1, "Display flag", "display", "b",
				"0=No visual display, 1=Has display, (Probably not reliable)."));
		getMessageBlocks().add(new PayloadBlock(143, 143, 1, "DSC Flag", "dsc", "b",
				"If 1, unit is attached to a VHF voice radio with DSC capability."));
		getMessageBlocks().add(new PayloadBlock(144, 144, 1, "Band flag", "band", "b",
				"Base stations can command units to switch frequency. If this flag is 1, the unit can use any part of the marine channel."));
		getMessageBlocks().add(new PayloadBlock(145, 145, 1, "Message 22 flag", "msg22", "b",
				"If 1, unit can accept a channel assignment via Message Type 22."));
		getMessageBlocks().add(new PayloadBlock(146, 146, 1, "Assigned", "assigned", "b",
				"Assigned-mode flag: 0 = autonomous mode (default), 1 = assigned mode."));
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
			setType(parseUINT(block.getBits()));
			break;
		case 6:
			setRepeat(parseUINT(block.getBits()));
			break;
		case 8:
			setMmsi(parseUINT(block.getBits()));
			break;
		case 46:
			setSpeed(parseUFLOAT(block.getBits()) / 10f);
			break;
		case 56:
			setAccuracy(parseBOOLEAN(block.getBits()));
			break;
		case 57:
			setLongitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 85:
			setLatitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 112:
			setCourse(parseUFLOAT(block.getBits()) / 10f);
			break;
		case 124:
			setHeading(parseUINT(block.getBits()));
			break;
		case 133:
			setSecond(parseUINT(block.getBits()));
			break;
		case 139:
			setRegional(parseUINT(block.getBits()));
			break;
		case 141:
			setCs(parseBOOLEAN(block.getBits()));
			break;
		case 142:
			setDisplay(parseBOOLEAN(block.getBits()));
			break;
		case 143:
			setDsc(parseBOOLEAN(block.getBits()));
			break;
		case 144:
			setBand(parseBOOLEAN(block.getBits()));
			break;
		case 145:
			setMsg22(parseBOOLEAN(block.getBits()));
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
		buffer.append("\"transponder\":" + getTransponderType());
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
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
		buffer.append(", \"commFlag\":" + getCommFlag());
		buffer.append(", \"commFlagText\":\"" + LookupValues.getCommunicationFlag(getCommFlag()) + "\"");
		buffer.append(", \"radio\":" + getRadio());
		buffer.append(", \"commState\":" + getCommState());
		buffer.append(", \"commtech\":\"" + LookupValues.getCommunicationTechnology(getType()) + "\"");
		buffer.append("}");

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
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float longitude = 0f;
	private float latitude = 0f;
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
	private int commFlag = 0;
	private int radio = 0;
	private CommunicationState commState = null;
}
