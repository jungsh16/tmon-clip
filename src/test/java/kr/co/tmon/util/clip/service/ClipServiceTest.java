package kr.co.tmon.util.clip.service;

import java.net.InetAddress;

import kr.co.tmon.util.clip.vo.ClientInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ClipServiceTest {

	private ClientInfo clientInfo;
	private ClipService clipService;

	@Before
	public void init() throws Exception {
		clientInfo = new ClientInfo();
		clientInfo.setId("");
		clientInfo.setPassword("");
		clientInfo.setIp(InetAddress.getLocalHost().getHostAddress());
		clientInfo.setPort(8501);
		clipService = new ClipService(clientInfo);
	}

	@After
	public void close() {
		clipService.close();
	}

	@Ignore
	@Test
	public void testClipChanged() throws Exception {
		clipService.flavorsChanged(null);
		clipService.remoteClipChanged();
	}
}
