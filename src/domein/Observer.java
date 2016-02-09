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
public interface Observer {
    public void update(Double amount);
    public void updateEncryptedStatus(String key);
    public void updateDecryptedStatus(Double time, int amountWords);
}
