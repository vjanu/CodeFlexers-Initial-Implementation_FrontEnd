package com.example.plusgo;


public class BaseContent {

    /*
    Do not use this base ip route inside classes, instead use the ipaddress of the hosted each component.
    So that all four laptops have to be conected in same network and can up the individual python files and backend seperately
    */

    //TODO : ADD THE IP OF SERVER WITH NODE JS HERE

    final public String BASEIPROUTE = "http://3.14.29.42";
    public String IpAddress = BASEIPROUTE+":8083";

    /*
    DOCUMENT VALIDATION AND PROFILE RATING MAINTAINANCE
     */
    //PORTS 8088,8089,8090 - USED IN DVPRM
    final public String DVPRMBASEIPROUTE ="http://3.14.29.42"; //TODO : DVPRM CHANGE ACCORDINGLY
    //image upload service written in php
    public String phpIP = DVPRMBASEIPROUTE+":8080";

    /*
    USER PROFILING
     */
    final public String UPMBASEIPROUTE ="http://3.14.29.42"; //TODO : UPM CHANGE ACCORDINGLY
    public String pythonIpAddress = UPMBASEIPROUTE+":8099";

    /*
    FARE CALCULATION
     */
    final public String FCBASEIPROUTE ="http://3.14.29.42"; //TODO : FC CHANGE ACCORDINGLY
    public String pythonIpAddressGetDistance = FCBASEIPROUTE+":8097";
    public String pythonIpAddressGetEstimateFuel = FCBASEIPROUTE+":8096";

    /*
    OPTIMUM PATH RECOGNITION
     */
    final public String OPRBASEIPROUTE ="http://3.14.29.42"; //TODO : OPR CHANGE ACCORDINGLY

}