package com.fantingame.pay.utils.unionpay;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * 
 * @author swimming
 * 
 */
public class PluginAlgorithm {

	/**
	 * for java 版本号1.0.0
	 * 
	 * @param source
	 * @param keyStorePath
	 * @param password
	 * @return BASE64(RSA(MD5(src),privatekey))
	 */
	public static String getSignature(String source, String keyStorePath,
			String password) {
		String signData = null;
		try {
			PrivateKey privateKey = CertificateCoder.getPrivateKey(
					keyStorePath, password);
			byte[] md5_data = new MD5().getMD5(source);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);

			byte[] encDate = cipher.doFinal(md5_data);

			return Base64.encodeLines(encDate).replaceAll("\t|\r|\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signData;
	}

	/**
	 * for java 版本号1.0.0
	 * 
	 * @param source
	 * @param keyStorePath
	 * @param password
	 * @return BASE64(RSA(MD5(src),privatekey))
	 */
	public static String getSignature(String source,
			InputStream keyStoreStream, String password) {
		String signData = null;
		try {
			PrivateKey privateKey = CertificateCoder.getPrivateKey(
					keyStoreStream, password);
			byte[] md5_data = new MD5().getMD5(source);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);

			byte[] encDate = cipher.doFinal(md5_data);

			return Base64.encodeLines(encDate).replaceAll("\t|\r|\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signData;
	}

	/**
	 * for net 版本号1.1.0
	 * 
	 * @param data
	 * @param keyPath
	 * @param keyPasswd
	 * @return
	 */
	public static String signBase64(byte[] data, String keyPath,
			String keyPasswd) {
		try {
			RSAPrivateKey pbk = (RSAPrivateKey) CertificateCoder.getPrivateKey(
					keyPath, keyPasswd);
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature
					.getInstance("MD5withRSA");
			signet.initSign(pbk);
			signet.update(data);
			// 对信息的数字签名
			byte[] signed = signet.sign();

			return Base64.encodeLines(signed);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 传统签名for net 版本号1.1.0
	 * 
	 * @param data
	 * @param keyPath
	 * @param keyPasswd
	 * @return
	 */
	public static String signBase64(byte[] data, InputStream keyStoreStream,
			String keyPasswd) {
		try {
			RSAPrivateKey pbk = (RSAPrivateKey) CertificateCoder.getPrivateKey(
					keyStoreStream, keyPasswd);
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature
					.getInstance("MD5withRSA");
			signet.initSign(pbk);
			signet.update(data);
			// 对信息的数字签名
			byte[] signed = signet.sign();

			return Base64.encodeLines(signed);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
