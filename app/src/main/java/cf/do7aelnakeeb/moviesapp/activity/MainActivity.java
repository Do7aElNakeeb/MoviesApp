package cf.do7aelnakeeb.moviesapp.activity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MoviesGrid moviesGrid = new MoviesGrid();

            fragmentTransaction.add(R.id.movieGridFragment, moviesGrid, "Movies Grid");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }


        if(findViewById(R.id.movieDetailsFragment) != null){
            movieDetailsFrag = true;
            getFragmentManager().popBackStack();

            MovieDetails movieDetails = (MovieDetails) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);
            if(movieDetails == null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                movieDetails = new MovieDetails();
                MoviesGrid moviesGrid = new MoviesGrid();
                ft.replace(R.id.movieGridFragment, moviesGrid, "Movies Grid");
                ft.replace(R.id.movieDetailsFragment, movieDetails, "Movie Details Fragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }

    @Override
    public void onMovieSelected(Movie movie) {
        if(movieDetailsFrag){
            Log.d("Movie Name", movie.getName());
            MovieDetails movieDetails = (MovieDetails) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);
            movieDetails.updateMovieDetails(movie);
        }
        else{
            Log.d("Movie Name1", movie.getName());
            MovieDetails movieDetails = new MovieDetails();
            movieDetails.setMovie(movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.movieGridFragment, movieDetails, "Movie Details Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}