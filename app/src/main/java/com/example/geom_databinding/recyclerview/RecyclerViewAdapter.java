package com.example.geom_databinding.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geom_databinding.R;
import com.example.geom_databinding.model.Point;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private int layoutID;
    private RecyclerViewModel recyclerViewModel;
    private List<Point> points;

    // 어답터 생성자
    public RecyclerViewAdapter(@LayoutRes int layoutID, RecyclerViewModel recyclerViewModel){
        this.layoutID = layoutID;
        this.recyclerViewModel = recyclerViewModel;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position){
        holder.bind(recyclerViewModel, position);
    }

    @Override
    public int getItemCount(){
        return points == null ? 0 : points.size();
    }

    @Override
    public int getItemViewType(int position){
        return getLayoutIdForPosition(position);
    }

    private int getLayoutIdForPosition(int position){
        return this.layoutID;
    }

    public void setPoints(List<Point> list){
        this.points = list;
    }

    public List<Point> getPoints(){
        return this.points;
    }

    public Point getPointAtPosition(int position){
        return this.points.get(position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        public RecyclerViewHolder(ViewDataBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RecyclerViewModel viewModel, Integer position){

            /** Very Important!! **/
            // re-usable ViewHolder
            viewModel.setImages(position);
            binding.setVariable(BR.viewmodel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }
    }
}

// reference : https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4
