package cf.do7aelnakeeb.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.AppController;
import cf.do7aelnakeeb.moviesapp.app.Movie;
import cf.do7aelnakeeb.moviesapp.helper.MoviesAdapter;

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
            Log.d("casdcc", movie.getName());
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieDetailsFrag = true;
//            movie = movieDetails.selectedMovie;
//            Log.d("aaaaa", movie.getName());

            if (movieDetails != null) {
                Log.d("bbbb", "nullo");
                getSupportFragmentManager().beginTransaction().remove(movieDetails).commit();
            }
/*
            movieDetails = (MovieDetails) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);
            if(movieDetails == null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                movieDetails = new MovieDetails();
//                moviesGrid = new MoviesGrid();
//                movieDetails.setMovie(moviesGrid.movieArrayList.get(0));
//                ft.replace(R.id.movieGridFragment, moviesGrid, "Movies Grid");
                getSupportFragmentManager().beginTransaction().remove(movieDetails).commit();
                ft.replace(R.id.movieDetailsFragment, movieDetails, "Movie Details Fragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
*/
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
//                movieDetails.setMovie(moviesGrid.movieArrayList.get(0));
//                getSupportFragmentManager().beginTransaction().remove(movieDetails).commit();
                ft.replace(R.id.movieDetailsFragment, movieDetails, "Movie Details Fragment");
                ft.replace(R.id.movieGridFragment, moviesGrid, "Movies Grid");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("selectedMovie", movie);
        if (outState != null) {
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
/*
    @Override
    protected void onPause() {
        super.onPause();

        prefs = getSharedPreferences("selectedMovie", Context.MODE_PRIVATE);

        editor = prefs.edit();
        editor.putString("id", movie.getId());
        editor.putString("name", movie.getName());
        editor.putString("description", movie.getDescription());
        editor.putString("rating", movie.getRating());
        editor.putString("release_date", movie.getReleaseDate());
        editor.putString("image", movie.getImage());

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefs = getSharedPreferences("selectedMovie", Context.MODE_PRIVATE);
        if (movie == null) {
            movie = new Movie(prefs.getString("id", ""), prefs.getString("name", ""), prefs.getString("description", ""),
                    prefs.getString("rating", ""), prefs.getString("image", ""), prefs.getString("release_date", ""));
            Log.d("ccc", movie.getName());
        }
    }
    */
}