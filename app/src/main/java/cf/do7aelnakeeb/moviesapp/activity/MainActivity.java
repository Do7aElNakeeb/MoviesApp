package cf.do7aelnakeeb.moviesapp.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.Movie;

public class MainActivity extends AppCompatActivity implements MoviesGrid.OnMovieSelected{

    Boolean movieDetailsFrag = false;

    MoviesGrid moviesGrid;
    MovieDetails movieDetails;

    Movie movie;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            moviesGrid = new MoviesGrid();

            fragmentTransaction.add(R.id.movieGridFragment, moviesGrid, "Movies Grid");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }

        else {

            movie = new Movie(savedInstanceState.getString("id"), savedInstanceState.getString("name"), savedInstanceState.getString("description"),
                    savedInstanceState.getString("rating"), savedInstanceState.getString("image"), savedInstanceState.getString("release_date"));
//            Log.d("MovieNameWhenNotNull", movie.getName());
        }

        if(findViewById(R.id.movieDetailsFragment) != null){
            movieDetailsFrag = true;
            //getFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStackImmediate();

            movieDetails = (MovieDetails) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);
            if(movieDetails == null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                movieDetails = new MovieDetails();
                moviesGrid = new MoviesGrid();
                if (movie != null){
                    movieDetails.setMovie(movie);
                }

                ft.replace(R.id.movieDetailsFragment, movieDetails, "Movie Details Fragment");
                ft.replace(R.id.movieGridFragment, moviesGrid, "Movies Grid");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movie = new Movie(savedInstanceState.getString("id"), savedInstanceState.getString("name"), savedInstanceState.getString("description"),
                savedInstanceState.getString("rating"), savedInstanceState.getString("image"), savedInstanceState.getString("release_date"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (movie != null) {
            outState.putString("id", movie.getId());
            outState.putString("name", movie.getName());
            outState.putString("description", movie.getDescription());
            outState.putString("rating", movie.getRating());
            outState.putString("release_date", movie.getReleaseDate());
            outState.putString("image", movie.getImage());
        }

    }

    @Override
    public void onMovieSelected(Movie movie) {
        prefs = getSharedPreferences("selectedMovie", MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("id", movie.getId());
        editor.putString("name", movie.getName());
        editor.putString("description", movie.getDescription());
        editor.putString("rating", movie.getRating());
        editor.putString("release_date", movie.getReleaseDate());
        editor.putString("image", movie.getImage());

        editor.commit();

        this.movie = movie;
        if(movieDetailsFrag){
            Log.d("Movie Name", movie.getName());
            movieDetails = (MovieDetails) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);
            movieDetails.updateMovieDetails(movie);
        }
        else{
            Log.d("Movie Name1", movie.getName());
            movieDetails = new MovieDetails();
            movieDetails.setMovie(movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.movieGridFragment, movieDetails, "Movie Details Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}