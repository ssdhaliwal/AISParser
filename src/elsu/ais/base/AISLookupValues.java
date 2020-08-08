package elsu.ais.base;

import java.util.HashMap;

public class AISLookupValues {

	private static HashMap<Integer, String> messageType = new HashMap<Integer, String>() {
		{
			put(1, "Position report - Class A Scheduled");
			put(2, "Position report - Class A Assigned");
			put(3, "Position report - Class A Special");
			put(4, "Base station report");
			put(5, "Static and voyage related data");
			put(6, "Binary addressed message");
			put(7, "Binary acknowledge-ment");
			put(8, "Binary broadcast message");
			put(9, "Standard SAR aircraft position report");
			put(10, "UTC/date inquiry");
			put(11, "UTC/date response");
			put(12, "Addressed safety related message");
			put(13, "Safety related acknowledge-ment");
			put(14, "Safety related broadcast message");
			put(15, "Interrogation");
			put(16, "Assignment mode command");
			put(17, "DGNSS broadcast binary message");
			put(18, "Standard Class B equipment position report");
			put(19, "Extended Class B equipment position report");
			put(20, "Data link management message");
			put(21, "Aids-to-navigation report");
			put(22, "Channel management(6)");
			put(23, "Group assignment command");
			put(24, "Static data report");
			put(25, "Single slot binary message");
			put(26, "Multiple slot binary message with Communi-cations State");
			put(27, "Position report for long-range applications");
		}
	};

	public static String getMessageType(Integer number) {
		if (!messageType.containsKey(number)) {
			return "--";
		}

		return messageType.get(number);
	}

	private static HashMap<Integer, String> navigationStatus = new HashMap<Integer, String>() {
		{
			put(0, "Under way using engine");
			put(1, "At anchor");
			put(2, "Not under command");
			put(3, "Restricted manoeuverability");
			put(4, "Constrained by her draught");
			put(5, "Moored");
			put(6, "Aground");
			put(7, "Engaged in Fishing");
			put(8, "Under way sailing");
			put(9, "Reserved for future amendment of Navigational Status for HSC");
			put(10, "Reserved for future amendment of Navigational Status for WIG");
			put(11, "Reserved for future use");
			put(12, "Reserved for future use");
			put(13, "Reserved for future use");
			put(14, "AIS-SART is active");
			put(15, "Not defined (default)");
		}
	};

	public static String getNavigationStatus(Integer status) {
		if (!navigationStatus.containsKey(status)) {
			return "--";
		}

		return navigationStatus.get(status);
	}

	private static HashMap<Integer, String> maneuverIndicator = new HashMap<Integer, String>() {
		{
			put(0, "Not available (default)");
			put(1, "No special maneuver");
			put(2, "Special maneuver (such as regional passing arrangement)");
		}
	};

	public static String getManeuverIndicator(Integer maneuver) {
		if (!maneuverIndicator.containsKey(maneuver)) {
			return "--";
		}

		return maneuverIndicator.get(maneuver);
	}

	private static HashMap<Integer, String> CommunicationTechnology = new HashMap<Integer, String>() {
		{
			put(1, "SOTDMA");
			put(2, "SOTDMA");
			put(3, "ITDMA");
		}
	};

	public static String getCommunicationTechnology(Integer commState) {
		if (!CommunicationTechnology.containsKey(commState)) {
			return "--";
		}

		return CommunicationTechnology.get(commState);
	}

	private static HashMap<Integer, String> DTE = new HashMap<Integer, String>() {
		{
			put(0, "Available");
			put(1, "Not Available");
		}
	};

	public static String getDte(Integer dte) {
		if (!DTE.containsKey(dte)) {
			return "--";
		}

		return DTE.get(dte);
	}

	private static HashMap<Integer, String> CommunicationSyncState = new HashMap<Integer, String>() {
		{
			put(0, "UTC direct");
			put(1, "UTC indirect");
			put(2, "Base direct");
			put(3, "Base indirect");
		}
	};

	public static String getCommunicationSyncState(Integer commState) {
		if (!CommunicationSyncState.containsKey(commState)) {
			return "--";
		}

		return CommunicationSyncState.get(commState);
	}

	private static HashMap<Integer, String> EPFDFixTypes = new HashMap<Integer, String>() {
		{
			put(0, "Undefined (default)");
			put(1, "GPS");
			put(2, "GLONASS");
			put(3, "Combined GPS/GLONASS");
			put(4, "Loran-C");
			put(5, "Chayka");
			put(6, "Integrated navigation system");
			put(7, "Surveyed");
			put(8, "Galileo");
			put(15, "Undefined");
		}
	};

	public static String getEPFDFixType(Integer epfd) {
		if (!EPFDFixTypes.containsKey(epfd)) {
			return "--";
		}

		return EPFDFixTypes.get(epfd);
	}

	private static HashMap<Integer, String> LRBControl = new HashMap<Integer, String>() {
		{
			put(0, "default – Class-A AIS station stops transmission of Message 27 within an AIS base station coverage area.");
			put(1, "Request Class-A station to transmit Message 27 within an AIS base station coverage area");
		}
	};

	public static String getLRBControl(Integer lrbcontrol) {
		if (!LRBControl.containsKey(lrbcontrol)) {
			return "--";
		}

		return LRBControl.get(lrbcontrol);
	}

	private static HashMap<Integer, String> ShipTypes = new HashMap<Integer, String>() {
		{
			put(0, "Not available (default)");
			put(1, "Reserved for future use");
			put(2, "Reserved for future use");
			put(3, "Reserved for future use");
			put(4, "Reserved for future use");
			put(5, "Reserved for future use");
			put(6, "Reserved for future use");
			put(7, "Reserved for future use");
			put(8, "Reserved for future use");
			put(9, "Reserved for future use");
			put(10, "Reserved for future use");
			put(11, "Reserved for future use");
			put(12, "Reserved for future use");
			put(13, "Reserved for future use");
			put(14, "Reserved for future use");
			put(15, "Reserved for future use");
			put(16, "Reserved for future use");
			put(17, "Reserved for future use");
			put(18, "Reserved for future use");
			put(19, "Reserved for future use");
			put(20, "Wing in ground (WIG), all ships of this type");
			put(21, "Wing in ground (WIG), Hazardous category A");
			put(22, "Wing in ground (WIG), Hazardous category B");
			put(23, "Wing in ground (WIG), Hazardous category C");
			put(24, "Wing in ground (WIG), Hazardous category D");
			put(25, "Wing in ground (WIG), Reserved for future use");
			put(26, "Wing in ground (WIG), Reserved for future use");
			put(27, "Wing in ground (WIG), Reserved for future use");
			put(28, "Wing in ground (WIG), Reserved for future use");
			put(29, "Wing in ground (WIG), Reserved for future use");
			put(30, "Fishing");
			put(31, "Towing");
			put(32, "Towing: length exceeds 200m or breadth exceeds 25m");
			put(33, "Dredging or underwater ops");
			put(34, "Diving ops");
			put(35, "Military ops");
			put(36, "Sailing");
			put(37, "Pleasure Craft");
			put(38, "Reserved");
			put(39, "Reserved");
			put(40, "High speed craft (HSC), all ships of this type");
			put(41, "High speed craft (HSC), Hazardous category A");
			put(42, "High speed craft (HSC), Hazardous category B");
			put(43, "High speed craft (HSC), Hazardous category C");
			put(44, "High speed craft (HSC), Hazardous category D");
			put(45, "High speed craft (HSC), Reserved for future use");
			put(46, "High speed craft (HSC), Reserved for future use");
			put(47, "High speed craft (HSC), Reserved for future use");
			put(48, "High speed craft (HSC), Reserved for future use");
			put(49, "High speed craft (HSC), No additional information");
			put(50, "Pilot Vessel");
			put(51, "Search and Rescue vessel");
			put(52, "Tug");
			put(53, "Port Tender");
			put(54, "Anti-pollution equipment");
			put(55, "Law Enforcement");
			put(56, "Spare - Local Vessel");
			put(57, "Spare - Local Vessel");
			put(58, "Medical Transport");
			put(59, "Noncombatant ship according to RR Resolution No. 18");
			put(60, "Passenger, all ships of this type");
			put(61, "Passenger, Hazardous category A");
			put(62, "Passenger, Hazardous category B");
			put(63, "Passenger, Hazardous category C");
			put(64, "Passenger, Hazardous category D");
			put(65, "Passenger, Reserved for future use");
			put(66, "Passenger, Reserved for future use");
			put(67, "Passenger, Reserved for future use");
			put(68, "Passenger, Reserved for future use");
			put(69, "Passenger, No additional information");
			put(70, "Cargo, all ships of this type");
			put(71, "Cargo, Hazardous category A");
			put(72, "Cargo, Hazardous category B");
			put(73, "Cargo, Hazardous category C");
			put(74, "Cargo, Hazardous category D");
			put(75, "Cargo, Reserved for future use");
			put(76, "Cargo, Reserved for future use");
			put(77, "Cargo, Reserved for future use");
			put(78, "Cargo, Reserved for future use");
			put(79, "Cargo, No additional information");
			put(80, "Tanker, all ships of this type");
			put(81, "Tanker, Hazardous category A");
			put(82, "Tanker, Hazardous category B");
			put(83, "Tanker, Hazardous category C");
			put(84, "Tanker, Hazardous category D");
			put(85, "Tanker, Reserved for future use");
			put(86, "Tanker, Reserved for future use");
			put(87, "Tanker, Reserved for future use");
			put(88, "Tanker, Reserved for future use");
			put(89, "Tanker, No additional information");
			put(90, "Other Type, all ships of this type");
			put(91, "Other Type, Hazardous category A");
			put(92, "Other Type, Hazardous category B");
			put(93, "Other Type, Hazardous category C");
			put(94, "Other Type, Hazardous category D");
			put(95, "Other Type, Reserved for future use");
			put(96, "Other Type, Reserved for future use");
			put(97, "Other Type, Reserved for future use");
			put(98, "Other Type, Reserved for future use");
			put(99, "Other Type, no additional information");
		}
	};

	public static String getShipType(Integer shipType) {
		if (!ShipTypes.containsKey(shipType)) {
			return "--";
		}

		return ShipTypes.get(shipType);
	}

	private static HashMap<Integer, String> NavaidTypes = new HashMap<Integer, String>() {
		{
			put(0, "Default, Type of Aid to Navigation not specified");
			put(1, "Reference point");
			put(2, "RACON (radar transponder marking a navigation hazard)");
			put(3, "Fixed structure off shore, such as oil platforms, wind farms, rigs. (Note: This code should identify an obstruction that is fitted with an Aid-to-Navigation AIS station.)");
			put(4, "Spare, Reserved for future use.");
			put(5, "Light, without sectors");
			put(6, "Light, with sectors");
			put(7, "Leading Light Front");
			put(8, "Leading Light Rear");
			put(9, "Beacon, Cardinal N");
			put(10, "Beacon, Cardinal E");
			put(11, "Beacon, Cardinal S");
			put(12, "Beacon, Cardinal W");
			put(13, "Beacon, Port hand");
			put(14, "Beacon, Starboard hand");
			put(15, "Beacon, Preferred Channel port hand");
			put(16, "Beacon, Preferred Channel starboard hand");
			put(17, "Beacon, Isolated danger");
			put(18, "Beacon, Safe water");
			put(19, "Beacon, Special mark");
			put(20, "Cardinal Mark N");
			put(21, "Cardinal Mark E");
			put(22, "Cardinal Mark S");
			put(23, "Cardinal Mark W");
			put(24, "Port hand Mark");
			put(25, "Starboard hand Mark");
			put(26, "Preferred Channel Port hand");
			put(27, "Preferred Channel Starboard hand");
			put(28, "Isolated danger");
			put(29, "Safe Water");
			put(30, "Special Mark");
			put(31, "Light Vessel / LANBY / Rigs");
		}
	};

	public static String getNavAidType(Integer navaidType) {
		if (!NavaidTypes.containsKey(navaidType)) {
			return "--";
		}

		return NavaidTypes.get(navaidType);
	}

	private static HashMap<Integer, String> AssignedMode = new HashMap<Integer, String>() {
		{
			put(0, "Station operating in autonomous and continuous mode (default)");
			put(1, "Station operating in assigned mode");
		}
	};

	public static String getAssignedMode(Integer maneuver) {
		if (!AssignedMode.containsKey(maneuver)) {
			return "--";
		}

		return AssignedMode.get(maneuver);
	}

	private static HashMap<Integer, String> AltitudeSensor = new HashMap<Integer, String>() {
		{
			put(0, "GNSS");
			put(1, "Barometric");
		}
	};

	public static String getAltitudeSensor(Integer altitudeSensor) {
		if (!AltitudeSensor.containsKey(altitudeSensor)) {
			return "--";
		}

		return AltitudeSensor.get(altitudeSensor);
	}

	private static HashMap<Integer, String> CommunicationFlag = new HashMap<Integer, String>() {
		{
			put(0, "SOTDMA");
			put(1, "ITDMA");
		}
	};

	public static String getCommunicationFlag(Integer commFlag) {
		if (!CommunicationFlag.containsKey(commFlag)) {
			return "--";
		}

		return CommunicationFlag.get(commFlag);
	}

	private static HashMap<Integer, String> GNSSMessageType = new HashMap<Integer, String>() {
		{
			// GPS Message Number
			put(1, "Differential GNSS corrections (full set of satellites)");
			put(3, "Reference station parameters");
			put(4, "Reference Station Datum");
			put(5, "Constellation health");
			put(6, "Null frame");
			put(7, "Radio beacon almanacs");
			put(9, "Subset differential GNSS corrections (this may replace Types 1 or 31)");
			put(16, "Special message");

			// GLONASS Message Number
			put(31, "Differential GNSS corrections (full set of satellites)");
			put(32, "Reference station parameters");
			put(4, "Reference Station Datum");
			put(33, "Constellation health");
			put(34, "Null frame");
			put(35, "Radio beacon almanacs");
			put(34, "Subset differential GNSS corrections (this may replace Types 1 or 31)");
			put(36, "Special message");

			// GPS/GLONASS message Number
			put(27, "Extended radio beacon almanac");
		}
	};

	public static String getGNSSMessageType(Integer type) {
		if (!GNSSMessageType.containsKey(type)) {
			return "--";
		}

		return GNSSMessageType.get(type);
	}

	private static HashMap<Integer, String> TXRXMode = new HashMap<Integer, String>() {
		{
			put(0, "Tx A/Tx B, Rx A/Rx B (default)");
			put(1, "Tx A, Rx A/Rx B");
			put(2, "Tx B, Rx A/Rx B");
		}
	};

	public static String getTXRXMode(Integer txrx) {
		if (!TXRXMode.containsKey(txrx)) {
			return "--";
		}

		return TXRXMode.get(txrx);
	}

	private static HashMap<Integer, String> StationType = new HashMap<Integer, String>() {
		{
			put(0, "all types of mobiles (default)");
			put(1, "Class A mobile stations only");
			put(2, "all types of Class B mobile stations");
			put(3, "SAR airborne mobile station");
			put(4, "Class B “SO” mobile stations only");
			put(5, "Class B “CS” shipborne mobile station only");
			put(6, "inland waterways");
			put(7, "regional use");
			put(8, "regional use");
			put(9, "regional use");
			put(10, "base station coverage area (see Message 4 and Message 27)");
		}
	};

	public static String getStationType(Integer type) {
		if (!StationType.containsKey(type)) {
			return "--";
		}

		return StationType.get(type);
	}

	private static HashMap<Integer, String> ReportingInterval = new HashMap<Integer, String>() {
		{
			put(0, "As given by the autonomous mode");
			put(1, "10 min");
			put(2, "6 min");
			put(3, "3 min");
			put(4, "1 min");
			put(5, "30 s");
			put(6, "15 s");
			put(7, "10 s");
			put(8, "5 s");
			put(9, "Next shorter reporting interval");
			put(10, "Next longer reporting interval");
			put(11, "2s");
		}
	};

	public static String getReportingInterval(Integer interval) {
		if (!ReportingInterval.containsKey(interval)) {
			return "--";
		}

		return ReportingInterval.get(interval);
	}

	private static HashMap<Integer, String> DestinationIndicator = new HashMap<Integer, String>() {
		{
			put(0, "Broadcast (no Destination ID field used)");
			put(1, "Addressed (Destination ID uses 30 data bits for MMSI)");
		}
	};

	public static String getDestinationIndicator(Integer indicator) {
		if (!DestinationIndicator.containsKey(indicator)) {
			return "--";
		}

		return DestinationIndicator.get(indicator);
	}

	private static HashMap<Integer, String> BinaryDataFlag = new HashMap<Integer, String>() {
		{
			put(0, "unstructured binary data (no Application Identifier bits used)");
			put(1, "binary data coded as defined by using the 16-bit Application identifier");
		}
	};

	public static String getBinaryDataFlag(Integer dataFlag) {
		if (!BinaryDataFlag.containsKey(dataFlag)) {
			return "--";
		}

		return BinaryDataFlag.get(dataFlag);
	}

	private static HashMap<Integer, String> PositionLatency = new HashMap<Integer, String>() {
		{
			put(0, "Reported position latency is less than 5 seconds");
			put(1, "Reported position latency is greater than 5 seconds = default");
		}
	};

	public static String getPositionLatency(Integer positionLatency) {
		if (!PositionLatency.containsKey(positionLatency)) {
			return "--";
		}

		return PositionLatency.get(positionLatency);
	}

	private static HashMap<Integer, String> UnitOfQuantity = new HashMap<Integer, String>() {
		{
			put(0, "Not available (default)");
			put(1, "kg");
			put(2, "metric tons");
			put(3, "metric kilotons");
		}
	};

	public static String getUnitOfQuantity(Integer unit) {
		if (!UnitOfQuantity.containsKey(unit)) {
			return "--";
		}

		return UnitOfQuantity.get(unit);
	}
}
