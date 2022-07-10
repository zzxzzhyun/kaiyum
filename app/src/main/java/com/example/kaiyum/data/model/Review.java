package com.example.kaiyum.data.model;

public class Review {
    private String userName;
    private int score;
    private String text;

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

    @Override
    public String toString() {
        return "Review{" +
                "userName='" + userName + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }
}
