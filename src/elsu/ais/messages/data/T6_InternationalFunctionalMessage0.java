package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T6_BinaryAddressedMessage;
import elsu.sentence.SentenceBase;

public class T6_InternationalFunctionalMessage0 extends T6_BinaryAddressedMessage {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T6_InternationalFunctionalMessage0 binaryMessage = new T6_InternationalFunctionalMessage0();

		if (aisMessage instanceof T6_BinaryAddressedMessage) {
			binaryMessage.parseMessage((T6_BinaryAddressedMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_InternationalFunctionalMessage0() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(88, 88, 1, "Acknowledge required", "acknowledge", "", "1 = reply is required, optional for addressed binary messages and not used for binary broadcast messages; 0 = reply is not required, optional for an addressed binary message and required for binary broadcast messages"));
		getMessageBlocks().add(new AISPayloadBlock(89, 99, 11, "sequence number", "seqno", "", "6-bit ASCII as defined in Table 47, Annex 8."));
		getMessageBlocks().add(new AISPayloadBlock(100, -1, 906, "Text", "text", "", ""));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}
		
		// this is to ignore the master block which created the child data
		if (block.getEnd() == -1) {
			return;
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
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("type", getType());
			node.put("typeText", AISLookupValues.getMessageType(getType()));
			node.put("repeat", getRepeat());
			node.put("mmsi", getMmsi());
			node.put("dac", getDac());
			node.put("fid", getFid());
			node.put("acknowledge", getAcknowledge());
			node.put("seqno", getSeqno());
			node.put("text", getText());

			if (SentenceBase.logLevel >= 2) {
				node.put("dataBits", getData());
				node.put("dataRaw", getDataRaw());
			}

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
