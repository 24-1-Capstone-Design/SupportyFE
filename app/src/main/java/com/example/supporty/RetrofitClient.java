package com.example.supporty;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import android.util.Log;

public class RetrofitClient {
    // 시영 GCP 외부 IP 주소
    private static final String BASE_URL = "http://34.47.84.187:80";

    // 유진 GCP 외부 IP 주소
   //private static final String BASE_URL = "http://34.64.220.129:80";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
                    .readTimeout(30, TimeUnit.SECONDS)    // 읽기 타임아웃 설정
                    .writeTimeout(30, TimeUnit.SECONDS)   // 쓰기 타임아웃 설정
                    .build();

            Log.d("RetrofitClient", "Retrofit 클라이언트 초기화");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // OkHttpClient 설정
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
