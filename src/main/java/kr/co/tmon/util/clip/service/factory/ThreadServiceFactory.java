package kr.co.tmon.util.clip.service.factory;

import kr.co.tmon.util.clip.service.EventReceiverService;
import kr.co.tmon.util.clip.service.HeartBeatservice;
import kr.co.tmon.util.clip.vo.ClientInfo;

public class ThreadServiceFactory {

	private ClientInfo clientInfo;

	public ThreadServiceFactory(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	public HeartBeatservice getHeartBeatService() {
		return new HeartBeatservice(clientInfo);
	}

	public EventReceiverService getEventReceiverService() {
		return new EventReceiverService(clientInfo.getPort());
	}
}
