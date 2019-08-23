package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	public static int PORT = 8800;
	public static List<PrintWriter> pwList;
	public static ServerSocket serverSocket = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pwList = new ArrayList<>();

		try {
			// 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 바인딩(IP주소 + PORT)
			InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
			serverSocket.bind(inetSocketAddress); // 운영체제에서 bind되서 IP주소와 PORT번호까지 같이 출력 포트번호쓴다고 말하는 것
			System.out.println("바인딩 IP와 포트번호:" + inetSocketAddress);

			while (true) {
				// accept해서 클라이언트에서 보낼 연결요청 랜덤으로 열린 PORT 번호를 (connect) 랜덤으로 열린 IP주소와 PORT번호를
				// 서버소켓이 받아옴
				Socket socket = serverSocket.accept(); // serverSocket이 PORT번호 받아 올때 accept할때 socket생성
				// 서버 스레드랑 연동해서 이제 스레드의 소켓이 알아서 할 것이라서 BufferedReader부터 주석 처리
				// 스레드 시작시켜서 accpet에서 socket생성후 던져주기
				new ChatServerThread(socket, pwList).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false)
					serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}