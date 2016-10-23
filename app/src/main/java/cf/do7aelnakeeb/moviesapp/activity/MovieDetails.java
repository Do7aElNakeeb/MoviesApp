package cf.do7aelnakeeb.moviesapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.Movie;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MovieDetails extends Fragment {

    TextView Title;
    TextView ReleaseDate;
    TextView Rating;
    TextView Description;
    ImageView Image;

    Movie selectedMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            selectedMovie = (Movie) savedInstanceState.getSerializable("selectedMovie");
        }

        Title = (TextView) getView().findViewById(R.id.MovieTitle);
        ReleaseDate = (TextView) getView().findViewById(R.id.ReleaseDate);
        Rating = (TextView) getView().findViewById(R.id.MovieRating);
        Description = (TextView) getView().findViewById(R.id.MovieDescription);
        Image = (ImageView) getView().findViewById(R.id.MovieImage);

        if (selectedMovie != null) {
            updateMovieDetails(selectedMovie);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("selectedMovie", selectedMovie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.movie_details, container, false);

    }

    public void setMovie(Movie movie){
        Log.d("Movie Name4", movie.getName());
        selectedMovie = movie;
    }

    public void updateMovieDetails(Movie movie){
        Log.d("Movie Name3", movie.getName());
        Picasso.with(getActivity()).load(AppConst.MoviesDBImageURL + movie.getImage()).into(Image);
        Title.setText(movie.getName());
        Description.setText(movie.getDescription());
        ReleaseDate.setText(movie.getReleaseDate().substring(0,4));
        Rating.setText(movie.getRating() + "/10");
    }
}
