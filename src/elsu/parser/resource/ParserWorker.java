package elsu.parser.resource;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import elsu.common.CollectionUtils;
import elsu.sentence.SentenceFactory;

public class ParserWorker implements Runnable {
	public ParserWorker(String name, LinkedBlockingQueue<ArrayList<String>> messageQueue) {
		setName(name);
		this.messageQueue = messageQueue;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public SentenceFactory getSentenceFactory() {
		return sentenceFactory;
	}
	
	@Override
	public void run() {
		ArrayList<String> messages = null;
		while (!Thread.currentThread().isInterrupted() && !isShutdown)
		{
			try {
				messages = this.messageQueue.take();
				
				if (messages != null) {
					getSentenceFactory().parseSentence(messages);
				}
				
				Thread.yield();
			} catch (Exception ex) {
				getSentenceFactory().notifyError(null, null, "error processing message, " + this.name + ", [" + CollectionUtils.ArrayListToString(messages) + "], " + ex.getMessage());
			}
		}
	}

	private String name = "";
	public boolean isShutdown = false;
	private LinkedBlockingQueue<ArrayList<String>> messageQueue = null;
	private SentenceFactory sentenceFactory = new SentenceFactory();
}
