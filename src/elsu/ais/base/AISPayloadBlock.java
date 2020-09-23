package elsu.ais.base;

import java.math.BigInteger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.sentence.SentenceBase;

public class AISPayloadBlock {

	public AISPayloadBlock(int start, int end, int length, String description, String name, String type, String units) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setDescription(description);
		setName(name);
		setType(type);
		setUnits(units);
	}

	public AISPayloadBlock(int start, int end, int length, int group, String description, String name, String type, String units) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setGroup(group);
		setDescription(description);
		setName(name);
		setType(type);
		setUnits(units);
	}

	public AISPayloadBlock(int start, int end, int length, int group, String description, String name, String type, String units, boolean padding) {
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
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("start", getStart());
			node.put("end", getEnd());
			node.put("length", getLength());
			node.put("group", getGroup());
			node.put("description", getDescription());
			node.put("name", getName());
			node.put("type", getType());
			node.put("units", getUnits());
			node.put("bits", getBits());
			node.put("hexValue", getHexValue());
			node.put("padding", isPadding());
			node.put("exception", isException());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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
