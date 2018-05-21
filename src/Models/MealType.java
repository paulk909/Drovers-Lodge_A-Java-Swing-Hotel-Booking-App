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
public class MealType {
    private int mealID;
    private String mealType;
    private double mealPrice;

    public MealType() {
    }

    public MealType(int mealID, String mealType, double mealPrice) {
        this.mealID = mealID;
        this.mealType = mealType;
        this.mealPrice = mealPrice;
    }

    public int getMealID() {
        return mealID;
    }

    public String getMealType() {
        return mealType;
    }

    public double getMealPrice() {
        return mealPrice;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }
    
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setMealPrice(double mealPrice) {
        this.mealPrice = mealPrice;
    }
    
    
    
}
