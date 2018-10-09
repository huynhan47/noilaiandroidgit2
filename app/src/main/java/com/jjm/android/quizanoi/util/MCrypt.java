package com.jjm.android.quizanoi.util;

import android.util.Base64;
import android.util.Log;

import com.facebook.share.internal.ShareConstants;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    public static boolean DEBUG_LOG_ENABLED = false;
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String TAG = "AESCrypt";
    private static final byte[] ivBytes = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        return new SecretKeySpec(digest.digest(), "AES");
    }

    public static String encrypt(String password, String message) throws GeneralSecurityException {
        try {
            return Base64.encodeToString(encrypt(generateKey(password), ivBytes, message.getBytes("UTF-8")), 2);
        } catch (UnsupportedEncodingException e) {
            if (DEBUG_LOG_ENABLED) {
                Log.e(TAG, "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] encrypt(SecretKeySpec key, byte[] iv, byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(1, key, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(message);
        log("cipherText", cipherText);
        return cipherText;
    }

    public static String decrypt(String password, String base64EncodedCipherText) throws GeneralSecurityException {
        try {
            SecretKeySpec key = generateKey(password);
            log("base64EncodedCipherText", base64EncodedCipherText);
            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, 2);
            log("decodedCipherText", decodedCipherText);
            byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);
            log("decryptedBytes", decryptedBytes);
            String message = new String(decryptedBytes, "UTF-8");
            log(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, message);
            return message;
        } catch (UnsupportedEncodingException e) {
            if (DEBUG_LOG_ENABLED) {
                Log.e(TAG, "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] decrypt(SecretKeySpec key, byte[] iv, byte[] decodedCipherText) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(2, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
        log("decryptedBytes", decryptedBytes);
        return decryptedBytes;
    }

    private static void log(String what, byte[] bytes) {
        if (DEBUG_LOG_ENABLED) {
            Log.d(TAG, what + "[" + bytes.length + "] [" + bytesToHex(bytes) + "]");
        }
    }

    private static void log(String what, String value) {
        if (DEBUG_LOG_ENABLED) {
            Log.d(TAG, what + "[" + value.length() + "] [" + value + "]");
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }
}
