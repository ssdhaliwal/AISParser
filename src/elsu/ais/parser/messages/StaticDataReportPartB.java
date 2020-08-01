package elsu.ais.parser.messages;

import java.util.ArrayList;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.messages.StaticDataReport;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StaticDataReportPartB extends StaticDataReport {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StaticDataReportPartB staticPartBReport = new StaticDataReportPartB();

		if (aisMessage instanceof StaticDataReport) {
			staticPartBReport.parseMessage((StaticDataReport) aisMessage);
		} else {
			((StaticDataReport) staticPartBReport).parseMessage(messageBits);
		}
		staticPartBReport.parseMessage(messageBits);

		return staticPartBReport;
	}

	public StaticDataReportPartB() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(40, 47, 8, "Ship Type", "shiptype", "e", "(Part B) See \"Ship Types\""));
		getMessageBlocks().add(new PayloadBlock(48, 65, 18, "Vendor ID", "vendorid", "t", "(Part B) 3 six-bit chars"));
		getMessageBlocks().add(new PayloadBlock(66, 69, 4, "Unit Model Code", "model", "u", "(Part B)"));
		getMessageBlocks().add(new PayloadBlock(70, 89, 20, "Serial Number", "serial", "u", "(Part B)"));
		getMessageBlocks()
				.add(new PayloadBlock(90, 131, 42, "Call Sign", "callsign", "t", "(Part B) As in Message Type 5"));
		getMessageBlocks().add(new PayloadBlock(132, 140, 9, "Dimension to Bow", "to_bow", "u", "(Part B) Meters"));
		getMessageBlocks().add(new PayloadBlock(141, 149, 9, "Dimension to Stern", "to_stern", "u", "(Part B) Meters"));
		getMessageBlocks().add(new PayloadBlock(150, 155, 6, "Dimension to Port", "to_port", "u", "(Part B) Meters"));
		getMessageBlocks()
				.add(new PayloadBlock(156, 161, 6, "Dimension to Starboard", "to_starboard", "u", "(Part B) Meters"));
		getMessageBlocks()
				.add(new PayloadBlock(132, 161, 30, "Mothership MMSI", "mothership_mmsi", "u", "(Part B) See below"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 40:
				setShiptype(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 48:
				setVendorId(AISMessage.text_decoder(block.getBits()));
				break;
			case 66:
				setModel(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setSerial(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 90:
				setCallSign(AISMessage.text_decoder(block.getBits()));
				break;
			case 132:
				if (isAuxiliary()) {
					setMothershipMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
				} else {
					setToBow(AISMessage.unsigned_integer_decoder(block.getBits()));
				}
				break;
			case 141:
				if (!isAuxiliary()) {
					setToStern(AISMessage.unsigned_integer_decoder(block.getBits()));
				}
				break;
			case 150:
				if (!isAuxiliary()) {
					setToPort(AISMessage.unsigned_integer_decoder(block.getBits()));
				}
				break;
			case 156:
				if (!isAuxiliary()) {
					setToStarboard(AISMessage.unsigned_integer_decoder(block.getBits()));
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partno\":" + getPartno());
		buffer.append(", \"shipType\":" + getShiptype());
		buffer.append(", \"shipTypeText\":\"" + LookupValues.getShipType(getShiptype()) + "\"");
		buffer.append(", \"vendorId\":\"" + getVendorId().trim() + "\"");
		buffer.append(", \"model\":" + getModel());
		buffer.append(", \"serial\":" + getSerial());
		buffer.append(", \"to_bow\":" + getToBow());
		buffer.append(", \"to_stern\":" + getToStern());
		buffer.append(", \"to_port\":" + getToPort());
		buffer.append(", \"to_starboard\":" + getToStarboard());
		buffer.append(", \"mothership_mmsi\":" + getMothershipMmsi());
		buffer.append("}");

		return buffer.toString();
	}

	public int getShiptype() {
		return shiptype;
	}

	public void setShiptype(int shiptype) {
		this.shiptype = shiptype;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId.replace("@", "");
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign.replace("@", "");
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

	public int getMothershipMmsi() {
		return mothership_mmsi;
	}

	public void setMothershipMmsi(int mothership_mmsi) {
		this.mothership_mmsi = mothership_mmsi;
	}

	private int shiptype = 0;
	private String vendorId = "";
	private int model = 0;
	private int serial = 0;
	private String callSign = "";
	private int to_bow = 0;
	private int to_stern = 0;
	private int to_port = 0;
	private int to_starboard = 0;
	private int mothership_mmsi = 0;
}
