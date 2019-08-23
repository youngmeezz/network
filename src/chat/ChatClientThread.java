package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientThread extends Thread {

	Socket socket;
	BufferedReader br;

	public ChatClientThread(Socket socket) throws UnsupportedEncodingException, IOException {
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
	}

	@Override
	public void run() {
		String message = null;
		try {
			while (true) {
				message = br.readLine();

				if ("quit".equals(message)) {
					break;
				}

				System.out.println(message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (this.socket != null && !this.socket.isClosed()) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}