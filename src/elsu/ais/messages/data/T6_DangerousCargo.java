package elsu.ais.messages.data;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T6_BinaryAddressedMessage;
import elsu.sentence.SentenceBase;

public class T6_DangerousCargo extends T6_BinaryAddressedMessage {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T6_DangerousCargo binaryMessage = new T6_DangerousCargo();

		if (aisMessage instanceof T6_BinaryAddressedMessage) {
			binaryMessage.parseMessage((T6_BinaryAddressedMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_DangerousCargo() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(88, 117, 30, "Last Port Of Call", "lastport", "t", "5 6-bit characters, UN locode"));
		getMessageBlocks().add(new AISPayloadBlock(118, 121, 4, "ETA month (UTC)", "lmonth", "u", "1-12, 0=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(122, 126, 5, "ETA day (UTC)", "lday", "u", "1-31, 0=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(127, 131, 5, "ETA hour (UTC)", "lhour", "u", "0-23, 24=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(132, 137, 6, "ETA minute (UTC)", "lminute", "u", "0-59, 60=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(138, 167, 30, "Next Port Of Call", "nextport", "t", "5 6-bit characters, UN locode"));
		getMessageBlocks().add(new AISPayloadBlock(168, 171, 4, "ETA month (UTC)", "nmonth", "u", "1-12, 0=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(172, 176, 5, "ETA day (UTC)", "nday", "u", "1-31, 0=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(177, 181, 5, "ETA hour (UTC)", "nhour", "u", "0-23, 24=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(182, 187, 6, "ETA minute (UTC)", "nminute", "u", "0-59, 60=N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(188, 307, 120, "Main Dangerous Good", "dangerous", "t", "20 6-bit characters"));
		getMessageBlocks().add(new AISPayloadBlock(308, 331, 24, "IMD Category", "imdcat", "t", "4  6-bit characters"));
		getMessageBlocks().add(new AISPayloadBlock(332, 344, 13, "UN Number", "unid", "u", "1-3363 UN Number"));
		getMessageBlocks().add(new AISPayloadBlock(345, 354, 10, "Amount of Cargo", "amount", "u", "Unsigned integer"));
		getMessageBlocks().add(new AISPayloadBlock(355, 356, 2, "Unit of Quantity", "unit", "e", "See \"Cargo Unit Codes\""));
		// getMessageBlocks().add(new PayloadBlock(357, 359, 3, "Spare", "", "x", "Not used"));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 88:
			setLastPortOfCall(parseTEXT(block.getBits()));
			break;
		case 118:
			setLpocETAMonth(parseUINT(block.getBits()));
			break;
		case 122:
			setLpocETADay(parseUINT(block.getBits()));
			break;
		case 127:
			setLpocETAHour(parseUINT(block.getBits()));
			break;
		case 132:
			setLpocETAMinute(parseUINT(block.getBits()));
			break;
		case 138:
			setNextPortOfCall(parseTEXT(block.getBits()));
			break;
		case 168:
			setNpocETAMonth(parseUINT(block.getBits()));
			break;
		case 172:
			setLpocETADay(parseUINT(block.getBits()));
			break;
		case 177:
			setLpocETAHour(parseUINT(block.getBits()));
			break;
		case 182:
			setLpocETAMinute(parseUINT(block.getBits()));
			break;
		case 188:
			setDangerousCargo(parseTEXT(block.getBits()));
			break;
		case 308:
			setImdCategory(parseTEXT(block.getBits()));
			break;
		case 332:
			setUNNumber(parseUINT(block.getBits()));
			break;
		case 345:
			setAmount(parseUINT(block.getBits()));
			break;
		case 355:
			setUnitOfQuantity(parseUINT(block.getBits()));
			break;
		}
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			result = SentenceBase.objectMapper.writeValueAsString(this);
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
		/*
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"lastPortOfCall\":\"" + getLastPortOfCall() + "\"");
		buffer.append(", \"lpocETAMonth\":" + getLpocETAMonth());
		buffer.append(", \"lpocETADay\":" + getLpocETADay());
		buffer.append(", \"lpocETAHour\":" + getLpocETAHour());
		buffer.append(", \"lpocETAMinute\":" + getLpocETAMinute());
		buffer.append(", \"nextPortOfCall\":\"" + getNextPortOfCall() + "\"");
		buffer.append(", \"npocETAMonth\":" + getNpocETAMonth());
		buffer.append(", \"npocETADay\":" + getNpocETADay());
		buffer.append(", \"npocETAHour\":" + getNpocETAHour());
		buffer.append(", \"npocETAMinute\":" + getNpocETAMinute());
		buffer.append(", \"dangerousCargo\":\"" + getDangerousCargo() + "\"");
		buffer.append(", \"imdCategory\":\"" + getImdCategory() + "\"");
		buffer.append(", \"UNNumber\":" + getUNNumber());
		buffer.append(", \"amount\":" + getAmount());
		buffer.append(", \"unitOfQuantity\":" + getUnitOfQuantity());
		buffer.append(", \"unitOfQuantityText\":\"" + getUnitOfQuantity() + "\"");
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		buffer.append("}");

		return buffer.toString();
		*/
	}

	public String getFunctionalName() {
		return "Type6/DangerousCargoIndication";
	}

	public String getLastPortOfCall() {
		return lastPortOfCall;
	}

	public void setLastPortOfCall(String lastPortOfCall) {
		this.lastPortOfCall = lastPortOfCall;
	}

	public int getLpocETAMonth() {
		return lpocETAMonth;
	}

	public void setLpocETAMonth(int lpocETAMonth) {
		this.lpocETAMonth = lpocETAMonth;
	}

	public int getLpocETADay() {
		return lpocETADay;
	}

	public void setLpocETADay(int lpocETADay) {
		this.lpocETADay = lpocETADay;
	}

	public int getLpocETAHour() {
		return lpocETAHour;
	}

	public void setLpocETAHour(int lpocETAHour) {
		this.lpocETAHour = lpocETAHour;
	}

	public int getLpocETAMinute() {
		return lpocETAMinute;
	}

	public void setLpocETAMinute(int lpocETAMinute) {
		this.lpocETAMinute = lpocETAMinute;
	}

	public String getNextPortOfCall() {
		return nextPortOfCall;
	}

	public void setNextPortOfCall(String nextPortOfCall) {
		this.nextPortOfCall = nextPortOfCall;
	}

	public int getNpocETAMonth() {
		return npocETAMonth;
	}

	public void setNpocETAMonth(int npocETAMonth) {
		this.npocETAMonth = npocETAMonth;
	}

	public int getNpocETADay() {
		return npocETADay;
	}

	public void setNpocETADay(int npocETADay) {
		this.npocETADay = npocETADay;
	}

	public int getNpocETAHour() {
		return npocETAHour;
	}

	public void setNpocETAHour(int npocETAHour) {
		this.npocETAHour = npocETAHour;
	}

	public int getNpocETAMinute() {
		return npocETAMinute;
	}

	public void setNpocETAMinute(int npocETAMinute) {
		this.npocETAMinute = npocETAMinute;
	}

	public String getDangerousCargo() {
		return dangerousCargo;
	}

	public void setDangerousCargo(String dangerousCargo) {
		this.dangerousCargo = dangerousCargo;
	}

	public String getImdCategory() {
		return imdCategory;
	}

	public void setImdCategory(String imdCategory) {
		this.imdCategory = imdCategory;
	}

	public int getUNNumber() {
		return UNNumber;
	}

	public void setUNNumber(int UNNumber) {
		this.UNNumber = UNNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getUnitOfQuantity() {
		return unitOfQuantity;
	}

	public void setUnitOfQuantity(int unitOfQuantity) {
		this.unitOfQuantity = unitOfQuantity;
	}

	private String lastPortOfCall = "";
	private int lpocETAMonth = 0;
	private int lpocETADay = 0;
	private int lpocETAHour = 0;
	private int lpocETAMinute = 0;
	private String nextPortOfCall = "";
	private int npocETAMonth = 0;
	private int npocETADay = 0;
	private int npocETAHour = 0;
	private int npocETAMinute = 0;
	private String dangerousCargo = "";
	private String imdCategory = "";
	private int UNNumber = 0;
	private int amount = 0;
	private int unitOfQuantity = 0;
}
