package elsu.ais.parser.sentence.tags;

import java.util.Date;

import elsu.ais.parser.AISBase;

public class SentenceTagBlock {

	public static SentenceTagBlock fromString(String tags) throws Exception {
		SentenceTagBlock tb = new SentenceTagBlock(tags);
		return tb;
	}

	public SentenceTagBlock() {
	}

	public SentenceTagBlock(String tags) throws Exception {
		parseTagBlock(tags);
	}

	private void parseTagBlock(String tags) throws Exception {
		String[] params = tags.split(",");

		// extract and update last field of checksum
		String[] cValues = params[params.length - 1].split("\\*");
		int checksum = 0;
		
		// remove * from last field in params
		params[params.length - 1] = params[params.length - 1].replaceAll("\\*.*", "");
		
		try {
			setChecksum(cValues[1]);
			checksum = Integer.valueOf(cValues[1], 16);
			
			int calcChecksum = AISBase.calculateChecksum(tags);
			if (calcChecksum != checksum) {
				setChecksumError(true);
			}
		} catch (Exception exi) {
			setChecksumError(true);
		}
				
		for (String tag : params) {
			// unix time c:
			if (tag.startsWith("c:")) {
				setTime(Integer.valueOf(tag.replace("c:", "")));
			}
			// destination d:
			else if (tag.startsWith("d:")) {
				setDestination(tag.replace("d:", ""));
			}
			// sentence goup g:
			else if (tag.startsWith("g:")) {
				setSentenceGroup(SentenceGroup.fromString(tag.replace("g:", "")));
			}
			// line-count n:
			if (tag.startsWith("n:")) {
				setLinecount(Integer.valueOf(tag.replace("n:", "")));
			}
			// relative time r:
			if (tag.startsWith("r:")) {
				setRelativetime(Integer.valueOf(tag.replace("r:", "")));
			}
			// source s:
			else if (tag.startsWith("s:")) {
				setDestination(tag.replace("s:", ""));
			}
			// text t:
			else if (tag.startsWith("t:")) {
				setText(tag.replace("t:", ""));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ SentenceTagBlock: {");
		sb.append(" time: \"" + AISBase.getFormattedDate(getTime()) + "\"");
		sb.append(", destination: \"" + getDestination() + "\"");
		sb.append(", group: " + getSentenceGroup());
		sb.append(", linecount: " + getLinecount());
		sb.append(", relativetime: " + getRelativetime());
		sb.append(", source: \"" + getSource() + "\"");
		sb.append(", text: \"" + getText() + "\"");
		sb.append(", checksum: \"" + getChecksum() + "\"");
		sb.append(", checksumError: " + isChecksumError());
		sb.append("}}");
		
		return sb.toString();
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public SentenceGroup getSentenceGroup() {
		return grouping;
	}

	public void setSentenceGroup(SentenceGroup grouping) {
		this.grouping = grouping;
	}

	public int getLinecount() {
		return linecount;
	}

	public void setLinecount(int linecount) {
		this.linecount = linecount;
	}

	public int getRelativetime() {
		return relativetime;
	}

	public void setRelativetime(int relativetime) {
		this.relativetime = relativetime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public boolean isChecksumError() {
		return checksumError;
	}

	public void setChecksumError(boolean error) {
		this.checksumError = error;
	}

	private int time = 0;
	private String destination = "";
	private SentenceGroup grouping = null;
	private int linecount = 0;
	private int relativetime = 0;
	private String source = "";
	private String text = "";
	private String checksum = "";
	private boolean checksumError = false;
}
