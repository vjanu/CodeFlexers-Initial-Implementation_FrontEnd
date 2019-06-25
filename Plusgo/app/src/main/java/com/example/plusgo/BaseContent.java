package com.example.plusgo;



public class BaseContent {

    /*
    Do not use this base ip route inside classes, instead use the ipaddress of the hosted each component.
    So that all four laptops have to be conected in same network and can up the individual python files and backend seperately
    */

    //TODO : ADD THE IP OF SERVER WITH NODE JS HERE
    final public String BASEIPROUTE = "http://10.98.200.109";
    public String IpAddress = BASEIPROUTE+":8083";

    /*
    DOCUMENT VALIDATION AND PROFILE RATING MAINTAINANCE
     */
    //PORTS 8088,8089,8090 - USED IN DVPRM
    final public String DVPRMBASEIPROUTE ="http://10.98.200.109"; //TODO : DVPRM CHANGE ACCORDINGLY
    //image upload service written in php
    public String phpIP = DVPRMBASEIPROUTE+":80";

    /*
    USER PROFILING
     */
    final public String UPMBASEIPROUTE ="http://192.168.43.102"; //TODO : UPM CHANGE ACCORDINGLY
    public String pythonIpAddress = UPMBASEIPROUTE+":99";

    /*
    FARE CALCULATION
     */
    final public String FCBASEIPROUTE ="http://192.168.8.100"; //TODO : FC CHANGE ACCORDINGLY
    public String pythonIpAddressGetDistance = FCBASEIPROUTE+":95";
    public String pythonIpAddressGetEstimateFuel = FCBASEIPROUTE+":96";

    /*
    OPTIMUM PATH RECOGNITION
     */
    final public String OPRBASEIPROUTE ="http://192.168.1.4"; //TODO : OPR CHANGE ACCORDINGLY

}