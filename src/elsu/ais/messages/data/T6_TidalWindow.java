package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.ais.base.AISMessageBase;
import elsu.ais.base.AISPayloadBlock;
import elsu.ais.messages.T6_BinaryAddressedMessage;
import elsu.sentence.SentenceBase;

public class T6_TidalWindow extends T6_BinaryAddressedMessage {

	public static AISMessageBase fromAISMessage(AISMessageBase aisMessage, String messageBits) throws Exception {
		T6_TidalWindow binaryMessage = new T6_TidalWindow();

		if (aisMessage instanceof T6_BinaryAddressedMessage) {
			binaryMessage.parseMessage((T6_BinaryAddressedMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public T6_TidalWindow() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new AISPayloadBlock(88, 91, 4, "Month", "month", "u", "1-12; 0 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(92, 96, 5, "Day", "day", "u", "1-31; 0 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(97, 123, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new AISPayloadBlock(124, 151, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new AISPayloadBlock(152, 156, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(157, 162, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(163, 167, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(168, 173, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(174, 182, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(183, 189, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
		getMessageBlocks().add(new AISPayloadBlock(190, 216, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new AISPayloadBlock(217, 244, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new AISPayloadBlock(245, 249, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(250, 255, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(256, 260, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(261, 266, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(267, 275, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(276, 282, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
		getMessageBlocks().add(new AISPayloadBlock(283, 309, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new AISPayloadBlock(310, 337, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new AISPayloadBlock(338, 342, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(343, 348, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(349, 353, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(354, 359, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(360, 368, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new AISPayloadBlock(369, 375, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
	}

	public void parseMessageBlock(AISPayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 183) {
				return;
			} else {
				throw new Exception("parsing error; " + block);
			}
		}
		
		// this is to ignore the master block which created the child data
		if (block.getEnd() == -1) {
			return;
		}

		switch (block.getStart()) {
		case 88:
			setMonth(parseUINT(block.getBits()));
			break;
		case 92:
			setDay(parseUINT(block.getBits()));
			break;
		
		case 97:
		case 124:
		case 152:
		case 157:
		case 163:
		case 168:
		case 174:
		case 183:
			if (getTidals_1() == null) {
				setTidals_1(new T6_TidalWindow_Tidals());
			}
			
			switch (block.getStart()) {
			case 97:
				getTidals_1().setLatitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 124:
				getTidals_1().setLongitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 152:
				getTidals_1().setFromHour(parseUINT(block.getBits()));
				break;
			case 157:
				getTidals_1().setFromMinute(parseUINT(block.getBits()));
				break;
			case 163:
				getTidals_1().setToHour(parseUINT(block.getBits()));
				break;
			case 168:
				getTidals_1().setToMinute(parseUINT(block.getBits()));
				break;
			case 174:
				getTidals_1().setCurrentDirection(parseUINT(block.getBits()));
				break;
			case 183:
				getTidals_1().setCurrentSpeed(parseUFLOAT(block.getBits()) / 10f);
				break;
			}
			break;
			
		case 190:
		case 217:
		case 245:
		case 250:
		case 256:
		case 261:
		case 267:
		case 276:
			if (getTidals_2() == null) {
				setTidals_2(new T6_TidalWindow_Tidals());
			}
			
			switch (block.getStart()) {
			case 190:
				getTidals_2().setLatitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 217:
				getTidals_2().setLongitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 245:
				getTidals_2().setFromHour(parseUINT(block.getBits()));
				break;
			case 250:
				getTidals_2().setFromMinute(parseUINT(block.getBits()));
				break;
			case 256:
				getTidals_2().setToHour(parseUINT(block.getBits()));
				break;
			case 261:
				getTidals_2().setToMinute(parseUINT(block.getBits()));
				break;
			case 267:
				getTidals_2().setCurrentDirection(parseUINT(block.getBits()));
				break;
			case 276:			
				getTidals_2().setCurrentSpeed(parseUFLOAT(block.getBits()) / 10f);
				break;
			}
			break;
		
		case 283:
		case 310:
		case 338:
		case 343:
		case 349:
		case 354:
		case 360:
		case 369:
			if (getTidals_3() == null) {
				setTidals_3(new T6_TidalWindow_Tidals());
			}
			
			switch(block.getStart()) {
			case 283:
				getTidals_3().setLatitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 310:
				getTidals_3().setLongitude(parseFLOAT(block.getBits()) / 600000f);
				break;
			case 338:
				getTidals_3().setFromHour(parseUINT(block.getBits()));
				break;
			case 343:
				getTidals_3().setFromMinute(parseUINT(block.getBits()));
				break;
			case 349:
				getTidals_3().setToHour(parseUINT(block.getBits()));
				break;
			case 354:
				getTidals_3().setToMinute(parseUINT(block.getBits()));
				break;
			case 360:
				getTidals_3().setCurrentDirection(parseUINT(block.getBits()));
				break;
			case 369:
				getTidals_3().setCurrentSpeed(parseUFLOAT(block.getBits()) / 10f);
				break;
			}
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
			node.set("tidals_1", SentenceBase.objectMapper.readTree(((getTidals_1() != null) ? getTidals_1().toString() : "")));
			node.set("tidals_2", SentenceBase.objectMapper.readTree(((getTidals_2() != null) ? getTidals_2().toString() : "")));
			node.set("tidals_3", SentenceBase.objectMapper.readTree(((getTidals_3() != null) ? getTidals_3().toString() : "")));

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
		return "Type6/TidalWindow";
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public T6_TidalWindow_Tidals getTidals_1() {
		return tidals_1;
	}

	public void setTidals_1(T6_TidalWindow_Tidals tidals_1) {
		this.tidals_1 = tidals_1;
	}

	public T6_TidalWindow_Tidals getTidals_2() {
		return tidals_2;
	}

	public void setTidals_2(T6_TidalWindow_Tidals tidals_2) {
		this.tidals_2 = tidals_2;
	}

	public T6_TidalWindow_Tidals getTidals_3() {
		return tidals_3;
	}

	public void setTidals_3(T6_TidalWindow_Tidals tidals_3) {
		this.tidals_3 = tidals_3;
	}

	private int month = 0;
	private int day = 0;
	private T6_TidalWindow_Tidals tidals_1 = null;
	private T6_TidalWindow_Tidals tidals_2 = null;
	private T6_TidalWindow_Tidals tidals_3 = null;
}
