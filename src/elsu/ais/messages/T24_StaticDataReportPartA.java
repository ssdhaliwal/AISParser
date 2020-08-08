package elsu.ais.messages;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T24_StaticDataReport;

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
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partNumber\":" + getPartNumber());
		buffer.append(", \"shipName\":\"" + getShipName().trim() + "\"");
		buffer.append("}");

		return buffer.toString();
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipname(String shipName) {
		this.shipName = shipName.replace("@", "");
	}

	private String shipName = "";

}
