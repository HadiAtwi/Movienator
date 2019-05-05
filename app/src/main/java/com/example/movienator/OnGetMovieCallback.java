package com.example.movienator;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}