package models;

/**
 * Created by David on 2/10/15.
 */
public class UserCompensation {
    private int userID;
    private double currentCompaRatio;
    private double currentSalary;

    private String userDivision;
    private String userPosition;
    private String userLocation;
    private String userManagerId;

    public UserCompensation(User user, Compensation comp){
        this.setCurrentCompaRatio(comp.getCurrentCompaRatio());
        this.setCurrentSalary(comp.getCurrentSalary());
        this.setUserID(comp.getUserID());
        this.setUserDivision(user.getUserDivision());
        this.setUserLocation(user.getUserLocation());
        this.setUserPosition(user.getUserPosition());
        this.setUserManagerId(user.getUserManagerID());
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getUserDivision() {
        return userDivision;
    }

    public void setUserDivision(String userDivision) {
        this.userDivision = userDivision;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserManagerId() {
        return userManagerId;
    }

    public void setUserManagerId(String userManagerId) {
        this.userManagerId = userManagerId;
    }
}
