/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 1:40 PM
 *
 */

package com.example.plusgo.DVPRM.model;

public class copassenger {

    private String userId;
    private String name;
    private String Description;
    private String rating;
    private String image_url;

    public copassenger() {
    }

    public copassenger(String userId, String name, String description, String rating, String image_url) {
        this.userId = userId;
        this.name = name;
        Description = description;
        this.rating = rating;
        this.image_url = image_url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public String getRating() {
        return rating;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
