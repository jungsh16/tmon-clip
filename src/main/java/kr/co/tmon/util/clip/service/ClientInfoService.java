package kr.co.tmon.util.clip.service;

import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import kr.co.tmon.util.clip.vo.ClientInfo;

public class ClientInfoService {

	private static final int PORT_CLIENT = 8501;
	private static final String DIALOG_TITLE = "ID & Password";
	private static final String FIELD_NAME_PASSWORD = "Password";
	private static final String FIELD_NAEM_ID = "ID";

	public ClientInfo getClientInfo() throws Exception {
		ClientInfo clientInfo = new ClientInfo();

		JTextField idField = new JTextField();
		JTextField passwordField = (JTextField) new JPasswordField();

		showDialog(idField, passwordField);

		validateFields(idField, passwordField);

		clientInfo.setId(idField.getText());
		clientInfo.setPassword(passwordField.getText());
		clientInfo.setIp(InetAddress.getLocalHost().getHostAddress());
		clientInfo.setPort(PORT_CLIENT);

		return clientInfo;
	}

	private void validateFields(JTextField idField, JTextField passwordField) {
		if (StringUtils.isEmpty(idField.getText()) || StringUtils.isEmpty(passwordField.getText()))
			System.exit(0);
	}

	private void showDialog(JTextField idField, JTextField passwordField) {
		Object[] test = { FIELD_NAEM_ID, idField, FIELD_NAME_PASSWORD, passwordField };
		int result = JOptionPane.showConfirmDialog(null, test, DIALOG_TITLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.CANCEL_OPTION)
			System.exit(0);
	}
}
