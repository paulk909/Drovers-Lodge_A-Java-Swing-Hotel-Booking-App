/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Paul
 */
public class CardType {
    
    private int cardTypeID;
    private String cardType;

    public CardType() {
    }

    public CardType(int cardTypeID, String cardType) {
        this.cardTypeID = cardTypeID;
        this.cardType = cardType;
    }

    public int getCardTypeID() {
        return cardTypeID;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardTypeID(int cardTypeID) {
        this.cardTypeID = cardTypeID;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    
    
    
}
