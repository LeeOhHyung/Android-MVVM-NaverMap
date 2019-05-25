package com.example.geom_databinding.model;

import com.google.gson.annotations.SerializedName;
import com.naver.maps.geometry.LatLng;

import java.io.Serializable;

/**
 * 직렬화 할 마커 클래스
 * /current 사용함
 * */
public class Point implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("dist")
    private double dist;

    @SerializedName("g")
    private g g;

    private int position;

    public int getId() {
        return id;
    }

    public double getDist() {
        return dist;
    }

    public Point.g getG() {
        return g;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }

    public class g implements Serializable{
        @SerializedName("y")
        private double lat;

        @SerializedName("x")
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
