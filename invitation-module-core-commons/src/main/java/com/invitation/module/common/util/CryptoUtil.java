package com.invitation.module.common.util;

import com.invitation.module.common.logger.DetailLogger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class CryptoUtil {

    private String aesKey = "invitationAesKey";
    private String iv;
    private Key keySpec;


    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /** 암호화를 위한 난수 생성 **/
    public String createSalt(int length) {

        // 1. Random, byte 객체 생성
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[length];

        // 2. 난수 생성
        r.nextBytes(salt);

        // 3. byte to String (10진수의 문자열로 변경
        StringBuffer sb = new StringBuffer();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        };

        return sb.toString();
    }

    /** SHA-256 암호화 : 난수와 암호화할 대상과 조합하여 생성 64 byte **/
    public String encryptSHA256(String target) {

        if (CommonsUtil.isEmpty(target)) {
            DetailLogger.error("SHA-256 암호화를 위한 파라미터가 부족합니다. target={}", target);
            return null;
        }
        try {
            // 1. 알고리즘 객체 생성
            MessageDigest md = MessageDigest.getInstance(UtilValues.INSTANCE_SHA_256.CODE());

            // 2. 암호화대상과 난수를 합친 문자열에 SHA 256 적용
            String salt = createSalt(20);
            String merge = target.concat(salt);
            DetailLogger.info("SHA-256 암호화 target={}, salt={}, merge={}", target, salt, merge);

            md.update(merge.getBytes());
            byte[] byteArr = md.digest();

            // 3. byte to String (10진수의 문자열로 변경)
            StringBuffer sb = new StringBuffer();
            for (byte b : byteArr) {
                sb.append(String.format("%02x", b));
            }

            String result = sb.toString();
            DetailLogger.info("SHA-256 암호화 완료 : {}", result);

            return result;

        } catch (NoSuchAlgorithmException e) {
            e.getStackTrace();
            DetailLogger.error("SHA-256 암호화중 예외 발생 e={}", e.getMessage());
            throw new NoSuchElementException("SHA-256 암호화중 예외 발생");
        } catch (Exception e) {
            e.getStackTrace();
            DetailLogger.error("SHA-256 암호화중 예외 발생 e={}", e.getMessage());
        }
        return null;
    }


    /** 16자리의 키 값을 입력하여 객체를 생성
     * @param length 키 길이
     * @throws UnsupportedEncodingException 키 값의 길이가 16 이하일 경우 발생
     * **/
    public void createSecretKeySpec(int length) throws UnsupportedEncodingException {

        if (!CommonsUtil.isEmpty(iv)) {
            DetailLogger.error("AES-256 암호화 이미 생성된 SecretKeySpec 이 있습니다. keySpec={}", keySpec);
            return;
        }

        if (length < 1) {
            DetailLogger.error("AES-256 암호화를 위한 SecretKeySpec 생성에 실패했습니다. length={}", length);
            return;
        }

        DetailLogger.info("AES-256 암호화 keySpec 생성 시작");

        this.iv = aesKey.substring(0, length);
        byte[] keyBytes = new byte[length];
        byte[] bytes = aesKey.getBytes(StandardCharsets.UTF_8);

        int len = bytes.length;
        if (len < keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(bytes, 0, keyBytes, 0, len);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, UtilValues.STR_AES.CODE());

        this.keySpec = secretKeySpec;
        DetailLogger.info("AES-256 암호화 keySpec 생성 완료.");
    }




    /** AES-256 암호화
     * @param target 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws java.security.GeneralSecurityException
     * **/

    public String encryptAES256(String target) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

        if (CommonsUtil.isEmpty(target)) {
            DetailLogger.error("AES-256 암호화를 위한 파라미터가 부족합니다. target={}", target);
            return null;
        }

        if (CommonsUtil.isEmpty(iv)) {
            createSecretKeySpec(16);
        }

        Cipher cipher = Cipher.getInstance(UtilValues.INSTANCE_AES_256_CBC_PKCS5PADDING.CODE());// 패딩기법

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

        byte[] encrypted = cipher.doFinal(target.getBytes(StandardCharsets.UTF_8));
        String encryedStr = new String(Base64.encodeBase64(encrypted));

        return encryedStr;
    }

    public String decryptAES256(String target) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {

        if (CommonsUtil.isEmpty(target)) {
            DetailLogger.error("AES-256 복호화를 위한 파라미터가 부족합니다. target={}", target);
            return null;
        }

        if (CommonsUtil.isEmpty(iv)) {
            createSecretKeySpec(16);
        }

        Cipher cipher = Cipher.getInstance(UtilValues.INSTANCE_AES_256_CBC_PKCS5PADDING.CODE());// 패딩기법

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] byteStr = Base64.decodeBase64(target.getBytes());

        return new String(cipher.doFinal(byteStr), UtilValues.ENCODING_UTF_8.CODE());
    }

    // 암호화
    public String encryptAES(String target) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            byte[] key = new byte[16];
            int i = 0;

            for(byte b : aesKey.getBytes()) {
                key[i++%16] ^= b;
            }

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));

            return new String(Hex.encodeHex(cipher.doFinal(target.getBytes("UTF-8")))).toUpperCase();
        } catch(Exception e) {
            return target;
        }
    }

    // 복호화
    public String decryptAES(String target) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            byte[] key = new byte[16];
            int i = 0;

            for(byte b : aesKey.getBytes()) {
                key[i++%16] ^= b;
            }

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));

            return new String(cipher.doFinal(Hex.decodeHex(target.toCharArray())));
        } catch(Exception e) {
            return target;
        }
    }
}
