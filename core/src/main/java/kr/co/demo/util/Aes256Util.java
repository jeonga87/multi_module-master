package kr.co.demo.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Aes256Util extends TagSupport {
    private static final Logger log = LoggerFactory.getLogger(Aes256Util.class);

    String securityKey = "koreaswimming!@#";

    private static String iv;
    private static Key keySpec;


    /** 사용법 */
    /**
     *  Aes128 aes = new Aes128();
     *
     *  String encode = aes.encrypt(key);
     *  String decode = ase.decrypt();
     *
     * */

    public Aes256Util() {
        try {
            this.iv = securityKey.substring(0, 16);
            byte[] keyBytes = new byte[16];
            byte[] b = securityKey.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            this.keySpec = keySpec;
        } catch (UnsupportedEncodingException e) {}
    }


    /** 암호화 */
    public String encrypt(String key) {
        if(key == null || key == "") return null;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

            byte[] encrypted = c.doFinal(key.getBytes("UTF-8"));

            String newKey = new String(Base64.encodeBase64(encrypted));
            return newKey;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException e) {}
        return null;
    }

    /** 복호화 */
    public static String decrypt(String key) {
        if(key == null || key == "") return null;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

            byte[] crypted = Base64.decodeBase64(key.getBytes());

            return new String(c.doFinal(crypted), "UTF-8");
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException
                | UnsupportedEncodingException e) {}
        return null;
    }
}
