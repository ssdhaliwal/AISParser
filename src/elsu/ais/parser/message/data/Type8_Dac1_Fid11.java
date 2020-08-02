package elsu.ais.parser.message.data;

import elsu.ais.parser.base.AISMessage;
import elsu.ais.parser.messages.BinaryBroadCastMessage;
import elsu.ais.parser.resources.LookupValues;
import elsu.ais.parser.resources.PayloadBlock;

public class Type8_Dac1_Fid11 extends BinaryBroadCastMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) throws Exception {
		Type8_Dac1_Fid11 binaryMessage = new Type8_Dac1_Fid11();

		if (aisMessage instanceof BinaryBroadCastMessage) {
			binaryMessage.parseMessage((BinaryBroadCastMessage) aisMessage);
		} else {
			((BinaryBroadCastMessage) binaryMessage).parseMessage(messageBits);
		}
		binaryMessage.parseMessage(messageBits);

		return binaryMessage;
	}

	public Type8_Dac1_Fid11() {
		initialize();
	}

	private void initialize() {
		getMessageBlocks().add(new PayloadBlock(56, 79, 24, "Latitude", "lat", "I3",
				"Unit = minutes * 0.001, 0x7FFFFF = N/A (default), E positive, W negative."));
		getMessageBlocks().add(new PayloadBlock(80, 104, 25, "Longitude", "lon", "I3",
				"Unit = minutes * 0.001, 0xFFFFFF = N/A (default), N positive, S negative."));
		getMessageBlocks().add(new PayloadBlock(105, 109, 5, "Day (UTC)", "day", "u", "1-31, 31=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(110, 114, 5, "Hour (UTC)", "hour", "u", "0-23, 31=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(115, 120, 6, "Minute (UTC)", "minute", "u", "0-59, 63=N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(121, 127, 7, "Average Wind Speed", "wspeed", "u",
				"10-min avg wind speed, knots, 127 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(128, 134, 7, "Gust Speed", "wgust", "u",
				"10-min max wind speed, knots, 127 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(135, 143, 9, "Wind Direction", "wdir", "u",
				"0-359, degrees from true north 511 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(144, 152, 9, "Wind Gust Direction", "wgustdir", "u",
				"0-359, degrees from true north 511 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(153, 163, 11, "Air Temperature", "temperature", "u",
				"Dry bulb temp: 0.1 deg C -60.0 to +60.0, 2047 = N/A (default),"));
		getMessageBlocks().add(new PayloadBlock(164, 170, 7, "Relative Humidity", "humidity", "u",
				"0-100%, units of 1%, 127 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(171, 180, 10, "Dew Point", "dewpoint", "u",
				"-20.0 to +50.0: 0.1 deg C, 1023 = N/A (default),"));
		getMessageBlocks().add(new PayloadBlock(181, 189, 9, "Air Pressure", "pressure", "u",
				"800-1200hPa: units 1hPa, 511 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(190, 191, 2, "Pressure Tendency", "pressuretend", "e",
				"0 = steady, 1 = decreasing, 2 = increasing, 3 - N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(192, 199, 8, "Horiz. Visibility", "visibility", "U1",
				"0-25.0, units of 0.1nm 255 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(200, 208, 9, "Water Level", "waterlevel", "I1",
				"-10.0 to +30.0 in 0.1m, 511 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(209, 210, 2, "Water Level Trend", "leveltrend", "e",
				"0 = steady, 1 = decreasing, 2 = increasing, 3 - N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(211, 218, 8, "Surface Current Speed", "cspeed", "U1",
				"0.0-25.0 knots: units 0.1 knot"));
		getMessageBlocks().add(new PayloadBlock(219, 227, 9, "Surface Current Direction", "cdir", "u",
				"0-359: deg from true north, 511 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(228, 235, 8, "Current Speed #2", "cspeed2", "U1",
				"0.0-25.0 in units of 0.1 knot, 255 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(236, 244, 9, "Current Direction #2", "cdir2", "u",
				"0-359: deg. fom true north, 511 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(245, 249, 5, "Measurement Depth #2", "cdepth2", "U1",
				"0-30m down: units 0.1m, 31 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(250, 257, 8, "Current Speed #3", "cspeed3", "U1",
				"0.0-25.0: units of 0.1 knot, 255 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(258, 266, 9, "Current Direction #3", "cdir3", "u",
				"0-359: degrees fom true north, 511 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(267, 271, 5, "Measurement Depth #3", "cdepth3", "U1",
				"0-30m down: units 0.1m, 31 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(272, 279, 8, "Wave height", "waveheight", "U1",
				"0-25m: units of 0.1m, 255 = N/A (default)."));
		getMessageBlocks().add(
				new PayloadBlock(280, 285, 6, "Wave period", "waveperiod", "u", "Seconds 0-60: 63 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(286, 294, 9, "Wave direction", "wavedir", "u",
				"0-359: deg. ffom true north, 511 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(295, 302, 8, "Swell height", "swellheight", "U1",
				"0-25m: units of 0.1m 255 = N/A (default)."));
		getMessageBlocks().add(
				new PayloadBlock(303, 308, 6, "Swell period", "swellperiod", "u", "Seconds 0-60: 63 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(309, 317, 9, "Swell direction", "swelldir", "u",
				"0-359: deg. fom true north, 511 = N/A (default)."));
		getMessageBlocks().add(new PayloadBlock(318, 321, 4, "Sea state", "seastate", "e", "See \"Beaufort Scale\""));
		getMessageBlocks().add(new PayloadBlock(322, 331, 10, "Water Temperature", "watertemp", "U1",
				"-10.0 to 50.0: units 0.1 C, 1023 = N/A (default)."));
		getMessageBlocks()
				.add(new PayloadBlock(332, 334, 3, "Precipitation", "preciptype", "e", "See \"Precipitation Types\""));
		getMessageBlocks().add(new PayloadBlock(335, 343, 9, "Salinity", "salinity", "U1",
				"0.0-50.0%: units 0.1%, 511 = N/A (default)"));
		getMessageBlocks().add(new PayloadBlock(344, 345, 2, "Ice", "ice", "e",
				"0 = No 1 = Yes 2 = (reserved for future use) 3 = not available = default"));
		getMessageBlocks().add(new PayloadBlock(346, 351, 6, "Spare", "", "x", "Not used"));
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
		buffer.append(", \"latitude\":" + getLatitude());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"averageWindSpeed\":" + getAverageWindSpeed());
		buffer.append(", \"windGust\":" + getWindGust());
		buffer.append(", \"windDirection\":" + getWindDirection());
		buffer.append(", \"windGustDirection\":" + getWindGustDirection());
		buffer.append(", \"airTemperature\":" + getAirTemperature());
		buffer.append(", \"relativeHumidity\":" + getRelativeHumidity());
		buffer.append(", \"dewPoint\":" + getDewPoint());
		buffer.append(", \"airPressure\":" + getAirPressure());
		buffer.append(", \"airPressureTrendency\":" + getAirPressureTendency());
		buffer.append(", \"horizontalVisibility\":" + getHorizontalVisibility());
		buffer.append(", \"waterLevel\":" + getWaterLevel());
		buffer.append(", \"waterLevelTrend\":" + getWaterLevelTrend());
		buffer.append(", \"surfaceCurrentSpeed\":" + getSurfaceCurrentSpeed());
		buffer.append(", \"surfaceCurrentDirection\":" + getSurfaceCurrentDirection());
		buffer.append(", \"currentSpeed2\":" + getCurrentSpeed2());
		buffer.append(", \"currentDirection2\":" + getCurrentDirection2());
		buffer.append(", \"currentMeasurementLevel2\":" + getCurrentMeasurementLevel2());
		buffer.append(", \"currentSpeed3\":" + getCurrentSpeed3());
		buffer.append(", \"currentDirection3\":" + getCurrentDirection3());
		buffer.append(", \"currentMeasurementLevel3\":" + getCurrentMeasurementLevel3());
		buffer.append(", \"significantWaveHeight\":" + getSignificantWaveHeight());
		buffer.append(", \"wavePeriod\":" + getWavePeriod());
		buffer.append(", \"waveDirection\":" + getWaveDirection());
		buffer.append(", \"swellHeight\":" + getSwellHeight());
		buffer.append(", \"swellPeriod\":" + getSwellPeriod());
		buffer.append(", \"swellDirection\":" + getSwellDirection());
		buffer.append(", \"seaState\":" + getSeaState());
		buffer.append(", \"waterTemperature\":" + getWaterTemperature());
		buffer.append(", \"precipitationType\":" + getPrecipitationType());
		buffer.append(", \"salinity\":" + getSalinity());
		buffer.append(", \"ice\":" + getIce());
		buffer.append("}");

		return buffer.toString();
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getAverageWindSpeed() {
		return averageWindSpeed;
	}

	public void setAverageWindSpeed(int averageWindSpeed) {
		this.averageWindSpeed = averageWindSpeed;
	}

	public int getWindGust() {
		return windGust;
	}

	public void setWindGust(int windGust) {
		this.windGust = windGust;
	}

	public int getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	public int getWindGustDirection() {
		return windGustDirection;
	}

	public void setWindGustDirection(int windGustDirection) {
		this.windGustDirection = windGustDirection;
	}

	public int getAirTemperature() {
		return airTemperature;
	}

	public void setAirTemperature(int airTemperature) {
		this.airTemperature = airTemperature;
	}

	public int getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(int relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public int getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	public int getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(int airPressure) {
		this.airPressure = airPressure;
	}

	public int getAirPressureTendency() {
		return airPressureTendency;
	}

	public void setAirPressureTendency(int airPressureTendency) {
		this.airPressureTendency = airPressureTendency;
	}

	public float getHorizontalVisibility() {
		return horizontalVisibility;
	}

	public void setHorizontalVisibility(float horizontalVisibility) {
		this.horizontalVisibility = horizontalVisibility;
	}

	public float getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(float waterLevel) {
		this.waterLevel = waterLevel;
	}

	public int getWaterLevelTrend() {
		return waterLevelTrend;
	}

	public void setWaterLevelTrend(int waterLevelTrend) {
		this.waterLevelTrend = waterLevelTrend;
	}

	public float getSurfaceCurrentSpeed() {
		return surfaceCurrentSpeed;
	}

	public void setSurfaceCurrentSpeed(float surfaceCurrentSpeed) {
		this.surfaceCurrentSpeed = surfaceCurrentSpeed;
	}

	public int getSurfaceCurrentDirection() {
		return surfaceCurrentDirection;
	}

	public void setSurfaceCurrentDirection(int surfaceCurrentDirection) {
		this.surfaceCurrentDirection = surfaceCurrentDirection;
	}

	public float getCurrentSpeed2() {
		return currentSpeed2;
	}

	public void setCurrentSpeed2(float currentSpeed2) {
		this.currentSpeed2 = currentSpeed2;
	}

	public int getCurrentDirection2() {
		return currentDirection2;
	}

	public void setCurrentDirection2(int currentDirection2) {
		this.currentDirection2 = currentDirection2;
	}

	public float getCurrentMeasurementLevel2() {
		return currentMeasurementLevel2;
	}

	public void setCurrentMeasurementLevel2(float currentMeasurementLevel2) {
		this.currentMeasurementLevel2 = currentMeasurementLevel2;
	}

	public float getCurrentSpeed3() {
		return currentSpeed3;
	}

	public void setCurrentSpeed3(float currentSpeed3) {
		this.currentSpeed3 = currentSpeed3;
	}

	public int getCurrentDirection3() {
		return currentDirection3;
	}

	public void setCurrentDirection3(int currentDirection3) {
		this.currentDirection3 = currentDirection3;
	}

	public float getCurrentMeasurementLevel3() {
		return currentMeasurementLevel3;
	}

	public void setCurrentMeasurementLevel3(float currentMeasurementLevel3) {
		this.currentMeasurementLevel3 = currentMeasurementLevel3;
	}

	public float getSignificantWaveHeight() {
		return significantWaveHeight;
	}

	public void setSignificantWaveHeight(float significantWaveHeight) {
		this.significantWaveHeight = significantWaveHeight;
	}

	public int getWavePeriod() {
		return wavePeriod;
	}

	public void setWavePeriod(int wavePeriod) {
		this.wavePeriod = wavePeriod;
	}

	public int getWaveDirection() {
		return waveDirection;
	}

	public void setWaveDirection(int waveDirection) {
		this.waveDirection = waveDirection;
	}

	public float getSwellHeight() {
		return swellHeight;
	}

	public void setSwellHeight(float swellHeight) {
		this.swellHeight = swellHeight;
	}

	public int getSwellPeriod() {
		return swellPeriod;
	}

	public void setSwellPeriod(int swellPeriod) {
		this.swellPeriod = swellPeriod;
	}

	public int getSwellDirection() {
		return swellDirection;
	}

	public void setSwellDirection(int swellDirection) {
		this.swellDirection = swellDirection;
	}

	public int getSeaState() {
		return seaState;
	}

	public void setSeaState(int seaState) {
		this.seaState = seaState;
	}

	public float getWaterTemperature() {
		return waterTemperature;
	}

	public void setWaterTemperature(float waterTemperature) {
		this.waterTemperature = waterTemperature;
	}

	public int getPrecipitationType() {
		return precipitationType;
	}

	public void setPrecipitationType(int precipitationType) {
		this.precipitationType = precipitationType;
	}

	public float getSalinity() {
		return salinity;
	}

	public void setSalinity(float salinity) {
		this.salinity = salinity;
	}

	public int getIce() {
		return ice;
	}

	public void setIce(int ice) {
		this.ice = ice;
	}

	private float latitude = 0.0f;
	private float longitude = 0.0f;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int averageWindSpeed = 0;
	private int windGust = 0;
	private int windDirection = 0;
	private int windGustDirection = 0;
	private int airTemperature = 0;
	private int relativeHumidity = 0;
	private int dewPoint = 0;
	private int airPressure = 0;
	private int airPressureTendency = 0;
	private float horizontalVisibility = 0.0f;
	private float waterLevel = 0;
	private int waterLevelTrend = 0;
	private float surfaceCurrentSpeed = 0.0f;
	private int surfaceCurrentDirection = 0;
	private float currentSpeed2 = 0.0f;
	private int currentDirection2 = 0;
	private float currentMeasurementLevel2 = 0.0f;
	private float currentSpeed3 = 0.0f;
	private int currentDirection3 = 0;
	private float currentMeasurementLevel3 = 0.0f;
	private float significantWaveHeight = 0.0f;
	private int wavePeriod = 0;
	private int waveDirection = 0;
	private float swellHeight = 0.0f;
	private int swellPeriod = 0;
	private int swellDirection = 0;
	private int seaState = 0;
	private float waterTemperature = 0.0f;
	private int precipitationType = 0;
	private float salinity = 0.0f;
	private int ice = 0;

}
