package elsu.sentence.tags;

import com.fasterxml.jackson.databind.node.ObjectNode;

import elsu.sentence.SentenceBase;

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
				try {
					setLinenumber(Integer.valueOf(params[i]));
				} catch (Exception exi) {
					setLinenumber(0);
					setExceptions("linenumber value invalid (" + params[i] + ")");
				}
			} else if (i == 1) {
				try {
				setTotallines(Integer.valueOf(params[i]));
				} catch (Exception exi) {
					setTotallines(0);
					setExceptions("totallines value invalid (" + params[i] + ")");
				}
			} else if (i == 2) {
				try {
				setCode(params[i]);
				} catch (Exception exi) {
					setCode("");
					setExceptions("code value invalid (" + params[i] + ")");
				}
			}
		}
	}
	
	@Override
	public String toString() {
		String result = "";
		
		try {
			// result = SentenceBase.objectMapper.writeValueAsString(this);
			ObjectNode node = SentenceBase.objectMapper.createObjectNode();

			node.put("lineNumber", getLinenumber());
			node.put("totalLines", getTotallines());
			node.put("code", getCode());
			node.put("exceptions", getExceptions());

			result = SentenceBase.objectMapper.writeValueAsString(node);
			node = null;
		} catch (Exception exi) {
			result = "error, Sentence, " + exi.getMessage();
		}
		
		return result;
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

	public String getExceptions() {
		return this.exceptions;
	}

	public void setExceptions(String error) {
		this.exceptions += (this.exceptions.isEmpty() ? "" : ", ") + error;
	}

	private int linenumber = 0;
	private int totallines = 0;
	private String code = "";
	private String exceptions = "";
}
