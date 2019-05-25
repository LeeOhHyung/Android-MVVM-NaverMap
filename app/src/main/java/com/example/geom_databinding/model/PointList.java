package com.example.geom_databinding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointList {

    @SerializedName("list")
    @Expose
    private List<Point> list;

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }
}
