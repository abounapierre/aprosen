package com.abouna.sante.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author abouna
 */
public class Util {
    public Util(){
        
    }
    
    public static String hacherMotDepasse(String algorithme,
        String monMessage) {
        byte[] digest = null;
        try {
        MessageDigest sha = MessageDigest.getInstance(algorithme);
        digest = sha.digest(monMessage.getBytes());
        } catch (NoSuchAlgorithmException e) {
        }
        return bytesToHex(digest);
        }
    
    public static String bytesToHex(byte[] b) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A','B', 'C', 'D', 'E', 'F' };
        StringBuilder buffer = new StringBuilder();   
        for (int j = 0; j < b.length; j++) {
        buffer.append(hexDigits[(b[j] >> 4) & 0x0f]);
        buffer.append(hexDigits[b[j] & 0x0f]);
        }
        return buffer.toString();
    }
}
