package elsu.ais.parser.messages.data;

import java.util.ArrayList;

import elsu.ais.parser.AISMessage;
import elsu.ais.parser.messages.BinaryBroadCastMessage;
import elsu.ais.parser.resources.PayloadBlock;

public class Type8_Dac1_Fid11 extends BinaryBroadCastMessage {

	public static AISMessage fromAISMessage(AISMessage aisMessage, String messageBits) {
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

	public void parseMessage(String message) {
		for (PayloadBlock block : getMessageBlocks()) {
			if (block.getEnd() == -1) {
				block.setBits(message.substring(block.getStart(), message.length()));
			} else {
				block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
			}

			switch (block.getStart()) {
			case 56:
				setLat(AISMessage.float_decoder(block.getBits()) / 60000f);
				break;
			case 80:
				setLon(AISMessage.float_decoder(block.getBits()) / 60000f);
				break;
			case 105:
				setDay(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 110:
				setHour(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 115:
				setMinute(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 121:
				setWspeed(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 128:
				setWgust(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 135:
				setWdir(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 144:
				setWgustdir(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 153:
				setTemperature(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 164:
				setHumidity(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 171:
				setDewpoint(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 181:
				setPressure(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 190:
				setPressuretend(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 192:
				setVisibility(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 200:
				setWaterlevel(AISMessage.float_decoder(block.getBits()));
				break;
			case 209:
				setLeveltrend(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 211:
				setCspeed(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 219:
				setCdir(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 228:
				setCspeed2(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 236:
				setCdir2(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 245:
				setCdepth2(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 250:
				setCspeed3(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 258:
				setCdir3(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 267:
				setCdepth3(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 272:
				setWaveheight(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 280:
				setWaveperiod(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 286:
				setWavedir(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 295:
				setSwellheight(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 303:
				setSwellperiod(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 309:
				setSwelldir(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 318:
				setSeastate(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 322:
				setWatertemp(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 332:
				setPreciptype(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			case 335:
				setSalinity(AISMessage.unsigned_float_decoder(block.getBits()));
				break;
			case 344:
				setIce(AISMessage.unsigned_integer_decoder(block.getBits()));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{ \"BinaryBroadCastMessage_DAC1_FID11\": {");
		buffer.append("\"type\":" + getType());
		buffer.append(", \"repeat\":" + getRepeat());
		buffer.append(", \"mmsi\":" + getMmsi());
		buffer.append(", \"dac\":" + getDac());
		buffer.append(", \"fid\":" + getFid());
		buffer.append(", \"data_bits\":\"" + getData() + "\"");
		buffer.append(", \"data_raw\":\"" + getDataRaw() + "\"");
		buffer.append(", \"lat\":" + getLat());
		buffer.append(", \"lon\":" + getLon());
		buffer.append(", \"day\":" + getDay());
		buffer.append(", \"hour\":" + getHour());
		buffer.append(", \"minute\":" + getMinute());
		buffer.append(", \"wspeed\":" + getWspeed());
		buffer.append(", \"wgust\":" + getWgust());
		buffer.append(", \"wdir\":" + getWdir());
		buffer.append(", \"wgustdir\":" + getWgustdir());
		buffer.append(", \"temperature\":" + getTemperature());
		buffer.append(", \"humidity\":" + getHumidity());
		buffer.append(", \"dewpoint\":" + getDewpoint());
		buffer.append(", \"pressure\":" + getPressure());
		buffer.append(", \"pressuretend\":" + getPressuretend());
		buffer.append(", \"visibility\":" + getVisibility());
		buffer.append(", \"waterlevel\":" + getWaterlevel());
		buffer.append(", \"leveltrend\":" + getLeveltrend());
		buffer.append(", \"cspeed\":" + getCspeed());
		buffer.append(", \"cdir\":" + getCdir());
		buffer.append(", \"cspeed2\":" + getCspeed2());
		buffer.append(", \"cdir2\":" + getCdir2());
		buffer.append(", \"cdepth2\":" + getCdepth2());
		buffer.append(", \"cspeed3\":" + getCspeed3());
		buffer.append(", \"cdir3\":" + getCdir3());
		buffer.append(", \"cdepth3\":" + getCdepth3());
		buffer.append(", \"waveheight\":" + getWaveheight());
		buffer.append(", \"waveperiod\":" + getWaveperiod());
		buffer.append(", \"wavedir\":" + getWavedir());
		buffer.append(", \"swellheight\":" + getSwellheight());
		buffer.append(", \"swellperiod\":" + getSwellperiod());
		buffer.append(", \"swelldir\":" + getSwelldir());
		buffer.append(", \"seastate\":" + getSeastate());
		buffer.append(", \"watertemp\":" + getWatertemp());
		buffer.append(", \"preciptype\":" + getPreciptype());
		buffer.append(", \"salinity\":" + getSalinity());
		buffer.append(", \"ice\":" + getIce());
		buffer.append("}}");

		return buffer.toString();
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
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

	public int getWspeed() {
		return wspeed;
	}

	public void setWspeed(int wspeed) {
		this.wspeed = wspeed;
	}

	public int getWgust() {
		return wgust;
	}

	public void setWgust(int wgust) {
		this.wgust = wgust;
	}

	public int getWdir() {
		return wdir;
	}

	public void setWdir(int wdir) {
		this.wdir = wdir;
	}

	public int getWgustdir() {
		return wgustdir;
	}

	public void setWgustdir(int wgustdir) {
		this.wgustdir = wgustdir;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getDewpoint() {
		return dewpoint;
	}

	public void setDewpoint(int dewpoint) {
		this.dewpoint = dewpoint;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getPressuretend() {
		return pressuretend;
	}

	public void setPressuretend(int pressuretend) {
		this.pressuretend = pressuretend;
	}

	public float getVisibility() {
		return visibility;
	}

	public void setVisibility(float visibility) {
		this.visibility = visibility;
	}

	public float getWaterlevel() {
		return waterlevel;
	}

	public void setWaterlevel(float waterlevel) {
		this.waterlevel = waterlevel;
	}

	public int getLeveltrend() {
		return leveltrend;
	}

	public void setLeveltrend(int leveltrend) {
		this.leveltrend = leveltrend;
	}

	public float getCspeed() {
		return cspeed;
	}

	public void setCspeed(float cspeed) {
		this.cspeed = cspeed;
	}

	public int getCdir() {
		return cdir;
	}

	public void setCdir(int cdir) {
		this.cdir = cdir;
	}

	public float getCspeed2() {
		return cspeed2;
	}

	public void setCspeed2(float cspeed2) {
		this.cspeed2 = cspeed2;
	}

	public int getCdir2() {
		return cdir2;
	}

	public void setCdir2(int cdir2) {
		this.cdir2 = cdir2;
	}

	public float getCdepth2() {
		return cdepth2;
	}

	public void setCdepth2(float cdepth2) {
		this.cdepth2 = cdepth2;
	}

	public float getCspeed3() {
		return cspeed3;
	}

	public void setCspeed3(float cspeed3) {
		this.cspeed3 = cspeed3;
	}

	public int getCdir3() {
		return cdir3;
	}

	public void setCdir3(int cdir3) {
		this.cdir3 = cdir3;
	}

	public float getCdepth3() {
		return cdepth3;
	}

	public void setCdepth3(float cdepth3) {
		this.cdepth3 = cdepth3;
	}

	public float getWaveheight() {
		return waveheight;
	}

	public void setWaveheight(float waveheight) {
		this.waveheight = waveheight;
	}

	public int getWaveperiod() {
		return waveperiod;
	}

	public void setWaveperiod(int waveperiod) {
		this.waveperiod = waveperiod;
	}

	public int getWavedir() {
		return wavedir;
	}

	public void setWavedir(int wavedir) {
		this.wavedir = wavedir;
	}

	public float getSwellheight() {
		return swellheight;
	}

	public void setSwellheight(float swellheight) {
		this.swellheight = swellheight;
	}

	public int getSwellperiod() {
		return swellperiod;
	}

	public void setSwellperiod(int swellperiod) {
		this.swellperiod = swellperiod;
	}

	public int getSwelldir() {
		return swelldir;
	}

	public void setSwelldir(int swelldir) {
		this.swelldir = swelldir;
	}

	public int getSeastate() {
		return seastate;
	}

	public void setSeastate(int seastate) {
		this.seastate = seastate;
	}

	public float getWatertemp() {
		return watertemp;
	}

	public void setWatertemp(float watertemp) {
		this.watertemp = watertemp;
	}

	public int getPreciptype() {
		return preciptype;
	}

	public void setPreciptype(int preciptype) {
		this.preciptype = preciptype;
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

	private float lat = 0.0f;
	private float lon = 0.0f;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int wspeed = 0;
	private int wgust = 0;
	private int wdir = 0;
	private int wgustdir = 0;
	private int temperature = 0;
	private int humidity = 0;
	private int dewpoint = 0;
	private int pressure = 0;
	private int pressuretend = 0;
	private float visibility = 0.0f;
	private float waterlevel = 0;
	private int leveltrend = 0;
	private float cspeed = 0.0f;
	private int cdir = 0;
	private float cspeed2 = 0.0f;
	private int cdir2 = 0;
	private float cdepth2 = 0.0f;
	private float cspeed3 = 0.0f;
	private int cdir3 = 0;
	private float cdepth3 = 0.0f;
	private float waveheight = 0.0f;
	private int waveperiod = 0;
	private int wavedir = 0;
	private float swellheight = 0.0f;
	private int swellperiod = 0;
	private int swelldir = 0;
	private int seastate = 0;
	private float watertemp = 0.0f;
	private int preciptype = 0;
	private float salinity = 0.0f;
	private int ice = 0;

}
