package kr.co.tmon.util.clip.service;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;

import kr.co.tmon.util.clip.vo.ClientInfo;

import org.junit.Ignore;
import org.junit.Test;

public class ClientInfoServiceTest {

	@Ignore
	@Test
	public void testGetClientInfo() throws Exception {
		ClientInfoService clientInfoService = new ClientInfoService();
		ClientInfo clientInfo = clientInfoService.getClientInfo();

		int expectedPort = 8501;
		assertEquals(expectedPort, clientInfo.getPort());

		String expectedIp = InetAddress.getLocalHost().getHostAddress();
		assertEquals(expectedIp, clientInfo.getIp());
	}
}
