package elsu.ais.parser.messages;

import java.util.ArrayList;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.messages.StaticDataReport;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StaticDataReportPartB extends StaticDataReport {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StaticDataReportPartB staticPartBReport = new StaticDataReportPartB();

		staticPartBReport.setRawMessage(aisMessage.getRawMessage());
		staticPartBReport.setBinaryMessage(aisMessage.getBinaryMessage());
		staticPartBReport.setEncodedMessage(aisMessage.getEncodedMessage());
		staticPartBReport.setErrorMessage(aisMessage.getErrorMessage());

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
		messageBlocks.add(new PayloadBlock(40, 47, 8, "Ship Type", "shiptype", "e", "(Part B) See \"Ship Types\""));
		messageBlocks.add(new PayloadBlock(48, 65, 18, "Vendor ID", "vendorid", "t", "(Part B) 3 six-bit chars"));
		messageBlocks.add(new PayloadBlock(66, 69, 4, "Unit Model Code", "model", "u", "(Part B)"));
		messageBlocks.add(new PayloadBlock(70, 89, 20, "Serial Number", "serial", "u", "(Part B)"));
		messageBlocks
				.add(new PayloadBlock(90, 131, 42, "Call Sign", "callsign", "t", "(Part B) As in Message Type 5"));
		messageBlocks.add(new PayloadBlock(132, 140, 9, "Dimension to Bow", "to_bow", "u", "(Part B) Meters"));
		messageBlocks.add(new PayloadBlock(141, 149, 9, "Dimension to Stern", "to_stern", "u", "(Part B) Meters"));
		messageBlocks.add(new PayloadBlock(150, 155, 6, "Dimension to Port", "to_port", "u", "(Part B) Meters"));
		messageBlocks
				.add(new PayloadBlock(156, 161, 6, "Dimension to Starboard", "to_starboard", "u", "(Part B) Meters"));
		messageBlocks
				.add(new PayloadBlock(132, 161, 30, "Mothership MMSI", "mothership_mmsi", "u", "(Part B) See below"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : messageBlocks) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 40:
				setShiptype(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 48:
				setVendorid(AISMessage.text_decoder(block.getBits()));
				break;
			case 66:
				setModel(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 70:
				setSerial(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 90:
				setCallsign(AISMessage.text_decoder(block.getBits()));
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

		buffer.append("{ \"StaticDataReportPartB\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partno\":" + getPartno());
		buffer.append(", \"shipType\":\"" + getShiptype() + "/" + LookupValues.getShipType(getShiptype()) + "\"");
		buffer.append(", \"vendorid\":\"" + getVendorid().trim() + "\"");
		buffer.append(", \"model\":" + getModel());
		buffer.append(", \"serial\":" + getSerial());
		buffer.append(", \"to_bow\":" + getToBow());
		buffer.append(", \"to_stern\":" + getToStern());
		buffer.append(", \"to_port\":" + getToPort());
		buffer.append(", \"to_starboard\":" + getToStarboard());
		buffer.append(", \"mothership_mmsi\":" + getMothershipMmsi());
		buffer.append("}}");

		return buffer.toString();
	}

	public int getShiptype() {
		return shiptype;
	}

	public void setShiptype(int shiptype) {
		this.shiptype = shiptype;
	}

	public String getVendorid() {
		return vendorid;
	}

	public void setVendorid(String vendorid) {
		this.vendorid = vendorid.replace("@", "");
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

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign.replace("@", "");
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
	private String vendorid = "";
	private int model = 0;
	private int serial = 0;
	private String callsign = "";
	private int to_bow = 0;
	private int to_stern = 0;
	private int to_port = 0;
	private int to_starboard = 0;
	private int mothership_mmsi = 0;
}
