package elsu.ais.parser.message.data;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.messages.AddressedBinaryMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class Type6_InternationalFunctionalMessage0 extends AddressedBinaryMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		Type6_InternationalFunctionalMessage0 binaryMessage = new Type6_InternationalFunctionalMessage0();

		if (aisMessage instanceof AddressedBinaryMessage) {
			binaryMessage.parseMessage((AddressedBinaryMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public Type6_InternationalFunctionalMessage0() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(88, 88, 1, "Acknowledge required", "acknowledge", "", "1 = reply is required, optional for addressed binary messages and not used for binary broadcast messages; 0 = reply is not required, optional for an addressed binary message and required for binary broadcast messages"));
		getMessageBlocks().add(new PayloadBlock(89, 99, 11, "sequence number", "seqno", "", "6-bit ASCII as defined in Table 47, Annex 8."));
		getMessageBlocks().add(new PayloadBlock(100, -1, 906, "Text", "text", "", ""));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 88:
			setAcknowledge(parseUINT(block.getBits()));
			break;
		case 89:
			setSeqno(parseUINT(block.getBits()));
			break;
		case 100:
			setText(parseTEXT(block.getBits()));
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
		buffer.append(", \"acknowledge\":" + getAcknowledge());
		buffer.append(", \"seqno\":" + getSeqno());
		buffer.append(", \"text\":" + getText());
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		buffer.append("}");

		return buffer.toString();
	}

	public String getFunctionalName() {
		return "Type6/InternationalFunctionalMessage0";
	}

	public int getAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(int acknowledge) {
		this.acknowledge = acknowledge;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private int acknowledge = 0;
	private int seqno = 0;
	private String text = "";
}
