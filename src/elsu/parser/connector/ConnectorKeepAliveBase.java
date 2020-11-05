package elsu.parser.connector;

import elsu.support.ConfigLoader;

public abstract class ConnectorKeepAliveBase extends ConnectorBase {
	public ConnectorKeepAliveBase(ConfigLoader config, String connName) throws Exception {
		super();
		initialize(config, connName);
	}

	private void initialize(ConfigLoader config, String connName) throws Exception {
		noDataTimeout = Integer.parseInt(
				config.getProperty("application.services.service." + connName + ".attributes.key.monitor.noDataTimeout")
						.toString());
		retryWaitTime = Integer.parseInt(
				config.getProperty("application.services.service." + connName + ".attributes.key.monitor.idleTimeout")
						.toString());
	}

	protected abstract void resetConnection();

	public void checkKeepAlive() {
		// if thread monitor is not running, try to start it
		if (!isMonitorRunning && !isShutdown) {
			monitorRecordCounter = 0L;

			// start no data monitor thread
			Thread tMonitor = new Thread(new Runnable() {
				@Override
				public void run() {
					isMonitorRunning = true;

					System.out.println("client monitor started...");
					while (isRunning && isMonitorRunning && !isShutdown) {
						try {
							// yield processing to other threads
							// for specified
							// time, any exceptions are ignored
							try {
								monitorRecordCounter = 0L;
								Thread.sleep(noDataTimeout);
							} catch (Exception exi) {
							}

							// check the data count
							if (monitorRecordCounter == 0L) {
								resetConnection();
							}
						} catch (Exception exi) {
							isMonitorRunning = false;
							try {
								sendError("client monitor error, " + exi.getMessage());
							} catch (Exception ex) {
								System.out.println(
										getClass().toString() + ", run(), " + "network monitor-2, " + ex.getMessage());
							}
						}
					}

					System.out.println("client monitor stopped...");
					isMonitorRunning = false;
				}
			});

			// start the thread to create connection for the service.
			tMonitor.start();
		}
	}

	boolean isMonitorRunning = false;
	protected long lifetimeCounter = 0L;
	protected long monitorRecordCounter = 0L;

	public boolean isShutdown = false;
	protected boolean isRunning = false;

	protected int noDataTimeout = 30000;
	protected int retryWaitTime = 5000;
}
