package elsu.nmea.base;

import java.util.*;

public abstract class NMEAMessageBase {
	public static Map<String, Map<Integer, String>> params = new HashMap<String, Map<Integer, String>>() {
		{
			put("ABK", new HashMap<Integer, String>() {
				{
					put(1, "mmsi");
					put(2, "channel");
					put(3, "messageId");
					put(4, "seqno");
					put(5, "acknowledgement");
				}
			});
			put("VSI", new HashMap<Integer, String>() {
				{
					put(1, "uniqueId");
					put(2, "vdmvdoLink");
					put(3, "timeOfMessageArrival");
					put(4, "firstSlotNumber");
					put(5, "signalStrength");
					put(6, "signalToNoiseRatio");
				}
			});
		}
	};
}
