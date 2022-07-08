package com.example.kaiyum.data.model;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {
    private OkHttpClient okClient;
    private static HttpConnection instance = new HttpConnection();
    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection(){
        this.okClient = new OkHttpClient();
    }


    // 웹서버로 요청
    public void requestWebServer(String name, Callback callback) {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:80/campus?name=" + name)
                .get()
                .build();
        okClient.newCall(request).enqueue(callback);
    }
}
