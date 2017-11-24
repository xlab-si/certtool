package si.xlab.certtool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CertTool {

  private static Certificate donwloadCert(String address)
      throws IOException, NoSuchAlgorithmException, KeyManagementException,
      CertificateEncodingException {
    URL url = new URL(address);
    SSLContext sslCtx = SSLContext.getInstance("TLS");
    sslCtx.init(null, new TrustManager[] {new X509TrustManager() {

      private X509Certificate[] accepted;

      @Override
      public void checkClientTrusted(X509Certificate[] xcs, String string)
          throws CertificateException {}

      @Override
      public void checkServerTrusted(X509Certificate[] xcs, String string)
          throws CertificateException {
        accepted = xcs;
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return accepted;
      }
    }}, null);

    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    connection.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String string, SSLSession ssls) {
        return true;
      }
    });
    connection.setSSLSocketFactory(sslCtx.getSocketFactory());
    connection.connect();
    Certificate[] certificates = connection.getServerCertificates();
    connection.disconnect();
    return certificates[0];
  }

  private static void usage(String[] args) {
    System.out.println("Usage:");
    System.out.println("  java -jar certtool.jar OUTPUT " +
                       "ALIAS1 URL1 [ALIAS2 URL2 ...]");
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      usage(args);
      return;
    }

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    String password = UUID.randomUUID().toString().replace("-", "");
    ks.load(null, password.toCharArray());

    for (int i = 1; i < args.length; i += 2) {
      String alias = args[i];
      String address = args[i + 1];
      Certificate cert = donwloadCert(address);
      ks.setCertificateEntry(alias, cert);
    }

    FileOutputStream fos = new FileOutputStream(args[0]);
    ks.store(fos, password.toCharArray());
    fos.close();

    System.out.println("Password set on keystore: " + password);
  }

}
