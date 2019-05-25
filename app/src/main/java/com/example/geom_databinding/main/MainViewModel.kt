package com.example.geom_databinding.main


import android.util.Log

import com.example.geom_databinding.base.BaseViewModel
import com.example.geom_databinding.model.Point
import com.example.geom_databinding.model.PointList
import com.example.geom_databinding.net.MapFactory
import com.example.geom_databinding.net.MapRequestBody
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

import java.util.ArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : BaseViewModel() {

    private val TAG = javaClass.simpleName

    private var mMap: NaverMap? = null
    private var mapFactory: MapFactory? = null

    private val markers = MutableLiveData<List<Marker>>()
    val points = MutableLiveData<List<Point>>()

    private val tempMarkerList = ArrayList<Marker>()

    override fun init() {}

    fun init(mMap: NaverMap) {
        this.mMap = mMap
        this.mapFactory = MapFactory()
    }

    //MainActivity에서 observe 중
    fun getMarkers(zoom: Double?): LiveData<List<Marker>> {

        setMarkers(zoom)
        return this.markers
    }

    fun setMarkers(zoom: Double?) {
        for (i in tempMarkerList) {
            i.map = null
        }
        tempMarkerList.clear()

        fetchList(tempMarkerList, markers, zoom)
    }

    // MainActivity is observing ths markers
    private fun fetchList(markers: ArrayList<Marker>, liveDataMarkers: MutableLiveData<List<Marker>>?, zoom: Double?) {

        val api = mapFactory!!.create()

        // Make Callback Method
        val req = api.getPoints(MapRequestBody(

                // 카메라 경도, 위도
                mMap!!.cameraPosition.target.longitude,
                mMap!!.cameraPosition.target.latitude,

                mMap!!.contentBounds.southWest.longitude, // 뷰포인트 남서
                mMap!!.contentBounds.southWest.latitude,

                mMap!!.contentBounds.southEast.longitude, // 뷰포인트 남동
                mMap!!.contentBounds.southEast.latitude,

                mMap!!.contentBounds.northEast.longitude, // 뷰포인트 북동
                mMap!!.contentBounds.northEast.latitude,

                mMap!!.contentBounds.northWest.longitude, // 뷰포인트 북서
                mMap!!.contentBounds.northWest.latitude
        ))

        // insert callback queue
        req.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, res: Response<JsonObject>) {
                if (res.isSuccessful) {
                    Log.d(TAG, "result : " + res.body()!!)
                    val list = Gson().fromJson(res.body(), PointList::class.java)  // 파싱


                    for (i in 0 until list.list.size) {
                        val PointAt = list.list[i]
                        val marker = Marker(LatLng(PointAt.g.lat, PointAt.g.lng))
                        marker.icon = MarkerIcons.YELLOW
                        marker.captionText = PointAt.id.toString()
                        marker.captionTextSize = 14f
                        marker.width = zoom!!.toInt() * 6
                        marker.height = zoom.toInt() * 8

                        markers.add(marker)
                    }

                    points!!.value = list.list // RecyclerView에서 사용할 List<Point>
                    liveDataMarkers!!.setValue(markers)

                } else {
                    Log.d(TAG, "result : 실패")
                    Log.d(TAG, "result.res.body() : " + res.body()!!)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.e(TAG, "result : 에러 발생")
                t.printStackTrace()
                t.message
            }
        })
    }

    fun getPoints(): LiveData<List<Point>> {
        return this.points
    }
}
