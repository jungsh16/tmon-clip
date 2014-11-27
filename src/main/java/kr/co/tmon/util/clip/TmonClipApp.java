package kr.co.tmon.util.clip;

import java.awt.SystemTray;
import java.awt.TrayIcon;

import kr.co.tmon.util.clip.service.ClientInfoService;
import kr.co.tmon.util.clip.service.ClipService;
import kr.co.tmon.util.clip.service.EventReceiverService;
import kr.co.tmon.util.clip.service.HeartBeatservice;
import kr.co.tmon.util.clip.service.TrayService;
import kr.co.tmon.util.clip.service.factory.ThreadServiceFactory;
import kr.co.tmon.util.clip.vo.ClientInfo;

public class TmonClipApp {

	public static void main(String[] args) throws Exception {
		ClientInfoService clientService = new ClientInfoService();
		ClientInfo clientInfo = clientService.getClientInfo();

		ClipService clipService = new ClipService(clientInfo);

		ThreadServiceFactory threadServiceFactory = new ThreadServiceFactory(clientInfo);

		HeartBeatservice heartBeatService = threadServiceFactory.getHeartBeatService();
		heartBeatService.start();

		EventReceiverService eventReceiverService = threadServiceFactory.getEventReceiverService();
		eventReceiverService.addListener(clipService);
		eventReceiverService.start();

		TrayService trayService = new TrayService(clipService);
		TrayIcon trayIcon = trayService.getTrayIcon();
		SystemTray.getSystemTray().add(trayIcon);
	}
}
