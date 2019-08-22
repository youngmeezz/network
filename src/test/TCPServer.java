package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private static final int PORT = 6000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2. Binding:
			//   Socket에 SocketAddress(IPAddress + Port)
			//   바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);
			
			//3. accept:
			//   클라이언트로 부터 연결요청(Connect)을 기다린다.
			Socket socket = serverSocket.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = 
					(InetSocketAddress)socket.getRemoteSocketAddress();
			
			String remoteHostAdress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();
			
			System.out.println("[TCPServer] connected from client[" + 
				remoteHostAdress + ":" + 
				remoteHostPort +
			"]");
			
			try {
				//4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true) {
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); //Blocking
					if(readByteCount == -1) {
						System.out.println("[TCPServer] closed by client");
						break;
					}
				}
				
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//. 자원정리
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
