package hd.hackdayii;

/**
 * Created by kiwi-the-worst on 4/8/17.
 */
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class AESCrypto {
    private static byte[] keyValue;
    private static String CIPHER_ALGORITHM = "AES";
    private static SecureRandom random = new SecureRandom();

    public static byte[] generateIv(int length) {
        byte[] b = new byte[length];
        random.nextBytes(b);

        return b;
    }

    public static String decrypt(String encrypted)
            throws Exception {
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(enc);
        return new String(result);
    }

    public static String decrypt(String raw, String encrypted)
            throws Exception {
        byte[] enc = toByte(encrypted);
        byte[] bytekey = toByte(raw);
        byte[] result = decrypt(bytekey, enc);
        return new String(result);
    }

    private static byte[] decrypt(byte[] encrypted)
            throws Exception {

        SecretKey skeySpec = new SecretKeySpec(keyValue, CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    public static String[] encrypt(byte[] bytes)
            throws Exception {
        byte[] rawKey = getRawKey();

        byte[] result = encrypt(rawKey, bytes);
        String arr[] = new String[2];
        arr[0] = toHex(result);
        arr[1] = toHex(bytes);
        return arr;
    }

    public static String[] encrypt(String cleartext)
            throws Exception {
        byte[] rawKey = getRawKey();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        String arr[] = new String[2];
        arr[0] = toHex(result);
        arr[1] = toHex(rawKey);
        return arr;
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    // Helper methods

    private static byte[] getRawKey() throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        keyValue = generateIv(cipher.getBlockSize());
        SecretKey key = new SecretKeySpec(keyValue, CIPHER_ALGORITHM);
        byte[] raw = key.getEncoded();
        return raw;
    }

    public static byte[] toByte(String hexString) {
        Log.d("tst",hexString);
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }

        return buff.toString();
    }

}
