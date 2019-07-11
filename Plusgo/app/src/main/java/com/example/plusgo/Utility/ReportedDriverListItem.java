/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 1:29 PM
 *
 */

package com.example.plusgo.Utility;

public class ReportedDriverListItem {
    private String uid;
    private String duid;
    private String name;
    private String image;
    private String amt;
    private long count;

    public String getDuid() {
        return duid;
    }

    public void setDuid(String duid) {
        this.duid = duid;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ReportedDriverListItem(String uid, String name, String amt, long count, String image, String duid) {
        this.uid = uid;
        this.name = name;
        this.amt = amt;
        this.count = count;
        this.image = image;
        this.duid= duid;
    }



}
