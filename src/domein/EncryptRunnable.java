/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;

/**
 *
 * @author donovandesmedt
 */
public class EncryptRunnable implements Runnable {

    private String bestandsNaam, extension;
    private DomeinController dc;

    public EncryptRunnable(String bestandsNaam, String extension, DomeinController dc) {
        this.bestandsNaam = bestandsNaam;
        this.extension = extension;
        this.dc = dc;
    }

    @Override
    public void run() {
        /**
         * Op basis van de extensie de juiste inleesMethode selecteren.
         */
        if (extension.contains("txt")) {
            dc.leesTekstFile(bestandsNaam);
        } else {
            dc.leesDocFile(bestandsNaam);
        }
        /**
         * Woordenlijst omzetten naar een lijst van characters.
         */
        List<Character> letters = dc.convertWoordenLijstToLetterLijst();
        dc.calculateWithAndHeight(dc.getTextFileSize());
        dc.convertPixelsToImg(bestandsNaam, letters);
        dc.showEncryptionStatus();
    }
}
