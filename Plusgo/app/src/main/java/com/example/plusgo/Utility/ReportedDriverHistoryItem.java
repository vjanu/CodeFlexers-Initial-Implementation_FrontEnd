/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 1:29 PM
 *
 */

package com.example.plusgo.Utility;

public class ReportedDriverHistoryItem {
    private String source;
    private String destination;
    private String price;
    private String image;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateT() {
        return dateT;
    }

    public void setDateT(String dateT) {
        this.dateT = dateT;
    }

    private String dateT;

    public ReportedDriverHistoryItem(String source, String destination, String price,  String dateT) {
        this.source = source;
        this.destination = destination;
        this.price = price;
//        this.image = image;
        this.dateT = dateT;
    }
}
