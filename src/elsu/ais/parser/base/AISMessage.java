package elsu.ais.parser.base;

import java.util.ArrayList;

import elsu.ais.parser.messages.*;
import elsu.ais.parser.message.data.*;
import elsu.ais.parser.resources.PayloadBlock;
import elsu.ais.parser.sentence.AISSentence;

public abstract class AISMessage extends AISBase {

	public static AISSentence fromSentence(AISSentence sentence) throws Exception {
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
			break;
		case 7:
		case 13:
			sentence.setAISMessage(BinaryAcknowledge.fromAISMessage(sentence.getBitString()));
			break;
		case 8:
			sentence.setAISMessage(BinaryBroadCastMessage.fromAISMessage(sentence.getBitString()));

			BinaryBroadCastMessage bbcm = (BinaryBroadCastMessage) sentence.getAISMessage();
			if ((bbcm.getDac() == 1) && (bbcm.getFid() == 11)) {
				Type8_Dac1_Fid11 tdf = (Type8_Dac1_Fid11) Type8_Dac1_Fid11.fromAISMessage(bbcm,
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
			break;
		case 23:
			break;
		case 24:
			sentence.setAISMessage(StaticDataReport.fromAISMessage(sentence.getBitString()));

			StaticDataReport sdr = (StaticDataReport) sentence.getAISMessage();
			if (sdr.getPartno() == 0) {
				StaticDataReportPartA sdrPartA = (StaticDataReportPartA) StaticDataReportPartA.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartA);
			} else if (sdr.getPartno() == 1) {
				StaticDataReportPartB sdrPartB = (StaticDataReportPartB) StaticDataReportPartB.fromAISMessage(sdr,
						sentence.getBitString());
				sentence.setAISMessage(sdrPartB);
			}
			break;
		case 25:
			break;
		case 26:
			break;
		case 27:
			break;
		default:
			throw new Exception("invalid message number");
		}

		return sentence;
	}

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
