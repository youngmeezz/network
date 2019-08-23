package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {

	Socket socket = null;
	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;
	String nickname;
	List<PrintWriter> pwList;

	
	//소켓에서 생성자 만들어서 파라미터 받아올때 socket이랑 pwList만 쓰는 이유가 뭐지? -> socket만 받아오고 아직 내용
	ChatServerThread(Socket socket, List<PrintWriter> pwList) throws UnsupportedEncodingException, IOException {
		this.socket = socket;
		this.pwList = pwList;
		this.doJoin();
	}

	
	@Override
	public void run() {
		try {
			String message;

			while (true) {
				message = bufferedReader.readLine();

				if ("quit".equals(message)) {
					break;
				}

				this.broadcast(this.nickname + ": " + message);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.doQuit(); //doQuit -> 채팅방 닫을때 / break할때 퇴장하셨습니다. 메시지 출력
		}
	}

	// 1. doJoin
	// 2. doQuit
	// 3. broadcast
	private void doJoin() throws UnsupportedEncodingException, IOException {

		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		
		this.nickname = this.bufferedReader.readLine();
		
		this.broadcast(this.nickname + "님이 채팅방에 입장하셨습니다.");
		// multi-thread로 동시접근 되는 것을 막음
		// 멀티스레드로 인하여 동기화 제어
		synchronized (this.pwList) {
			this.pwList.add(this.printWriter); // 리스트에 내용 추가 하는 방법
		}
	}

	private void doQuit() {
		synchronized (this.pwList) {
			this.pwList.remove(this.printWriter);
		}
		this.printWriter.println("quit");
		this.printWriter.flush();

		this.broadcast(this.nickname + "님이 퇴장하셨습니다.");// 클라이언트 채팅방에 2명 모두 등장한다구 하지 않았나??

		//Quit하는 순간 socket닫아야 함
		if (this.socket != null && !this.socket.isClosed()) {
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//broadcast함수에서 message에서 받아온거 pwList에 담아서 반복문으로 출력 
	private void broadcast(String message) {
		synchronized (this.pwList) {
			for (PrintWriter pw : this.pwList) {
				pw.println(message);
				pw.flush();
			}
		}
	}
}