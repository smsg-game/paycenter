package com.fantingame.pay.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class UrlRequestUtils {
	public static final Logger LOG = Logger.getLogger(UrlRequestUtils.class);

	public class Mode {
		public final static String POST = "POST";
		public final static String GET = "GET";
	};

	public static String execute(String url, Map<String, String> paraMap, String mode) {
		// 请求者
		HttpUriRequest request = null;
		// 应答者
		HttpResponse response = null;
		// 配置对象
		// BasicHttpContext context = new BasicHttpContext();
		if (Mode.POST.equals(mode)) {// POST 请求
			request = new HttpPost(url);
			if (!paraMap.isEmpty()) {// 参数不为空
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				Set<String> key = paraMap.keySet();
				// 遍历
				for (Iterator it = key.iterator(); it.hasNext();) {
					String s = (String) it.next();
					nvps.add(new BasicNameValuePair(s, paraMap.get(s)));
				}
				try {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} else {// GET 请求
			url = buildURL(url, paraMap);
			LOG.debug(url);

			request = new HttpGet(url);
		}
		// 发送请求
		response = getHttpResponse(request);
		if (response == null || response.getStatusLine().getStatusCode() != 200) {// 通讯失败
			// 终止连接
			request.abort();
			return null;
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "utf-8");
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} finally {
			request.abort();
		}
		return null;
	}

	public static String executeHttps(String url, Map<String, String> paraMap, String mode) {
		// 请求者
		HttpUriRequest request = null;
		// 应答者
		HttpResponse response = null;
		// 配置对象
		// BasicHttpContext context = new BasicHttpContext();
		if (Mode.POST.equals(mode)) {// POST 请求
			request = new HttpPost(url);
			if (!paraMap.isEmpty()) {// 参数不为空
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				Set<String> key = paraMap.keySet();
				// 遍历
				for (Iterator it = key.iterator(); it.hasNext();) {
					String s = (String) it.next();
					nvps.add(new BasicNameValuePair(s, paraMap.get(s)));
				}
				try {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} else {// GET 请求
			url = buildURL(url, paraMap);
			request = new HttpGet(url);
		}
		// 发送请求
		response = getHttpsResponse(request);
		if (response == null || response.getStatusLine().getStatusCode() != 200) {// 通讯失败
			// 终止连接
			request.abort();
			return null;
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "utf-8");
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			request.abort();
			LOG.error(e.getMessage(), e);
		} finally {
			request.abort();
		}
		return null;
	}

	public static String executeHttpByJson(String url, String jsonParam) {
		// 请求者
		HttpPost httpPost = null;
		// 应答者
		HttpResponse response = null;
		try {
			httpPost = new HttpPost(url);
			StringEntity stringentity = new StringEntity(jsonParam, "UTF-8");
			// httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(stringentity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发送请求
		response = getHttpResponse(httpPost);
		if (response == null || response.getStatusLine().getStatusCode() != 200) {// 通讯失败
			// 终止连接
			httpPost.abort();
			return null;
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "utf-8");
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} finally {
			httpPost.abort();
		}
		return null;
	}

	public static String executeHttpByString(String url, String info) {
		// 请求者
		HttpPost httpPost = null;
		// 应答者
		HttpResponse response = null;
		try {
			httpPost = new HttpPost(url);
			StringEntity stringentity = new StringEntity(info, "UTF-8");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setEntity(stringentity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发送请求
		response = getHttpResponse(httpPost);
		if (response == null || response.getStatusLine().getStatusCode() != 200) {// 通讯失败
			// 终止连接
			httpPost.abort();
			return null;
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "utf-8");
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			httpPost.abort();
			LOG.error(e.getMessage(), e);
		} finally {
			httpPost.abort();
		}
		return null;
	}

	public static String buildParam(Map paramMap) {

		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sb.append(key).append("=").append(paramMap.get(key));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}
		String paraStr = sb.toString();

		return paraStr;
	}

	public static String buildURL(String url, Map paramMap) {
		if (paramMap == null || paramMap.isEmpty()) {
			return url;
		}
		if (StringUtils.isBlank(url)) {
			return url;
		}
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sb.append(key).append("=").append(paramMap.get(key));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}
		String paraStr = sb.toString();
		StringBuffer buffer = new StringBuffer();

		buffer.append(url);
		if (url.indexOf("?") != -1) {
			if (url.indexOf("?") == url.length() - 1) {
				buffer.append(paraStr);
			} else {
				buffer.append("&").append(paraStr);
			}
		} else {
			buffer.append("?").append(paraStr);
		}
		return buffer.toString();
	}

	/**
	 * 获取响应对象
	 * 
	 * @param request
	 *            请求对象
	 * @param context
	 *            配置参数
	 * @return
	 */
	public static HttpResponse getHttpResponse(HttpUriRequest request) {
		try {
			// return
			// HttpConnectionPoolManager.getHttpClient().execute(request);
			HttpClient httpClient = new DefaultHttpClient();
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			request.abort();
			LOG.error("网络连接异常或者客户端协议错误:" + request.getURI());
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			request.abort();
			LOG.info("网络连接异常:" + request.getURI());
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取响应对象
	 * 
	 * @param request
	 *            请求对象
	 * @param context
	 *            配置参数
	 * @return
	 */
	public static HttpResponse getHttpsResponse(HttpUriRequest request) {
		try {
			// return
			// HttpConnectionPoolManager.getHttpClient().execute(request);
			HttpClient client = new DefaultHttpClient();
			HttpClient httpClient = wrapClient(client);
			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			request.abort();
			LOG.error("网络连接异常或者客户端协议错误:" + request.getURI());
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			request.abort();
			LOG.info("网络连接异常:" + request.getURI());
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public static HttpClient wrapClient(HttpClient client) {
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { xtm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
			PoolingClientConnectionManager mgr = new PoolingClientConnectionManager(registry);
			return new DefaultHttpClient(mgr, client.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


}

class TrustAnyTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}
}