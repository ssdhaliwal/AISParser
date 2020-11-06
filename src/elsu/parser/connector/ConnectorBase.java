package elsu.parser.connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import elsu.base.IAISEventListener;
import elsu.parser.resource.ParserWorker;
import elsu.sentence.SentenceFactory;

public abstract class ConnectorBase extends Thread {

	public ConnectorBase() {
	}

	protected void initializeThreadPool(int max_threads) {
		setMaxThreads(max_threads);
		workers = new ParserWorker[getMaxThreads()];

		// create thread pool for # of parsers
		workerPool = Executors.newFixedThreadPool(getMaxThreads());

		// create workers
		for (int i = 0; i < getMaxThreads(); i++) {
			getWorkers()[i] = new ParserWorker("aisworker_" + i, getMessageQueue());
			workerPool.execute(getWorkers()[i]);
		}

		workerPool.shutdown();
	}

	public ExecutorService getWorkerPool() {
		return workerPool;
	}

	public ParserWorker[] getWorkers() {
		return workers;
	}

	public LinkedBlockingQueue<ArrayList<String>> getMessageQueue() {
		return messageQueue;
	}

	public void addListener(IAISEventListener listener) {
		for (int i = 0; i < getMaxThreads(); i++) {
			(getWorkers()[i]).getSentenceFactory().addEventListener(listener);
		}
	}

	public void removeListener(IAISEventListener listener) {
		for (int i = 0; i < getMaxThreads(); i++) {
			(getWorkers()[i]).getSentenceFactory().removeEventListener(listener);
		}
	}

	public void sendError(String error) throws Exception {
		sentenceFactory.notifyError(null, null, error);
	}

	public void sendMessage(ArrayList<String> messages) throws Exception {
		messageQueue.add(messages);
	}

	public void sendMessage(String message) throws Exception {
		try {
			sentenceFactory.parseSentence(message);
		} catch (Exception ex) {
			sendError("error processing message, " + message + ", " + ex.getMessage());
		}
	}

	public void sendTermination() {
		// terminate workers
		for (int i = 0; i < getMaxThreads(); i++) {
			(getWorkers()[i]).isShutdown = true;
		}
	}

	public int getMaxThreads() {
		return max_threads;
	}

	public void setMaxThreads(int max_threads) {
		this.max_threads = max_threads;
	}
	
	public String getDateTimeUTC() {
		Date date = new Date();
		return dateFormat.format(date);
	}

	private int max_threads = 1;
	private ExecutorService workerPool = null;
	private ParserWorker[] workers = null;
	private LinkedBlockingQueue<ArrayList<String>> messageQueue = new LinkedBlockingQueue<ArrayList<String>>();
	private SentenceFactory sentenceFactory = new SentenceFactory();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss z");
}
