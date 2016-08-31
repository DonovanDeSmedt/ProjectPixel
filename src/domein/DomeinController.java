/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import gui.PixelSchermController;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import org.apache.poi.poifs.crypt.ChainingMode;
import persistentie.PixelMapper;

/**
 *
 * @author donovandesmedt
 */
public class DomeinController 
{
    private Pixel p;
    private Text t;
    private Security s;
    private PixelMapper pm;
    private Progress pr;
    private Timer timer;
    private int time;
    
    public DomeinController()
    {   
        pr = new Progress();
        pm = new PixelMapper(pr);
        s = new Security();
        p = new Pixel(pm,s,pr);
        t = new Text(pm);       
    }
    public void encrypt(String bestandsNaam, String extension, String key){
        /**
         * De encryptionskey genereren en setten in de klasse Pixel.
         */
        generateEncryptionKey(key);
        /**
         * Op een nieuwe thread het encryptieproces starten.
         */
        Thread encryptionThread = new Thread(new EncryptRunnable(bestandsNaam, extension, this));
        encryptionThread.start();
    }
    public void showEncryptionStatus(){
        pr.notifyAllObservers(getEncryptionKey());
    }
    public void decrypt(String bestandsNaam, String key){
        /**
         * De encryptionkey setten in de klasse Pixel.
         */
        generateEncryptionKey(key);
        /**
         * Op een nieuwe thread het decryptieproces starten.
         */
        Thread decryptionThread = new Thread(new DecryptRunnable(bestandsNaam,this));
        decryptionThread.start();
        
    }    
    public void showDecryptionStatus(){
        pr.notifyAllObservers((double) time / 10, p.getAmountOfWords());
    }
    public void generateEncryptionKey(String key)
    {
        s.generateEncryptionKey(key);
    }
    public String getEncryptionKey(){
        return s.getEncryptionKey();
    }
    
    /**
     * Encryptiemethodes zonder multithreading.
     * @param bestandsNaam
     * @return 
     */
//    public String txtToImg(String bestandsNaam)
//    {
//        /**
//         * Txt-inlezen en omzetten naar een array van pixels en hiervan een
//         * image maken en opslaan.
//         */
//        leesTekstFile(bestandsNaam);
//        List<Character> letters = convertWoordenLijstToLetterLijst();
//        convertPixelsToImg(bestandsNaam);
//        return p.getEncryptionKey();
//    }
//
//    public String docxToImg(String bestandsNaam)
//    {
//        leesDocFile(bestandsNaam);
//        List<Character> letters = convertWoordenLijstToLetterLijst();
//        convertPixelsToImg(bestandsNaam);
//        return p.getEncryptionKey();
//    }

    public void ImgToTxt(String bestandsNaam)
    {
        /**
         * Image inlezen en omzetten naar een array van letters en een txt file
         * van maken en opslaan.
         */
        List<RGB> pixels = convertImgToPixels(bestandsNaam);
        List<String> letters = convertPixelsToLetters(pixels);
        t.saveTekstFile(letters, bestandsNaam);
    }

    public void saveTekst(List<String> letters, String bestandsNaam)
    {
        t.saveTekstFile(letters, bestandsNaam);
    }

    public void leesTekstFile(String bestandsNaam)
    {
        /**
         * Tekstfile inlezen.
         */
        t.leesTekstFile(bestandsNaam);
    }

    public void leesDocFile(String bestandsNaam)
    {
        /**
         * Word document inlezen.
         */
        t.leesDocFile(bestandsNaam);
    }

    public List<Character> convertWoordenLijstToLetterLijst()
    {
        /**
         * Van woordenlijst een letterlijst maken.
         */
        return t.verdeelLijst();
    }

    

    public void convertPixelsToImg(String path, List<Character> letterLijst)
    {
        /**
         * Letterlijst omzetten naar een lijst van RGB objecten.
         */
        List<RGB> kleurenLijst = p.maakPixels(letterLijst, getTextFileSize());
        /**
         * Image met de doorgegeven afmetingen creeren op basis van de RGB lijst.
         */
        Image img = (p.createImgFromArray(t.getWidth(), t.getHeight(), kleurenLijst));
        p.saveImage("naam", img, path);
    }

    public void calculateWithAndHeight(int size){
        t.calculateWithAndHeight(size);
    }
    public int getTextFileSize(){
        return t.getTextFileSize();
    }
    private List<RGB> convertImgToPixels(String bestandsNaam)
    {
        /**
         * Image inlezen en omzetten naar een double array van pixels.
         */
        BufferedImage image = p.readImage(bestandsNaam);
        /**
         * Pixels uit image halen en in lijst zetten.
         */
        return p.getPixelData(image);

    }

    private List<String> convertPixelsToLetters(List<RGB> pixels)
    {
        /**
         * Pixels omzetten naar letters en deze in een lijst plaatsen.
         */
        return p.convertPixelLetterLijst(pixels);
    } 

    public void setTimer()
    {
        timer = new Timer();
        time = 0;
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(new Runnable()
                {
                    public void run()
                    {
                        time++;
                    }
                });
            }
        }, 1, 100);
    }
    public void stopTimer(){
        timer.cancel();
    }
    public void addObserver(Observer observer){
        pr.addObserver(observer);
    }

}
