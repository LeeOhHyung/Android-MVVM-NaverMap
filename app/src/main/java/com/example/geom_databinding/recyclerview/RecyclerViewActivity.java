package com.example.geom_databinding.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.geom_databinding.R;
import com.example.geom_databinding.databinding.ActivityRecyclerviewBinding;
import com.example.geom_databinding.main.MainActivity;
import com.example.geom_databinding.model.Point;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.Flowable;

public class RecyclerViewActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    // ViewModel, DataBinding
    private RecyclerViewModel recyclerViewModel;
    private ActivityRecyclerviewBinding recyclerviewBinding;

    // List
    private List<Point> points;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setupActionBar(actionBar); // 뒤로가기 버튼 설정
        setupRecyclerViewBindings(); // 데이터바인딩 셋팅(초기화)
        setupDataOnRecyclerView();  // 리싸이클러뷰 셋팅
    }

    private void setupActionBar(ActionBar actionBar){
        if(actionBar == null){
            actionBar = getSupportActionBar();
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case android.R.id.home :
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupRecyclerViewBindings(){
        recyclerViewModel = ViewModelProviders.of(this).get(RecyclerViewModel.class);
        recyclerViewModel.init();

        // 데이터바인딩
        recyclerviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        recyclerviewBinding.setViewmodel(recyclerViewModel);
        recyclerviewBinding.setLifecycleOwner(this);
    }

    private void setupDataOnRecyclerView(){
        Intent intent = getIntent();
        points = (List<Point>) intent.getSerializableExtra("LIST");

        Log.d(TAG, "points.size() : " + points.size());

        if(points.size() > 0){

            // 어답터에 리스트 연결
            recyclerViewModel.setRecyclerViewAdapter(points);
        }

        setupListClick();
    }

    private void setupListClick(){
        recyclerViewModel.getSelected().observe(this, new Observer<Point>() {
            @Override
            public void onChanged(Point point) {
                if(point != null){
                    Toast.makeText(RecyclerViewActivity.this, "position : " + point.getPosition(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
