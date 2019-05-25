package com.example.geom_databinding.recyclerview;

import android.util.ArrayMap;

import com.example.geom_databinding.R;
import com.example.geom_databinding.model.Point;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecyclerViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();

    // 어답터
    private RecyclerViewAdapter adapter;
    private MutableLiveData<Point> selected;

    // 사진
    private ObservableArrayMap<Integer, Integer> images;

    public void init(){
        this.adapter = new RecyclerViewAdapter(R.layout.recyclerview_carditem, this);
        this.selected = new MutableLiveData<>();
        this.images = new ObservableArrayMap<>();
    }

    public RecyclerViewAdapter getAdapter(){
        return this.adapter;
    }

    public ObservableArrayMap<Integer, Integer> getImages(){
        return this.images;
    }

    public void setRecyclerViewAdapter(List<Point> list){

        if(list == null || list.size() == 0){
            list = new ArrayList<>();
        }

        this.adapter.setPoints(list);
        this.adapter.notifyDataSetChanged();

    }

    // 리스트 row를 클릭하면 나오는 토스트 메세지
    public void onItemClick(int index){
        Point db = getPointAt(index);
        db.setPosition(index);

        selected.setValue(db);
    }

    public Point getPointAt(Integer position){
        if(adapter.getItemCount() > position && adapter.getPoints() != null){
            return adapter.getPointAtPosition(position);
        }

        return null;
    }

    public String setCardViewIdText(Integer id){
        return ("id : " + id);
    }

    public String setCardViewDistText(Double dist){
        return "거리 : " + String.format("%.2f", dist) + "km";
    }

    public MutableLiveData<Point> getSelected(){
        return this.selected;
    }

    // images에 사진 넣음
    public void setImages(Integer position){

        int random = (int)(Math.random()*100) + 1;
        int[] sampleImages = {
                R.drawable.img2, R.drawable.img2, R.drawable.img3, R.drawable.img4,
                R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8
        }; // 8개

        if(adapter.getPoints() != null && images != null){
            List<Point> points = adapter.getPoints();
            if(points.size() > 0){
                images.put(points.get(position).getId(), sampleImages[random % (sampleImages.length)]);
            }
        }
    }
}
