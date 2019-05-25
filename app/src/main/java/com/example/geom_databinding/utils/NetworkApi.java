package com.example.geom_databinding.utils;

import com.example.geom_databinding.net.MapApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi {

    private final String TAG = getClass().getSimpleName();

    private static MapApi mapApi;
    private static final String BASE_URL = "YOUR_URL_ADDRESS";

    public static MapApi getApi(){
        if(mapApi != null){

//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//
//            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(logging)
//                    .build();
//
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(
//                            Point.class,
//                            new JsonPointDeserializer())
//                    .create();
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mapApi = retrofit.create(MapApi.class);
        }

        return mapApi;
    }
}
