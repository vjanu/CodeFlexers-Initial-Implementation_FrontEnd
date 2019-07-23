package com.example.plusgo.FC;

public class Current_Passenger {


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;
    private String UserId; //passenger Id
    private String TripId;
    private String Source;
    private String Destination;
    private String Status;
    private String Token;
    private String UserImage;
    private String mileage;
    private Double price;

    public Current_Passenger(String name, String source, String destination, String status, String mileage , Double price) {
        Name = name;
        Source = source;
        Destination = destination;
        Status = status;
        this.mileage = mileage;
        this.price = price;
    }



    public String getMileage() {
        return mileage;
    }

    public Current_Passenger(String mileage) {
        this.mileage = mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }


    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        this.UserImage = userImage;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Current_Passenger(String name, String userId, String tripId, String source, String destination, String status,String token) {
        Name = name;
        UserId = userId;
        TripId = tripId;
        Source = source;
        Destination = destination;
        Status = status;
        Token = token;

    }

    public Current_Passenger(String name, String userId, String tripId, String source, String destination, String status,String token,String userImage) {
        Name = name;
        UserId = userId;
        TripId = tripId;
        Source = source;
        Destination = destination;
        Status = status;
        Token = token;
        UserImage = userImage;

    }
}
