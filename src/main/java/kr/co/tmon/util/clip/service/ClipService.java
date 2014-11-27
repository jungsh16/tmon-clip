package kr.co.tmon.util.clip.service;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import kr.co.tmon.util.clip.listener.RemoteClipListener;
import kr.co.tmon.util.clip.vo.ClientInfo;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ClipService implements FlavorListener, RemoteClipListener {

	private static final int TIME_OUT_MILLIS = 5000;
	private static final String CHANNEL_TYPE = "sftp";
	private static final String ENCODING = "UTF-8";
	private static final String IMAGE_FORMAT_PNG = "png";
	private static final String TEMP_REMOTE_FILE_NAME = "data.clip";

	private ClientInfo clientInfo;
	private String dataServerIp = "10.10.111.21";
	private int dataServerPort = 22;
	private volatile boolean isRecursive;

	private Session session;
	private ChannelSftp channelSftp;
	private Clipboard clipboard;

	public ClipService(ClientInfo clientInfo) throws Exception {
		this.clientInfo = clientInfo;

		initSession();

		initChannelSftp();

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.addFlavorListener(this);
	}

	private void initChannelSftp() throws JSchException {
		channelSftp = (ChannelSftp) session.openChannel(CHANNEL_TYPE);
		channelSftp.connect(TIME_OUT_MILLIS);
	}

	private void initSession() throws JSchException {
		session = new JSch().getSession(dataServerIp);
		session.setUserInfo(clientInfo);
		session.setPort(dataServerPort);
		session.connect(TIME_OUT_MILLIS);
	}

	public void close() {
		channelSftp.disconnect();
		session.disconnect();
	}

	@Override
	public void flavorsChanged(FlavorEvent event) {
		if (isRecursive) {
			isRecursive = false;
			return;
		}

		try {
			DataFlavor dataFlavor = getDataFlavor();

			if (dataFlavor == null)
				return;

			sendData(dataFlavor);

			EventSender.sendClipEvent(clientInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DataFlavor getDataFlavor() throws Exception {
		DataFlavor dataFlavor = null;

		if (isImage())
			dataFlavor = DataFlavor.imageFlavor;
		else if (isString())
			dataFlavor = DataFlavor.stringFlavor;
		else
			// TODO 예상치 못한 Local Data Type 로그 남기기
			printClipLog();

		return dataFlavor;
	}

	private boolean isString() {
		return clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
	}

	private boolean isImage() {
		return clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor);
	}

	private void sendData(DataFlavor dataFlavor) throws Exception {
		Object data = clipboard.getData(dataFlavor);

		OutputStream channelOutputStream = channelSftp.put(TEMP_REMOTE_FILE_NAME, ChannelSftp.OVERWRITE);

		if (data instanceof RenderedImage)
			ImageIO.write((RenderedImage) data, IMAGE_FORMAT_PNG, channelOutputStream);
		else if (data instanceof String)
			IOUtils.write((String) data, channelOutputStream, ENCODING);

		channelOutputStream.close();
	}

	@Override
	public void remoteClipChanged() {
		try {
			String mediaType = getMediaType();
			System.out.println("RemoteMediaType // " + mediaType);

			Transferable contents = getContents(mediaType);

			isRecursive = true;

			clipboard.setContents(contents, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Transferable getContents(String mediaType) throws Exception {
		Transferable contents = null;
		InputStream channelInputStream = channelSftp.get(TEMP_REMOTE_FILE_NAME);

		if (mediaType.startsWith("image")) {
			BufferedImage image = ImageIO.read(channelInputStream);
			contents = new ImageTransferable(image);
			System.out.println("RemoteImage // " + image);
		} else if (mediaType.startsWith("text")) {
			String text = IOUtils.toString(channelInputStream, "UTF-8");
			contents = new StringTransferable(text);
			System.out.println("RemoteText // " + text);
		} else {
			// TODO 예상치 못한 Remote Data Type 로그 남기기
			printClipLog();
		}

		channelInputStream.close();

		return contents;
	}

	private String getMediaType() throws Exception {
		InputStream channelInputStream = channelSftp.get(TEMP_REMOTE_FILE_NAME);

		Tika tika = new Tika();
		String mediaType = tika.detect(channelInputStream);

		channelInputStream.close();

		return mediaType;
	}

	private static class ImageTransferable implements Transferable {
		private Image image;

		public ImageTransferable(BufferedImage image) {
			this.image = image;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.imageFlavor;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (this.isDataFlavorSupported(flavor) == false)
				throw new UnsupportedFlavorException(flavor);

			return this.image;
		}
	}

	private static class StringTransferable implements Transferable {

		private final String text;

		public StringTransferable(String text) {
			this.text = text;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.stringFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.stringFlavor;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor) == false)
				throw new UnsupportedFlavorException(flavor);

			return text;
		}
	}

	private void printClipLog() throws Exception {
		DataFlavor selectBestTextFlavor = DataFlavor.selectBestTextFlavor(clipboard.getAvailableDataFlavors());

		if (selectBestTextFlavor != null) {
			System.out.println("Text-----------------------------------");
			System.out.println(selectBestTextFlavor);
			System.out.println(clipboard.getData(selectBestTextFlavor).getClass());
			System.out.println(clipboard.getData(selectBestTextFlavor));
		} else if (isImage()) {
			System.out.println("Image-----------------------------------");
			System.out.println(DataFlavor.imageFlavor);
			System.out.println(clipboard.getData(DataFlavor.imageFlavor).getClass());
			System.out.println(clipboard.getData(DataFlavor.imageFlavor));
		} else {
			Arrays.asList(clipboard.getAvailableDataFlavors()).forEach(new Consumer<DataFlavor>() {
				@Override
				public void accept(DataFlavor dataFlavor) {
					System.out.println(dataFlavor);
				}
			});
		}
	}
}
