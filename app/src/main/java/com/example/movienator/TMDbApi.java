package com.example.movienator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {


    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // https://api.themoviedb.org/3/movie/{movie_id}?api_key=39c69a10c366d0d25de6fec34b2cf1bc&language=en-US
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    //  https://api.themoviedb.org/3/discover/movie?api_key=39c69a10c366d0d25de6fec34b2cf1bc&language=en-US&with_genres=28
    @GET("discover/movie")
    Call<MoviesResponse> getPersonalized (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("with_genres") String genres,
            @Query("page") int page
    );
}
