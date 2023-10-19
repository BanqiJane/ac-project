package xyz.acproject.utils.security;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author Jane
 * @ClassName CertUtils
 * @Description TODO
 * @date 2021/7/23 11:08
 * @Copyright:2021
 */
public class CertUtils {

    public static PublicKey getPublicKey(byte[] keyInByte){
        X509Certificate cert = null;
        PublicKey pubKey = null;
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate)certificatefactory.generateCertificate(new ByteArrayInputStream(keyInByte));
            pubKey  = cert.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKey;
    }

    public static KeyStore getKeyStore(byte[] keyInByte,String cAalias){
        KeyStore keyStore = null;
        try {
            // 证书工厂
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            // 秘钥仓库
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry(cAalias, certificateFactory.generateCertificate(new ByteArrayInputStream(keyInByte)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyStore;
    }

}
