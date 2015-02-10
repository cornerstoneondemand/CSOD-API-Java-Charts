package models;

/**
 * Created by David on 2/10/15.
 */
public class User {
    private int id;
    private String userID;
    private String userDivision;
    private String userPosition;
    private String userLocation;
    private String userManagerID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserId(String userID) {
        this.userID = userID;
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

    public String getUserManagerID() {
        return userManagerID;
    }

    public void setUserManagerID(String userManagerID) {
        this.userManagerID = userManagerID;
    }
}
