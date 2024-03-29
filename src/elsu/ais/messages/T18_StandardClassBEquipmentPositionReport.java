package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.CommunicationState;
import elsu.sentence.SentenceBase;

public class T18_StandardClassBEquipmentPositionReport extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T18_StandardClassBEquipmentPositionReport positionReport = new T18_StandardClassBEquipmentPositionReport();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public T18_StandardClassBEquipmentPositionReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 18"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks().add(new AISPayloadBlock(38, 45, 8, "Regional Reserved", "reserved", "x", "Not used"));
		getMessageBlocks()
				.add(new AISPayloadBlock(46, 55, 10, "Speed Over Ground", "speed", "U1", "As in common navigation block"));
		getMessageBlocks().add(new AISPayloadBlock(56, 56, 1, "Position Accuracy", "accuracy", "b", "See below"));
		getMessageBlocks().add(new AISPayloadBlock(57, 84, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new AISPayloadBlock(85, 111, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(
				new AISPayloadBlock(112, 123, 12, "Course Over Ground", "course", "U1", "0.1 degrees from true north"));
		getMessageBlocks()
				.add(new AISPayloadBlock(124, 132, 9, "True Heading", "heading", "u", "0 to 359 degrees, 511 = N/A"));
		getMessageBlocks().add(new AISPayloadBlock(133, 138, 6, "Time Stamp", "second", "u", "Second of UTC timestamp."));
		getMessageBlocks().add(new AISPayloadBlock(139, 140, 2, "Regional reserved", "regional", "u", "Uninterpreted"));
		getMessageBlocks().add(new AISPayloadBlock(141, 141, 1, "CS Unit", "cs", "b",
				"0=Class B SOTDMA unit 1=Class B CS (Carrier Sense) unit"));
		getMessageBlocks().add(new AISPayloadBlock(142, 142, 1, "Display flag", "display", "b",
				"0=No visual display, 1=Has display, (Probably not reliable)."));
		getMessageBlocks().add(new AISPayloadBlock(143, 143, 1, "DSC Flag", "dsc", "b",
				"If 1, unit is attached to a VHF voice radio with DSC capability."));
		getMessageBlocks().add(new AISPayloadBlock(144, 144, 1, "Band flag", "band", "b",
				"Base stations can command units to switch frequency. If this flag is 1, the unit can use any part of the marine channel."));
		getMessageBlocks().add(new AISPayloadBlock(145, 145, 1, "Message 22 flag", "msg22", "b",
				"If 1, unit can accept a channel assignment via Message Type 22."));
		getMessageBlocks().add(new AISPayloadBlock(146, 146, 1, "Assigned", "assigned", "b",
				"Assigned-mode flag: 0 = autonomous mode (default), 1 = assigned mode."));
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
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("transponder", getTransponderType());
			node.put("type", getType());
			node.put("typeText", AISLookupValues.getMessageType(getType()));
			node.put("repeat", getRepeat());
			node.put("mmsi", getMmsi());
			
			node.put("speed", getSpeed());
			node.put("accuracy", isAccuracy());
			node.put("longitude", getLongitude());
			node.put("latitude", getLatitude());
			node.put("course", getCourse());
			node.put("heading", getHeading());
			node.put("second", getSecond());
			node.put("regional", getRegional());
			node.put("cs", isCs());
			node.put("display", isDisplay());
			node.put("dsc", isDsc());
			node.put("band", isBand());
			node.put("msg22", isMsg22());
			node.put("assigned", isAssigned());
			node.put("raim", isRaim());
			node.put("commFlag", getCommFlag());
			node.put("commFlagText", AISLookupValues.getCommunicationFlag(getCommFlag()));
			node.put("radio", getRadio());
			node.set("commState", SentenceBase.objectMapper.readTree(((getCommState() != null) ? getCommState().toString() : "")));
			node.put("commtech", AISLookupValues.getCommunicationTechnology(getType()));

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
	private float speed = 102.3f;
	private boolean accuracy = false;
	private float longitude = 181f;
	private float latitude = 91f;
	private float course = 360.0f;
	private int heading = 511;
	private int second = 60;
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
