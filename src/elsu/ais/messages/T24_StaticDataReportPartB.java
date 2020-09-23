package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T24_StaticDataReport;
import elsu.ais.messages.data.VesselDimensions;
import elsu.sentence.SentenceBase;

public class T24_StaticDataReportPartB extends T24_StaticDataReport {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T24_StaticDataReportPartB staticPartBReport = new T24_StaticDataReportPartB();

		if (aisMessage instanceof T24_StaticDataReport) {
			staticPartBReport.parseMessage((T24_StaticDataReport) aisMessage);
		} else {
			((T24_StaticDataReport) staticPartBReport).parseMessage(messageBits);
		}
		staticPartBReport.parseMessage(messageBits);

		return staticPartBReport;
	}

	public T24_StaticDataReportPartB() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 47, 8, "Ship Type", "shiptype", "e", "(Part B) See \"Ship Types\""));
		getMessageBlocks().add(new AISPayloadBlock(48, 65, 18, "Vendor ID", "vendorid", "t", "(Part B) 3 six-bit chars"));
		getMessageBlocks().add(new AISPayloadBlock(66, 69, 4, "Unit Model Code", "model", "u", "(Part B)"));
		getMessageBlocks().add(new AISPayloadBlock(70, 89, 20, "Serial Number", "serial", "u", "(Part B)"));
		getMessageBlocks()
				.add(new AISPayloadBlock(90, 131, 42, "Call Sign", "callsign", "t", "(Part B) As in Message Type 5"));
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 69, 30, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		getMessageBlocks().add(new AISPayloadBlock(162, 165, 4, "Position Fix Type", "epfd", "u", "(Part B) See below"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 40:
			setShipType(parseUINT(block.getBits()));
			break;
		case 48:
			setVendorId(parseTEXT(block.getBits()));
			break;
		case 66:
			setModel(parseUINT(block.getBits()));
			break;
		case 70:
			setSerial(parseUINT(block.getBits()));
			break;
		case 90:
			setCallSign(parseTEXT(block.getBits()));
			break;
		case 162:
			setEpfd(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("type", getType());
			node.put("typeText", AISLookupValues.getMessageType(getType()));
			node.put("repeat", getRepeat());
			node.put("mmsi", getMmsi());
			
			node.put("auxiliary", isAuxiliary());
			node.put("partno", getPartNumber());
			node.put("shipType", getShipType());
			node.put("shipTypeText", AISLookupValues.getShipType(getShipType()));
			node.put("vendorId", getVendorId().trim());
			node.put("model", getModel());
			node.put("serial", getSerial());
			node.set("dimension", SentenceBase.objectMapper.readTree(((getDimension() != null) ? getDimension().toString() : "")));
			node.put("epfd", getEpfd());
			node.put("epfdText", AISLookupValues.getEPFDFixType(getEpfd()));

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
	}

	public int getShipType() {
		return shipType;
	}

	public void setShipType(int shipType) {
		this.shipType = shipType;
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

	private int shipType = 0;
	private String vendorId = "";
	private int model = 0;
	private int serial = 0;
	private String callSign = "";
	private VesselDimensions dimension = null;
	private int epfd = 0;
}
