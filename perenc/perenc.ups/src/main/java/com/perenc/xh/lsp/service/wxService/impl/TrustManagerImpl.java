package com.perenc.xh.lsp.service.wxService.impl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustManagerImpl implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		//do nothing
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		//do nothing
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}