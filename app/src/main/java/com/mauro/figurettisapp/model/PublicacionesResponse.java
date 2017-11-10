package com.mauro.figurettisapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 10/11/2017.
 */

public class PublicacionesResponse {
    @SerializedName("results")
    private List<Publicaciones> results;

    public List<Publicaciones> getResults() {
        return results;
    }

    public void setResults(List<Publicaciones> results) {
        this.results = results;
    }
}
