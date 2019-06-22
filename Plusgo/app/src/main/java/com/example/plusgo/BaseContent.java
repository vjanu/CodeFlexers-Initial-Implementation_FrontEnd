
package com.example.plusgo;

public class BaseContent {

    //TODO : ADD THE IP OF SERVER HERE
    final public String BASEIPROUTE = "http://10.98.200.109";

    //IP ADDRESS OF THE NODEJS BACKEND
    public String IpAddress = BASEIPROUTE+":8083";

    //TODO : PORTS 8088,8089,8090 - USED IN DVPRM
    //for image upload service written in php
    //public String phpIP = "http://192.168.1.4:80";
    public String phpIP = BASEIPROUTE+":80";



    //User-Profiling
    public String pythonIpAddress = BASEIPROUTE+":99";

    public String pythonIpAddressGetDistance = BASEIPROUTE+":95";
    public String pythonIpAddressGetEstimateFuel = BASEIPROUTE+":96";


}

