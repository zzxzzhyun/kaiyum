package com.example.kaiyum.data.model;

import java.sql.Date;

public class Review {
    private long unid;
    private int rid;
    private String userName;
    private String restaurantName;
    private int score;
    private String text;
    private String imgUrl;
    private String timestamp;

    public long getUnid() {
        return unid;
    }

    public void setUnid(long unid) {
        this.unid = unid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimestamp() {return timestamp;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Review{" +
                "unid=" + unid +
                ", rid=" + rid +
                ", userName='" + userName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
