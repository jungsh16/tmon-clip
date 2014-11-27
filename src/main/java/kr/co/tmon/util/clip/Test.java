package kr.co.tmon.util.clip;

import java.net.Socket;

public class Test {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("10.10.11.22", 8501);
		System.out.println(socket.getInetAddress());
		socket.close();
	}
}
