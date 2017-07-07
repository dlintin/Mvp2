/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dmovies.diand.diandmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import model.Movie;


public class Main_Adapter extends RecyclerView.Adapter<Main_Adapter.NumberViewHolder> {

    private JSONArray data;
    private Context context;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(Movie clickedItemIndex);
    }

    public Main_Adapter(Context context, JSONArray JSONdata, ListItemClickListener listener) {
        this.context=context;
        data = JSONdata;
        mOnClickListener = listener;
    }
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView moviesposter;
        TextView original_title;
        public String id;

        public NumberViewHolder(View itemView) {
            super(itemView);
            moviesposter = (ImageView) itemView.findViewById(R.id.tv_item_number);
            original_title = (TextView) itemView.findViewById(R.id.original_title);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            try{
                Picasso.with(context).load("https://image.tmdb.org/t/p/w154" +data.getJSONObject(position).getString("poster_path"))
                        .placeholder(R.drawable.logo_item)
                        .into(moviesposter);
                moviesposter.setScaleType(ImageView.ScaleType.FIT_XY);
                original_title.setText(data.getJSONObject(getAdapterPosition()).getString("original_title"));

            }catch(JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onClick(View v) {
            try {
                Movie movie = new Movie(
                        data.getJSONObject(getAdapterPosition()).getLong("id"),
                        data.getJSONObject(getAdapterPosition()).getString("original_title"),
                        data.getJSONObject(getAdapterPosition()).getString("poster_path"),
                        data.getJSONObject(getAdapterPosition()).getString("original_language"),
                        data.getJSONObject(getAdapterPosition()).getString("overview"),
                        data.getJSONObject(getAdapterPosition()).getString("vote_average"),
                        data.getJSONObject(getAdapterPosition()).getString("release_date")
                );
                mOnClickListener.onListItemClick(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_main_recycle;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
//        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

@Override
    public int getItemCount(){
    return data.length();
}

}
