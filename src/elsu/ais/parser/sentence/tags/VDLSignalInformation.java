package elsu.ais.parser.sentence.tags;

import elsu.ais.parser.AISBase;

public class VDLSignalInformation {

	public static VDLSignalInformation fromString(String message) {
		return new VDLSignalInformation(message);
	}

	public VDLSignalInformation() {
	}

	public VDLSignalInformation(String message) {
		parseMessage(message);
	}

	private void parseMessage(String message) {
		String[] params = message.split(",");

		// extract and update last field of checksum
		String[] cValues = params[params.length - 1].split("\\*");
		int checksum = 0;

		// remove * from last field in params
		params[params.length - 1] = params[params.length - 1].replaceAll("\\*.*", "");

		try {
			setChecksum(cValues[1]);
			checksum = Integer.valueOf(cValues[1], 16);

			int calcChecksum = AISBase.calculateChecksum(message);
			if (calcChecksum != checksum) {
				setChecksumError(true);
			}
		} catch (Exception exi) {
			setChecksumError(true);
		}

		// parse all the values
		for (int i = 0; i < params.length; i++) {
			switch (i) {
			case 0:
				break;
			case 1:
				setId(params[i]);
				break;
			case 2:
				try {
					setLink(Integer.valueOf(params[i]));
				} catch (Exception exi) {
					setLink(0);
					setExceptions("link value invalid (" + params[i] + ")");
				}
				break;
			case 3:
				try {
					setUtc(Float.valueOf(params[i]));
				} catch (Exception exi) {
					setLink(0);
					setExceptions("utc value invalid (" + params[i] + ")");
				}
				break;
			case 4:
				try {
					setSlotnumber(Integer.valueOf(params[i]));
				} catch (Exception exi) {
					setSlotnumber(0);
					setExceptions("slownumber value invalid (" + params[i] + ")");
				}
				break;
			case 5:
				try {
					setSignalstrength(Float.valueOf(params[i]));
				} catch (Exception exi) {
					setSignalstrength(0);
					setExceptions("signalstrength value invalid (" + params[i] + ")");
				}
				break;
			case 6:
				try {
					setSignalnoiseratio(Float.valueOf(params[i]));
				} catch (Exception exi) {
					setSignalnoiseratio(0);
					setExceptions("signalnoiseratio value invalid (" + params[i] + ")");
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("{ VDLSignalInformation: {");
		result.append(" id: \"" + getId() + "\"");
		result.append(", link: " + getLink());
		result.append(", utc: " + getUtc());
		result.append(", slotnumber: " + getSlotnumber());
		result.append(", signalstrength: " + getSignalstrength());
		result.append(", signalnoiseratio: \"" + getSignalnoiseratio() + "\"");
		result.append(", checksum: \"" + getChecksum() + "\"");
		result.append(", checksumError: " + isChecksumError());
		result.append(", exceptions: \"" + getExceptions() + "\"");
		result.append("}}");

		return result.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLink() {
		return link;
	}

	public void setLink(int link) {
		this.link = link;
	}

	public float getUtc() {
		return utc;
	}

	public void setUtc(float utc) {
		this.utc = utc;
	}

	public int getSlotnumber() {
		return slotnumber;
	}

	public void setSlotnumber(int slotnumber) {
		this.slotnumber = slotnumber;
	}

	public float getSignalstrength() {
		return signalstrength;
	}

	public void setSignalstrength(float signalstrength) {
		this.signalstrength = signalstrength;
	}

	public float getSignalnoiseratio() {
		return signalnoiseratio;
	}

	public void setSignalnoiseratio(float signalnoiseratio) {
		this.signalnoiseratio = signalnoiseratio;
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

	private String id = "";
	private int link = 0;
	private float utc = 0;
	private int slotnumber = 9999;
	private float signalstrength = 0;
	private float signalnoiseratio = 0;
	private String checksum = "";
	private boolean checksumError = false;
	private String exceptions = "";
}
