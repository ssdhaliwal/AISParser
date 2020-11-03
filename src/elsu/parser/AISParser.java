package elsu.parser;

import java.util.*;
import java.util.concurrent.TimeUnit;

import elsu.base.IAISEventListener;
import elsu.parser.connector.ConnectorBase;
import elsu.parser.connector.StreamFileConnector;
import elsu.parser.connector.StreamSocketConnector;
import elsu.sentence.SentenceBase;
import elsu.support.*;

public class AISParser implements IAISEventListener {
	public AISParser(ConfigLoader config) throws Exception {
		initialize(config);

		for (ConnectorBase connector : this.connectors) {
			connector.addListener(this);
		}
		
		for (ConnectorBase connector : this.connectors) {
			connector.getWorkerPool().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
	}
	
	private void initialize(ConfigLoader config) throws Exception {
		try {
			String debugLevel = config.getProperty("application.services.key.processing.debug").toString();
			if (debugLevel.equals("debug")) {
				SentenceBase.logLevel = 6;
			} else if (debugLevel.equals("all")) {
				SentenceBase.logLevel = 6;
			} else if (debugLevel.equals("debug")) {
				SentenceBase.logLevel = 5;
			} else if (debugLevel.equals("info")) {
				SentenceBase.logLevel = 4;
			} else if (debugLevel.equals("warn")) {
				SentenceBase.logLevel = 3;
			} else if (debugLevel.equals("error")) {
				SentenceBase.logLevel = 2;
			} else if (debugLevel.equals("fatal")) {
				SentenceBase.logLevel = 1;
			} else if (debugLevel.equals("off")) {
				SentenceBase.logLevel = 0;
			}
		} catch (Exception exi) {
			System.out.println(getClass().getName() + ", initialize(), null, config item application.services.key.processing.debug not defined, debugLeve set to all");
		}
		
		String connectionList = config.getProperty("application.services.activeList").toString();
		String[] connections = connectionList.split(",");
		for (String connection : connections) {
			if (config.getProperty("application.services.service." + connection + ".attributes.key.type").toString().equals("file")) {
				connector = new StreamFileConnector(config, connection);
			} else {
				connector = new StreamSocketConnector(config, connection);
			}
			
			connector.start();
			connectors.add(connector);
		}
	}

	@Override
	public void onAISError(Exception ex, Object o, String message) {
		try {
			if (SentenceBase.logLevel >= 2) {
				System.out.println(getClass().toString() + ", " + "onAISError(), " + ex.getMessage() + ", " + o + ", " + message);
			}
		} catch (Exception exi) {
			System.out.println(getClass().toString() + ", " + "onAISError(), " + "message error notification exception, " + exi.getMessage());
		}
	}

	@Override
	public void onAISComplete(Object o) {
		try {
			if (SentenceBase.logLevel >= 4) {
				System.out.println(getClass().toString() + ", " + "onAISComplete(), " + "complete, " + o);
			}
		} catch (Exception exi) {
			System.out.println(getClass().toString() + ", " + "onAISComplete(), " + "message complete notification exception, " + exi.getMessage());
		}
	}

	@Override
	public void onAISUpdate(Object o) {
		try {
			if (SentenceBase.logLevel >= 4) {
				System.out.println(getClass().toString() + ", " + "onAISComplete(), " + "update, " + o);
			}
		} catch (Exception exi) {
			System.out.println(getClass().toString() + ", " + "onAISError(), " + "message error notification exception, " + exi.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigLoader config = null;
		try {
			config = new ConfigLoader("config/app.config", null);
		} catch (Exception ex) {
			throw new Exception("elsu.parser, " + "main(), " + "config resource not found: config/app.config");
		}

		System.out.println("elsu.parser, " + "main(), start, " + (new Date()));

		try {
			AISParser parser = new AISParser(config);
			System.out.println("elsu.parser, " + "main(), complete, " + (new Date()));
		} catch (Exception ex) {
			System.out.println("elsu.parser, " + "main(), unknown, " + ex.getMessage());
		}
	}

	ConnectorBase connector = null;
	private ArrayList<ConnectorBase> connectors = new ArrayList<>();
}
