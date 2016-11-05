package cf.do7aelnakeeb.moviesapp.activity;

import android.content.Intent;
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieDetailsFrag = true;
//            movie = movieDetails.selectedMovie;
//            Log.d("aaaaa", movie.getName());
            Toast.makeText(this, "Landscape Orient", Toast.LENGTH_SHORT).show();
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
            getFragmentManager().popBackStack();

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
    }

    @Override
    public void onMovieSelected(Movie movie) {
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