/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 *
 * @author donovandesmedt
 */
public class Security {
    private String encryptionKey;
    private BigDecimal keygen;
    private BigDecimal pi;
    private String key;
    public Security(){
        pi = new BigDecimal(Math.PI);
    }
    public void generateEncryptionKey()
    {
        encryptionKey = "";
        String type = "xyz";
        SecureRandom random = new SecureRandom();
        encryptionKey += 10 + random.nextInt(90);
        encryptionKey += type.charAt(random.nextInt(3));
        encryptionKey += 10 + random.nextInt(90);
        createKey(encryptionKey);
    }
    public String getEncryptionKey()
    {
        return encryptionKey;
    }
    public void createKey(String key)
    {
        String encryptionKey = key;
        int part1 = Integer.parseInt(encryptionKey.substring(0, 2));
        int part2 = Integer.parseInt(encryptionKey.substring(3,5));
        int newKey = part1 + part2;
        char type = Character.toLowerCase(encryptionKey.charAt(2));
        setKey(newKey, type);
    }
    private void setKey(int key, char type)
    {
        switch(type)
        {
            case 'x': this.keygen = new BigDecimal(Math.abs(Math.sin(key))); break;
            case 'y': this.keygen = new BigDecimal(Math.abs(Math.cos(key))); break;
            case 'z': this.keygen = new BigDecimal(Math.abs(Math.tan(key))); break;
        }
        this.keygen.add(pi);
        /**
         * De keygen (BigDecimal) voor de eenvoudigheid naar een String omzetten.
         */ 
        this.key = keygen.toString().substring(2);
        System.out.println("I'VE BEEN HERE");
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
