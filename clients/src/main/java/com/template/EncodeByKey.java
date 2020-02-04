package com.template;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

public class EncodeByKey {
    public static String password = "!\"#$%&'()0\\aisdf_";
    public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String path = args[0];
        String pass = args[1];
        String type = args[2];
        String alias = args[3];
        String keyPairPass = args[4];
        KeyStore keyStore = loadKeyStore(path, pass, type);
        KeyPair keyPair = getKeyPair(keyStore, alias, keyPairPass);
        String encode = encode(keyPair, password);
        System.out.println("====encode====");
        System.out.println(encode);
        System.out.println("====decode====");
        String decode = decode(keyPair, encode);
        System.out.println(decode);

    }
    public static KeyStore loadKeyStore(String path,String storePassword,String keyStoreType)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        Path filePath = Paths.get(path);
        final URI keystoreUri = filePath.toFile().toURI();
        final URL keystoreUrl = keystoreUri.toURL();
        final KeyStore keystore = KeyStore.getInstance(keyStoreType);
        InputStream is = null;
        try {
            is = keystoreUrl.openStream();
            keystore.load(is, null == storePassword ? null : storePassword.toCharArray());
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return keystore;
    }

    public static KeyPair getKeyPair(final KeyStore keystore,
                                     final String alias, final String password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        final Key key = (PrivateKey) keystore.getKey(alias, password.toCharArray());
        Certificate certificate = keystore.getCertificate(alias);
        PublicKey publicKey = certificate.getPublicKey();
        return new KeyPair(publicKey, (PrivateKey) key);
    }

    public static String encode(final KeyPair keyPair,final String target) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] bytes = cipher.doFinal(target.getBytes());
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static String decode(final KeyPair keyPair,final String target) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] decode = Base64.getDecoder().decode(target);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        return new String(cipher.doFinal(decode));
    }
}
