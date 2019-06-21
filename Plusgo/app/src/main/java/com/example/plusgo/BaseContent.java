
package com.example.plusgo;

public class BaseContent {

    //TODO : ADD THE IP OF SERVER HERE
    final public String BASEIPROUTE = "http://10.98.200.109";

    //IP ADDRESS OF THE NODEJS BACKEND
    public String IpAddress = BASEIPROUTE+":8083";

    //for image upload service written in php
//    public String phpIP = "http://192.168.1.4:8080";
    public String phpIP = BASEIPROUTE+":8080";

//    public String pythonIpAddress = "http://192.168.1.4:99";
    public String pythonIpAddress = BASEIPROUTE+":99";//TODO : INSERT WHOS COMPONENT IS RELATED TO THIS PORT
    public String pythonIpAddressGetDistance = BASEIPROUTE+":95";
    public String pythonIpAddressGetEstimateFuel = BASEIPROUTE+":96";

}

