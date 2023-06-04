/*
 *   Created by Abhinav Pandey on 06/05/23, 1:52 AM
 */

package com.example.scrappy;

import android.net.Uri;

import java.util.List;

public class model {

    private String postId;
    private String userId;
    private String caption;
    private double latitude;
    private double longitude;
    private String address;
    private long timestamp;
    private List<String> imageUris;

    public model(String postId, String userId, String caption, double latitude, double longitude, String address, long timestamp, List<String> imageUris) {
        this.postId = postId;
        this.userId = userId;
        this.caption = caption;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.timestamp = timestamp;
        this.imageUris = imageUris;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUris = imageUris;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public model() {
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCaption() {
        return caption;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<String> getImageUrl() {
        return imageUris;
    }

}
