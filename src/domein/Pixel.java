/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import persistentie.PixelMapper;

/**
 *
 * @author donovandesmedt
 */
public class Pixel
{

    private PixelMapper pm;
    private Progress pr;
    private Security s;
    private String progress;
    private int grootteLijst;
    private DecimalFormat df;
    
    

    public Pixel(PixelMapper pm, Security s, Progress pr)
    {
        this.pm = pm;
        this.s = s;
        this.pr = pr;
        df = new DecimalFormat("0.00");  
    }
    public BufferedImage readImage(String bestandsnaam){
        return (BufferedImage) pm.readImage(bestandsnaam);
    }
    public List<RGB> maakPixels(List<Character> letterLijst, int size)
    {
        List<RGB> kleurenLijst = new ArrayList<>();
        for (int i = 0; i < letterLijst.size() - 2; i = i + 3)
        {
            int r = letterLijst.get(i);
            int g = letterLijst.get(i + 1);
            int b = letterLijst.get(i + 2);
            kleurenLijst.add(new RGB(r, g, b));
        }
        while(kleurenLijst.size() < size){
            kleurenLijst.add(new RGB(30, 30, 30));
        }
        return kleurenLijst;
    }

    public Image createImgFromArray(int width, int heigth, List<RGB> kleuren)
    {
        /**
         * Eerst de offset bereken, dit dient voor de encryptie.
         */

        /**
         * De teller moet ervoor zorgen dat er telkens per 3 wordt geteld wegens
         * de 3 waarden van RGB met de binnenste lust lukt dit niet.
         */
        
        /**
         * In de for-lus wordt de methode encrypteer aangeroepen, met als parameter
         * i % 46 Dit omwille van het feit dat de key maar 46 characters lang is
         * en men steeds 2 charcters neemt dus laatst mogelijke is 45.
         */
        BufferedImage img = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < heigth; j++)
            {
                if((heigth * i)+j == kleuren.size())
                    break;
                int a = 123;
                int r = (kleuren.get((heigth*i)+j).getR() + s.encrypteer(i % 43))%256;
                int g = (kleuren.get((heigth*i)+j).getG() + s.encrypteer(i % 43))%256;
                int b = (kleuren.get((heigth*i)+j).getB() + s.encrypteer(i % 43))%256;
                int p = (a << 24) | (r << 16) | (g << 8) | b;
                

                img.setRGB(i, j, p);
            }
        }
        return img;
    }
    
    public void saveImage(String imgNaam, Image img, String path)
    {
        pm.saveImage(imgNaam, (BufferedImage) img, path);
    }
    
    public List<RGB> getPixelData(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        pr.initProgressbar();
        /**
         * grootteLijst is het aantal keer dat er door de dubbele lus wordt 
         * geloopt
         * Elke keer worden er 3 letters opgeslagen --> *3.
         */
        grootteLijst = width * height * 3;
        List<RGB> ingelezenPixelLijst = new ArrayList<>();
        int[][] listForOffset = new int[3][3];
        int red = 0;
        int green = 0;
        int blue = 0;
        int index = 0;
        
        for (int row = 0; row < width; row++)
        {
            for (int col = 0; col < height; col++)
            {
                Color c = new Color(img.getRGB(row, col));

                red = c.getRed() - s.encrypteer(row%43);
                if (red < 0)
                    red += 256;
                green = c.getGreen() - s.encrypteer(row%43);
                if(green < 0)
                    green += 256;
                blue = c.getBlue() - s.encrypteer(row%43);
                if(blue < 0)
                    blue += 256;
                    
                ingelezenPixelLijst.add(new RGB(red, green, blue));
                /**
                 * De grootte van de progressbar aanpassen,
                 * deze methode vult zal de eerste 33% van de progressbar opvullen.
                 */
                
                progress = df.format((double) index++ / grootteLijst);
                pr.progressChecker(progress);
            }
        }
        return ingelezenPixelLijst;
    }

    public List<String> convertPixelLetterLijst(List<RGB> ingelezenPixelLijst)
    {   
        /**
         * Deze methode zal de tweede 33% van de progressbar opvullen.
         */
        List<String> convertedLetterLijst = new ArrayList<>();
        int index = grootteLijst;     
        int range = ingelezenPixelLijst.size()*9;
        for(RGB rgb : ingelezenPixelLijst)
        {
            convertedLetterLijst.add(String.format("%c", (char) rgb.getR()));
            convertedLetterLijst.add(String.format("%c", (char) rgb.getG()));
            convertedLetterLijst.add(String.format("%c", (char) rgb.getB()));
            progress = (df.format((double) (index+=3) / range));
            pr.progressChecker(progress);
        };
        return convertedLetterLijst;
    }

    

    

    /**
     * Progressbar aanpassen op basis van de vorderingen van het decrypteren
     *
     * @param amount
     */
//    public void setProgressbar(double amount)
//    {
//        //dc.setProgressbar(amount);
//    }
    public int getAmountOfWords(){
        return pm.getAmountOfWords();
    }
}
