package chat;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	public static int PORT = 8800;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Socket socket = new Socket();

		// 소켓 생성 -> 예외 처리
		InetSocketAddress inetSocketAddress;

		try {

			inetSocketAddress = new InetSocketAddress("127.0.0.1", PORT);
			socket.connect(inetSocketAddress);

			// client가 작성하는 것이라서 writer만 필요 내가 받는 것이 socket.getOutputStream()
			PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			System.out.print("닉네임>>");
			String nickName = scanner.nextLine();

			printwriter.println(nickName);
			printwriter.flush();

			new ChatClientThread(socket).start();

			String message;

			while (true) {
				message = scanner.nextLine();

				printwriter.println(message);
				printwriter.flush();

				if ("quit".equals(message)) {
					break;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}