package com.example.geom_databinding.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.geom_databinding.R;
import com.example.geom_databinding.databinding.ActivityMainBinding;
import com.example.geom_databinding.recyclerview.RecyclerViewActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders;
import com.example.geom_databinding.base.BaseActivity
import com.naver.maps.map.*

class MainActivity : BaseActivity(), OnMapReadyCallback {

    private val TAG = javaClass.name
    companion object {
        const val ClientID = "YOUR_CLIENT_ID"
    }

    // 지도 관련
    private lateinit var mMap: NaverMap
    private lateinit var mapFragment: MapFragment
    private lateinit var options: NaverMapOptions

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityMainBinding

    override fun before() {
        setupBindings()
        setupMapFragment()
    }

    override fun setupObserving() {

    }

    override fun after() {
    }

    override fun initToolbar() {
    }

    private fun setupBindings(){
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mViewModel.init()

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewmodel = mViewModel
        mBinding.lifecycleOwner = this
    }

    // 네이버 지도 Fragment설정
    private fun setupMapFragment(){
        /** 네이버 지도 API 에서 클라이언트ID값을 입력해야한다.
         *  Application 에서 MAPS가 선택되어 있지 않거나, 사용량 초과한 경우 429에러 발생
         **/
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(ClientID)

        /** XML의 MapFragment 연결 **/
        mapFragment = supportFragmentManager.findFragmentById(R.id.main_map_view) as MapFragment?
                ?: MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().add(R.id.main_map_view, it).commit()
                }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(@NonNull naverMap: NaverMap) {
        naverMap.moveCamera(CameraUpdate.toCameraPosition(CameraPosition(LatLng(35.231574, 129.084433), 12.0)))
        mMap = naverMap
        mViewModel.init(mMap)

        mMap.addOnCameraIdleListener{
            setupDataOnMap(mMap)
        }

        mMap.addOnCameraChangeListener { reason, animated ->
            // reason : reason camera changing
            // animated : is animated when camera changed
        }
    }

    private fun setupDataOnMap(@NonNull naverMap: NaverMap){

        val zoom = naverMap.cameraPosition.zoom

        mViewModel.getMarkers(zoom).observe(this, Observer {
            if(!it.isNullOrEmpty()){
                for(i in it){
                    i.map = naverMap
                }
            } else {
                mapFragment.onResume()
            }
        })
    }

    //옵션메뉴
    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        if(item.itemId == R.id.menu_recyclerview){
            startRecyclerActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // 액티비티 전환 // 메인 -> 리사이클러
    private fun startRecyclerActivity(){
        val intent = Intent(this, RecyclerViewActivity::class.java)
        intent.putExtra("LIST", mViewModel.points.value as Serializable)
        startActivity(intent)
    }

}