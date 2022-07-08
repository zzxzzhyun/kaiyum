package com.example.kaiyum;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

public class KaiYum extends Application {
    private static KaiYum instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, String.valueOf(R.string.kakao_native_key));
    }


}
