package elsu.ais.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T24_StaticDataReport;
import elsu.sentence.SentenceBase;

public class T24_StaticDataReportPartA extends T24_StaticDataReport {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T24_StaticDataReportPartA staticPartAReport = new T24_StaticDataReportPartA();

		if (aisMessage instanceof T24_StaticDataReport) {
			staticPartAReport.parseMessage((T24_StaticDataReport) aisMessage);
		} else {
			((T24_StaticDataReport) staticPartAReport).parseMessage(messageBits);
		}
		staticPartAReport.parseMessage(messageBits);

		return staticPartAReport;
	}

	public T24_StaticDataReportPartA() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks()
				.add(new AISPayloadBlock(40, 159, 120, "Vessel Name", "shipname", "t", "(Part A) 20 sixbit chars"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 40:
			setShipname(parseTEXT(block.getBits()));
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
			node.put("partNumber", getPartNumber());
			node.put("shipName", getShipName().trim());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipname(String shipName) {
		this.shipName = shipName.replace("@", "");
	}

	private String shipName = "";

}
