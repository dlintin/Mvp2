package com.dmovies.diand.diandmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import com.dmovies.diand.diandmovies.data.MovieContract;
import com.dmovies.diand.diandmovies.data.MovieDbhelper;
import com.dmovies.diand.diandmovies.model.Movie;
import com.dmovies.diand.diandmovies.utilities.NetworkUtils;

import static com.dmovies.diand.diandmovies.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.dmovies.diand.diandmovies.data.MovieContract.MovieEntry.ID;

/**
 * Created by USER on 27/05/2017.
 */

public class Detail_Movie extends AppCompatActivity {

    private String data;
    private SQLiteDatabase mDb;
    private RecyclerAdapterTrailer adapter;
    private RecycleAdapterReview adapterreview;
    final FragmentActivity c = this;
    private String id;
    private RecyclerView recyclerView,recyclerViewReview;
    private LinearLayoutManager layoutManager,xlayoutmanager;
    Movie movie;
    String author,content;
    TextView judul_reviews, content_reviews;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        MovieDbhelper dbhelper = new MovieDbhelper(this);
        mDb = dbhelper.getWritableDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewtrailer);
        recyclerViewReview = (RecyclerView) findViewById(R.id.eviewxxxX);

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

        layoutManager = new LinearLayoutManager(c);
        xlayoutmanager = new LinearLayoutManager(c);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewReview.setHasFixedSize(true);
        recyclerViewReview.setLayoutManager(xlayoutmanager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewReview.setNestedScrollingEnabled(false);
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
        loadreview(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adordelete();

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

    private void loadreview(String i){
        RequestQueue review = Volley.newRequestQueue(this.getApplicationContext());
        String urlx = "";
        urlx = "http://api.themoviedb.org/3/movie/"+ i +"/reviews?api_key=230c0ee4f52d5c9fb13d5c01a91b24cd";
        Log.d("URL FINAL", "" + urlx);
        StringRequest ReviewRequest = new StringRequest(Request.Method.GET, urlx, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                Log.d("TRAILER", "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ObjectReview a = mGson.fromJson(response, ObjectReview.class);
                adapterreview = new RecycleAdapterReview(c, a.results);
                recyclerViewReview.setAdapter(adapterreview);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TRAILER", "Error " + error.getMessage());
            }
        });
        review.add(ReviewRequest);
    }

    private void adordelete(){
        String currentMovieId = String.valueOf(movie.id);
        String whereClause = MovieContract.MovieEntry.ID + " = ?";
        String[] whereArgs = new String[]{currentMovieId};
        Cursor c = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, whereClause, whereArgs, null);

        Log.d("CURSOR", "Response " + c);
        if (c.getCount() == 0) {

            // row does not exist, you can insert or do sth else
            addMovie();
            Toast.makeText(Detail_Movie.this, "Added to Favorite!",
                    Toast.LENGTH_LONG).show();


        } else {
            // already inserted, update this row or do sth else
            removeMovie();
            Toast.makeText(Detail_Movie.this, "Favorite Deleted! Refresh Favorite",
                    Toast.LENGTH_LONG).show();

        }
        assert c != null;
        c.close();
    }

    /**
     * Remove Movie from favorites db and delete image from file directory
     */
    private void removeMovie() {
        String currentMovieId = String.valueOf(movie.id);
        String whereClause = MovieContract.MovieEntry.ID + " = ?";
        String[] whereArgs = new String[]{currentMovieId};
        this.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, whereClause, whereArgs);


        File photofile = new File(this.getFilesDir(), currentMovieId);
        if (photofile.exists()) {
            photofile.delete();
        }
    }



    /**
     * Add Movie to favorite db and write image to file
     */
    private void addMovie() {
        /* Add Movie to ContentProvider */
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.ID, movie.id);
        values.put(MovieContract.MovieEntry.ORIGINAL_TITLE, movie.original_title);
        values.put(MovieContract.MovieEntry.RELEASE, movie.release_date);
        values.put(MovieContract.MovieEntry.VOTE, movie.vote_average);
        values.put(MovieContract.MovieEntry.OVERVIEW, movie.overview);
        values.put(MovieContract.MovieEntry.POSTER, movie.poster_image);
        values.put(MovieContract.MovieEntry.original_language, movie.original_language);
        Uri insertedMovieUri = this.getContentResolver().
                insert(MovieContract.MovieEntry.CONTENT_URI, values);

        /* Write the file to disk */
        String filename = String.valueOf(id);
        FileOutputStream outputStream;
        try {
            outputStream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

