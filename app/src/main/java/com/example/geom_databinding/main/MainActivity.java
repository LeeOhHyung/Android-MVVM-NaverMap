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
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private final String ClientID = "YOUR_CLIENT_ID";

    // 지도 관련
    private NaverMap mMap;
    private MapFragment mapFragment;
    private NaverMapOptions options;

    // ViewModel, DataBinding
    private MainViewModel mainViewModel;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터바인딩 셋팅(초기화)
        setupBindings();
    }

    private void setupBindings(){

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // 데이터바인딩
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setViewmodel(mainViewModel);
        mBinding.setLifecycleOwner(this);

        // 지도 MapFragment 셋팅(초기화)
        setupMapFragment();
    }

    // 네이버 지도 Fragment설정
    public void setupMapFragment(){
        /** 네이버 지도 API 에서 클라이언트ID값을 입력해야한다.
         *  Application 에서 MAPS가 선택되어 있지 않거나, 사용량 초과한 경우 429에러 발생
         **/
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient(ClientID)
        );

        try {

            /** 초기 위치 설정 **/
            options = new NaverMapOptions()
                    .camera(new CameraPosition(new LatLng(35.231574, 129.084433), 12));


            /** XML의 MapFragment 연결 **/
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_view);

            // MapFragment is reassigned whatever null or not
            mapFragment = MapFragment.newInstance(options);
            getSupportFragmentManager().beginTransaction().add(R.id.main_map_view, mapFragment).commit();

            assert mapFragment != null;
            mapFragment.getMapAsync(this);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "에러 발생");
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        mMap = naverMap;
        mainViewModel.init(mMap);

        mMap.addOnCameraIdleListener(()->{

            // 마커를 지도에 셋팅
            setupDataOnMap(mMap);
        });

        mMap.addOnCameraChangeListener((reason, animated)->{

            // reason : 움직인 이유
            // animated : 카메라가 움직일때 애니메이션이 있었는가.
        });

    }

    public void setupDataOnMap(@NonNull NaverMap naverMap){

        if(mainViewModel != null){
            int zoom = (int) naverMap.getCameraPosition().zoom;

            /** ViewModel에 있는 MutableLiveData<Marker> 를 받아옴. **/
            mainViewModel.getMarkers(zoom).observe(this, (List<Marker> markers) -> {

                // Marker 리스트가 null이 아니거나, 비어있지 않다면
                if(markers != null && !markers.isEmpty()){
                    Log.d(TAG, "마커의 개수 : " + markers.size());

                    // 지도에 마커 설정하는 부분 만들어주면 됨.
                    for(Marker i : markers){
                        i.setMap(naverMap);
                    }
                }

                else {
                    mapFragment.onResume();
                }
            });
        }
    }

    //옵션메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // 메뉴 클릭했을때 실행되는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_recyclerview){
            StartRecyclerActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 액티비티 전환 // 메인 -> 리사이클러
    private void StartRecyclerActivity(){
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        intent.putExtra("LIST", (Serializable) mainViewModel.getPoints().getValue());

        startActivity(intent);
    }

}