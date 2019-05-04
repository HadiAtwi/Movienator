package com.example.movienator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;

    private MoviesRepository moviesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Attach MoviesAdapter to recyclerView
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.movies_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MoviesAdapter(new ArrayList<Movie>());
        recyclerView.setAdapter(adapter);


        moviesRepository = MoviesRepository.getInstance();

        //moviesList = findViewById(R.id.movies_list);
        //moviesList.setLayoutManager(new LinearLayoutManager(this));

        moviesRepository.getMovies(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new MoviesAdapter(movies);
                moviesList.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Attach MoviesAdapter to recyclerView
//        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.movies_list);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setHasFixedSize(true);
//        MoviesAdapter adapter = new MoviesAdapter();
//        recyclerView.setAdapter(adapter);
//
//    }
//}
