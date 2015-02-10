package models;

/**
 * Created by David on 2/10/15.
 */
public class Compensation {
    private int userID;
    private double currentCompaRatio;
    private double currentSalary;


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userId;
    }

    public double getCurrentCompaRatio() {
        return currentCompaRatio;
    }

    public void setCurrentCompaRatio(double currentCompaRatio) {
        this.currentCompaRatio = currentCompaRatio;
    }

    public double getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(double currentSalary) {
        this.currentSalary = currentSalary;
    }
}
