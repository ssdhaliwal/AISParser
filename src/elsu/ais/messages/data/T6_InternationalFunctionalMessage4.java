package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T6_BinaryAddressedMessage;
import elsu.sentence.SentenceBase;

public class T6_InternationalFunctionalMessage4 extends T6_BinaryAddressedMessage {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T6_InternationalFunctionalMessage4 binaryMessage = new T6_InternationalFunctionalMessage4();

		if (aisMessage instanceof T6_BinaryAddressedMessage) {
			binaryMessage.parseMessage((T6_BinaryAddressedMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_InternationalFunctionalMessage4() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(88, 97, 10, "Requested DAC", "requestedDAC", "", "IAI, RAI, or text"));
		getMessageBlocks().add(new AISPayloadBlock(98, 225, 128, "FI Availability", "FIAvailability", "", "See apprioriate FI reference document"));
		getMessageBlocks().add(new AISPayloadBlock(226, 351, 126, "spare", "spare", "", ""));
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
			setFIAvailability(parseBITS(block.getBits()));
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
			node.put("FIAvailability", getFIAvailability());

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
