package com.example.plusgo.FC;

public class Passenger {
    private String TripId;
    private String DateTime;
    private Double Fare;

    public Passenger(String tripId, String dateTime, Double fare) {
        TripId = tripId;
        DateTime = dateTime;
        Fare = fare;
    }

    public Passenger() {
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public Double getFare() {
        return Fare;
    }

    public void setFare(Double fare) {
        Fare = fare;
    }


}
