package elsu.parser.test.sender;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import elsu.common.GlobalStack;

public class SenderWorker implements Runnable {

	public SenderWorker(Socket client, ArrayList<String> fileData, int interval) {
		this.socket = client;
		this.fileData = fileData;
		this.interval = interval;
	}

	@Override
	public void run() {
		try {
			InputStream input = this.socket.getInputStream();
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));

			String line = "";
			int i = 0, fileSize = this.fileData.size();
			while (!Thread.currentThread().isInterrupted()) {
				line = fileData.get(i++);
				System.out.println(i + ", " + line);

				output.write(line + GlobalStack.LINESEPARATOR);
				
				Thread.sleep(interval);
				if (i >= fileSize) {
					break;
				}
			}

			System.out.println("file sent...");
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
			} catch (Exception exi) {
			}
		}
	}

	private int interval = 100;
	private Socket socket = null;
	private ArrayList<String> fileData = null;
}
