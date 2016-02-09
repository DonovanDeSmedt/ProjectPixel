/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import com.sun.org.apache.xpath.internal.axes.SubContextList;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author donovandesmedt
 */
public class Progress implements Subject{
    private List<String> processList;
    private Set<Observer> observers;
    private DecimalFormat df;
    
    public Progress(){
        observers = new HashSet<>();
        df = new DecimalFormat("0.00");
    }
    public void initProgressbar()
    {
        /**
         * De processList opvullen met O.1, 0.2, 0.3, ...
         */
        processList = new ArrayList<>();
        for (int i = 1; i < 101; i++)
        {
            processList.add(df.format(0.01 * i));
        }
    }
    /**
     * Deze methode zal ervoor zorgen dat de progressbar enkel geüpdate wordt
     * wanneer de waarde met 0.1 stijgt.
     *
     * @param amount
     */
    public void progressChecker(String amount)
    {
        
        if (processList.contains(amount))
        {
            //dc.setProgressbar(Double.parseDouble(amount));
            notifyAllObservers(Double.parseDouble(amount));
            processList.remove(amount);
        }
        
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Double amount) {
        observers.forEach(e -> e.update(amount));
    }
    @Override
    public void notifyAllObservers(String key) {
        observers.forEach(e -> e.updateEncryptedStatus(key));
    }
    @Override
    public void notifyAllObservers(Double time, int amountWords) {
        observers.forEach(e -> e.updateDecryptedStatus(time, amountWords));
    }
}
