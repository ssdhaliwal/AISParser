package elsu.ais.messages.data;

import java.util.ArrayList;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISPayloadBlock;
import elsu.sentence.Sentence;
import elsu.sentence.SentenceBase;

public class GNSSMessage {

	public static GNSSMessage fromPayload(String messageBits) {
		GNSSMessage commState = new GNSSMessage();
		commState.parseMessage(messageBits);

		return commState;
	}

	public GNSSMessage() {
		initialize();
	}

	private void initialize() {
		messageBlocks.add(new AISPayloadBlock(0, 5, 6, "Message Type", "type", "u", ""));
		messageBlocks.add(new AISPayloadBlock(6, 15, 10, "Station Identification", "stationId", "u", ""));
		messageBlocks.add(new AISPayloadBlock(16, 28, 13, "Zcount", "zCount", "u", ""));
		messageBlocks.add(new AISPayloadBlock(29, 31, 3, "Sequence Number", "seqno", "u", ""));
		messageBlocks.add(new AISPayloadBlock(32, 36, 5, "words", "words", "u", ""));
		messageBlocks.add(new AISPayloadBlock(37, 39, 3, "health", "health", "u", ""));
		messageBlocks.add(new AISPayloadBlock(40, -1, 6, "GNSS Message", "gnssMessage", "u", ""));
	}

	public void parseMessage(String message) {
		for (AISPayloadBlock block : messageBlocks) {
			try {
				if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
					block.setBits(message.substring(block.getStart(), message.length()));
				} else {
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				}
			} catch (IndexOutOfBoundsException iobe) {
				if (block.getStart() >= 6) {
					continue;
				}
			}

			switch (block.getStart()) {
			case 0:
				setType(Sentence.parseUINT(block.getBits()));
				break;
			case 6:
				setStationId(Sentence.parseUINT(block.getBits()));
				break;
			case 16:
				setZCount(Sentence.parseUINT(block.getBits()));
				break;
			case 29:
				setSeqno(Sentence.parseUINT(block.getBits()));
				break;
			case 32:
				setWords(Sentence.parseUINT(block.getBits()));
				break;
			case 37:
				setHealth(Sentence.parseUINT(block.getBits()));
				break;
			case 40:
				setData(Sentence.parseBITS(block.getBits()));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append(", \"type\":" + getType());
		buffer.append(", \"typeText\":\"" + AISLookupValues.getGNSSMessageType(getType()) + "\"");
		buffer.append(", \"stationId\":" + getStationId());
		buffer.append(", \"zCount\":" + getZCount());
		buffer.append(", \"seqno\":" + getSeqno());
		buffer.append(", \"words\":" + getWords());
		buffer.append(", \"health\":" + getHealth());
		if (SentenceBase.logLevel >= 2) {
			buffer.append(", \"dataBits\":\"" + getData() + "\"");
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

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getZCount() {
		return zCount;
	}

	public void setZCount(int zCount) {
		this.zCount = zCount;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	private int type = 0;
	private int stationId = 0;
	private int zCount = 0;
	private int seqno = 0;
	private int words = 0;
	private int health = 0;
	private String data = "";

	private ArrayList<AISPayloadBlock> messageBlocks = new ArrayList<>();
}
