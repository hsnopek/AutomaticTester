package hr.hsnopek.automatictester.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class CertUtil {

    public static KeyStore loadKeystoreFromPath(String path, String password) {
        KeyStore keyStore;
        try(InputStream is = new FileInputStream(path)) {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, password.toCharArray());
        } catch (IOException | KeyStoreException e) {
            throw new AssertionError(e);
        } catch (CertificateException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyStore;
    }
}
