package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.data.VesselDimensions;

public class T21_AidToNavigationReport extends AISMessageBase {

	public static AISMessageBase fromAISMessage(String messageBits) throws Exception {
		T21_AidToNavigationReport atonMessage = new T21_AidToNavigationReport();
		atonMessage.parseMessage(messageBits);

		return atonMessage;
	}

	public T21_AidToNavigationReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 21"));
		getMessageBlocks().add(new AISPayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(new AISPayloadBlock(38, 42, 5, "Aid type", "aid_type", "e", "See \"Navaid Types\""));
		getMessageBlocks().add(new AISPayloadBlock(43, 162, 120, "Name", "name", "t", "Name in sixbit chars"));
		getMessageBlocks().add(new AISPayloadBlock(163, 163, 1, "Position Accuracy", "accuracy", "b", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(164, 191, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new AISPayloadBlock(192, 218, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new AISPayloadBlock(219, 227, 30, "Vessel Dimensions", "dimension", "u", "Meters"));
		getMessageBlocks().add(new AISPayloadBlock(249, 252, 4, "Type of EPFD", "epfd", "e", "As in Message Type 4"));
		getMessageBlocks().add(new AISPayloadBlock(253, 258, 6, "UTC second", "second", "u", "As in Message Types 1-3"));
		getMessageBlocks()
				.add(new AISPayloadBlock(259, 259, 1, "Off-Position Indicator", "off_position", "b", "See Below"));
		getMessageBlocks().add(new AISPayloadBlock(260, 267, 8, "Regional reserved", "regional", "u", "Uninterpreted"));
		getMessageBlocks().add(new AISPayloadBlock(268, 268, 1, "RAIM flag", "raim", "b", "As in CNB"));
		getMessageBlocks().add(new AISPayloadBlock(269, 269, 1, "Virtual-aid flag", "virtual_aid", "b", "See Below"));
		getMessageBlocks()
				.add(new AISPayloadBlock(270, 270, 1, "Assigned-mode flag", "assigned", "b", "See [IALA] for details"));
		// getMessageBlocks().add(new PayloadBlock(271, 271, 1, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new AISPayloadBlock(272, -1, 88, "Name Extension", "", "t", "See Below"));
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
			setAidType(parseUINT(block.getBits()));
			break;
		case 43:
			setName(parseTEXT(block.getBits()));
			break;
		case 163:
			setAccuracy(parseBOOLEAN(block.getBits()));
			break;
		case 164:
			setLongitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 192:
			setLatitude(parseFLOAT(block.getBits()) / 600000f);
			break;
		case 219:
			setDimension(block.getBits());
			break;
		case 249:
			setEpfd(parseUINT(block.getBits()));
			break;
		case 253:
			setSecond(parseUINT(block.getBits()));
			break;
		case 259:
			setOffPosition(parseBOOLEAN(block.getBits()));
			break;
		case 260:
			setRegional(parseUINT(block.getBits()));
			break;
		case 268:
			setRaim(parseBOOLEAN(block.getBits()));
			break;
		case 269:
			setVirtualAid(parseBOOLEAN(block.getBits()));
			break;
		case 270:
			setAssigned(parseBOOLEAN(block.getBits()));
			break;
		case 272:
			setNameExtension(parseTEXT(block.getBits()));
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
		buffer.append(", \"aidType\":" + getAidType());
		buffer.append(", \"aidTypeText\":\"" + AISLookupValues.getNavAidType(getAidType()) + "\"");
		buffer.append(", \"name\":\"" + getName().trim() + "\"");
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"dimension\":" + getDimension());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + AISLookupValues.getEPFDFixType(getEpfd()) + "\"");
		buffer.append(", \"second\":" + getSecond());
		buffer.append(", \"offPosition\":" + isOffPosition());
		buffer.append(", \"regional\":" + getRegional());
		buffer.append(", \"raim\":" + isRaim());
		buffer.append(", \"virtualAid\":" + isVirtualAid());
		buffer.append(", \"assigned\":" + isAssigned());
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

	public int getAidType() {
		return aidType;
	}

	public void setAidType(int aidType) {
		this.aidType = aidType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.replace("@", "");
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

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public boolean isOffPosition() {
		return offPosition;
	}

	public void setOffPosition(boolean offPosition) {
		this.offPosition = offPosition;
	}

	public int getRegional() {
		return regional;
	}

	public void setRegional(int regional) {
		this.regional = regional;
	}

	public boolean isRaim() {
		return raim;
	}

	public void setRaim(boolean raim) {
		this.raim = raim;
	}

	public boolean isVirtualAid() {
		return virtualAid;
	}

	public void setVirtualAid(boolean virtualAid) {
		this.virtualAid = virtualAid;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public void setNameExtension(String name) {
		this.name += name.replace("@", "");
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int aidType = 0;
	private String name = "";
	private boolean accuracy = false;
	private float longitude = 181f;
	private float latitude = 91f;
	private VesselDimensions dimension = null;
	private int epfd = 0;
	private int second = 60;
	private boolean offPosition = false;
	private int regional = 0;
	private boolean raim = false;
	private boolean virtualAid = false;
	private boolean assigned = false;
}
