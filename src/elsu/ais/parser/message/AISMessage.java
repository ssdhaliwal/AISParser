package elsu.ais.parser.message;

import java.util.ArrayList;

import elsu.ais.parser.AISBase;
import elsu.ais.parser.resources.PayloadBlock;

public abstract class AISMessage extends AISBase {
	
	public ArrayList<PayloadBlock> getMessageBlocks() {
		return messageBlocks;
	}

	public void setMessageBlocks(ArrayList<PayloadBlock> messageBlocks) {
		this.messageBlocks = messageBlocks;
	}

	private ArrayList<PayloadBlock> messageBlocks = new ArrayList<>();

}
