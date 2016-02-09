/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import persistentie.PixelMapper;

/**
 *
 * @author donovandesmedt
 */
public class Text {
    private PixelMapper pm;
    private List<String> woordenLijst;
    private int sizeKleurenLijst;
    private int width;
    private int height;
    private int size;
    public Text(PixelMapper pm){
        this.pm = pm;
    }
    /**
     * Een .txt bestand inlezen.
     * @param bestandsNaam 
     */
    public void leesTekstFile(String bestandsNaam)
    {
        woordenLijst = pm.leesTekstFile(bestandsNaam);
    }
    /**
     * Een .docx bestand inlezen.
     * @param bestandsNaam 
     */
    public void leesDocFile(String bestandsNaam)
    {
        woordenLijst = pm.leesDocFile(bestandsNaam);
    }
    public void saveTekstFile(List<String> letters, String bestandsNaam)
    {
        String path = "";
        for (int i = bestandsNaam.length() - 1; i > 1; i--)
        {
            if (bestandsNaam.charAt(i) == '/')
            {
                path = bestandsNaam.substring(0, i) + "/decrypted.txt";
                break;
            }
        }
        pm.createTekstFile(letters, path);
    }
    public List<String> getWoordenlijst(){
        return woordenLijst;
    }
    /**
     * Lijst van woorden omzetten naar lijst van chars 
     * Deze char-list wordt later in een int list omgezet
     * @return 
     */
    public List<Character> verdeelLijst()
    {
        /**
         * Txt file inlezen en woord per woord in een list plaatsen.
         */

        List<Character> letterLijst = new ArrayList<>();

        /**
         * Van elk woord elke letter in een list plaatsen.
         */
        for (String word : woordenLijst)
        {
            StringBuilder sb = new StringBuilder(word);
            for (int i = 0; i < sb.length(); i++)
            {
                letterLijst.add(sb.charAt(i));
            }
        }
        size = letterLijst.size()/3;
        return letterLijst;
    }
    public int getTextFileSize(){
        return size;
    }
   public void calculateWithAndHeight(int size){
        List<Integer> delers = new ArrayList<>();

        /**
         * Van elke 3 letters een pixel maken (rgb).
         */
        sizeKleurenLijst = size;
        /**
         * De delers berekenen van de lengte van de kleurenLijst, deze twee
         * delers moeten beiden zo groot mogelijk zijn Eerst kijken ofdat het
         * getal toevallig geen volkomen kwadraag is, want dan werkt de methode
         * calculateWidthAndHeight niet.
         */
        long vk = (long) Math.sqrt(sizeKleurenLijst);
        if (sizeKleurenLijst == (vk * vk))
        {
            width = (int) vk;
            height = (int) vk;
        } else
        {
            delers = calculateWidthAndHeight();
            /**
             * Als de eerste deler = 1 dan is de tweede veel te groot --> eentje
             * bijtellen en opnieuw berekenen.
             */
            if (delers.get(0) == 1)
            {
                sizeKleurenLijst++;
                delers = calculateWidthAndHeight();
            }          
            width = delers.get(0);
            height = delers.get(1);
        }
    }

    //DEZE METHODE MOET P.MAAKPIXELS ALS PARAMETER KRIGJEN
    public int[] convertLetterToPixelLijst(List<RGB> kleurenLijst)
    {
        int[] kleuren = new int[sizeKleurenLijst * 3];
        /**
         * Teller zorgt ervoor dat telkens het volgende object uit de
         * kleurenlijst wordt genomen ipv telkens i=i+3.
         */
        int teller = 0;
        for (int i = 0; i < kleuren.length - 2; i = i + 3)
        {
            if (kleurenLijst.size() > teller)// als de teller op 17 staat is er een probleem
            {
                kleuren[i] = kleurenLijst.get(teller).getR();
                kleuren[i + 1] = kleurenLijst.get(teller).getG();
                kleuren[i + 2] = kleurenLijst.get(teller).getB();
            } else
            {
                kleuren[i] = 30;
                kleuren[i + 1] = 30;
                kleuren[i + 2] = 30;
            }
            teller++;
        }
        return kleuren;
    }
    private List<Integer> calculateWidthAndHeight()
    {
        List<Integer> delers = new ArrayList<>();
        List<Integer> deeltal = new ArrayList<>();
        int num = sizeKleurenLijst;
        int incrementer = 1;
        if (num % 2 != 0)
        {
            incrementer = 2; //only test the odd ones
        }

        for (int i = 1; i <= num / 2; i = i + incrementer)
        {
            if (num % i == 0)
            {
                delers.add(i);
            }
        }
        delers.add(num);
        deeltal.add(delers.get((delers.size() / 2) - 1));
        deeltal.add(delers.get(delers.size() / 2));
        return deeltal;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}
