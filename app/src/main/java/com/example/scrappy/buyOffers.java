/*
 *   Created by Abhinav Pandey on 03/06/23, 3:44 PM
 */

package com.example.scrappy;

public class buyOffers {

    private String notificationId;
    private String buyerId;
    private String sellerId;
    private String postId;
    private String buyerName;
    private long timestamp;

    public buyOffers(String notificationId, String buyerId, String sellerId, String postId, String buyerName, long timestamp) {
        this.notificationId = notificationId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.postId = postId;
        this.buyerName = buyerName;
        this.timestamp = timestamp;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
