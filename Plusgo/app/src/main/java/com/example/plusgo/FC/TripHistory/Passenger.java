package com.example.plusgo.FC.TripHistory;

public class Passenger {

    private String PassengerName;
    private String TripId;
    private String DateTime;
    private String StartingPoint;
    private String DestinationPoint;
    private Double Fare;
    private String DriverName;
    private String Image;
    private String SourceLatLong;
    private String DestinationLatLong;
    private String DriverId;

    public Passenger(String tripId, String passengerName,  String image,Double fare) {
        PassengerName = passengerName;
        TripId = tripId;
        Fare = fare;
        Image = image;
    }

    public Passenger(Double fare) {
        Fare = fare;
    }



    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }



    public String getSourceLatLong() {
        return SourceLatLong;
    }

    public void setSourceLatLong(String sourceLatLong) {
        this.SourceLatLong = sourceLatLong;
    }

    public String getDestinationLatLong() {
        return DestinationLatLong;
    }

    public void setDestinationLatLong(String destinationLatLong) {
        this.DestinationLatLong = destinationLatLong;
    }




    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public Passenger( String tripId,String passengerName, String startingPoint, String destinationPoint) {
        PassengerName = passengerName;
        TripId = tripId;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
    }

    public Passenger( String tripId,String passengerName, String startingPoint, String destinationPoint , String image) {
        PassengerName = passengerName;
        TripId = tripId;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
        Image = image;
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



    public Passenger(String tripId, String driverName, String dateTime,String startingPoint,String destinationPoint, Double fare , String image) {
        TripId = tripId;
        DriverName = driverName;
        DateTime = dateTime;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
        Fare = fare;
        Image = image;
    }

    public Passenger(String tripId, String driverName, String dateTime,String startingPoint,String destinationPoint, Double fare , String image,String sourceLatLong , String destinationLatLong ,String driverId) {
        TripId = tripId;
        DriverName = driverName;
        DateTime = dateTime;
        StartingPoint = startingPoint;
        DestinationPoint = destinationPoint;
        Fare = fare;
        Image = image;
        SourceLatLong = sourceLatLong;
        DestinationLatLong = destinationLatLong;
        DriverId = driverId;
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
