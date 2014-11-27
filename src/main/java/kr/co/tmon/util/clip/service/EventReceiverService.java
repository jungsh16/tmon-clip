package kr.co.tmon.util.clip.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import kr.co.tmon.util.clip.listener.RemoteClipListener;

public class EventReceiverService extends Thread {

	private int clientPort;
	private RemoteClipListener remoteClipListener;

	public EventReceiverService(int clientPort) {
		this.clientPort = clientPort;
	}

	public void addListener(RemoteClipListener remoteClipListener) {
		this.remoteClipListener = remoteClipListener;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(clientPort);

			while (true) {
				serverSocket.accept().close();
				remoteClipListener.remoteClipChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket(serverSocket);
		}
	}

	private void closeSocket(ServerSocket serverSocket) {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
}
