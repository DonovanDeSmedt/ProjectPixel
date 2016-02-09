/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Pixel;
import domein.RGB;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author donovandesmedt
 */
public class Uitvoer
{
    private Pixel p;
    private DomeinController dc;
    /**
     * @param args the command line arguments
     */
    public Uitvoer(DomeinController dc)
    {
        this.dc = dc;
        //dc.txtToImg();
        //System.out.println(dc.ImgToTxt());
        System.exit(0);
    }
}
