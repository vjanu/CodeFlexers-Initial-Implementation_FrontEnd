package com.example.plusgo.FC.TripHistory;

public class Driver {
    private String TripId;
    private String Date;
    private Double Earn;
    private String Source;
    private String Destination;
    private String Time;

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Double getEarn() {
        return Earn;
    }

    public void setEarn(Double earn) {
        Earn = earn;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }





    public Driver(String tripId, String date, Double earn, String source, String destination, String time) {
        TripId = tripId;
        Date = date;
        Earn = earn;
        Source = source;
        Destination = destination;
        Time = time;
    }



    public Driver() {
    }






}
