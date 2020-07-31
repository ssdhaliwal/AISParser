package elsu.ais.parser.messages;

import java.util.ArrayList;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.messages.StaticDataReport;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class StaticDataReportPartA extends StaticDataReport {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
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
		getMessageBlocks().add(new PayloadBlock(40, 159, 120, "Vessel Name", "shipname", "t", "(Part A) 20 sixbit chars"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 40:
				setShipname(AISMessage.text_decoder(block.getBits()));
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
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partno\":" + getPartno());
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
