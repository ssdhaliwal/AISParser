package elsu.ais.messages.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.ais.base.AISLookupValues;
import elsu.sentence.SentenceBase;

public class T6_TidalWindow_Tidals {

	public T6_TidalWindow_Tidals() {
	}

	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("latitide", getLatitude());
			node.put("longitude", getLongitude());
			node.put("fromHour", getFromHour());
			node.put("fromMinute", getFromMinute());
			node.put("toHour", getToHour());
			node.put("toMinute", getToMinute());
			node.put("currentDirection", getCurrentDirection());
			node.put("currentSpeed", getCurrentSpeed());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
	public int getFromHour() {
		return fromHour;
	}
	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}
	public int getFromMinute() {
		return fromMinute;
	}
	public void setFromMinute(int fromMinute) {
		this.fromMinute = fromMinute;
	}
	public int getToHour() {
		return toHour;
	}
	public void setToHour(int toHour) {
		this.toHour = toHour;
	}
	public int getToMinute() {
		return toMinute;
	}
	public void setToMinute(int toMinute) {
		this.toMinute = toMinute;
	}
	public int getCurrentDirection() {
		return currentDirection;
	}
	public void setCurrentDirection(int currentDirection) {
		this.currentDirection = currentDirection;
	}
	public float getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	private float latitude = 0.0f;
	private float longitude = 0.0f;
	private int  fromHour = 0;
	private int fromMinute = 0;
	private int toHour = 0;
	private int toMinute = 0;
	private int currentDirection = 0;
	private float currentSpeed = 0.0f;
}
