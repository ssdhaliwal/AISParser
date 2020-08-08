package elsu.ais.messages;

import elsu.ais.base.AISMessage;
import elsu.ais.messages.StaticDataReport;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;

public class StaticDataReportPartA extends StaticDataReport {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		StaticDataReportPartA staticPartAReport = new StaticDataReportPartA();

		if (aisMessage instanceof StaticDataReport) {
			staticPartAReport.parseMessage((StaticDataReport) aisMessage);
		} else {
			((StaticDataReport) staticPartAReport).parseMessage(messageBits);
		}
		staticPartAReport.parseMessage(messageBits);

		return staticPartAReport;
	}

	public StaticDataReportPartA() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks()
				.add(new PayloadBlock(40, 159, 120, "Vessel Name", "shipname", "t", "(Part A) 20 sixbit chars"));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
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
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
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
