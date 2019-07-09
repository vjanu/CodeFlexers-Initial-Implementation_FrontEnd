package com.example.plusgo.FC.TripHistory;

public class Passenger {

    private String PassengerName;
    private String TripId;
    private String DateTime;
    private String StartingPoint;
    private String DestinationPoint;
    private Double Fare;
    private String DriverName;




    public Passenger( String tripId,String passengerName, String startingPoint, String destinationPoint) {
        PassengerName = passengerName;
        TripId = tripId;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
    }

    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }



    public String getStartingPoint() {
        return StartingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        StartingPoint = startingPoint;
    }

    public String getDestinationPoint() {
        return DestinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        DestinationPoint = destinationPoint;
    }



    public Passenger(String tripId, String driverName, String dateTime,String startingPoint,String destinationPoint, Double fare) {
        TripId = tripId;
        DriverName = driverName;
        DateTime = dateTime;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
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
