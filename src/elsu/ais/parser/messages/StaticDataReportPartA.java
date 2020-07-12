package elsu.ais.parser.messages;

import java.util.ArrayList;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.messages.StaticDataReport;
import elsu.ais.parser.messages._PayloadBlock;

public class StaticDataReportPartA extends StaticDataReport {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StaticDataReportPartA staticPartAReport = new StaticDataReportPartA();
		
		staticPartAReport.setRawMessage(aisMessage.getRawMessage());
		staticPartAReport.setBinaryMessage(aisMessage.getBinaryMessage());
		staticPartAReport.setEncodedMessage(aisMessage.getEncodedMessage());
		staticPartAReport.setErrorMessage(aisMessage.getErrorMessage());

		if (aisMessage instanceof StaticDataReport) {
			staticPartAReport.parseMessage((StaticDataReport)aisMessage);
		} else {
			((StaticDataReport)staticPartAReport).parseMessage(messageBits);
		}
		staticPartAReport.parseMessage(messageBits);
		
		return staticPartAReport;
	}

	public StaticDataReportPartA() {
		initialize();
	}

	private void initialize() {
		ArrayList<_PayloadBlock> messageBlocks = getMessageBlock();
		messageBlocks.clear();
		
		messageBlocks.add(new _PayloadBlock(40, 159, 120, "Vessel Name", "shipname", "t", "(Part A) 20 sixbit chars"));
	}
	
	public void parseMessage(String message) {
		for (_PayloadBlock block : getMessageBlock()) {
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
		
		buffer.append("{ \"StaticDataReportPartA\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partno\":" + getPartno());
		buffer.append(", \"shipName\":\"" + getShipname().trim() + "\"");
		buffer.append("}}");
		
		return buffer.toString();
	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname.replace("@", "");
	}
	
	private String shipname = "";
	
}
