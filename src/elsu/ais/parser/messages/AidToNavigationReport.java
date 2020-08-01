package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class AidToNavigationReport extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		AidToNavigationReport atonMessage = new AidToNavigationReport();
		atonMessage.parseMessage(messageBits);

		return atonMessage;
	}

	public AidToNavigationReport() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 21"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(new PayloadBlock(38, 42, 5, "Aid type", "aid_type", "e", "See \"Navaid Types\""));
		getMessageBlocks().add(new PayloadBlock(43, 162, 120, "Name", "name", "t", "Name in sixbit chars"));
		getMessageBlocks().add(new PayloadBlock(163, 163, 1, "Position Accuracy", "accuracy", "b", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(164, 191, 28, "Longitude", "lon", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(192, 218, 27, "Latitude", "lat", "I4", "Minutes/10000 (as in CNB)"));
		getMessageBlocks().add(new PayloadBlock(219, 227, 9, "Dimension to Bow", "to_bow", "u", "Meters"));
		getMessageBlocks().add(new PayloadBlock(228, 236, 9, "Dimension to Stern", "to_stern", "u", "Meters"));
		getMessageBlocks().add(new PayloadBlock(237, 242, 6, "Dimension to Port", "to_port", "u", "Meters"));
		getMessageBlocks().add(new PayloadBlock(243, 248, 6, "Dimension to Starboard", "to_starboard", "u", "Meters"));
		getMessageBlocks().add(new PayloadBlock(249, 252, 4, "Type of EPFD", "epfd", "e", "As in Message Type 4"));
		getMessageBlocks().add(new PayloadBlock(253, 258, 6, "UTC second", "second", "u", "As in Message Types 1-3"));
		getMessageBlocks().add(new PayloadBlock(259, 259, 1, "Off-Position Indicator", "off_position", "b", "See Below"));
		getMessageBlocks().add(new PayloadBlock(260, 267, 8, "Regional reserved", "regional", "u", "Uninterpreted"));
		getMessageBlocks().add(new PayloadBlock(268, 268, 1, "RAIM flag", "raim", "b", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(269, 269, 1, "Virtual-aid flag", "virtual_aid", "b", "See Below"));
		getMessageBlocks()
				.add(new PayloadBlock(270, 270, 1, "Assigned-mode flag", "assigned", "b", "See [IALA] for details"));
		getMessageBlocks().add(new PayloadBlock(271, 271, 1, "Spare", "", "x", "Not used"));
		getMessageBlocks().add(new PayloadBlock(272, -1, 88, "Name Extension", "", "t", "See Below"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 0:
				setType(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 6:
				setRepeat(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 8:
				setMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 38:
				setAidType(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 43:
				setName(AISMessage.text_decoder(block.getBits()));
				break;
			case 163:
				setAccuracy(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 164:
				setLongitude(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 192:
				setLatitude(AISMessage.float_decoder(block.getBits()) / 600000f);
				break;
			case 219:
				setToBow(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 228:
				setToStern(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 237:
				setToPort(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 243:
				setToStarboard(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 249:
				setEpfd(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 253:
				setSecond(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 259:
				setOffPosition(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 260:
				setRegional(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 268:
				setRaim(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 269:
				setVirtualAid(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 270:
				setAssigned(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 272:
				setNameExtension(AISMessage.text_decoder(block.getBits()));
				break;
			}
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
		buffer.append(", \"aidType\":" + getAidType());
		buffer.append(", \"aidTypeText\":\"" + LookupValues.getNavAidType(getAidType()) + "\"");
		buffer.append(", \"name\":\"" + getName().trim() + "\"");
		buffer.append(", \"accuracy\":" + isAccuracy());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"to_bow\":" + getToBow());
		buffer.append(", \"to_stern\":" + getToStern());
		buffer.append(", \"to_port\":" + getToPort());
		buffer.append(", \"to_starboard\":" + getToStarboard());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + LookupValues.getEPFDFixType(getEpfd()) + "\"");
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

	public int getToBow() {
		return to_bow;
	}

	public void setToBow(int to_bow) {
		this.to_bow = to_bow;
	}

	public int getToStern() {
		return to_stern;
	}

	public void setToStern(int to_stern) {
		this.to_stern = to_stern;
	}

	public int getToPort() {
		return to_port;
	}

	public void setToPort(int to_port) {
		this.to_port = to_port;
	}

	public int getToStarboard() {
		return to_starboard;
	}

	public void setToStarboard(int to_starboard) {
		this.to_starboard = to_starboard;
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
	private float longitude = 0f;
	private float latitude = 0f;
	private int to_bow = 0;
	private int to_stern = 0;
	private int to_port = 0;
	private int to_starboard = 0;
	private int epfd = 0;
	private int second = 0;
	private boolean offPosition = false;
	private int regional = 0;
	private boolean raim = false;
	private boolean virtualAid = false;
	private boolean assigned = false;
}
