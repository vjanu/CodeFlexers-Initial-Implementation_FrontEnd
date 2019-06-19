
package com.example.plusgo;

public class BaseContent {

    final public String BASEIPROUTE = "http://192.168.8.102";

    //IP ADDRESS OF THE NODEJS BACKEND
    public String IpAddress = BASEIPROUTE+":8083";

    //for image upload service written in php
//    public String phpIP = "http://192.168.1.4:8080";
    public String phpIP = BASEIPROUTE+":8080";

//    public String pythonIpAddress = "http://192.168.1.4:99";
    public String pythonIpAddress = BASEIPROUTE+":99";

}

