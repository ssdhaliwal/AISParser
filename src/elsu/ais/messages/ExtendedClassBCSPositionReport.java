package elsu.ais.messages;

import elsu.ais.base.AISMessage;
import elsu.ais.message.data.VesselDimensions;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;

public class ExtendedClassBCSPositionReport extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		ExtendedClassBCSPositionReport positionReport = new ExtendedClassBCSPositionReport();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public ExtendedClassBCSPositionReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 19"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNN"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(new PayloadBlock(46, 55, 10, "Speed Over Ground", "speed", "U1", "As in CNB."));
		getMessageBlocks().add(new PayloadBlock(56, 56, 1, "Position Accuracy", "accuracy", "b", "As in CNB."));
		getMessageBlocks().add(new PayloadBlock(57, 84, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(85, 111, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(112, 123, 12, "Course Over Ground", "course", "U1",
				"Relative to true north, units of 0.1 degrees"));
		getMessageBlocks()
				.add(new PayloadBlock(124, 132, 9, "True Heading", "heading", "u", "0 to 359 degrees, 511 = N/A"));
		getMessageBlocks().add(new PayloadBlock(133, 138, 6, "Time Stamp", "second", "u", "Second of UTC timestamp."));
		getMessageBlocks().add(new PayloadBlock(139, 142, 4, "Regional reserved", "regional", "u", "Uninterpreted"));
		getMessageBlocks().add(new PayloadBlock(143, 262, 120, "Name", "shipname", "s", "20 6-bit characters"));
		getMessageBlocks()
				.add(new PayloadBlock(263, 270, 8, "Type of ship and cargo", "shiptype", "u", "As in Message 5"));
		getMessageBlocks().add(new PayloadBlock(271, 300, 30, "Vessel Dimensions", "dimension", "u", "Meters"));
		getMessageBlocks()
				.add(new PayloadBlock(301, 304, 4, "Position Fix Type", "epfd", "e", "See \"EPFD Fix Types\""));
		getMessageBlocks().add(new PayloadBlock(305, 305, 1, "RAIM flag", "raim", "b", "As in CNB."));
		getMessageBlocks()
				.add(new PayloadBlock(306, 306, 1, "DTE", "dte", "u", "0=Data terminal ready, 1=Not ready (default)."));
		getMessageBlocks()
				.add(new PayloadBlock(307, 307, 1, "Assigned mode flag", "assigned", "u", "See [IALA] for details"));
		// getMessageBlocks().add(new PayloadBlock(308, 311, 4, "Spare", "", "x", "Unused, should be zero"));
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
		case 143:
			setShipName(parseTEXT(block.getBits()));
			break;
		case 263:
			setShipType(parseUINT(block.getBits()));
			break;
		case 271:
			setDimension(block.getBits());
			break;
		case 301:
			setEpfd(parseUINT(block.getBits()));
			break;
		case 305:
			setRaim(parseBOOLEAN(block.getBits()));
			break;
		case 306:
			setDte(parseUINT(block.getBits()));
			break;
		case 307:
			setAssigned(parseUINT(block.getBits()));
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
		buffer.append(", \"shipName\":\"" + getShipName().trim() + "\"");
		buffer.append(", \"shipType\":" + getShipType());
		buffer.append(", \"shipTypeText\":\"" + LookupValues.getShipType(getShipType()) + "\"");
		buffer.append(", \"dimension\":" + getDimension());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + LookupValues.getEPFDFixType(getEpfd()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"dte\":" + getDte());
		buffer.append(", \"dteText\":\"" + LookupValues.getDte(getDte()) + "\"");
		buffer.append(", \"assigned\":" + getAssigned());
		buffer.append(", \"assignedText\":\"" + LookupValues.getAssignedMode(getAssigned()) + "\"");
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

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName.replace("@", "");
	}

	public int getShipType() {
		return shipType;
	}

	public void setShipType(int shipType) {
		this.shipType = shipType;
	}

	public VesselDimensions getDimension() {
		return dimension;
	}

	public void setDimension(String bits) {
		try {
			this.dimension = VesselDimensions.fromPayload(bits);
		} catch (Exception exi) {}
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

	public int getDte() {
		return dte;
	}

	public void setDte(int dte) {
		this.dte = dte;
	}

	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int regional = 0;
	private float speed = 0.0f;
	private boolean accuracy = false;
	private float longitude = 0f;
	private float latitude = 0f;
	private float course = 0.0f;
	private int heading = 0;
	private int second = 0;
	private String shipName = "";
	private int shipType = 0;
	private VesselDimensions dimension = null;
	private int epfd = 0;
	private boolean raim = false;
	private int dte = 1;
	private int assigned = 0;
}
