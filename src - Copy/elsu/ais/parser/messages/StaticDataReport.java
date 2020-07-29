package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.resources.PayloadBlock;

public class StaticDataReport extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		StaticDataReport staticReport = new StaticDataReport();

		staticReport.setRawMessage(aisMessage.getRawMessage());
		staticReport.setBinaryMessage(aisMessage.getBinaryMessage());
		staticReport.setEncodedMessage(aisMessage.getEncodedMessage());
		staticReport.setErrorMessage(aisMessage.getErrorMessage());

		staticReport.parseMessage(messageBits);

		return staticReport;
	}

	public StaticDataReport() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 24"));
		messageBlocks.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		messageBlocks.add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		messageBlocks.add(new PayloadBlock(38, 39, 2, "Part Number", "partno", "u", "0-1"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : messageBlocks) {
			if (block.getEnd() == -1) {
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
				setPartno(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
		}
	}

	public void parseMessage(StaticDataReport message) {
		this.type = message.getType();
		this.repeat = message.getRepeat();
		this.mmsi = message.getMmsi();
		this.partno = message.getPartno();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"StaticDataReport\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"auxiliary\":" + isAuxiliary());
		buffer.append(", \"partno\":" + getPartno());
		buffer.append("}}");

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
		this.setAuxilizary();
	}

	public int getPartno() {
		return partno;
	}

	public void setPartno(int partno) {
		this.partno = partno;
	}

	public boolean isAuxiliary() {
		return auxiliary;
	}

	private void setAuxilizary() {
		if ((getMmsi() + "").substring(0, 2) == "98") {
			this.auxiliary = true;
		}
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int partno = 0;
	private boolean auxiliary = false;
}
