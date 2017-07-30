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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import data.MovieContract;
import data.MovieDbhelper;
import model.Movie;
import utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements Main_Adapter.ListItemClickListener{


    private Main_Adapter mAdapter;
    private RecyclerView mNumbersList;
    private String data;
    private String search;
    private String TAG = MainActivity.class.getSimpleName();
    private SQLiteDatabase mDb;
    SharedPref sharedPref;
    JSONArray jsonItems;

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // User Session Manager
        sharedPref = new SharedPref(getApplicationContext());
        MovieDbhelper dbhelper = new MovieDbhelper(this);
        mDb = dbhelper.getWritableDatabase();

        // get user data from session
        HashMap<String, String> user = sharedPref.getUserDetails();

        // get name
        String name = user.get(SharedPref.KEY_ID);

        Log.d(TAG, "TERSIMPAN : "+ name);
        if (savedInstanceState!=null)
        {
            if(Objects.equals(name, "top_rated")){
                setTitle("Top Rated Movie");
                refresh_data_top("top_rated");
            }
            if(Objects.equals(name, "popular")){
                setTitle("Popular Movie");
                refresh_data_pop("popular");
            }
            if(Objects.equals(name, "favorite")){

                jsonItems = getFavorite();
                if(jsonItems.length()>0){
                    setTitle("Favorite");
                    sharedPref.createUserLoginSession("favorite");
                    refresh_local_data();
                }else{
                    Toast.makeText(this, "Favorite Is Empty", Toast.LENGTH_LONG).show();
                }


            }
        }

        else {
            if (isOnline()) {
                refresh_data("popular");
            }
        }



//        refresh_data("popular");
    }

    private JSONArray getFavorite() {
        Cursor cursor = mDb.query(MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        JSONArray data = new JSONArray();
        while (cursor.moveToNext()) {
            try {
                JSONObject JSONmovie = new JSONObject();
                JSONmovie.put("id", cursor.getString(cursor.getColumnIndex("id")));
                JSONmovie.put("original_title", cursor.getString(cursor.getColumnIndex("original_title")));
                JSONmovie.put("poster_path", cursor.getString(cursor.getColumnIndex("poster_path")));
                JSONmovie.put("vote_average", cursor.getString(cursor.getColumnIndex("vote_average")));
                JSONmovie.put("original_language", cursor.getString(cursor.getColumnIndex("original_language")));
                JSONmovie.put("release_date", cursor.getString(cursor.getColumnIndex("release_date")));
                JSONmovie.put("overview", cursor.getString(cursor.getColumnIndex("overview")));
                data.put(JSONmovie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, data.toString());
        return data;
    }

    private void refresh_local_data(){
        if (isOnline()) {

            jsonItems = getFavorite();
            if(jsonItems.length()>0){
                mAdapter = new Main_Adapter(this,jsonItems,this);
                Log.d(TAG, "MADAPTER : "+ mAdapter);
                Log.d(TAG, "JSON ADAPTER : "+ jsonItems);
                Log.d(TAG, "MNUMBERLIST: "+ mNumbersList);
                mNumbersList.setAdapter(mAdapter);

            }
        } else {
            Toast.makeText(this, "Network Is Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void refresh_data(String sort) {
        if (isOnline()) {
            setTitle("Popular Movie");
            URL getGithubSearchUrl = NetworkUtils.buildUrl(search);
            new GithubQueryTask().execute(getGithubSearchUrl);
            mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mNumbersList.setLayoutManager(layoutManager);
            mNumbersList.setHasFixedSize(true);
        } else {
            Toast.makeText(this, "Network Is Not Available", Toast.LENGTH_SHORT).show();
        }
    }
    public void refresh_data_pop(String sort) {
        if (isOnline()) {
            URL getGithubSearchUrl = NetworkUtils.buildUrlpopular(search);
            new GithubQueryTask().execute(getGithubSearchUrl);
            mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mNumbersList.setLayoutManager(layoutManager);
            mNumbersList.setHasFixedSize(true);
        } else {
            Toast.makeText(this, "Network Is Not Available", Toast.LENGTH_SHORT).show();
        }
    }
    public void refresh_data_top(String sort) {
        if (isOnline()) {
            URL getGithubSearchUrl = NetworkUtils.buildUrltop(search);
            new GithubQueryTask().execute(getGithubSearchUrl);
            mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mNumbersList.setLayoutManager(layoutManager);
            mNumbersList.setHasFixedSize(true);
        } else {
            Toast.makeText(this, "Network Is Not Available", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ItemThatWasClickedId  =item.getItemId();
        if (ItemThatWasClickedId == R.id.action_top_rated) {
            setTitle("Top Rated Movie");
            sharedPref.createUserLoginSession("top_rated");
            refresh_data_top("top_rated");
        }
        if (ItemThatWasClickedId == R.id.action_popular) {
            setTitle("Popular Movie");
            sharedPref.createUserLoginSession("popular");
            refresh_data_pop("popular");
        }
        if (ItemThatWasClickedId == R.id.action_favorite) {
             jsonItems = getFavorite();
            if(jsonItems.length()>0){
            setTitle("Favorite");
                sharedPref.createUserLoginSession("favorite");
                refresh_local_data();
            }else{
                Toast.makeText(this, "Favorite Is Empty", Toast.LENGTH_LONG).show();
            }
        }
        if (ItemThatWasClickedId == R.id.action_about) {
            Toast.makeText(this, "By: Dianto Lintin", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent i = new Intent(this, Detail_Movie.class);
        i.putExtra("movie", movie );
        startActivity(i);
    }

    class GithubQueryTask extends AsyncTask<URL,Void,String>{
        private String TAG = GithubQueryTask.class.getSimpleName();

        @Override
        protected String doInBackground(URL... params){

            try{
                data = NetworkUtils.getResponseFromHttpUrl(params[0]);
                Log.d(TAG, data);
                JSONObject JSONData = new JSONObject(data);
                JSONArray JSONItems = JSONData.getJSONArray("results");

                mAdapter = new Main_Adapter(MainActivity.this,JSONItems,MainActivity.this);

            }catch (IOException e){
                e.printStackTrace();
                Log.d(TAG, data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String GreenAdapter){
            if (GreenAdapter != null){
                mNumbersList.setAdapter(mAdapter);
            }
        }

    }


}
