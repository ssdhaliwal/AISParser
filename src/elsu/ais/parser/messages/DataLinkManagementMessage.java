package elsu.ais.parser.messages;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.message.data.GNSSMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class DataLinkManagementMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) throws Exception {
		DataLinkManagementMessage binaryMessage = new DataLinkManagementMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public DataLinkManagementMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 20"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 decimal digits"));
		// getMessageBlocks().add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		getMessageBlocks()
				.add(new PayloadBlock(40, 51, 12, "Offset number 1", "offset1", "u", "Reserved offset number"));
		getMessageBlocks().add(new PayloadBlock(52, 55, 4, "Reserved slots", "number1", "u", "Consecutive slots"));
		getMessageBlocks()
				.add(new PayloadBlock(56, 58, 3, "Time-out", "timeout1", "u", "Allocation timeout in minutes"));
		getMessageBlocks().add(new PayloadBlock(59, 69, 11, "Increment", "increment1", "u", "Repeat increment"));
		getMessageBlocks()
				.add(new PayloadBlock(70, 81, 12, "Offset number 2", "offset2", "u", "Reserved offset number"));
		getMessageBlocks().add(new PayloadBlock(82, 85, 4, "Reserved slots", "number2", "u", "Consecutive slots"));
		getMessageBlocks()
				.add(new PayloadBlock(86, 88, 3, "Time-out", "timeout2", "u", "Allocation timeout in minutes"));
		getMessageBlocks().add(new PayloadBlock(89, 99, 11, "Increment", "increment2", "u", "Repeat increment"));
		getMessageBlocks()
				.add(new PayloadBlock(100, 111, 12, "Offset number 3", "offset3", "u", "Reserved offset number"));
		getMessageBlocks().add(new PayloadBlock(112, 115, 4, "Reserved slots", "number3", "u", "Consecutive slots"));
		getMessageBlocks()
				.add(new PayloadBlock(116, 118, 3, "Time-out", "timeout3", "u", "Allocation timeout in minutes"));
		getMessageBlocks().add(new PayloadBlock(119, 129, 11, "Increment", "increment3", "u", "Repeat increment"));
		getMessageBlocks()
				.add(new PayloadBlock(130, 141, 12, "Offset number 4", "offset4", "u", "Reserved offset number"));
		getMessageBlocks().add(new PayloadBlock(142, 145, 4, "Reserved slots", "number4", "u", "Consecutive slots"));
		getMessageBlocks()
				.add(new PayloadBlock(146, 148, 3, "Time-out", "timeout4", "u", "Allocation timeout in minutes"));
		getMessageBlocks().add(new PayloadBlock(149, 159, 11, "Increment", "increment4", "u", "Repeat increment"));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 70) {
				return;
			} else {
				throw new Exception("parsing error; " + block);
			}
		}

		switch (block.getStart()) {
		case 0:
			setType(parseUINT(block.getBits()));
			break;
		case 6:
			setRepeat(parseUINT(block.getBits()));
			break;
		case 8:
			setMmsi(parseUINT(block.getBits()));
			break;
		case 40:
			setOffset1(parseUINT(block.getBits()));
			break;
		case 52:
			setNumber1(parseUINT(block.getBits()));
			break;
		case 56:
			setTimeout1(parseUINT(block.getBits()));
			break;
		case 59:
			setIncrement1(parseUINT(block.getBits()));
			break;
		case 70:
			setOffset2(parseUINT(block.getBits()));
			break;
		case 82:
			setNumber2(parseUINT(block.getBits()));
			break;
		case 86:
			setTimeout2(parseUINT(block.getBits()));
			break;
		case 89:
			setIncrement2(parseUINT(block.getBits()));
			break;
		case 100:
			setOffset3(parseUINT(block.getBits()));
			break;
		case 112:
			setNumber3(parseUINT(block.getBits()));
			break;
		case 116:
			setTimeout3(parseUINT(block.getBits()));
			break;
		case 119:
			setIncrement3(parseUINT(block.getBits()));
			break;
		case 130:
			setOffset4(parseUINT(block.getBits()));
			break;
		case 142:
			setNumber4(parseUINT(block.getBits()));
			break;
		case 146:
			setTimeout4(parseUINT(block.getBits()));
			break;
		case 149:
			setIncrement4(parseUINT(block.getBits()));
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
		buffer.append(", \"offset1\":" + getOffset1());
		buffer.append(", \"number1\":" + getNumber1());
		buffer.append(", \"timeout1\":" + getTimeout1());
		buffer.append(", \"increment1\":" + getIncrement1());
		buffer.append(", \"offset2\":" + getOffset2());
		buffer.append(", \"number2\":" + getNumber2());
		buffer.append(", \"timeout2\":" + getTimeout2());
		buffer.append(", \"increment2\":" + getIncrement2());
		buffer.append(", \"offset3\":" + getOffset3());
		buffer.append(", \"number3\":" + getNumber3());
		buffer.append(", \"timeout3\":" + getTimeout3());
		buffer.append(", \"increment3\":" + getIncrement3());
		buffer.append(", \"offset4\":" + getOffset4());
		buffer.append(", \"number4\":" + getNumber4());
		buffer.append(", \"timeout4\":" + getTimeout4());
		buffer.append(", \"increment4\":" + getIncrement4());
		buffer.append("}");

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
	}

	public int getOffset1() {
		return offset1;
	}

	public void setOffset1(int offset1) {
		this.offset1 = offset1;
	}

	public int getNumber1() {
		return number1;
	}

	public void setNumber1(int number1) {
		this.number1 = number1;
	}

	public int getTimeout1() {
		return timeout1;
	}

	public void setTimeout1(int timeout1) {
		this.timeout1 = timeout1;
	}

	public int getIncrement1() {
		return increment1;
	}

	public void setIncrement1(int increment1) {
		this.increment1 = increment1;
	}

	public int getOffset2() {
		return offset2;
	}

	public void setOffset2(int offset2) {
		this.offset2 = offset2;
	}

	public int getNumber2() {
		return number2;
	}

	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	public int getTimeout2() {
		return timeout2;
	}

	public void setTimeout2(int timeout2) {
		this.timeout2 = timeout2;
	}

	public int getIncrement2() {
		return increment2;
	}

	public void setIncrement2(int increment2) {
		this.increment2 = increment2;
	}

	public int getOffset3() {
		return offset3;
	}

	public void setOffset3(int offset3) {
		this.offset3 = offset3;
	}

	public int getNumber3() {
		return number3;
	}

	public void setNumber3(int number3) {
		this.number3 = number3;
	}

	public int getTimeout3() {
		return timeout3;
	}

	public void setTimeout3(int timeout3) {
		this.timeout3 = timeout3;
	}

	public int getIncrement3() {
		return increment3;
	}

	public void setIncrement3(int increment3) {
		this.increment3 = increment3;
	}

	public int getOffset4() {
		return offset4;
	}

	public void setOffset4(int offset4) {
		this.offset4 = offset4;
	}

	public int getNumber4() {
		return number4;
	}

	public void setNumber4(int number4) {
		this.number4 = number4;
	}

	public int getTimeout4() {
		return timeout4;
	}

	public void setTimeout4(int timeout4) {
		this.timeout4 = timeout4;
	}

	public int getIncrement4() {
		return increment4;
	}

	public void setIncrement4(int increment4) {
		this.increment4 = increment4;
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int offset1 = 0;
	private int number1 = 0;
	private int timeout1 = 0;
	private int increment1 = 0;
	private int offset2 = 0;
	private int number2 = 0;
	private int timeout2 = 0;
	private int increment2 = 0;
	private int offset3 = 0;
	private int number3 = 0;
	private int timeout3 = 0;
	private int increment3 = 0;
	private int offset4 = 0;
	private int number4 = 0;
	private int timeout4 = 0;
	private int increment4 = 0;
}
