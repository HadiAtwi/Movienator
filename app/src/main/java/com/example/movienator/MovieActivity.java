package com.example.movienator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MovieActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    private MoviesRepository moviesRepository;
    private int movieId;

    /*_________________ Genres List and ID Map _____________*/
    List<String> currentGenres;
    Map<String, Integer> genreMap = new HashMap<String, Integer>();

    /*______________________________________________________*/


    /*_________________ Personalization Data _______________*/
    public static final String PREFERENCES = "personalizedData";
    SharedPreferences personalizedData ;
    /*______________________________________________________*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        moviesRepository = MoviesRepository.getInstance();

        setupToolbar();

        initUI();

        getMovie();

        personalizedData = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        genreMap.put("Action", 28);
        genreMap.put("Adventure", 12);
        genreMap.put("Animation", 16);
        genreMap.put("Comedy", 35);
        genreMap.put("Crime", 80);
        genreMap.put("Documentary", 99);
        genreMap.put("Drama", 18);
        genreMap.put("Family", 10751);
        genreMap.put("Fantasy", 14);
        genreMap.put("History", 36);
        genreMap.put("Horror", 27);
        genreMap.put("Music", 10402);
        genreMap.put("Mystery", 9648);
        genreMap.put("Romance", 10749);
        genreMap.put("Science Fiction", 878);
        genreMap.put("TV Movie", 10770);
        genreMap.put("Thriller", 53);
        genreMap.put("War", 10752);
        genreMap.put("Western", 37);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieTitle.setText(movie.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(MovieActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    public void likeMovie(View view){

        // Initialize editor
        SharedPreferences.Editor preferencesEditor = personalizedData.edit();

        // Check if minimum release date in preferences is less than the current movie's release date

        // Add the current movie's genre to the list of preferred genres

        String genresToAdd = "";

        Iterator<String> iterator = currentGenres.iterator();
        while(iterator.hasNext()) {
            String genre = iterator.next();
            genresToAdd += genreMap.get(genre) + "|";
        }

        if(personalizedData.contains("genres")){
            preferencesEditor.putString("genres", personalizedData.getString("genres", "") + genresToAdd.substring(0, genresToAdd.length()-1));
        }else{
            preferencesEditor.putString("genres", genresToAdd.substring(0, genresToAdd.length()-1));
        }

        preferencesEditor.commit();

        Toast.makeText(MovieActivity.this,"Added to likes.", Toast.LENGTH_LONG).show();

        // TODO keep it disabled even after then activity is closed and then reopened
        Button likeButton = findViewById(R.id.btnLike);
        likeButton.setEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Toast.makeText(MovieActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}