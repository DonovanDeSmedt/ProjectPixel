/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Pixel;
import domein.Progress;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author donovandesmedt
 */
public class PixelMapper 
{
    private Progress pr;
    private List<String> letterLijst;
    private DecimalFormat df;
    private int amountOfWords;
    
    public PixelMapper(Progress pr){
        this.pr = pr;
        df = new DecimalFormat("0.00");
    }
    public List<String> leesTekstFile(String bestandsNaam)
    {
        letterLijst = new ArrayList<>();
        try (Scanner s = new Scanner(Files.newInputStream(Paths.get(bestandsNaam))))
        {
            while (s.hasNextLine())
            {
                String zin = s.nextLine();
                Scanner word = new Scanner(zin);
                while (word.hasNext())
                {
                    letterLijst.add(word.next());
                    letterLijst.add(" ");
                }
                letterLijst.add("\n");
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Kan het bestand niet vinden");
        } catch (IOException e)
        {
            System.out.println("Er kan niet gelezen worden uit " + bestandsNaam);
        }
        return letterLijst;
    }
    public List<String> leesDocFile(String bestandsNaam)
    {
        List<String> zin = new ArrayList<>();
        letterLijst = new ArrayList<>();
        File file = null;
        XWPFWordExtractor extractor = null;
        try
        {
            XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get(bestandsNaam)));
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for(XWPFParagraph par: paragraphs){ 
            /**
             * Elke paragraph op spaties splitten en elk woord in de letterlijst plaatsen.
             */
                zin = Arrays.asList(par.getParagraphText().split(" "));
                for(String woord: zin){
                    letterLijst.add(woord);
                    letterLijst.add(" ");
                }
                letterLijst.add("\n");
            }
        }
        catch (Exception exep)
        {
            exep.printStackTrace();
        }
        
        return letterLijst;
    }
    public void createTekstFile(List<String> letters, String bestandsNaam)
    {
        /**
         * Deze methode zal de laatste 33% van de progressbar vullen.
         */
        int index = letters.size()*2;
        int range = letters.size()*3;
        /**
         * Het aantal woorden tellen.
         */
        amountOfWords = 0;
        String progress;
        try (Formatter f = new Formatter(Files.newOutputStream(Paths.get(bestandsNaam))))
        {
            for (String s : letters)
            {
                f.format("%s", s);             
                if(s.equals(" "))
                    amountOfWords++;
                progress = df.format((double) index++ / range);
                pr.progressChecker(progress);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Kan het bestand niet vinden");
        } catch (IOException e)
        {
            System.out.println("Kan niet schrijven naar" + bestandsNaam);
        }
    }

    public void saveImage(String imgNaam, BufferedImage img, String bestandsNaam)
    {
        String path = "";
        for (int i = bestandsNaam.length() - 1; i > 1; i--)
        {
            if (bestandsNaam.charAt(i) == '/')
            {
                path = bestandsNaam.substring(0, i) + "/encryptedImage.png";
                break;
            }
        }
        try
        {
            File imageFile = new File(path);
            ImageIO.write(img, "png", imageFile);
        } catch (IOException ex)
        {
            System.out.println("Fout bij opslaan ");
        }
    }

    public Image readImage(String imgNaam)
    {
        BufferedImage img = null;

        Image image = null;
        try
        {
            image = ImageIO.read(Files.newInputStream(Paths.get(imgNaam)));
        } catch (IOException ex)
        {
            System.out.println("Kan image " + imgNaam + "niet lezen");
        }
        return image;
    }
    public int getAmountOfWords(){
        return amountOfWords;
    }
}
