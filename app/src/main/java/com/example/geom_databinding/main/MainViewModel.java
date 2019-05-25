package com.example.geom_databinding.main;


import android.util.Log;

import com.example.geom_databinding.model.Point;
import com.example.geom_databinding.model.PointList;
import com.example.geom_databinding.net.MapApi;
import com.example.geom_databinding.net.MapFactory;
import com.example.geom_databinding.net.MapRequestBody;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();

    // 지도
    private NaverMap mMap;
    private MapFactory mapFactory;

    private MutableLiveData<List<Marker>> markers;  // 관찰하고 있는 마커 리스트
    private MutableLiveData<List<Point>> points;

    private ArrayList<Marker> tempMarkerList = new ArrayList<>();

    public void init(NaverMap mMap) {
        this.mMap = mMap;
        this.markers = new MutableLiveData<>();
        this.points = new MutableLiveData<>();
        this.mapFactory = new MapFactory();
    }

    //MainActivity에서 observe 중
    public LiveData<List<Marker>> getMarkers(int zoom){

        setMarkers(zoom);
        return this.markers;
    }

    public void setMarkers(int zoom){
        for(Marker i : tempMarkerList){
            i.setMap(null);
        }
        tempMarkerList.clear();

        FetchList(tempMarkerList, markers, zoom);
    }

    // this.markers -> 메인액티비티에서 observe하고 있는 리스트
    public void FetchList(ArrayList<Marker> markers, MutableLiveData<List<Marker>> liveDataMarkers, int zoom) {

        MapApi Api = mapFactory.create();

        // 콜백 생성
        Call<JsonObject> req = Api.getPoints(new MapRequestBody(

                // 카메라 경도, 위도
                mMap.getCameraPosition().target.longitude,
                mMap.getCameraPosition().target.latitude,

                mMap.getContentBounds().getSouthWest().longitude,    // 뷰포인트 남서
                mMap.getContentBounds().getSouthWest().latitude,

                mMap.getContentBounds().getSouthEast().longitude,    // 뷰포인트 남동
                mMap.getContentBounds().getSouthEast().latitude,

                mMap.getContentBounds().getNorthEast().longitude,    // 뷰포인트 북동
                mMap.getContentBounds().getNorthEast().latitude,

                mMap.getContentBounds().getNorthWest().longitude,    // 뷰포인트 북서
                mMap.getContentBounds().getNorthWest().latitude
        ));

        // 콜백 Queue에 넣는다
        req.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> res) {

                // 응답이 정상적으로 왔을때.
                // markers -> 관찰하고 있는 리스트
                if(res.isSuccessful()){
                    Log.d(TAG, "result : " + res.body());
                    PointList list = new Gson().fromJson(res.body(), PointList.class);  // 파싱

                    for(int i = 0; i < list.getList().size(); ++i){
                        Point PointAt = list.getList().get(i);
                        Marker marker = new Marker(new LatLng(
                                PointAt.getG().getLat(),
                                PointAt.getG().getLng()));
                        marker.setIcon(MarkerIcons.YELLOW);
                        marker.setCaptionText(String.valueOf(PointAt.getId()));
                        marker.setCaptionTextSize(14);
                        marker.setWidth(zoom * 6);
                        marker.setHeight(zoom * 8);

                        markers.add(marker);
                    }

                    points.setValue(list.getList()); // RecyclerView에서 사용할 List<Point>
                    liveDataMarkers.setValue(markers);

                }

                else {
                    Log.d(TAG, "result : 실패");
                    Log.d(TAG, "result.res.body() : " + res.body());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e(TAG, "result : 에러 발생");
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    public LiveData<List<Point>> getPoints(){

        if(this.points == null){
            this.points = new MutableLiveData<>();
        }

        return this.points;
    }
}
