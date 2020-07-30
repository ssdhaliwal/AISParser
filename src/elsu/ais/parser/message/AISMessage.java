package elsu.ais.parser.message;

import java.util.ArrayList;

import elsu.ais.parser.AISBase;
import elsu.ais.parser.resources.PayloadBlock;
import elsu.ais.parser.sentence.AISSentence;

public abstract class AISMessage extends AISBase {
	
	public static AISSentence fromSentence(AISSentence sentence) {
		// call appropriate message class
		switch (sentence.getMessageNumber()) {
		case 1:
			sentence.setAISMessage(PositionReportClassA.fromAISMessage(sentence.getBitString()));
			break;
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
