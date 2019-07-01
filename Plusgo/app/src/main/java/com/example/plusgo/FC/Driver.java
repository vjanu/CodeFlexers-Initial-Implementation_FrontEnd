package com.example.plusgo.FC;

public class Driver {
    private String TripId;
    private String DateTime;
    private Double Earn;

    public Driver() {
    }

    public Driver(String tripId, String dateTime, Double earn) {
        TripId = tripId;
        DateTime = dateTime;
        Earn = earn;
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

    public Double getEarn() {
        return Earn;
    }

    public void setEarn(Double earn) {
        Earn = earn;
    }


}
