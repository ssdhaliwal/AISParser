package elsu.ais.parser.resources;

import java.math.BigInteger;

public class PayloadBlock {

	public PayloadBlock(int start, int end, int length, String description, String name, String type, String units) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setDescription(description);
		setName(name);
		setType(type);
		setUnits(units);
	}

	public PayloadBlock(int start, int end, int length, int group, String description, String name, String type, String units) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setGroup(group);
		setDescription(description);
		setName(name);
		setType(type);
		setUnits(units);
	}

	public PayloadBlock(int start, int end, int length, int group, String description, String name, String type, String units, boolean padding) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setGroup(group);
		setDescription(description);
		setName(name);
		setType(type);
		setUnits(units);
		setPadding(padding);
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("{");
		buffer.append("\"start\":" + getStart());
		buffer.append(", \"end\":" + getEnd());
		buffer.append(", \"length\":" + getLength());
		buffer.append(", \"group\":" + getGroup());
		buffer.append(", \"description\":\"" + getDescription() + "\"");
		buffer.append(", \"name\":\"" + getName() + "\"");
		buffer.append(", \"type\":\"" + getType() + "\"");
		buffer.append(", \"units\":\"" + getUnits() + "\"");
		buffer.append(", \"bits\":\"" + getBits() + "\"");
		buffer.append(", \"hexValue\":\"" + getHexValue() + "\"");
		buffer.append(", \"padding\":\"" + isPadding() + "\"");
		buffer.append(", \"exception\":\"" + isException() + "\"");
		buffer.append("}");
		
		return buffer.toString();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		// stuff bits to length if needed
		if ((bits.length() > 0) && (bits.length() < getLength())) {
			this.bits = String.format("%-" + getLength() + "s", bits).replace(' ', '0');
		} else {
			this.bits = bits;
		}
		
		this.hex_value = new BigInteger(bits, 2).toString(16);
	}

	public boolean isPadding() {
		return padding;
	}
	
	public void setPadding(boolean padding) {
		this.padding = padding;
	}

	public String getHexValue() {
		return this.hex_value;
	}

	public boolean isException() {
		return exception;
	}
	
	public void setException(boolean exception) {
		this.exception = exception;
	}
	
	private int start = 0;
	private int end = 0;
	private int length = 0;
	private int group = 0;
	private String description = "";
	private String name = "";
	private String type = "";
	private String units = "";
	private String bits = "";
	private String hex_value = "";
	private boolean padding = false;
	private boolean exception = false; 
}
