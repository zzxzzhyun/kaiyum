package com.example.kaiyum.data.model;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private int name;

    @SerializedName("body")
    private String body;

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
    @Override
    public String toString() {
        return "Result{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
