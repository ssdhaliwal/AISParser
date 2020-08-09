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
					put(7, "positionAccuracy");
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
					put(3, "cargoType");
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
			put("AIR", new HashMap<Integer, String>() {
				{
					put(0, "AIS Interrogation Request");
					put(1, "station1Mmsi");
					put(2, "station1Message1Id");
					put(3, "station1Message1SubSection");
					put(4, "station1Message2");
					put(5, "station1Message2SubSection");
					put(6, "station2Mmsi");
					put(7, "station2MessageId");
					put(8, "station2MessageSubSection");
					put(9, "channel");
					put(10, "station1Message1SlotNumber");
					put(11, "station1Message2SlotNumber");
					put(12, "station2Message1SlotNumber");
				}
			});
			put("ASN", new HashMap<Integer, String>() {
				{
					put(0, "AIS Base Station Broadcast of Assignment Command");
					put(1, "destination1Mmsi");
					put(2, "destination1ReportingRate");
					put(3, "destination1SlotNumber");
					put(4, "destination1Increment");
					put(5, "destination2Mmsi");
					put(6, "destination2ReportingRate");
					put(7, "destination2SlotNumber");
					put(8, "destination2Increment");
					put(9, "broadcastChannel");
					put(10, "sentenceFlag");
				}
			});
			put("BGA", new HashMap<Integer, String>() {
				{
					put(0, "Base Station Configuration, General Command");
					put(1, "uniqueId");
					put(2, "rxChannelA");
					put(3, "rxChannelB");
					put(4, "txChannelA");
					put(5, "txChannelB");
					put(6, "powerLevelChannelA");
					put(7, "powerLevelChannelB");
					put(8, "vdlRetries");
					put(9, "vdlRepeatIndicator");
					put(10, "RATDMAControl");
					put(11, "UTCSourceSelection");
					put(12, "adsInterval");
					put(13, "talkedId");
					put(14, "sentenceFlag");
				}
			});
			put("BCL", new HashMap<Integer, String>() {
				{
					put(0, "Base Station Configuration, Location Command");
					put(1, "uniqueId");
					put(2, "positionSource");
					put(4, "latitude");
					put(5, "latitudeNS");
					put(6, "longitude");
					put(7, "longitudeEW");
					put(8, "positionAccuracy");
					put(9, "stationName");
					put(10, "sentenceFlag");
				}
			});
			put("CBR", new HashMap<Integer, String>() {
				{
					put(0, "Configure Broadcast Rates for AIS AtoN Station Message Command");
					put(1, "mmsi");
					put(2, "messageId");
					put(3, "messageIdIndex");
					put(4, "channelAStartUTCHour");
					put(5, "channelAStartUTCMinute");
					put(6, "channelASlotNumber");
					put(7, "channelASlotInterval");
					put(8, "commsType");
					put(9, "channelBStartUTCHour");
					put(10, "channelBStartUTCMinute");
					put(11, "channelBSlotNumber");
					put(12, "channelBSlotInterval");
					put(13, "sentenceFlag");
				}
			});
			put("DLM", new HashMap<Integer, String>() {
				{
					put(0, "Data Link Management Slot Allocations for Base Station Command");
					put(1, "recordIndex");
					put(2, "channel");
					put(3, "reservation1Ownership");
					put(4, "reservation1SlotNumber");
					put(5, "reservation1NumberOfSlots");
					put(6, "reservation1Timeout");
					put(7, "reservation1Increment");
					put(8, "reservation2Ownership");
					put(9, "reservation2SlotNumber");
					put(10, "reservation2NumberOfSlots");
					put(11, "reservation2Timeout");
					put(12, "reservation2Increment");
					put(13, "reservation3Ownership");
					put(14, "reservation3SlotNumber");
					put(15, "reservation3NumberOfSlots");
					put(16, "reservation3Timeout");
					put(17, "reservation3Increment");
					put(18, "reservation4Ownership");
					put(19, "reservation4SlotNumber");
					put(20, "reservation4NumberOfSlots");
					put(21, "reservation4Timeout");
					put(22, "reservation4Increment");
					put(23, "sentenceFlag");
				}
			});
			put("EBC", new HashMap<Integer, String>() {
				{
					put(0, "Configure Broadcast Schedules for Base Station Messages, Command");
					put(1, "uniqueId");
					put(2, "messageType");
					put(3, "channelAUTCMinute");
					put(4, "channelASlotNumber");
					put(5, "channelASlotInterval");
					put(6, "channelASlotCount");
					put(7, "channelBUTCMinute");
					put(8, "channelBSlotNumber");
					put(9, "channelBSlotInterval");
					put(10, "channelBSlotCount");
					put(11, "sentenceFlag");
				}
			});
			put("FSR", new HashMap<Integer, String>() {
				{
					put(0, "Frame Summary of AIS Reception");
					put(1, "uniqueId");
					put(2, "UTChhmmss.ss");
					put(3, "channel");
					put(4, "previousReceiveSlotsOccupied");
					put(5, "previousTranmitSlotsOccupied");
					put(6, "previousCrcFailures");
					put(7, "currentSlotsReservedExternal");
					put(8, "currentSlotsReservedStation");
					put(9, "previousAverageNoiseLevel");
					put(10, "slotsWithSignalStrength");
				}
			});
			put("LRF", new HashMap<Integer, String>() {
				{
					put(0, "AIS Long-Range Function");
					put(1, "seqno");
					put(2, "mmsi");
					put(3, "requestorName");
					put(4, "request");
					put(5, "replyStatus");
				}
			});
			put("LRI", new HashMap<Integer, String>() {
				{
					put(0, "AIS Long-Range Interrogation");
					put(1, "seqno");
					put(2, "controlFlag");
					put(3, "requstorMmsi");
					put(4, "destinationMmsi");
					put(5, "latitude1");
					put(6, "latitude1NS");
					put(7, "longitude1");
					put(8, "longitude1EW");
					put(9, "latitude2");
					put(10, "latitude2NS");
					put(11, "longitude2");
					put(12, "longitude2EW");
				}
			});
			put("LR1", new HashMap<Integer, String>() {
				{
					put(0, "AIS Long-range Reply Sentence 1");
					put(1, "seqno");
					put(2, "responderMmsi");
					put(3, "requstorMmsi");
					put(4, "shipName");
					put(5, "callSign");
					put(6, "imo");
				}
			});
			put("LR2", new HashMap<Integer, String>() {
				{
					put(0, "AIS Long-range Reply Sentence 2");
					put(1, "seqno");
					put(2, "responderMmsi");
					put(3, "UTCDate");
					put(4, "UTCTime");
					put(5, "callSign");
					put(6, "imo");
					put(7, "latitude");
					put(8, "latitudeNS");
					put(9, "longitude");
					put(10, "longitudeEW");
					put(11, "course");
					put(12, "courseUnit");
					put(13, "speed");
					put(14, "speedUnit");
				}
			});
			put("LR3", new HashMap<Integer, String>() {
				{
					put(0, "AIS Long-range Reply Sentence 3");
					put(1, "seqno");
					put(2, "responderMmsi");
					put(3, "destination");
					put(4, "etaUTCDate");
					put(5, "etaUTCTime");
					put(6, "draught");
					put(7, "cargoType");
					put(8, "length");
					put(9, "breadth");
					put(10, "shipType");
					put(11, "persons");
				}
			});
			put("SPO", new HashMap<Integer, String>() {
				{
					put(0, "Select AIS Device’s Processing and Output, Command");
					put(1, "uniqueId");
					put(2, "channel");
					put(3, "vdlSignalStrength");
					put(4, "vdlSlotNumber");
					put(5, "timeOfArrival");
					put(6, "signalToNoiseRatio");
					put(7, "previousChannelLoad");
					put(8, "messagesWithBadCRC");
					put(9, "forecastChannelLoad");
					put(10, "averageNoiseLevel");
					put(11, "slotsWithReceivedSignalStrength");
					put(12, "vsiForVDM");
					put(13, "vsiForVDO");
					put(14, "fsrForFrame");
					put(15, "sentenceFlag");
				}
			});
			put("SSD", new HashMap<Integer, String>() {
				{
					put(0, "AIS Ship Static Data");
					put(1, "callSign");
					put(2, "shipName");
					put(3, "toBow");
					put(4, "toStern");
					put(5, "toPort");
					put(6, "toStarboard");
					put(7, "dte");
					put(8, "source");
				}
			});
			put("TFR", new HashMap<Integer, String>() {
				{
					put(0, "Transmit Feed-Back Report");
					put(1, "sentencesForVDM");
					put(2, "vdmLink");
					put(3, "channel");
					put(4, "uniqueId");
					put(5, "requestedUTCHourMinute");
					put(6, "requestedSlotNumber");
					put(7, "assignedUTCHourMinute");
					put(8, "assignedSlotNumber");
					put(9, "numberOfSlots");
					put(10, "priority");
					put(11, "statis");
				}
			});
			put("TPC", new HashMap<Integer, String>() {
				{
					put(0, "Transmit Slot Prohibit, Command");
					put(1, "uniqueId");
					put(2, "seqno");
					put(3, "channel");
					put(4, "UTCTime");
					put(5, "referenceSlot");
					put(6, "slotOffsetFirstBlock");
					put(7, "consecutiveSlotsFirstBlock");
					put(8, "slotOffsetSecondBlock");
					put(9, "consecutiveSlotsSecondBlock");
					put(10, "slotOffsetThirdBlock");
					put(11, "consecutiveSlotsThirdBlock");
					put(12, "prohibitDurationControl");
					put(13, "sentenceFlag");
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
			put("TSP", new HashMap<Integer, String>() {
				{
					put(0, "Temporary Transmit Slot Prohibit");
					put(1, "uniqueId");
					put(2, "seqno");
					put(3, "channel");
					put(4, "UTCTime");
					put(5, "referenceSlot");
					put(6, "slotOffsetFirstBlock");
					put(7, "consecutiveSlotsFirstBlock");
					put(8, "slotOffsetSecondBlock");
					put(9, "consecutiveSlotsSecondBlock");
					put(10, "slotOffsetThirdBlock");
					put(11, "consecutiveSlotsThirdBlock");
				}
			});
			put("TSR", new HashMap<Integer, String>() {
				{
					put(0, "Transmit Slot Prohibit Status Report");
					put(1, "uniqueId");
					put(2, "seqno");
					put(3, "channel");
					put(4, "UTCTime");
					put(5, "status");
				}
			});
			put("VSD", new HashMap<Integer, String>() {
				{
					put(0, "AIS Voyage Static Data");
					put(1, "cargoType");
					put(2, "draught");
					put(3, "persons");
					put(4, "destination");
					put(5, "etaUTCTime");
					put(6, "etaUTCDay");
					put(7, "etaUTCMonth");
					put(8, "navigationStatus");
					put(9, "regionalFlags");
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
