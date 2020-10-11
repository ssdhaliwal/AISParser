package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T6_BinaryAddressedMessage;
import elsu.sentence.SentenceBase;

public class T6_InternationalFunctionalMessage2 extends T6_BinaryAddressedMessage {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T6_InternationalFunctionalMessage2 binaryMessage = new T6_InternationalFunctionalMessage2();

		if (aisMessage instanceof T6_BinaryAddressedMessage) {
			binaryMessage.parseMessage((T6_BinaryAddressedMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_InternationalFunctionalMessage2() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(88, 97, 10, "Requested DAC", "requestedDAC", "", "IAI, RAI, or text"));
		getMessageBlocks().add(new AISPayloadBlock(98, 103, 6, "Requested FI code", "requstedFI", "", "See apprioriate FI reference document"));
		// getMessageBlocks().add(new PayloadBlock(104, 167, 64, "spare", "spare", "", ""));
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
			setRequestedDAC(parseUINT(block.getBits()));
			break;
		case 98:
			setRequestedFI(parseUINT(block.getBits()));
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
			node.put("requestedDAC", getRequestedDAC());
			node.put("requestedFI", getRequestedFI());

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
		return "Type6/InternationalFunctionalMessage2";
	}

	public int getRequestedDAC() {
		return requestedDAC;
	}

	public void setRequestedDAC(int requestedDAC) {
		this.requestedDAC = requestedDAC;
	}

	public int getRequestedFI() {
		return requestedFI;
	}

	public void setRequestedFI(int requestedFI) {
		this.requestedFI = requestedFI;
	}

	private int requestedDAC = 0;
	private int requestedFI = 0;
}
