package org.zero01.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {

	/**
	 * �����ַ��������� SHA-256 �����ַ���
	 * 
	 * @param strText
	 * @return
	 */
	public String getSHA256(final String strText) {
		return SHA(strText, "SHA-256");
	}

	/**
	 * �����ַ��������� SHA-512 �����ַ���
	 * 
	 * @param strText
	 * @return
	 */
	public String getSHA512(final String strText) {
		return SHA(strText, "SHA-512");
	}

	/**
	 * �����ַ��������� MD5 �����ַ���
	 * 
	 * @param strText
	 * @return
	 */
	public String getMD5(final String strText) {
		return SHA(strText, "SHA-512");
	}

	/**
	 * �ַ��� SHA ����
	 * 
	 * @param strSourceText
	 * @return
	 */
	private String SHA(final String strText, final String strType) {
		// ����ֵ
		String strResult = null;

		// �Ƿ�����Ч�ַ���
		if (strText != null && strText.length() > 0) {
			try {
				// SHA ���ܿ�ʼ
				// �������ܶ��󣬴����������
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// ����Ҫ���ܵ��ַ���
				messageDigest.update(strText.getBytes());
				// �õ� byte ����
				byte byteBuffer[] = messageDigest.digest();

				// �� byte ����ת�� string ����
				StringBuffer strHexString = new StringBuffer();
				// ���� byte ����
				for (int i = 0; i < byteBuffer.length; i++) {
					// ת����16���Ʋ��洢���ַ�����
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// �õ����ؽY��
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}
}
