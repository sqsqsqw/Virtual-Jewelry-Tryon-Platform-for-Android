package com.Hashqi.try_on_client.Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionMD5Manager {
    public static String encryption  (String password){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i=0; i<s.length;i++){
                result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "error";
        }
    }
}