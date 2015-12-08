package com.fantingame.pay.utils.unionpay;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;

/**
 * 证书操作
 * 
 * 
 * @author swimming
 * 
 */

public class CertificateCoder {
	/**
	 * Java密钥Java Key Store，JKS)KEY_STORE
	 */
	public static final String KEY_STORE = "PKCS12";

	public static final String X509 = "X.509";

	/**
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String keyStorePath, String password)
			throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		String keyAlia = null;
		Enumeration enums = ks.aliases();
		while (enums.hasMoreElements()) { // we are readin just one certificate.
			String keyAlias = (String) enums.nextElement();
			keyAlia = keyAlias;
		}
		PrivateKey key = (PrivateKey) ks
				.getKey(keyAlia, password.toCharArray());
		return key;
	}

	/**
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(InputStream keyStoreStream,
			String password) throws Exception {
		KeyStore ks = getKeyStore(keyStoreStream, password);
		String keyAlia = null;
		Enumeration enums = ks.aliases();
		while (enums.hasMoreElements()) { // we are readin just one certificate.
			String keyAlias = (String) enums.nextElement();
			keyAlia = keyAlias;
		}
		PrivateKey key = (PrivateKey) ks
				.getKey(keyAlia, password.toCharArray());
		return key;
	}

	/**
	 * �?KeyStore获得私钥
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(String keyStorePath, String alias,
			String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
		return key;
	}

	/**
	 * �?Certificate获得公钥
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPublicKey(String certificatePath)
			throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey key = certificate.getPublicKey();
		return key;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath)
			throws Exception {
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(X509);
		FileInputStream in = new FileInputStream(certificatePath);

		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();

		return certificate;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static String getCertificate(InputStream certificateStream)
			throws Exception {
		String publicCert = null;
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(X509);
		Certificate certificate = certificateFactory
				.generateCertificate(certificateStream);
		certificateStream.close();
		byte[] bytes = certificate.getEncoded();
		publicCert = Base64.encodeLines(bytes);
		return publicCert;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath,
			String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		Certificate certificate = ks.getCertificate(alias);

		return certificate;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password)
			throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(KEY_STORE);
		ks.load(is, password.toCharArray());
		is.close();

		return ks;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(InputStream keyStoreStream,
			String password) throws Exception {
		// FileInputStream is = new FileInputStream(keyStoreStream);
		KeyStore ks = KeyStore.getInstance(KEY_STORE);
		ks.load(keyStoreStream, password.toCharArray());
		keyStoreStream.close();

		return ks;
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);

		// 对数据加�?
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);

		// 对数据加�?
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {

		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);
		// 对数据加�?
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {
		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);

		// 对数据加�?
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * 验证Certificate
	 * 
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(String certificatePath) {
		return verifyCertificate(new Date(), certificatePath);
	}

	/**
	 * 验证Certificate是否过期或无�?
	 * 
	 * @param date
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String certificatePath) {
		boolean status = true;
		try {
			// 取得证书
			Certificate certificate = getCertificate(certificatePath);
			// 验证证书是否过期或无�?
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证证书是否过期或无�?
	 * 
	 * @param date
	 * @param certificate
	 * @return
	 */
	private static boolean verifyCertificate(Date date, Certificate certificate) {
		boolean status = true;
		try {
			X509Certificate x509Certificate = (X509Certificate) certificate;
			x509Certificate.checkValidity(date);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 签名
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] sign, String keyStorePath, String alias,
			String password) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(
				keyStorePath, alias, password);
		// 获取私钥
		KeyStore ks = getKeyStore(keyStorePath, password);
		// 取得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
				password.toCharArray());

		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());
		signature.initSign(privateKey);
		signature.update(sign);
		return signature.sign();
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, byte[] sign,
			String certificatePath) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);

		// 获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);

		return signature.verify(sign);

	}

	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String keyStorePath,
			String alias, String password) {
		boolean status = true;
		try {
			Certificate certificate = getCertificate(keyStorePath, alias,
					password);
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(String keyStorePath, String alias,
			String password) {
		return verifyCertificate(new Date(), keyStorePath, alias, password);
	}

	/**
	 * 取得证书签名
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 */
	public static String getSignature(String keyStorePath, String password) {
		String sign = null;
		try {
			KeyStore inputKeyStore = getKeyStore(keyStorePath, password);
			String keyAlia = null;
			Enumeration enums = inputKeyStore.aliases();
			while (enums.hasMoreElements()) { // we are readin just one
												// certificate.

				String keyAlias = (String) enums.nextElement();
				keyAlia = keyAlias;
				/*
				 * if (inputKeyStore.isKeyEntry(keyAlias)) { Key key =
				 * inputKeyStore.getKey(keyAlias, password.toCharArray());
				 * Certificate[] certChain = inputKeyStore
				 * .getCertificateChain(keyAlias);
				 * 
				 * }
				 */
			}
			Certificate certificate = getCertificate(keyStorePath, keyAlia,
					password);

			X509Certificate x509Certificate = (X509Certificate) certificate;
			sign = new String(x509Certificate.getSignature());
			return sign;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 */
	public static String getPublicKey(String keyStorePath, String password) {
		String key = null;
		try {
			KeyStore inputKeyStore = getKeyStore(keyStorePath, password);
			String keyAlia = null;
			Enumeration enums = inputKeyStore.aliases();
			while (enums.hasMoreElements()) { // we are readin just one
												// certificate.

				String keyAlias = (String) enums.nextElement();
				keyAlia = keyAlias;
				/*
				 * if (inputKeyStore.isKeyEntry(keyAlias)) { Key key =
				 * inputKeyStore.getKey(keyAlias, password.toCharArray());
				 * Certificate[] certChain = inputKeyStore
				 * .getCertificateChain(keyAlias);
				 * 
				 * }
				 */
			}
			Certificate certificate = getCertificate(keyStorePath, keyAlia,
					password);
			// X509Certificate x509Certificate = (X509Certificate) certificate;
			PublicKey publicKey = certificate.getPublicKey();
			key = Base64.encodeLines(publicKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
}
