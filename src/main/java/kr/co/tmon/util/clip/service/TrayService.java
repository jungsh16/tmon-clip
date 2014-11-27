package kr.co.tmon.util.clip.service;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

public class TrayService {

	private static final String IMAGES_IC_SHARE_PNG = "/images/ic_share.png";

	private ClipService clipService;

	public TrayService(ClipService clipService) {
		this.clipService = clipService;
	}

	public TrayIcon getTrayIcon() {
		Image mainIconImage = new ImageIcon(TrayService.class.getResource(IMAGES_IC_SHARE_PNG)).getImage();

		String tooltip = "Tmon Clip";

		PopupMenu menu = new PopupMenu();
		MenuItem exitMenu = getExitMenu();
		menu.add(exitMenu);

		TrayIcon trayIcon = new TrayIcon(mainIconImage, tooltip, menu);
		trayIcon.setImageAutoSize(true);

		return trayIcon;
	}

	private MenuItem getExitMenu() {
		MenuItem exitMenu = new MenuItem("exit");
		exitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clipService.close();
				System.exit(0);
			}
		});

		return exitMenu;
	}

}
