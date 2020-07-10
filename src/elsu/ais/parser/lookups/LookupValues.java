package elsu.ais.parser.lookups;

import java.util.HashMap;

public class LookupValues {

	private static String[] navigationStatus = {
			"Under way using engine",
			"At anchor",
			"Not under command",
			"Restricted manoeuverability",
			"Constrained by her draught",
			"Moored",
			"Aground",
			"Engaged in Fishing",
			"Under way sailing",
			"Reserved for future amendment of Navigational Status for HSC",
			"Reserved for future amendment of Navigational Status for WIG",
			"Reserved for future use",
			"Reserved for future use",
			"Reserved for future use",
			"AIS-SART is active",
			"Not defined (default)"
	};
	
	public static String getNavigationStatus(int status) {
		return navigationStatus[status];
	}
	
	private static String[] maneuverIndicator = {
			"Not available (default)",
			"No special maneuver",
			"Special maneuver (such as regional passing arrangement)"			
	};
	
	public static String getManeuverIndicator(int maneuver) {
		return maneuverIndicator[maneuver];
	}
	
	private static String[] EPFDFixTypes = {
			"Undefined (default)",
			"GPS",
			"GLONASS",
			"Combined GPS/GLONASS",
			"Loran-C",
			"Chayka",
			"Integrated navigation system",
			"Surveyed",
			"Galileo"			
	};
	
	public static String getEPFDFixType(int epfd) {
		if (epfd == 15) {
			return "Undefined";
		}
		
		return EPFDFixTypes[epfd];
	}
	
	private static String[] ShipTypes = {
			"Not available (default)",
			"Reserved for future use",
			"Wing in ground (WIG), all ships of this type",
			"Wing in ground (WIG), Hazardous category A",
			"Wing in ground (WIG), Hazardous category B",
			"Wing in ground (WIG), Hazardous category C",
			"Wing in ground (WIG), Hazardous category D",
			"Wing in ground (WIG), Reserved for future use",
			"Wing in ground (WIG), Reserved for future use",
			"Wing in ground (WIG), Reserved for future use",
			"Wing in ground (WIG), Reserved for future use",
			"Wing in ground (WIG), Reserved for future use",
			"Fishing",
			"Towing",
			"Towing: length exceeds 200m or breadth exceeds 25m",
			"Dredging or underwater ops",
			"Diving ops",
			"Military ops",
			"Sailing",
			"Pleasure Craft",
			"Reserved",
			"Reserved",
			"High speed craft (HSC), all ships of this type",
			"High speed craft (HSC), Hazardous category A",
			"High speed craft (HSC), Hazardous category B",
			"High speed craft (HSC), Hazardous category C",
			"High speed craft (HSC), Hazardous category D",
			"High speed craft (HSC), Reserved for future use",
			"High speed craft (HSC), Reserved for future use",
			"High speed craft (HSC), Reserved for future use",
			"High speed craft (HSC), Reserved for future use",
			"High speed craft (HSC), No additional information",
			"Pilot Vessel",
			"Search and Rescue vessel",
			"Tug",
			"Port Tender",
			"Anti-pollution equipment",
			"Law Enforcement",
			"Spare - Local Vessel",
			"Spare - Local Vessel",
			"Medical Transport",
			"Noncombatant ship according to RR Resolution No. 18",
			"Passenger, all ships of this type",
			"Passenger, Hazardous category A",
			"Passenger, Hazardous category B",
			"Passenger, Hazardous category C",
			"Passenger, Hazardous category D",
			"Passenger, Reserved for future use",
			"Passenger, Reserved for future use",
			"Passenger, Reserved for future use",
			"Passenger, Reserved for future use",
			"Passenger, No additional information",
			"Cargo, all ships of this type",
			"Cargo, Hazardous category A",
			"Cargo, Hazardous category B",
			"Cargo, Hazardous category C",
			"Cargo, Hazardous category D",
			"Cargo, Reserved for future use",
			"Cargo, Reserved for future use",
			"Cargo, Reserved for future use",
			"Cargo, Reserved for future use",
			"Cargo, No additional information",
			"Tanker, all ships of this type",
			"Tanker, Hazardous category A",
			"Tanker, Hazardous category B",
			"Tanker, Hazardous category C",
			"Tanker, Hazardous category D",
			"Tanker, Reserved for future use",
			"Tanker, Reserved for future use",
			"Tanker, Reserved for future use",
			"Tanker, Reserved for future use",
			"Tanker, No additional information",
			"Other Type, all ships of this type",
			"Other Type, Hazardous category A",
			"Other Type, Hazardous category B",
			"Other Type, Hazardous category C",
			"Other Type, Hazardous category D",
			"Other Type, Reserved for future use",
			"Other Type, Reserved for future use",
			"Other Type, Reserved for future use",
			"Other Type, Reserved for future use",
			"Other Type, no additional information"
	};
	
	public static String getShipType(int shipType) {
		return ShipTypes[shipType];
	}
	
	private static String[] NavaidTypes = {
			"Default, Type of Aid to Navigation not specified",
			"Reference point",
			"RACON (radar transponder marking a navigation hazard)",
			"Fixed structure off shore, such as oil platforms, wind farms, rigs. (Note: This code should identify an obstruction that is fitted with an Aid-to-Navigation AIS station.)",
			"Spare, Reserved for future use.",
			"Light, without sectors",
			"Light, with sectors",
			"Leading Light Front",
			"Leading Light Rear",
			"Beacon, Cardinal N",
			"Beacon, Cardinal E",
			"Beacon, Cardinal S",
			"Beacon, Cardinal W",
			"Beacon, Port hand",
			"Beacon, Starboard hand",
			"Beacon, Preferred Channel port hand",
			"Beacon, Preferred Channel starboard hand",
			"Beacon, Isolated danger",
			"Beacon, Safe water",
			"Beacon, Special mark",
			"Cardinal Mark N",
			"Cardinal Mark E",
			"Cardinal Mark S",
			"Cardinal Mark W",
			"Port hand Mark",
			"Starboard hand Mark",
			"Preferred Channel Port hand",
			"Preferred Channel Starboard hand",
			"Isolated danger",
			"Safe Water",
			"Special Mark",
			"Light Vessel / LANBY / Rigs"
	};
	
	public static String getNavAidType(int navaidType) {
		return NavaidTypes[navaidType];
	}
}
