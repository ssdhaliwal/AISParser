package elsu.ais.message.data;

import elsu.ais.base.AISMessage;
import elsu.ais.messages.AddressedBinaryMessage;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;

public class Type6_InternationalFunctionalMessage4 extends AddressedBinaryMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		Type6_InternationalFunctionalMessage4 binaryMessage = new Type6_InternationalFunctionalMessage4();

		if (aisMessage instanceof AddressedBinaryMessage) {
			binaryMessage.parseMessage((AddressedBinaryMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public Type6_InternationalFunctionalMessage4() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(88, 97, 10, "Requested DAC", "requestedDAC", "", "IAI, RAI, or text"));
		getMessageBlocks().add(new PayloadBlock(98, 225, 128, "FI Availability", "FIAvailability", "", "See apprioriate FI reference document"));
		getMessageBlocks().add(new PayloadBlock(226, 351, 126, "spare", "spare", "", ""));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 88:
			setRequestedDAC(parseUINT(block.getBits()));
			break;
		case 98:
			setFIAvailability(parseBITS(block.getBits()));
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
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"requestedDAC\":" + getRequestedDAC());
		buffer.append(", \"FIAvailability\":" + getFIAvailability());
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		buffer.append("}");

		return buffer.toString();
	}

	public String getFunctionalName() {
		return "Type6/InternationalFunctionalMessage4";
	}

	public int getRequestedDAC() {
		return requestedDAC;
	}

	public void setRequestedDAC(int requestedDAC) {
		this.requestedDAC = requestedDAC;
	}

	public String getFIAvailability() {
		return FIAvailability;
	}

	public void setFIAvailability(String FIAvailability) {
		this.FIAvailability = FIAvailability;
	}

	private int requestedDAC = 0;
	private String FIAvailability = "";
}
