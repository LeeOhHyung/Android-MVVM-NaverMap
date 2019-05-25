package com.example.geom_databinding.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class ServiceGenerator {

    public final String TAG = getClass().getSimpleName();

    public static final String API_BASE_URL = "YOUR_URL_ADDRESS";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String AutoToken){

        if(AutoToken != null){

            httpClient.interceptors().add(new Interceptor() {

                @EverythingIsNonNull
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // 요청에 요청헤더를 추가함.
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", AutoToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }
}
