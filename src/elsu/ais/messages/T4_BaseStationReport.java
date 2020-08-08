package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.CommunicationState;

public class T4_BaseStationReport extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T4_BaseStationReport stationReport = new T4_BaseStationReport();
		stationReport.parseMessage(messageBits);

		return stationReport;
	}

	public T4_BaseStationReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 4"));
		getMessageBlocks()
				.add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		getMessageBlocks()
				.add(new AISPayloadBlock(38, 51, 14, "Year (UTC)", "year", "u", "UTC, 1-9999, 0 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(52, 55, 4, "Month (UTC)", "month", "u", "1-12; 0 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(56, 60, 5, "Day (UTC)", "day", "u", "1-31; 0 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(61, 65, 5, "Hour (UTC)", "hour", "u", "0-23; 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(66, 71, 6, "Minute (UTC)", "minute", "u", "0-59; 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(72, 77, 6, "Second (UTC)", "second", "u", "0-59; 60 = N/A (default)"));
		getMessageBlocks()
				.add(new AISPayloadBlock(78, 78, 1, "Fix quality", "accuracy", "b", "As in Common Navigation Block"));
		getMessageBlocks()
				.add(new AISPayloadBlock(79, 106, 28, "Longitude", "lon", "I4", "As in Common Navigation Block"));
		getMessageBlocks()
				.add(new AISPayloadBlock(107, 133, 27, "Latitude", "lat", "I4", "As in Common Navigation Block"));
		getMessageBlocks().add(new AISPayloadBlock(134, 137, 4, "Type of EPFD", "epfd", "e", "See \"EPFD Fix Types\""));
		getMessageBlocks()
				.add(new AISPayloadBlock(138, 138, 1, "Control for Long Range Broadcast", "lrbcontrol", "u", ""));
		// getMessageBlocks().add(new PayloadBlock(139, 147, 9, "Spare", "", "x", "Not used"));
		getMessageBlocks()
				.add(new AISPayloadBlock(148, 148, 1, "RAIM flag", "raim", "b", "As for common navigation block"));
		getMessageBlocks()
				.add(new AISPayloadBlock(149, 167, 19, "SOTDMA state", "radio", "u", "As in same bits for Type 1"));
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
			setYear(parseUINT(block.getBits()));
			break;
		case 52:
			setMonth(parseUINT(block.getBits()));
			break;
		case 56:
			setDay(parseUINT(block.getBits()));
			break;
		case 61:
			setHour(parseUINT(block.getBits()));
			break;
		case 66:
			setMinute(parseUINT(block.getBits()));
			break;
		case 72:
			setSecond(parseUINT(block.getBits()));
			break;
		case 78:
			setAccuracy(parseBOOLEAN(block.getBits()));
			break;
		case 79:
			setLongitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 107:
			setLatitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 134:
			setEpfd(parseUINT(block.getBits()));
			break;
		case 138:
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
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"year\":" + getYear());
		buffer.append(", \"month\":" + getMonth());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + AISLookupValues.getEPFDFixType(getEpfd()) + "\"");
		buffer.append(", \"lrbControl\":" + getLrbControl());
		buffer.append(", \"lrbControlText\":\"" + AISLookupValues.getLRBControl(getLrbControl()) + "\"");
		buffer.append(", \"raim\":" + isRaim());
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
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

	public int getEpfd() {
		return epfd;
	}

	public void setEpfd(int epfd) {
		this.epfd = epfd;
	}

	public int getLrbControl() {
		return lrbControl;
	}

	public void setLrbControl(int lrbControl) {
		this.lrbControl = lrbControl;
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
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private boolean accuracy = false;
	private float longitude = 0.0f;
	private float latitude = 0.0f;
	private int epfd = 0;
	private int lrbControl = 0;
	private boolean raim = false;
	private int radio = 0;
	private CommunicationState commState = null;
}