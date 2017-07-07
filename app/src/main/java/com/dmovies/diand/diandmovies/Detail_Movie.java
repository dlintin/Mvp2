package com.dmovies.diand.diandmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.android.popmovies.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import data.MovieContract;
import data.MovieDbhelper;
import model.Movie;
import utilities.NetworkUtils;

import static data.MovieContract.MovieEntry.ID;

/**
 * Created by USER on 27/05/2017.
 */

public class Detail_Movie extends AppCompatActivity {


    private SQLiteDatabase mDb;
    private RecyclerAdapterTrailer adapter;
    final FragmentActivity c = this;
    private String id;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    Movie movie;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        MovieDbhelper dbhelper = new MovieDbhelper(this);
        mDb = dbhelper.getWritableDatabase();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("movie")) {
                movie = (Movie) intent.getSerializableExtra("movie");
            }
        }


        Log.d("id", "" + intent.getLongExtra("id", 0));
        id = Long.toString(movie.id);
        Log.d("id", "" + id);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewtrailer);
        layoutManager = new LinearLayoutManager(c);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        TextView title = (TextView) findViewById(R.id.title);
        TextView year = (TextView) findViewById(R.id.year);
        TextView duration = (TextView) findViewById(R.id.durasi);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView deskripsi = (TextView) findViewById(R.id.deskripsi);
        ImageButton button = (ImageButton) findViewById(R.id.button);


        ImageView poster = (ImageView) findViewById(R.id.poster);
        title.setText(movie.original_title);
        Glide.with(this).load("http://image.tmdb.org/t/p/w500/" + movie.poster_image).into(poster);
        duration.setText(movie.original_language);
        year.setText(movie.release_date);
        rating.setText(movie.vote_average);
        deskripsi.setText(movie.overview);
        SubRequestData(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, movie.id);
                contentValues.put(MovieContract.MovieEntry.ORIGINAL_TITLE, movie.original_title);
                contentValues.put(MovieContract.MovieEntry.RELEASE, movie.release_date);
                contentValues.put(MovieContract.MovieEntry.VOTE, movie.vote_average);
                contentValues.put(MovieContract.MovieEntry.OVERVIEW, movie.overview);
                contentValues.put(MovieContract.MovieEntry.POSTER, movie.poster_image);
                contentValues.put(MovieContract.MovieEntry.original_language, movie.original_language);

                mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                Toast.makeText(Detail_Movie.this, "Added to Favorite!",
                        Toast.LENGTH_LONG).show();
            }
        });

        URL videosearchurl = NetworkUtils.buildtrailerURL(String.valueOf(movie.id), "videos");
        URL reviewsearchurl = NetworkUtils.buildtrailerURL(String.valueOf(movie.id), "reviews");
        new Detail_Movie.ReviewQueryTask().execute(videosearchurl);
        new Detail_Movie.MovieQueryTask().execute(reviewsearchurl);
    }

        class ReviewQueryTask extends AsyncTask<URL,Void,Void> {
            private String TAG = Detail_Movie.class.getSimpleName();

            @Override
            protected Void doInBackground(URL... params) {
                try {
                    Log.d(TAG, NetworkUtils.getResponseFromHttpUrl(params[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

    class MovieQueryTask extends AsyncTask<URL,Void,Void> {
        private String TAG = Detail_Movie.class.getSimpleName();

        @Override
        protected Void doInBackground(URL... params) {
            try {
                Log.d(TAG, NetworkUtils.getResponseFromHttpUrl(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void SubRequestData(String i){
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "";
        url = "http://api.themoviedb.org/3/movie/" + i + "/videos?api_key=230c0ee4f52d5c9fb13d5c01a91b24cd";
        Log.d("URL FINAL", "" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                Log.d("TRAILER", "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ObjectTrailer a = mGson.fromJson(response, ObjectTrailer.class);
                adapter = new RecyclerAdapterTrailer(c, a.results);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TRAILER", "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

}

