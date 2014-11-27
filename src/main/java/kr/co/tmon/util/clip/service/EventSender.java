package kr.co.tmon.util.clip.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.tmon.util.clip.vo.ClientInfo;

public class EventSender {

	private static final int TIME_LIMIT_MILLIS = 5000;
	private static final String CODE_HEARTBEAT = "0";
	private static final String CODE_CLIP = "1";
	private static final String PARAM_IP = "&ip=";
	private static final String PARAM_PORT = "&port=";
	private static final String PARAM_ID = "&id=";

	private static final String RELAY_SERVER_URL = "http://clip.raspilla16.d2.tmon.co.kr/Send.php?code=";

	private EventSender() {
	}

	public static int sendClipEvent(ClientInfo clientInfo) throws Exception {
		URL url = new URL(RELAY_SERVER_URL + CODE_CLIP + getClientParams(clientInfo));

		return getResponseCode(url);
	}

	public static int sendHeartBeat(ClientInfo clientInfo) throws Exception {
		URL url = new URL(RELAY_SERVER_URL + CODE_HEARTBEAT + getClientParams(clientInfo));

		return getResponseCode(url);
	}

	private static String getClientParams(ClientInfo clientInfo) {
		return PARAM_IP + clientInfo.getIp() + PARAM_PORT + clientInfo.getPort() + PARAM_ID + clientInfo.getId();
	}

	private static int getResponseCode(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(TIME_LIMIT_MILLIS);
		int responseCode = connection.getResponseCode();
		connection.disconnect();

		return responseCode;
	}
}
