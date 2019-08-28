package udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Scanner;

public class UDPTimeClient {
	private static final String SERVER_IP = "127.0.0.1";
	
	public static void main(String[] args) {
		Scanner scanner = null;
		DatagramSocket socket = null;
		
		try {
			//1. 키보드 연결
			scanner = new Scanner(System.in);
			
			//2. 소켓 생성
			socket = new DatagramSocket();

			while(true) {
				//3. 사용자 입력을 받음
				System.out.print(">>");
				String message = scanner.nextLine();
				
				if("quit".equals(message)) {
					break;
				}
				
				
					//4. 메세지 전송
					byte[] sendData = message.getBytes("UTF-8"); 
					DatagramPacket sendPacket = new DatagramPacket(
						sendData,
						sendData.length, 
						new InetSocketAddress(SERVER_IP, UDPTimeServer.PORT));
					socket.send(sendPacket);
					
					if("".equals(message)) {
						
					//5. 메세지 수신
					DatagramPacket receivePacket = new DatagramPacket(new byte[UDPTimeServer.BUFFER_SIZE], UDPTimeServer.BUFFER_SIZE);
					socket.receive(receivePacket); //blocking
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
					message = format.format(new Date());
					System.out.println("<<" + message);
				}
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
			if(socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
}