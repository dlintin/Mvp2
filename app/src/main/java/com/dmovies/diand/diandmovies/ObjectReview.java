package com.dmovies.diand.diandmovies;

/**
 * Created by diand on 06-Aug-17.
 */
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ObjectReview {
    @SerializedName("results")
    List<DataReview> results;

    public class DataReview {
        @SerializedName("author")
        public String author;

        @SerializedName("content")
        public String content;


    }
}
