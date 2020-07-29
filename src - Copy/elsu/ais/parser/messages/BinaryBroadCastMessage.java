package elsu.ais.parser.messages;

import java.util.*;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.resources.PayloadBlock;

public class BinaryBroadCastMessage extends AISMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
		BinaryBroadCastMessage binaryMessage = new BinaryBroadCastMessage();

		binaryMessage.setRawMessage(aisMessage.getRawMessage());
		binaryMessage.setBinaryMessage(aisMessage.getBinaryMessage());
		binaryMessage.setEncodedMessage(aisMessage.getEncodedMessage());
		binaryMessage.setErrorMessage(aisMessage.getErrorMessage());

		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public BinaryBroadCastMessage() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new PayloadBlock(0, 5, 6, "Message Type", "type", "u", "Constant: 8"));
		messageBlocks
				.add(new PayloadBlock(6, 7, 2, "Repeat Indicator", "repeat", "u", "As in Common Navigation Block"));
		messageBlocks.add(new PayloadBlock(8, 37, 30, "Source MMSI", "mmsi", "u", "9 decimal digits"));
		messageBlocks.add(new PayloadBlock(38, 39, 2, "Spare", "", "x", "Not used"));
		messageBlocks.add(new PayloadBlock(40, 49, 10, "Designated Area Code", "dac", "u", "Unsigned integer"));
		messageBlocks.add(new PayloadBlock(50, 55, 6, "Functional ID", "fid", "u", "Unsigned integer"));
		messageBlocks
				.add(new PayloadBlock(56, -1, 952, "Data", "data", "d", "Binary data, May be shorter than 952 bits."));
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
			case 40:
				setDac(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 50:
				setFid(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 56:
				setData(AISMessage.bit_decoder(block.getBits()));
				break;
			}
		}
	}

	public void parseMessage(BinaryBroadCastMessage message) {
		this.type = message.getType();
		this.repeat = message.getRepeat();
		this.mmsi = message.getMmsi();
		this.data = message.getData();
		this.data_raw = message.getDataRaw();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"BinaryBroadCastMessage\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"data_bits\":\"" + getData() + "\"");
		buffer.append(", \"data_raw\":\"" + getDataRaw() + "\"");
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
	}

	public int getDac() {
		return dac;
	}

	public void setDac(int dac) {
		this.dac = dac;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getData() {
		return data;
	}

	public String getDataRaw() {
		return data_raw;
	}

	public void setData(String data) {
		this.data = data;
		this.data_raw = text_decoder_8bit(data);
	}

	private int type = 0;
	private int repeat = 0;
	private int mmsi = 0;
	private int dac = 0;
	private int fid = 0;
	private String data = "";
	private String data_raw = "";
}
