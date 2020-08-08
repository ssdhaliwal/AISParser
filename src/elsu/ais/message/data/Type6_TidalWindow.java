package elsu.ais.message.data;

import elsu.ais.base.AISMessage;
import elsu.ais.messages.AddressedBinaryMessage;
import elsu.ais.resources.LookupValues;
import elsu.ais.resources.PayloadBlock;

public class Type6_TidalWindow extends AddressedBinaryMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		Type6_TidalWindow binaryMessage = new Type6_TidalWindow();

		if (aisMessage instanceof AddressedBinaryMessage) {
			binaryMessage.parseMessage((AddressedBinaryMessage) aisMessage);
		} else {
			throw new Exception("parent message not parsed!; ");
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public Type6_TidalWindow() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(88, 91, 4, "Month", "month", "u", "1-12; 0 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(92, 96, 5, "Day", "day", "u", "1-31; 0 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(97, 123, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new PayloadBlock(124, 151, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new PayloadBlock(152, 156, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(157, 162, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(163, 167, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(168, 173, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(174, 182, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(183, 189, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(190, 216, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new PayloadBlock(217, 244, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new PayloadBlock(245, 249, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(250, 255, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(256, 260, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(261, 266, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(267, 275, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(276, 282, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(283, 309, 27, "Latitude", "latitude", "I", "Unit = minutes * 0.0001, 91000 = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new PayloadBlock(310, 337, 28, "Longitude", "longitude", "I", "Unit = minutes * 0.0001, 181000 = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new PayloadBlock(338, 342, 5, "From UTC Hour", "fromHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(343, 348, 6, "From UTC Minute", "fromMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(349, 353, 5, "To UTC Hour", "toHour", "u", "0-23, 24 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(354, 359, 6, "To UTC Minute", "toMinute", "u", "0-59, 60 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(360, 368, 9, "Current Dir Predicted", "currentDirection", "u", "0-359 deg, 360-N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(369, 375, 7, "Current Speed Predicted", "currentSpeed", "U", "0-126, units of 0.1 knots, 127 = N/A (default)."));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			if (block.getStart() >= 183) {
				return;
			} else {
				throw new Exception("parsing error; " + block);
			}
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
				setTidals_1(new Type6_TidalWindow_Tidals());
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
				setTidals_2(new Type6_TidalWindow_Tidals());
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
				setTidals_3(new Type6_TidalWindow_Tidals());
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
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"typeText\":\"" + LookupValues.getMessageType(getType()) + "\"");
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"tidals_1\":" + getTidals_1());
		buffer.append(", \"tidals_2\":" + getTidals_2());
		buffer.append(", \"tidals_3\":" + getTidals_3());
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		buffer.append("}");

		return buffer.toString();
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

	public Type6_TidalWindow_Tidals getTidals_1() {
		return tidals_1;
	}

	public void setTidals_1(Type6_TidalWindow_Tidals tidals_1) {
		this.tidals_1 = tidals_1;
	}

	public Type6_TidalWindow_Tidals getTidals_2() {
		return tidals_2;
	}

	public void setTidals_2(Type6_TidalWindow_Tidals tidals_2) {
		this.tidals_2 = tidals_2;
	}

	public Type6_TidalWindow_Tidals getTidals_3() {
		return tidals_3;
	}

	public void setTidals_3(Type6_TidalWindow_Tidals tidals_3) {
		this.tidals_3 = tidals_3;
	}

	private int month = 0;
	private int day = 0;
	private Type6_TidalWindow_Tidals tidals_1 = null;
	private Type6_TidalWindow_Tidals tidals_2 = null;
	private Type6_TidalWindow_Tidals tidals_3 = null;
}
