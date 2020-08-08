package elsu.ais.base;

import java.util.ArrayList;

import elsu.ais.messages.*;
import elsu.ais.messages.data.*;
import elsu.sentence.Sentence;
import elsu.sentence.SentenceBase;

public abstract class AISMessageBase extends SentenceBase {

	public static Sentence fromSentence(Sentence sentence) throws Exception {
		// call appropriate message class
		switch (sentence.getType()) {
		case 1:
			sentence.setAISMessage(T1_PositionReportClassA.fromAISMessage(sentence.getBitString()));
			break;
		case 2:
			sentence.setAISMessage(T2_PositionReportClassA.fromAISMessage(sentence.getBitString()));
			break;
		case 3:
			sentence.setAISMessage(T3_PositionReportClassA.fromAISMessage(sentence.getBitString()));
			break;
		case 4:
			sentence.setAISMessage(T4_BaseStationReport.fromAISMessage(sentence.getBitString()));
			break;
		case 5:
			sentence.setAISMessage(T5_StaticAndVoyageRelatedData.fromAISMessage(sentence.getBitString()));
			break;
		case 6:
			sentence.setAISMessage(T6_BinaryAddressedMessage.fromAISMessage(sentence.getBitString()));

			T6_BinaryAddressedMessage abm = (T6_BinaryAddressedMessage) sentence.getAISMessage();
			if ((abm.getDac() == 1) && (abm.getFid() == 0)) {
				T6_InternationalFunctionalMessage0 tdf = (T6_InternationalFunctionalMessage0) T6_InternationalFunctionalMessage0.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 2)) {
				T6_InternationalFunctionalMessage2 tdf = (T6_InternationalFunctionalMessage2) T6_InternationalFunctionalMessage2.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 3)) {
				T6_InternationalFunctionalMessage3 tdf = (T6_InternationalFunctionalMessage3) T6_InternationalFunctionalMessage3.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 4)) {
				T6_InternationalFunctionalMessage4 tdf = (T6_InternationalFunctionalMessage4) T6_InternationalFunctionalMessage4.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 12)) {
				T6_DangerousCargo tdf = (T6_DangerousCargo) T6_DangerousCargo.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			}
			break;
		case 7:
			sentence.setAISMessage(T7_BinaryAcknowledgement.fromAISMessage(sentence.getBitString()));
			break;
		case 8:
			sentence.setAISMessage(T8_BinaryBroadCastMessage.fromAISMessage(sentence.getBitString()));

			T8_BinaryBroadCastMessage bbcm = (T8_BinaryBroadCastMessage) sentence.getAISMessage();
			if ((bbcm.getDac() == 1) && (bbcm.getFid() == 11)) {
				T8_MeteorologicalHydrologicalData tdf = (T8_MeteorologicalHydrologicalData) T8_MeteorologicalHydrologicalData.fromAISMessage(bbcm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			}
			break;
		case 9:
			sentence.setAISMessage(T9_StandardSARPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 10:
			sentence.setAISMessage(T10_UTCDateInquiry.fromAISMessage(sentence.getBitString()));
			break;
		case 11:
			sentence.setAISMessage(T11_UTCDateResponse.fromAISMessage(sentence.getBitString()));
			break;
		case 12:
			sentence.setAISMessage(T12_AddressedSafetyRelatedMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 13:
			sentence.setAISMessage(T13_SatefyRelatedAcknowledgement.fromAISMessage(sentence.getBitString()));
			break;
		case 14:
			sentence.setAISMessage(T14_SafetyRelatedBroadcastMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 15:
			sentence.setAISMessage(T15_Interrogation.fromAISMessage(sentence.getBitString()));
			break;
		case 16:
			sentence.setAISMessage(T16_AssignedModeCommand.fromAISMessage(sentence.getBitString()));
			break;
		case 17:
			sentence.setAISMessage(T17_DGNSSBroadcastBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 18:
			sentence.setAISMessage(T18_StandardClassBEquipmentPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 19:
			sentence.setAISMessage(T19_ExtendedClassBEquipmentPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 20:
			sentence.setAISMessage(T20_DataLinkManagementMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 21:
			sentence.setAISMessage(T21_AidToNavigationReport.fromAISMessage(sentence.getBitString()));
			break;
		case 22:
			sentence.setAISMessage(T22_ChannelManagement.fromAISMessage(sentence.getBitString()));
			break;
		case 23:
			sentence.setAISMessage(T23_GroupAssignmentCommand.fromAISMessage(sentence.getBitString()));
			break;
		case 24:
			sentence.setAISMessage(T24_StaticDataReport.fromAISMessage(sentence.getBitString()));

			T24_StaticDataReport sdr = (T24_StaticDataReport) sentence.getAISMessage();
			if (sdr.getPartNumber() == 0) {
				T24_StaticDataReportPartA sdrPartA = (T24_StaticDataReportPartA) T24_StaticDataReportPartA.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartA);
			} else if (sdr.getPartNumber() == 1) {
				T24_StaticDataReportPartB sdrPartB = (T24_StaticDataReportPartB) T24_StaticDataReportPartB.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartB);
			}
			break;
		case 25:
			sentence.setAISMessage(T25_SingleSlotBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 26:
			sentence.setAISMessage(T26_MultipleSlotBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 27:
			sentence.setAISMessage(T27_PositionReportLongRangeApplications.fromAISMessage(sentence.getBitString()));
			break;
		default:
			throw new Exception("invalid message number");
		}

		return sentence;
	}

	public void parseMessage(String message) throws Exception  {
		boolean exception = false;
		
		for (AISPayloadBlock block : getMessageBlocks()) {
			try {
				if ((block.getEnd() == -1) || (block.getEnd() > message.length())) {
					block.setBits(message.substring(block.getStart(), message.length()));
				} else if (block.getEnd() < -1) {
					block.setEnd(message.length() + block.getEnd());
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				} else if (block.getStart() < 0) {
					block.setStart(message.length() + block.getStart());
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				} else {
					block.setBits(message.substring(block.getStart(), block.getEnd() + 1));
				}
			} catch (IndexOutOfBoundsException iobe) {
				exception = true;
			} catch (NumberFormatException nfe) {
				exception = true;
			}
			
			block.setException(exception);
			parseMessageBlock(block);
		}
	}
	
	public abstract void parseMessageBlock(AISPayloadBlock block) throws Exception ;
	
	public ArrayList<AISPayloadBlock> getMessageBlocks() {
		return messageBlocks;
	}

	public void setMessageBlocks(ArrayList<AISPayloadBlock> messageBlocks) {
		this.messageBlocks = messageBlocks;
	}

	public String getExceptions() {
		return this.exceptions;
	}

	public void setExceptions(String error) {
		this.exceptions += (this.exceptions.isEmpty() ? "" : ", ") + error;
	}

	private ArrayList<AISPayloadBlock> messageBlocks = new ArrayList<>();
	private String exceptions = "";

}
