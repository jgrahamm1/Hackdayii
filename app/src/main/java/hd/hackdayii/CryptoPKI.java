package hd.hackdayii;

/**
 * Created by kiwi-the-worst on 4/8/17.
 */

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.security.KeyStore;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class CryptoPKI {
    public String MY_PUBLIC_KEY_FILE = "mypubkey";
    public String MY_PRIVATE_KEY_FILE = "myprikey";
    public String OTHER_PUBLIC_KEY_FILE = "otherpubkey";

    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;

    public PublicKey myPublicKey;
    public PrivateKey privateKey;

//    public FileInputStream fis;
//    public KeyStore ks;

//    public CryptoPKI() throws FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException {
//
//        ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        fis = new FileInputStream("seckeystore");
//        ks.load(fis, null);
//
//        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("privateKey", null);
//        privateKey = pkEntry.getPrivateKey();
//    }

    public byte[] RSAEncryptPub(final String plain, PublicKey encryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
//        System.out.println("EEncrypted?????" + toHex(encryptedBytes));
        return encryptedBytes;
    }


    public byte[] RSADecryptPub(final byte[] encryptedBytes, PublicKey decryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, decryptKey);
        decryptedBytes = cipher1.doFinal(encryptedBytes);
//        decrypted = new String(decryptedBytes);
//        System.out.println("DDecrypted?????" + decrypted);
        return decryptedBytes;
    }

    public byte[] RSAEncryptPrivate(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
//        System.out.println("EEncrypted?????" + toHex(encryptedBytes));
        return encryptedBytes;
    }

    public byte[] RSADecryptPrivate(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(encryptedBytes);
//        System.out.println("DDecrypted?????" + decrypted);
        return decryptedBytes;
    }

    private PublicKey readPublicKey() throws Exception {
        InputStream in = new FileInputStream(MY_PUBLIC_KEY_FILE);
        ObjectInputStream oin =
                new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            oin.close();
        }
        return null;
    }


    private PublicKey readPrivateKey() throws Exception {
        InputStream in = new FileInputStream(MY_PRIVATE_KEY_FILE);
        ObjectInputStream oin =
                new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey priKey = fact.generatePublic(keySpec);
            return priKey;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            oin.close();
        }
        return null;
    }

    public void saveToFile(String fileName,
                                   BigInteger mod, BigInteger exp)
            throws Exception {
        ObjectOutputStream oout = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            oout.close();
        }
    }

    // Helper Methods
    public static String toHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }

        return buff.toString();
    }
}
