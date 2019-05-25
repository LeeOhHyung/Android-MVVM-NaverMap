package com.example.geom_databinding.net;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MapApi {

//    @FormUrlEncoded
//    @POST("current")
//    Call<JsonObject> getPoints(
//
//            @Field("lng") double lng,           // 카메라 경도
//            @Field("lat") double lat,           // 카메라 위도
//
//            @Field("swlng") double swlng,       // 남서
//            @Field("swlat") double swlat,
//
//            @Field("selng") double selng,       // 남동
//            @Field("selat") double selat,
//
//            @Field("nelng") double nelng,       // 북동
//            @Field("nelat") double nelat,
//
//            @Field("nwlng") double nwlng,       // 북서
//            @Field("nwlat") double nwlat
//    );

    @POST("current")
    Call<JsonObject> getPoints(@Body MapRequestBody body);
}
