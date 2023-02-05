package hr.hsnopek.automatictester.client;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.*;
import java.util.List;

@Component
public class HttpClient {

    public CloseableHttpClient build(KeyStore keyStore, String keyStorePassword, KeyStore trustStore,
                                     HttpRequestInterceptor... interceptors)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        CloseableHttpClient closeableHttpClient;
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                .loadTrustMaterial(trustStore, (certificate, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, (hostname, session) -> true);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslConnectionSocketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        HttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(httpClientConnectionManager);

        for(int i = 0; i < interceptors.length; i++){
            httpClientBuilder.addInterceptorLast(interceptors[i]);
        }

        closeableHttpClient = httpClientBuilder.build();
        return closeableHttpClient;
    }

}
