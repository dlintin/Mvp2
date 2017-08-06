package com.dmovies.diand.diandmovies.model;

import java.io.Serializable;

/**
 * Created by USER on 28/05/2017.
 */

public class Movie implements Serializable{


    public long id;
    public String original_title;
    public String poster_image;
    public String original_language;
    public String overview;
    public String vote_average;
    public String release_date;

    public Movie(long id, String original_title, String poster_image, String original_language, String overview, String vote_average, String release_date) {
        this.id = id;
        this.original_title = original_title;
        this.poster_image = poster_image;
        this.original_language = original_language;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }
}
