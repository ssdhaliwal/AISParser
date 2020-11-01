package elsu.parser.test.sender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import elsu.common.FileUtils;
import elsu.support.ConfigLoader;

public class SenderServer extends Thread {
	public SenderServer(ConfigLoader config) throws Exception {
		initialize(config);
	}

	private void initialize(ConfigLoader config) throws Exception {
		try {
			String sPort = config.getProperty("application.services.key.sender.port").toString();
			this.port = Integer.valueOf(sPort);
		} catch (Exception exi) {
			this.port = 8080;
		}

		this.filename = config.getProperty("application.services.key.sender.file").toString();
		fileData = FileUtils.readFileToList(this.filename);

		try {
			this.interval = Integer.parseInt(config.getProperty("application.services.key.sender.interval").toString());
		} catch (Exception exi) {
			this.interval = 100;
		}
	}

	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(this.port);
			while (!this.isStopped) {
				Socket client = null;
				try {
					client = this.socket.accept();
				} catch (IOException e) {
					if (this.isStopped) {
						System.out.println("Server Stopped.");
						break;
					}
					throw new RuntimeException("Error accepting client connection", e);
				}
				new Thread(new SenderWorker(client, this.fileData, this.interval)).start();
			}

			System.out.println("Server Stopped.");
		} catch (Exception ex) {
			System.out.println("socket open error, " + ex.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigLoader config = null;
		try {
			config = new ConfigLoader("config/app.config", null);
		} catch (Exception ex) {
			throw new Exception("elsu.parser, " + "main(), " + "config resource not found: config/app.config");
		}

		System.out.println("elsu.parser.test.sender, " + "main(), start, " + (new Date()));

		try {
			SenderServer sender = new SenderServer(config);
			sender.start();
			sender.join();

			sender.socket.close();
			System.out.println("elsu.parser.test.sender, " + "main(), complete, " + (new Date()));
		} catch (Exception ex) {
			System.out.println("elsu.parser.test.sender, " + "main(), unknown, " + ex.getMessage());
		}
	}

	private int interval = 100;
	private ArrayList<String> fileData = null;
	private String filename = "";
	private int port = 8080;
	private ServerSocket socket = null;
	private boolean isStopped = false;
}
