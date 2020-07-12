package elsu.ais.parser.messages;

import java.math.BigInteger;

public class _PayloadBlock {

	public _PayloadBlock(int start, int end, int length, String description, String member, String type, String units) {
		setStart(start);
		setEnd(end);
		setLength(length);
		setDescription(description);
		setMember(member);
		setType(type);
		setUnits(units);
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
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
		this.bits = bits;
		this.hex_value = new BigInteger(bits, 2).toString(16);
	}
	
	public String getHexValue() {
		return this.hex_value;
	}

	private int start = 0;
	private int end = 0;
	private int length = 0;
	private String description = "";
	private String member = "";
	private String type = "";
	private String units = "";
	private String bits = "";
	private String hex_value = "";
}
