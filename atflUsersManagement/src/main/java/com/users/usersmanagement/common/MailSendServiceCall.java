package com.users.usersmanagement.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
@PropertySource({ "classpath:persistence.properties" })
public class MailSendServiceCall {

	@Autowired
	private Environment env;

	static {
		disableSslVerification();
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	

/*	public  void callToMailService(LtMastEmailToken ltMastEmailToken) {

		try {
			
			String emailUrl = env.getProperty("mail_url");
			URL url = new URL(null, emailUrl + ltMastEmailToken.getTransactionId() + "" + "/" + ltMastEmailToken.getSupplierId());
			System.out.println("url " + url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			print_content(con);
		} catch (Exception e) {
			System.out.println("call to email service failed");
		}

	}*/

	public void callToSMSService(Long transId, Long userId) {
		try {
			System.out.println(transId + " " + userId);
			try {
				System.out.println("env "+env);
				
				String smsUrl = env.getProperty("sms_url");
				
				if(userId!=null) {
					URL url = new URL(null, smsUrl + transId + "" + "/" + userId);
					
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					
					System.out.println("url " + url);
					
					print_content(con);
				}else {
					URL url = new URL(null, smsUrl + transId );
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					System.out.println("url " + url);
					print_content(con);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.out.println("call to sms service failed");
			e.printStackTrace();
		}

	}

	private static void print_https_cert(HttpsURLConnection con) {

		if (con != null) {

			try {

				System.out.println("Response Code : " + con.getResponseCode());
				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					System.out.println("Cert Type : " + cert.getType());
					System.out.println("Cert Hash Code : " + cert.hashCode());
					System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
					System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
					System.out.println("\n");
				}

			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void print_content(HttpURLConnection con) {
		if (con != null) {

			try {

				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String input;

				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
