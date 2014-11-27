package kr.co.tmon.util.clip.service;

import kr.co.tmon.util.clip.vo.ClientInfo;

public class HeartBeatservice extends Thread {

	private static final long HEARBEAT_INTERVAL = 10 * 1000;

	private ClientInfo clientInfo;

	public HeartBeatservice(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	@Override
	public void run() {
		while (true) {
			try {
				EventSender.sendHeartBeat(clientInfo);
				Thread.sleep(HEARBEAT_INTERVAL);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
