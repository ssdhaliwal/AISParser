package elsu.nmea.base;

import java.util.*;

public abstract class NMEAMessageBase {
	public static Map<String, Map<Integer, String>> params = new HashMap<String, Map<Integer, String>>() {
		{
			put("ABK", new HashMap<Integer, String>() {
				{
					put(0, "AIS Addressed and Binary Broadcast Acknowledgement");
					put(1, "mmsi");
					put(2, "channel");
					put(3, "messageId");
					put(4, "seqno");
					put(5, "acknowledgement");
				}
			});
			put("ACA", new HashMap<Integer, String>() {
				{
					put(0, "AIS Regional Channel Assignment Message");
					put(1, "seqno");
					put(2, "Latitude1");
					put(3, "Latitude1NS");
					put(4, "Longitude1");
					put(5, "Longitude1EW");
					put(6, "Latitude2");
					put(7, "Latitude2NS");
					put(8, "Longitude2");
					put(9, "Longitude2EW");
					put(10, "zoneSize");
					put(11, "channelA");
					put(12, "channelABandwidth");
					put(13, "channelB");
					put(14, "channelBBandwidth");
					put(15, "txrxModeControl");
					put(16, "powerLevelControl");
					put(17, "informationSource");
					put(18, "inUseFlag");
					put(19, "timeOfInUseChange");
				}
			});
			put("ACF", new HashMap<Integer, String>() {
				{
					put(0, "General AtoN Station Configuration Command");
					put(1, "mmsi");
					put(2, "epfs");
					put(3, "chartedLatitude");
					put(4, "chartedLatitudeRegion");
					put(5, "chartedLongitude");
					put(6, "chartedLongitudeRegion");
					put(7, "accuracy");
					put(8, "rxChannelA");
					put(9, "rxChannelB");
					put(10, "txChannelA");
					put(11, "txChannelB");
					put(12, "powerLevel");
					put(13, "typeOfAtoN");
					put(14, "virtual");
					put(15, "sentenceStatus");
				}
			});
			put("ACG", new HashMap<Integer, String>() {
				{
					put(0, "Extended General AtoN Station Configuration Command");
					put(1, "mmsi");
					put(2, "status");
					put(3, "offPositionThreshold");
					put(4, "acknowledgementProcedure");
					put(5, "offPositionBehaviour");
					put(6, "synchLostBehaviour");
					put(7, "name");
					put(8, "dimensions");
					put(9, "sentenceStatus");
				}
			});
			put("ACM", new HashMap<Integer, String>() {
				{
					put(0, "AIS Base Station Addressed Channel Management Command");
					put(1, "mmsi1");
					put(2, "mmsi2");
					put(3, "channelA");
					put(4, "channelABandwidth");
					put(5, "channelB");
					put(6, "channelBBandwidth");
					put(7, "txrxMode");
					put(8, "powerLevelControl");
					put(9, "broadcastChannel");
					put(10, "zoneSize");
					put(11, "sentenceStatus");
				}
			});
			put("ACS", new HashMap<Integer, String>() {
				{
					put(0, "AIS Channel Management Information Source");
					put(1, "seqno");
					put(2, "mmsi");
					put(3, "UTCReceipt");
					put(4, "UTCDay");
					put(5, "UTCMonth");
					put(6, "UTCYear");
				}
			});
			put("AFB", new HashMap<Integer, String>() {
				{
					put(0, "AtoN Forced Broadcast Command");
					put(1, "mmsi");
					put(2, "messageId");
					put(3, "messageIdIndex");
					put(4, "startUTCHour");
					put(5, "startUTCMinute");
					put(6, "startSlot");
					put(7, "txChannel");
					put(8, "sentenceStatus");
				}
			});
			put("AGA", new HashMap<Integer, String>() {
				{
					put(0, "AIS Base Station Broadcast of a Group Assignment Command");
					put(1, "uniqueId");
					put(2, "stationType");
					put(3, "shipCargoType");
					put(4, "latitude1");
					put(5, "latitude1NS");
					put(6, "longitude1");
					put(7, "longitude1EW");
					put(8, "latitude2");
					put(9, "latitude2NS");
					put(10, "longitude2");
					put(11, "longitude2EW");
					put(12, "reportingInterval");
					put(13, "txrxMode");
					put(14, "quiteTime");
					put(15, "sentenceFlag");
				}
			});
			put("AID", new HashMap<Integer, String>() {
				{
					put(0, "AtoN Identification Configuration Command");
					put(1, "uniqueID");
					put(2, "aidAction");
					put(3, "mmsi");
					put(4, "aidType");
					put(5, "sentenceStatus");
				}
			});
			put("TSA", new HashMap<Integer, String>() {
				{
					put(0, "Transmit Slot Assignment");
					put(1, "uniqueId");
					put(2, "vdmLink");
					put(3, "channel");
					put(4, "UTCHourMinute");
					put(5, "slotNumber");
					put(6, "priority");
				}
			});
			put("VSI", new HashMap<Integer, String>() {
				{
					put(0, "VDL Signal Information");
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
