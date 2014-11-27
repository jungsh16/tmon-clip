package kr.co.tmon.util.clip.service;

import static org.junit.Assert.assertEquals;
import kr.co.tmon.util.clip.vo.ClientInfo;

import org.junit.Test;

public class EventSenderTest {

	@Test
	public void testSendClipEvent() throws Exception {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setId("raspilla16");
		int responseCode = EventSender.sendClipEvent(clientInfo);

		assertEquals(200, responseCode);
	}

	@Test
	public void testSendHeartBeat() throws Exception {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setId("raspilla16");

		int responseCode = EventSender.sendHeartBeat(clientInfo);

		assertEquals(200, responseCode);
	}
}
