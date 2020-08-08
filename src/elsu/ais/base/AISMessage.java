package elsu.ais.base;

import java.util.ArrayList;

import elsu.ais.message.data.*;
import elsu.ais.messages.*;
import elsu.ais.resources.PayloadBlock;
import elsu.sentence.Sentence;
import elsu.sentence.SentenceBase;

public abstract class AISMessage extends SentenceBase {

	public static Sentence fromSentence(Sentence sentence) throws Exception {
		// call appropriate message class
		switch (sentence.getType()) {
		case 1:
		case 2:
		case 3:
			sentence.setAISMessage(PositionReportClassA.fromAISMessage(sentence.getBitString()));
			break;
		case 4:
		case 11:
			sentence.setAISMessage(BaseStationReport.fromAISMessage(sentence.getBitString()));
			break;
		case 5:
			sentence.setAISMessage(StaticAndVoyageRelatedData.fromAISMessage(sentence.getBitString()));
			break;
		case 6:
			sentence.setAISMessage(AddressedBinaryMessage.fromAISMessage(sentence.getBitString()));

			AddressedBinaryMessage abm = (AddressedBinaryMessage) sentence.getAISMessage();
			if ((abm.getDac() == 1) && (abm.getFid() == 0)) {
				Type6_InternationalFunctionalMessage0 tdf = (Type6_InternationalFunctionalMessage0) Type6_InternationalFunctionalMessage0.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 2)) {
				Type6_InternationalFunctionalMessage2 tdf = (Type6_InternationalFunctionalMessage2) Type6_InternationalFunctionalMessage2.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 3)) {
				Type6_InternationalFunctionalMessage3 tdf = (Type6_InternationalFunctionalMessage3) Type6_InternationalFunctionalMessage3.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 4)) {
				Type6_InternationalFunctionalMessage4 tdf = (Type6_InternationalFunctionalMessage4) Type6_InternationalFunctionalMessage4.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			} else if ((abm.getDac() == 1) && (abm.getFid() == 12)) {
				Type6_DangerousCargo tdf = (Type6_DangerousCargo) Type6_DangerousCargo.fromAISMessage(abm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			}
			break;
		case 7:
		case 13:
			sentence.setAISMessage(BinaryAcknowledge.fromAISMessage(sentence.getBitString()));
			break;
		case 8:
			sentence.setAISMessage(BinaryBroadCastMessage.fromAISMessage(sentence.getBitString()));

			BinaryBroadCastMessage bbcm = (BinaryBroadCastMessage) sentence.getAISMessage();
			if ((bbcm.getDac() == 1) && (bbcm.getFid() == 11)) {
				Type8_MeteorologicalHydrologicalData tdf = (Type8_MeteorologicalHydrologicalData) Type8_MeteorologicalHydrologicalData.fromAISMessage(bbcm,
						sentence.getBitString());
				sentence.setAISMessage(tdf);
			}
			break;
		case 9:
			sentence.setAISMessage(StandardSARPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 10:
			sentence.setAISMessage(CoordinatedUTCInquiry.fromAISMessage(sentence.getBitString()));
			break;
		case 12:
			sentence.setAISMessage(AddressedSafetyRelatedMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 14:
			sentence.setAISMessage(SafetyRelatedMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 15:
			sentence.setAISMessage(Interrogation.fromAISMessage(sentence.getBitString()));
			break;
		case 16:
			sentence.setAISMessage(AssignedModeCommand.fromAISMessage(sentence.getBitString()));
			break;
		case 17:
			sentence.setAISMessage(GNSSBroadcastBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 18:
			sentence.setAISMessage(StandardClassBCSPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 19:
			sentence.setAISMessage(ExtendedClassBCSPositionReport.fromAISMessage(sentence.getBitString()));
			break;
		case 20:
			sentence.setAISMessage(DataLinkManagementMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 21:
			sentence.setAISMessage(AidToNavigationReport.fromAISMessage(sentence.getBitString()));
			break;
		case 22:
			sentence.setAISMessage(ChannelManagement.fromAISMessage(sentence.getBitString()));
			break;
		case 23:
			sentence.setAISMessage(GroupAssignmentControl.fromAISMessage(sentence.getBitString()));
			break;
		case 24:
			sentence.setAISMessage(StaticDataReport.fromAISMessage(sentence.getBitString()));

			StaticDataReport sdr = (StaticDataReport) sentence.getAISMessage();
			if (sdr.getPartNumber() == 0) {
				StaticDataReportPartA sdrPartA = (StaticDataReportPartA) StaticDataReportPartA.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartA);
			} else if (sdr.getPartNumber() == 1) {
				StaticDataReportPartB sdrPartB = (StaticDataReportPartB) StaticDataReportPartB.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartB);
			}
			break;
		case 25:
			sentence.setAISMessage(SingleSlotBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 26:
			sentence.setAISMessage(MultipleSlotBinaryMessage.fromAISMessage(sentence.getBitString()));
			break;
		case 27:
			sentence.setAISMessage(LongRangeAISBroadcastMessage.fromAISMessage(sentence.getBitString()));
			break;
		default:
			throw new Exception("invalid message number");
		}

		return sentence;
	}

	public void parseMessage(String message) throws Exception  {
		boolean exception = false;
		
		for (PayloadBlock block : getMessageBlocks()) {
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
	
	public abstract void parseMessageBlock(PayloadBlock block) throws Exception ;
	
	public ArrayList<PayloadBlock> getMessageBlocks() {
		return messageBlocks;
	}

	public void setMessageBlocks(ArrayList<PayloadBlock> messageBlocks) {
		this.messageBlocks = messageBlocks;
	}

	public String getExceptions() {
		return this.exceptions;
	}

	public void setExceptions(String error) {
		this.exceptions += (this.exceptions.isEmpty() ? "" : ", ") + error;
	}

	private ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();
	private String exceptions = "";

}
