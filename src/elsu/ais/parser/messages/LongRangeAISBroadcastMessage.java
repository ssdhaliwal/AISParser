package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class LongRangeAISBroadcastMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		LongRangeAISBroadcastMessage positionReport = new LongRangeAISBroadcastMessage();
		positionReport.parseMessage(messageBits);

		return positionReport;
	}

	public LongRangeAISBroadcastMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 27"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks()
				.add(new PayloadBlock(38, 38, 1, "Position Accuracy", "accuracy", "u", "See Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(39, 39, 1, "RAIM flag", "raim", "u", "See Common Navigation Block"));
		getMessageBlocks()
				.add(new PayloadBlock(40, 43, 4, "Navigation Status", "status", "u", "See Common Navigation Block"));
		getMessageBlocks().add(new PayloadBlock(44, 61, 18, "Longitude", "lon", "I4",
				"minutes/10 East positive, West negative 181000 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(62, 78, 17, "Latitude", "lat", "I4",
				"minutes/10 North positive, South negative 91000 = N/A (default)"));
		getMessageBlocks().add(
				new PayloadBlock(79, 84, 6, "Speed Over Ground", "speed", "u", "Knots (0-62); 63 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(85, 93, 9, "Course Over Ground", "course", "u",
				"0 to 359 degrees, 511 = not available."));
		getMessageBlocks().add(new PayloadBlock(94, 94, 1, "GNSS Position status", "latency", "u",
				"0 = Reported position latency is less than 5 seconds; 1 = Reported position latency is greater than 5 seconds = default"));
		getMessageBlocks().add(new PayloadBlock(95, 95, 1, "Spare", "", "x", "Not used"));
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
			setAccuracy(boolean_decoder(block.getBits()));
			break;
		case 39:
			setRaim(boolean_decoder(block.getBits()));
			break;
		case 40:
			setStatus(unsigned_integer_decoder(block.getBits()));
			break;
		case 44:
			setLongitude(float_decoder(block.getBits()) / 600f);
			break;
		case 62:
			setLatitude(float_decoder(block.getBits()) / 600f);
			break;
		case 79:
			setSpeed(unsigned_float_decoder(block.getBits()));
			break;
		case 85:
			setCourse(unsigned_float_decoder(block.getBits()));
			break;
		case 94:
			setPositionLatency(unsigned_integer_decoder(block.getBits()));
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
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"status\":" + getStatus());
		buffer.append(", \"statusText\":\"" + LookupValues.getNavigationStatus(getStatus()) + "\"");
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"speed\":" + getSpeed());
		buffer.append(", \"course\":" + getCourse());
		buffer.append(", \"positionLatency\":" + getPositionLatency());
		buffer.append(", \"positionLatencyText\":\"" + LookupValues.getPositionLatency(getPositionLatency()) + "\"");
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
