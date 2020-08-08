package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.SentenceBase;

public class T27_PositionReportLongRangeApplications extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T27_PositionReportLongRangeApplications positionReport = new T27_PositionReportLongRangeApplications();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public T27_PositionReportLongRangeApplications() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 27"));
		getMessageBlocks().add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks()
				.add(new AISPayloadBlock(38, 38, 1, "Position Accuracy", "accuracy", "u", "See Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(39, 39, 1, "RAIM flag", "raim", "u", "See Common Navigation Block"));
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 43, 4, "Navigation Status", "status", "u", "See Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(44, 61, 18, "Longitude", "lon", "I4",
				"minutes/10 East positive, West negative 181000 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(62, 78, 17, "Latitude", "lat", "I4",
				"minutes/10 North positive, South negative 91000 = N/A (default)"));
		getMessageBlocks().add(
				new AISPayloadBlock(79, 84, 6, "Speed Over Ground", "speed", "u", "Knots (0-62); 63 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(85, 93, 9, "Course Over Ground", "course", "u",
				"0 to 359 degrees, 511 = not available."));
		getMessageBlocks().add(new AISPayloadBlock(94, 94, 1, "GNSS Position status", "latency", "u",
				"0 = Reported position latency is less than 5 seconds; 1 = Reported position latency is greater than 5 seconds = default"));
		// getMessageBlocks().add(new PayloadBlock(95, 95, 1, "Spare", "", "x", "Not used"));
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
			setAccuracy(parseBOOLEAN(block.getBits()));
			break;
		case 39:
			setRaim(parseBOOLEAN(block.getBits()));
			break;
		case 40:
			setStatus(parseUINT(block.getBits()));
			break;
		case 44:
			setLongitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 62:
			setLatitude(parseFLOAT(block.getBits()) / 600f);
			break;
		case 79:
			setSpeed(parseUFLOAT(block.getBits()));
			break;
		case 85:
			setCourse(parseUFLOAT(block.getBits()));
			break;
		case 94:
			setPositionLatency(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"transponder\":\"" + getTransponderType() + "\"");
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"status\":" + getStatus());
		buffer.append(", \"statusText\":\"" + AISLookupValues.getNavigationStatus(getStatus()) + "\"");
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"positionLatency\":" + getPositionLatency());
		buffer.append(", \"positionLatencyText\":\"" + AISLookupValues.getPositionLatency(getPositionLatency()) + "\"");
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

	public boolean isAccuracy() {
		return accuracy;
	}

	public void setAccuracy(boolean accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isRaim() {
		return raim;
	}

	public void setRaim(boolean raim) {
		this.raim = raim;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getCourse() {
		return course;
	}

	public void setCourse(float course) {
		this.course = course;
	}

	public int getPositionLatency() {
		return positionLatency;
	}

	public void setPositionLatency(int positionLatency) {
		this.positionLatency = positionLatency;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private boolean accuracy = false;
	private boolean raim = false;
	private int status = 0;
	private float longitude = 0f;
	private float latitude = 0f;
	private float speed = 0.0f;
	private float course = 0.0f;
	private int positionLatency = 0;
}
