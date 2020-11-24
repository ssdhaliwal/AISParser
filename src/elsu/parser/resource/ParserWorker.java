package elsu.parser.resource;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import elsu.common.CollectionUtils;
import elsu.sentence.SentenceFactory;

public class ParserWorker implements Runnable {
	public ParserWorker(String name, ConcurrentLinkedQueue<ArrayList<String>> messageQueue) {
		setName(name);
		this.messageQueue = messageQueue;
		sentenceFactory = new SentenceFactory();
	}

	public ParserWorker(String name, ConcurrentLinkedQueue<ArrayList<String>> messageQueue,
			String messagesToProcess) {
		setName(name);
		this.messageQueue = messageQueue;
		
		sentenceFactory = new SentenceFactory(messagesToProcess);
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
				messages = this.messageQueue.poll();
				
				if (messages != null) {
					getSentenceFactory().parseSentence(messages);
				} else {
					Thread.sleep(1);
				}
				
				Thread.yield();
				messages = null;
			} catch (Exception ex) {
				getSentenceFactory().notifyError(null, null, "error processing message, " + this.name + ", [" + CollectionUtils.ArrayListToString(messages) + "], " + ex.getMessage());
			}
		}
	}

	private String name = "";
	public boolean isShutdown = false;
	private ConcurrentLinkedQueue<ArrayList<String>> messageQueue = null;
	private SentenceFactory sentenceFactory = null;
}
