package elsu.ais.parser.message.data;

import elsu.ais.parser.resources.LookupValues;

public class Type6_TidalWindow_Tidals {

	public Type6_TidalWindow_Tidals() {
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"latitide\":" + getLatitude());
		buffer.append(", \"longitude\":" + getLongitude());
		buffer.append(", \"fromHour\":" + getFromHour());
		buffer.append(", \"fromMinute\":" + getFromMinute());
		buffer.append(", \"toHour\":" + getToHour());
		buffer.append(", \"toMinute\":" + getToMinute());
		buffer.append(", \"currentDirection\":" + getCurrentDirection());
		buffer.append(", \"currentSpeed\":" + getCurrentSpeed());
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
