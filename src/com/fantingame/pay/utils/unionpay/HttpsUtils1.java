package com.fantingame.pay.utils.unionpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsUtils1 {
	public static String sendHttps(String requestData, String httpUrl) {
		StringBuilder result = new StringBuilder();
		HttpURLConnection urlConnection = null;
		URL url;
		try {
			url = new URL(httpUrl);
			// 判断是http请求还是https请求
			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				urlConnection = (HttpsURLConnection) url.openConnection();
				((HttpsURLConnection) urlConnection)
						.setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}
			urlConnection.setConnectTimeout(60000);// 设置超时时间1分钟
			urlConnection.setReadTimeout(60000);
			urlConnection.setRequestMethod("POST");// 设置请求类型为post
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Content-Type",
					"application/xml:charset=utf-8");
			// http.setRequestProperty("Cookie", DataDefine.mCookieStore);
			urlConnection.connect();
			DataOutputStream out = new DataOutputStream(
					urlConnection.getOutputStream());
			out.writeBytes(requestData);
			out.flush();
			out.close();
			BufferedReader in = null;
			if (urlConnection.getResponseCode() == 200) {
				// getCookie(http);
				in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						urlConnection.getErrorStream()));
			}
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					result.append(line.trim());
				}
			}
			in.close();

			in.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 下载文件
	 * 
	 * @param context
	 *            上下文环境
	 * @param strurl
	 *            下载地址
	 * @param path
	 *            下载路径
	 * @return
	 */
	public static boolean urlDownloadToFile(String requestData, String httpUrl,
			String path) {

		boolean bRet = false;
		HttpURLConnection urlConnection = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			// 判断是http请求还是https请求
			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				urlConnection = (HttpsURLConnection) url.openConnection();
				((HttpsURLConnection) urlConnection)
						.setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}
			urlConnection.setConnectTimeout(80000);// 设置超时时间
			urlConnection.setReadTimeout(80000);
			urlConnection.setRequestMethod("POST");// 设置请求类型为post
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Content-Type",
					"application/xml:charset=utf-8");
			urlConnection.connect();
			DataOutputStream out = new DataOutputStream(
					urlConnection.getOutputStream());
			out.writeBytes(requestData);
			out.flush();
			out.close();
			InputStream in = urlConnection.getInputStream();
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = in.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			in.close();

			urlConnection.disconnect();
			bRet = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bRet;
	}

	static TrustManager[] xtmArray = new MytmArray[] { new MytmArray() };

	/**
	 * 信任所有主机-对于任何证书都不做检查
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android 采用X509的证书信息机制
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, xtmArray, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
			// 不进行主机名确认
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			// System.out.println("Warning: URL Host: " + hostname + " vs. "
			// + session.getPeerHost());
			return true;
		}
	};

}

/*
 * 信任所有主机-对于任何证书都不做检查
 */
class MytmArray implements X509TrustManager {
	public X509Certificate[] getAcceptedIssuers() {
		// return null;
		return new X509Certificate[] {};
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		// System.out.println("cert: " + chain[0].toString() + ", authType: "
		// + authType);
	}
}
