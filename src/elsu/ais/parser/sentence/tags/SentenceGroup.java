package elsu.ais.parser.sentence.tags;

public class SentenceGroup {

	public static SentenceGroup fromString(String group) {
		return new SentenceGroup(group);
	}
	
	public SentenceGroup() {
	}
	
	public SentenceGroup(String group) {
		parseGroup(group);
	}
	
	private void parseGroup(String group) {
		String[] params = group.split("-");
		
		for(int i = 0; i < params.length; i++) {
			if (i == 0) {
				setLinenumber(Integer.valueOf(params[i]));
			} else if (i == 1) {
				setTotallines(Integer.valueOf(params[i]));
			} else if (i == 2) {
				setCode(params[i]);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ SentenceGroup: {");
		sb.append(" linenumber: " + getLinenumber());
		sb.append(", totallines: " + getTotallines());
		sb.append(", code: \"" + getCode() + "\"");
		sb.append("}}");
		
		return sb.toString();
	}
	
	public int getLinenumber() {
		return linenumber;
	}

	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}

	public int getTotallines() {
		return totallines;
	}

	public void setTotallines(int totallines) {
		this.totallines = totallines;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private int linenumber = 0;
	private int totallines = 0;
	private String code = "";
}
