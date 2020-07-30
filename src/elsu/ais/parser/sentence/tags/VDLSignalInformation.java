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
		for(int i = 0; i < params.length; i++) {
			switch (i) {
			case 0:
				break;
			case 1:
				setId(params[i]);
				break;
			case 2:
				setLink(Integer.valueOf(params[i]));
				break;
			case 3:
				setUtc(Float.valueOf(params[i]));
				break;
			case 4:
				setSlotnumber(Integer.valueOf(params[i]));
				break;
			case 5:
				setSignalstrength(Float.valueOf(params[i]));
				break;
			case 6:
				setSignalnoiseratio(Float.valueOf(params[i]));
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ VDLSignalInformation: {");
		sb.append(" id: \"" + getId() + "\"");
		sb.append(", link: " + getLink());
		sb.append(", utc: " + getUtc());
		sb.append(", slotnumber: " + getSlotnumber());
		sb.append(", signalstrength: " + getSignalstrength());
		sb.append(", signalnoiseratio: \"" + getSignalnoiseratio() + "\"");
		sb.append(", checksum: \"" + getChecksum() + "\"");
		sb.append(", checksumError: " + isChecksumError());
		sb.append("}}");
		
		return sb.toString();
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

	private String id = "";
	private int link = 0;
	private float utc = 0;
	private int slotnumber = 9999;
	private float signalstrength = 0;
	private float signalnoiseratio = 0;
	private String checksum = "";
	private boolean checksumError = false;
}
