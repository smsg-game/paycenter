package test;

import java.security.cert.X509Certificate;

public class MyTrustManager implements javax.net.ssl.X509TrustManager{

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
