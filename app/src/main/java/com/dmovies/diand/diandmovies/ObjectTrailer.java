package com.dmovies.diand.diandmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ObjectTrailer {
    @SerializedName("results")
    List<DataTrailer> results;

    public class DataTrailer {
        @SerializedName("name")
        public String name;

        @SerializedName("key")
        public String key;

        @SerializedName("type")
        public String type;
    }
}
