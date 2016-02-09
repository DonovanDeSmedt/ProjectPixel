/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

/**
 *
 * @author donovandesmedt
 */
public class DecryptRunnable implements Runnable {
    private String path;
    private DomeinController dc;
    public DecryptRunnable(String path, DomeinController dc){
        this.path = path;
        this.dc = dc;
    }
    @Override
    public void run() {
        dc.setTimer();
        dc.ImgToTxt(path);
        dc.stopTimer();
        dc.showDecryptionStatus();
    }

}
