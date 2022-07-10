package com.example.kaiyum.data.model;

public class Review {
    private long unid;
    private int rid;
    private String userName;
    private int score;
    private String text;
    private String bitmap;
    private String imgUrl;

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

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Review{" +
                "unid=" + unid +
                ", rid=" + rid +
                ", userName='" + userName + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
