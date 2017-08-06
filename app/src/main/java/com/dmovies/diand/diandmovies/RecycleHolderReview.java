package com.dmovies.diand.diandmovies;

/**
 * Created by diand on 06-Aug-17.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popmovies.R;

public class RecycleHolderReview extends RecyclerView.ViewHolder{
    public TextView author;
    public TextView content;


    public RecycleHolderReview(View itemView) {
        super(itemView);
        author = (TextView)itemView.findViewById(R.id.author);
        content = (TextView)itemView.findViewById(R.id.content);
    }

}