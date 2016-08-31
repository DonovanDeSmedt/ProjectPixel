/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author donovandesmedt
 */
public class Security {
    private long encryptionKey;
    private BigDecimal keygen;
    private BigDecimal pi;
    private String key;
    private final String type = "xyz";
    public Security(){
        pi = new BigDecimal(Math.PI);
    }
    public void generateEncryptionKey(String key)
    {
        encryptionKey = 0;
        for(int i=0; i<key.length(); i++){
            encryptionKey += (int) key.charAt(i);
        }
        keygen = new BigDecimal(Math.abs(Math.sin(encryptionKey)));
        keygen.add(pi);
        /**
         * De keygen (BigDecimal) voor de eenvoudigheid naar een String omzetten.
         */
        this.key = keygen.toString().substring(2);
    }
    public String getEncryptionKey()
    {
        return "Done";
    }
    public int encrypteer(int i)
    {   
        /**
         * De i'de en volgende pos van de key opvragen 
         * bv i = 3 --> key = 3.14159268 --> return 15.
         */
        return Integer.parseInt(this.key.substring(i, i+3))%256;
    }
}
