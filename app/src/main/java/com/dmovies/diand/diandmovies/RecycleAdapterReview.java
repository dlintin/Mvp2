package com.dmovies.diand.diandmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popmovies.R;

import java.util.List;

public class RecycleAdapterReview extends RecyclerView.Adapter<RecycleHolderReview> {
    public List<ObjectReview.DataReview> itemList;
    public Context context;

    public RecycleAdapterReview(Context context, List<ObjectReview.DataReview> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecycleHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, null);
        return new RecycleHolderReview(layoutView);

    }

    @Override
    public void onBindViewHolder(RecycleHolderReview holder, int position) {
        // ObjectTrailer.DataReview item = itemList.get(position);
        holder.author.setText(itemList.get(position).author);
        holder.content.setText(itemList.get(position).content);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}