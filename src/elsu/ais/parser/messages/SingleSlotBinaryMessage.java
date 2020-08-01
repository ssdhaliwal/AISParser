package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.base.AISBase;
import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class SingleSlotBinaryMessage extends AISMessage {

	public static AISMessage fromAISMessage(String messageBits) {
		SingleSlotBinaryMessage binaryMessage = new SingleSlotBinaryMessage();
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public SingleSlotBinaryMessage() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 25"));
		getMessageBlocks().add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in CNB"));
		getMessageBlocks().add(new PayloadBlock(8, 37, 30, "MMSI", "mmsi", "u", "9 digits"));
		getMessageBlocks().add(new PayloadBlock(38, 38, 1, "Destination indicator", "addressed", "b", "0=broadcast, 1=addressed."));
		getMessageBlocks().add(new PayloadBlock(39, 39, 1, "Binary data flag", "structured", "b", "See below"));

		// w/+Destination +ApplicationId
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, 0, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 0, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new PayloadBlock(72, 87, 16, 0, "Application ID", "app_id", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(88, 167, 112, 0, "Data", "data", "d", "Binary data"));
		
		// w/Destination -ApplicationId
		getMessageBlocks().add(new PayloadBlock(40, 69, 30, 1, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(70, 71, 2, 1, "Spare", "", "x", "Byte Alignment"));
		// getMessageBlocks().add(new PayloadBlock(72, 71, 0, 1, "Application ID", "app_id", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(72, 167, 96, 1, "Data", "data", "d", "Binary data"));
		
		// w/-Destination +ApplicationId
		// getMessageBlocks().add(new PayloadBlock(40, 39, 0, 2, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(40, 39, 0, 2, "Spare", "", "x", "Byte Alignment"));
		getMessageBlocks().add(new PayloadBlock(40, 55, 16, 2, "Application ID", "app_id", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(56, 167, 112, 2, "Data", "data", "d", "Binary data"));

		// w/-Destination -ApplicationId
		// getMessageBlocks().add(new PayloadBlock(40, 39, 0, 3, "Destination MMSI", "dest_mmsi", "u", "Message destination"));
		// getMessageBlocks().add(new PayloadBlock(40, 39, 0, 3, "Spare", "", "x", "Byte Alignment"));
		// getMessageBlocks().add(new PayloadBlock(40, 39, 0, 3, "Application ID", "app_id", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(40, 167, 128, 3, "Data", "data", "d", "Binary data"));
	}

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
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
				setAddressed(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 39:
				setStructured(AISMessage.boolean_decoder(block.getBits()));
				break;
			case 40:
			case 72:
			case 88:
			case 56:
				if (isAddressed() && isStructured() && (block.getGroup() == 0)) {
					switch (block.getStart()) {
					case 40:
						setDestinationMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
						break;
					case 72:
						setApplicationId(AISMessage.unsigned_integer_decoder(block.getBits()));
						break;
					case 88:
						setData(AISMessage.bit_decoder(block.getBits()));
						return;
					}
				} else if (isAddressed() && !isStructured() && (block.getGroup() == 1)) {
					switch (block.getStart()) {
					case 40:
						setDestinationMmsi(AISMessage.unsigned_integer_decoder(block.getBits()));
						break;
					case 72:
						setData(AISMessage.bit_decoder(block.getBits()));
						return;
					}
				} else if (!isAddressed() && isStructured() && (block.getGroup() == 2)) {
					switch (block.getStart()) {
					case 40:
						setApplicationId(AISMessage.unsigned_integer_decoder(block.getBits()));
						break;
					case 56:
						setData(AISMessage.bit_decoder(block.getBits()));
						return;
					}
				} else if (!isAddressed() && !isStructured() && (block.getGroup() == 3)) {
					switch (block.getStart()) {
					case 40:
						setData(AISMessage.bit_decoder(block.getBits()));
						return;
					}
				}
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
		buffer.append(", \"addressed\":" + isAddressed());
		buffer.append(", \"strictured\":" + isStructured());
		buffer.append(", \"destinationMmsi\":" + getDestinationMmsi());
		buffer.append(", \"applicationId\":" + getApplicationId());
			buffer.append(", \"data\":\"" + getData() + "\"");
			if (AISBase.debug) {
			buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		}
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

	public boolean isAddressed() {
		return addressed;
	}

	public void setAddressed(boolean addressed) {
		this.addressed = addressed;
	}

	public boolean isStructured() {
		return structured;
	}

	public void setStructured(boolean structured) {
		this.structured = structured;
	}

	public int getDestinationMmsi() {
		return destinationMmsi;
	}

	public void setDestinationMmsi(int mmsi) {
		this.destinationMmsi = mmsi;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getData() {
		return data;
	}

	public String getDataRaw() {
		return data;
	}

	public void setData(String bits) {
		this.data = bits;
		this.dataRaw = text_decoder_8bit(bits);
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private boolean addressed = false;
	private boolean structured = false;
	private int destinationMmsi = 0;
	private int applicationId = 0;
	private String data = "";
	private String dataRaw = "";
}
