package elsu.ais.parser.message.data;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.messages.AddressedBinaryMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class Type6_DangerousCargo extends AddressedBinaryMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		Type6_DangerousCargo binaryMessage = new Type6_DangerousCargo();

		if (aisMessage instanceof AddressedBinaryMessage) {
			binaryMessage.parseMessage((AddressedBinaryMessage) aisMessage);
		} else {
			((AddressedBinaryMessage) binaryMessage).parseMessage(messageBits);
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public Type6_DangerousCargo() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(88, 117, 30, "Last Port Of Call", "lastport", "t", "5 6-bit characters, UN locode"));
		getMessageBlocks().add(new PayloadBlock(118, 121, 4, "ETA month (UTC)", "lmonth", "u", "1-12, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(122, 126, 5, "ETA day (UTC)", "lday", "u", "1-31, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(127, 131, 5, "ETA hour (UTC)", "lhour", "u", "0-23, 24=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(132, 137, 6, "ETA minute (UTC)", "lminute", "u", "0-59, 60=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(138, 167, 30, "Next Port Of Call", "nextport", "t", "5 6-bit characters, UN locode"));
		getMessageBlocks().add(new PayloadBlock(168, 171, 4, "ETA month (UTC)", "nmonth", "u", "1-12, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(172, 176, 5, "ETA day (UTC)", "nday", "u", "1-31, 0=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(177, 181, 5, "ETA hour (UTC)", "nhour", "u", "0-23, 24=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(182, 187, 6, "ETA minute (UTC)", "nminute", "u", "0-59, 60=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(188, 307, 120, "Main Dangerous Good", "dangerous", "t", "20 6-bit characters"));
		getMessageBlocks().add(new PayloadBlock(308, 331, 24, "IMD Category", "imdcat", "t", "4  6-bit characters"));
		getMessageBlocks().add(new PayloadBlock(332, 344, 13, "UN Number", "unid", "u", "1-3363 UN Number"));
		getMessageBlocks().add(new PayloadBlock(345, 354, 10, "Amount of Cargo", "amount", "u", "Unsigned integer"));
		getMessageBlocks().add(new PayloadBlock(355, 356, 2, "Unit of Quantity", "unit", "e", "See \"Cargo Unit Codes\""));
		getMessageBlocks().add(new PayloadBlock(357, 359, 3, "Spare", "", "x", "Not used"));
	}

	public void parseMessageBlock(PayloadBlock block) throws Exception {
		if (block.isException()) {
			throw new Exception("parsing error; " + block);
		}

		switch (block.getStart()) {
		case 56:
			setLatitude(float_decoder(block.getBits()) / 600f);
			break;
		case 80:
			setLongitude(float_decoder(block.getBits()) / 600f);
			break;
		case 105:
			setDay(unsigned_integer_decoder(block.getBits()));
			break;
		case 110:
			setHour(unsigned_integer_decoder(block.getBits()));
			break;
		case 115:
			setMinute(unsigned_integer_decoder(block.getBits()));
			break;
		case 121:
			setAverageWindSpeed(unsigned_integer_decoder(block.getBits()));
			break;
		case 128:
			setWindGust(unsigned_integer_decoder(block.getBits()));
			break;
		case 135:
			setWindDirection(unsigned_integer_decoder(block.getBits()));
			break;
		case 144:
			setWindGustDirection(unsigned_integer_decoder(block.getBits()));
			break;
		case 153:
			setAirTemperature(unsigned_integer_decoder(block.getBits()));
			break;
		case 164:
			setRelativeHumidity(unsigned_integer_decoder(block.getBits()));
			break;
		case 171:
			setDewPoint(unsigned_integer_decoder(block.getBits()));
			break;
		case 181:
			setAirPressure(unsigned_integer_decoder(block.getBits()));
			break;
		case 190:
			setAirPressureTendency(unsigned_integer_decoder(block.getBits()));
			break;
		case 192:
			setHorizontalVisibility(unsigned_float_decoder(block.getBits()));
			break;
		case 200:
			setWaterLevel(float_decoder(block.getBits()));
			break;
		case 209:
			setWaterLevelTrend(unsigned_integer_decoder(block.getBits()));
			break;
		case 211:
			setSurfaceCurrentSpeed(unsigned_float_decoder(block.getBits()));
			break;
		case 219:
			setSurfaceCurrentDirection(unsigned_integer_decoder(block.getBits()));
			break;
		case 228:
			setCurrentSpeed2(unsigned_float_decoder(block.getBits()));
			break;
		case 236:
			setCurrentDirection2(unsigned_integer_decoder(block.getBits()));
			break;
		case 245:
			setCurrentMeasurementLevel2(unsigned_float_decoder(block.getBits()));
			break;
		case 250:
			setCurrentSpeed3(unsigned_float_decoder(block.getBits()));
			break;
		case 258:
			setCurrentDirection3(unsigned_integer_decoder(block.getBits()));
			break;
		case 267:
			setCurrentSpeed3(unsigned_float_decoder(block.getBits()));
			break;
		case 272:
			setSignificantWaveHeight(unsigned_float_decoder(block.getBits()));
			break;
		case 280:
			setWavePeriod(unsigned_integer_decoder(block.getBits()));
			break;
		case 286:
			setWaveDirection(unsigned_integer_decoder(block.getBits()));
			break;
		case 295:
			setSwellHeight(unsigned_float_decoder(block.getBits()));
			break;
		case 303:
			setSwellPeriod(unsigned_integer_decoder(block.getBits()));
			break;
		case 309:
			setSwellDirection(unsigned_integer_decoder(block.getBits()));
			break;
		case 318:
			setSeaState(unsigned_integer_decoder(block.getBits()));
			break;
		case 322:
			setWaterTemperature(unsigned_float_decoder(block.getBits()));
			break;
		case 332:
			setPrecipitationType(unsigned_integer_decoder(block.getBits()));
			break;
		case 335:
			setSalinity(unsigned_float_decoder(block.getBits()));
			break;
		case 344:
			setIce(unsigned_integer_decoder(block.getBits()));
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
		buffer.append(", \"dataBits\":\"" + getData() + "\"");
		buffer.append(", \"dataRaw\":\"" + getDataRaw() + "\"");
		buffer.append("}");

		return buffer.toString();
	}

	private String lastPortOfCall = "";
	private int lpocMonth = 0;
	private int lpocDay = 0;
	private int lpocHour = 0;
	private int lpocMinute = 0;
	private String nextPortOfCall = "";
	private int npocMonth = 0;
	private int npocDay = 0;
	private int npocHour = 0;
	private int npocMinute = 0;
	private String dangerous = "";
	private String imdcat = "";
	private int unid = 0;
	private int amount = 0;
	private int unitOfQuantity = 0;
}
