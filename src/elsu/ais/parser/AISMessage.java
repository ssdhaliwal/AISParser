package elsu.ais.parser;

import java.util.ArrayList;

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
