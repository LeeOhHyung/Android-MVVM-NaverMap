package com.example.geom_databinding.net;

import com.example.geom_databinding.model.Point;

import java.util.List;

public class MapResponse {

    //@SerializedName("marker")
    private List<Point> PointList;

    public List<Point> getPointList(){
        return this.PointList;
    }
}
