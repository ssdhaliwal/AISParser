package elsu.ais.parser.messages;

import java.util.ArrayList;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.VesselDimensions;
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
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		getMessageBlocks().add(new PayloadBlock(162, 165, 4, "Position Fix Type", "epfd", "u", "(Part B) See below"));
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
			case 162:
				setEpfd(AISMessage.unsigned_integer_decoder(block.getBits()));
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
		buffer.append(", \"dimension\":" + getDimension());
		buffer.append(", \"epfd\":" + getEpfd());
		buffer.append(", \"epfdText\":\"" + LookupValues.getEPFDFixType(getEpfd()) + "\"");
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

	public VesselDimensions getDimension() {
		return dimension;
	}

	public void setDimension(String bits) {
		this.dimension = VesselDimensions.fromPayload(bits, getType());
	}

	public int getEpfd() {
		return epfd;
	}

	public void setEpfd(int epfd) {
		this.epfd = epfd;
	}

	private int shiptype = 0;
	private String vendorId = "";
	private int model = 0;
	private int serial = 0;
	private String callSign = "";
	private VesselDimensions dimension = null;
	private int epfd = 0;
}
