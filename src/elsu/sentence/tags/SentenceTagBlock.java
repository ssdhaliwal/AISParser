package elsu.sentence.tags;

import java.util.Date;

import elsu.sentence.SentenceBase;

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

			int calcChecksum = SentenceBase.calculateChecksum(tags);
			if (calcChecksum != checksum) {
				setChecksumError(true);
			}
		} catch (Exception exi) {
			setChecksumError(true);
		}

		for (String tag : params) {
			// unix time c:
			if (tag.startsWith("c:")) {
				try {
					setTime(Integer.valueOf(tag.replace("c:", "")));
				} catch (Exception exi) {
					setTime(0);
					setExceptions("time value invalid (" + tag + ")");
				}
			}
			// destination d:
			else if (tag.startsWith("d:")) {
				try {
					setDestination(tag.replace("d:", ""));
				} catch (Exception exi) {
					setDestination("");
					setExceptions("destination value invalid (" + tag + ")");
				}
			}
			// sentence goup g:
			else if (tag.startsWith("g:")) {
				try {
					setSentenceGroup(SentenceGroup.fromString(tag.replace("g:", "")));
				} catch (Exception exi) {
					setSentenceGroup(null);
					setExceptions("sentenceGroup value invalid (" + tag + ")");
				}
			}
			// line-count n:
			if (tag.startsWith("n:")) {
				try {
					setLinecount(Integer.valueOf(tag.replace("n:", "")));
				} catch (Exception exi) {
					setLinecount(0);
					setExceptions("linecount value invalid (" + tag + ")");
				}
			}
			// relative time r:
			if (tag.startsWith("r:")) {
				try {
					setRelativetime(Integer.valueOf(tag.replace("r:", "")));
				} catch (Exception exi) {
					setRelativetime(0);
					setExceptions("relativetime value invalid (" + tag + ")");
				}
			}
			// source s:
			else if (tag.startsWith("s:")) {
				try {
					setDestination(tag.replace("s:", ""));
				} catch (Exception exi) {
					setDestination("");
					setExceptions("destination value invalid (" + tag + ")");
				}
			}
			// text t:
			else if (tag.startsWith("t:")) {
				try {
					setText(tag.replace("t:", ""));
				} catch (Exception exi) {
					setText("");
					setExceptions("text value invalid (" + tag + ")");
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("{");
		buffer.append("\"time\": \"" + SentenceBase.formatEPOCHToUTC(getTime()) + "\"");
		buffer.append(", \"destination\": \"" + getDestination() + "\"");
		buffer.append(", \"group\": " + getSentenceGroup());
		buffer.append(", \"lineCount\": " + getLinecount());
		buffer.append(", \"relativeTime\": " + getRelativetime());
		buffer.append(", \"source\": \"" + getSource() + "\"");
		buffer.append(", \"text\": \"" + getText() + "\"");
		buffer.append(", \"checksum\": \"" + getChecksum() + "\"");
		buffer.append(", \"checksumError\": " + isChecksumError());
		buffer.append(", \"exceptions\": \"" + getExceptions() + "\"");
		buffer.append("}");

		return buffer.toString();
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

	public String getExceptions() {
		return this.exceptions;
	}

	public void setExceptions(String error) {
		this.exceptions += (this.exceptions.isEmpty() ? "" : ", ") + error;
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
	private String exceptions = "";
}
