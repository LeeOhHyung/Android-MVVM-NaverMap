package com.example.geom_databinding.net

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MapApi {

    @POST("current")
    fun getPoints(@Body body: MapRequestBody): Call<JsonObject>
}
