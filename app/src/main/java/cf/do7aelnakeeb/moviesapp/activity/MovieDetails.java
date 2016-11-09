package cf.do7aelnakeeb.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.AppController;
import cf.do7aelnakeeb.moviesapp.app.Movie;
import cf.do7aelnakeeb.moviesapp.helper.DividerItemDecoration;
import cf.do7aelnakeeb.moviesapp.helper.ReviewsAdapter;
import cf.do7aelnakeeb.moviesapp.helper.SQLiteHandler;
import cf.do7aelnakeeb.moviesapp.helper.TrailersAdapter;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MovieDetails extends Fragment implements CompoundButton.OnCheckedChangeListener {

    // TODO the movie details opened in portrait become selected in landscape

    private static final String TAG = MovieDetails.class.getSimpleName();
    private ProgressBar TrailersLoading;
    private ProgressBar ReviewsLoading;

    ToggleButton favMovie;
    TextView Title;
    TextView ReleaseDate;
    TextView Rating;
    TextView Description;
    ImageView Image;

    TextView trailersTxtView;
    TextView reviewsTxtView;

    RecyclerView ReviewsRecyclerView;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Movie.Review> reviewArrayList;

    RecyclerView TrailersRecyclerView;
    TrailersAdapter trailersAdapter;
    ArrayList<Movie.Trailer> trailerArrayList;

    Movie selectedMovie;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    SQLiteHandler sqLiteHandler;

    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trailerArrayList = new ArrayList<Movie.Trailer>();
        setHasOptionsMenu(true);
        sqLiteHandler = new SQLiteHandler(getContext());

        try {
            sqLiteHandler.open();
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TrailersLoading = (ProgressBar) getView().findViewById(R.id.trailers_loading);
        ReviewsLoading = (ProgressBar) getView().findViewById(R.id.reviews_loading);
        trailersTxtView = (TextView) getView().findViewById(R.id.trailers_txtview);
        reviewsTxtView = (TextView) getView().findViewById(R.id.reviews_txtview);

        ReviewsRecyclerView = (RecyclerView) getView().findViewById(R.id.reviewsRecyclerView);
        ReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        TrailersRecyclerView = (RecyclerView) getView().findViewById(R.id.trailersRecyclerView);
        TrailersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        TrailersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        trailersAdapter = new TrailersAdapter(getActivity(), trailerArrayList);
        TrailersRecyclerView.setAdapter(trailersAdapter);


        Title = (TextView) getView().findViewById(R.id.MovieTitle);
        ReleaseDate = (TextView) getView().findViewById(R.id.ReleaseDate);
        Rating = (TextView) getView().findViewById(R.id.MovieRating);
        Description = (TextView) getView().findViewById(R.id.MovieDescription);
        Image = (ImageView) getView().findViewById(R.id.MovieImage);

        favMovie = (ToggleButton) getView().findViewById(R.id.MovieFavBtn);


        prefs = getActivity().getSharedPreferences("selectedMovie", Context.MODE_PRIVATE);

        if (selectedMovie != null) {
            updateMovieDetails(selectedMovie);
        }
        else {

            selectedMovie = new Movie(prefs.getString("id", ""), prefs.getString("name", ""), prefs.getString("description", ""),
                    prefs.getString("rating", ""), prefs.getString("image", ""), prefs.getString("release_date", ""));
            Log.d("MovieWhenNotNull4", selectedMovie.getName());
            updateMovieDetails(selectedMovie);
        }

        favMovie.setOnCheckedChangeListener(this);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("id", selectedMovie.getId());
        outState.putString("name", selectedMovie.getName());
        outState.putString("description", selectedMovie.getDescription());
        outState.putString("rating", selectedMovie.getRating());
        outState.putString("release_date", selectedMovie.getReleaseDate());
        outState.putString("image", selectedMovie.getImage());
    }

    private void syncMovieWithSharedPrefs(){
        prefs = getActivity().getSharedPreferences("selectedMovie", Context.MODE_PRIVATE);

        editor = prefs.edit();
        editor.putString("id", selectedMovie.getId());
        editor.putString("name", selectedMovie.getName());
        editor.putString("description", selectedMovie.getDescription());
        editor.putString("rating", selectedMovie.getRating());
        editor.putString("release_date", selectedMovie.getReleaseDate());
        editor.putString("image", selectedMovie.getImage());

        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.movie_details, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();

        try{
            sqLiteHandler.open();
        }
        catch (Exception e){
            Log.e("SQLiteError", e.getMessage());
        }
    }

    public void setMovie(Movie movie){
        Log.d("Movie Name4", movie.getName());
        selectedMovie = movie;
    }


    public void updateMovieDetails(Movie movie){
        selectedMovie = movie;
        //syncMovieWithSharedPrefs();
        Log.d("Movie Name3", movie.getName());
        Picasso.with(getActivity()).load(AppConst.MoviesDBImageURL + movie.getImage()).into(Image);
        Title.setText(movie.getName());
        Description.setText(movie.getDescription());
        ReleaseDate.setText(movie.getReleaseDate());
        Rating.setText(movie.getRating() + "/10");

        if(sqLiteHandler.getMoviesDetails(SQLiteHandler.ID + "=" + movie.getId()).size() != 0){
            favMovie.setChecked(true);
        }
        else {
            favMovie.setChecked(false);
        }
        syncTrailers();
        syncReviews();
    }

    private void syncTrailers(){

        // Tag used to cancel the request
        String tag_string_req = "req_trailers";

        TrailersLoading.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.GET, AppConst.MoviesDBURL + selectedMovie.getId() + "/videos?api_key=" + AppConst.APIKey,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "MoviesDB Response: " + response);

                        TrailersRecyclerView.setVisibility(View.VISIBLE);
                        TrailersLoading.setVisibility(View.GONE);
                        trailersTxtView.setVisibility(View.VISIBLE);

                        trailerArrayList = new ArrayList<Movie.Trailer>();

                        try {
                            // Extract JSON array from the response
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray resultsArr = jsonObject.getJSONArray("results");
                            System.out.println(jsonObject.length());
                            // If no of array elements is not zero
                            if(resultsArr.length() != 0){

                                Log.d("resultsArray", resultsArr.toString());
                                // Loop through each array element, get JSON object
                                for (int i = 0; i < resultsArr.length(); i++) {
                                    // Get JSON object
                                    JSONObject obj = (JSONObject) resultsArr.get(i);

                                    // DB QueryValues Object to insert into Movies ArrayList
                                    String id = obj.get("id").toString();
                                    String key = obj.get("key").toString();
                                    String name = obj.get("name").toString();
                                    String type = obj.get("type").toString();
                                    String site = obj.get("site").toString();

                                    trailerArrayList.add(new Movie.Trailer(id, key, name, type, site));

                                }

                                trailersAdapter = new TrailersAdapter(getActivity(), trailerArrayList);
                                TrailersRecyclerView.setAdapter(trailersAdapter);
                                selectedMovie.trailersArrayList = trailerArrayList;
                                getActivity().invalidateOptionsMenu();

                            }
                            else {
                                trailersTxtView.setVisibility(View.GONE);
                                TrailersRecyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                trailersTxtView.setVisibility(View.GONE);
                TrailersLoading.setVisibility(View.GONE);
                TrailersRecyclerView.setVisibility(View.GONE);
            }
        }) {
/*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", AppConst.APIKey );
                params.put("language", AppConst.MoviesDBAPILanguage);
                return params;
            }
            */

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void syncReviews(){

        // Tag used to cancel the request
        String tag_string_req = "req_reviews";

        ReviewsLoading.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConst.MoviesDBURL + selectedMovie.getId() + "/reviews?api_key=" + AppConst.APIKey,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "MoviesDB Response: " + response);

                        ReviewsRecyclerView.setVisibility(View.VISIBLE);
                        ReviewsLoading.setVisibility(View.GONE);
                        reviewsTxtView.setVisibility(View.VISIBLE);

                        reviewArrayList = new ArrayList<Movie.Review>();

                        try {
                            // Extract JSON array from the response
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray resultsArr = jsonObject.getJSONArray("results");
                            System.out.println(jsonObject.length());
                            // If no of array elements is not zero
                            if(resultsArr.length() != 0){

                                Log.d("resultsArray", resultsArr.toString());
                                // Loop through each array element, get JSON object
                                for (int i = 0; i < resultsArr.length(); i++) {
                                    // Get JSON object
                                    JSONObject obj = (JSONObject) resultsArr.get(i);

                                    // DB QueryValues Object to insert into Movies ArrayList
                                    String id = obj.get("id").toString();
                                    String author = obj.get("author").toString();
                                    String content = obj.get("content").toString();
                                    String url = obj.get("url").toString();

                                    reviewArrayList.add(new Movie.Review(id, author, content, url));

                                }

                                reviewsAdapter = new ReviewsAdapter(getActivity(), reviewArrayList);
                                ReviewsRecyclerView.setAdapter(reviewsAdapter);
                                selectedMovie.reviewsArrayList = reviewArrayList;

                            }
                            else {
                                reviewsTxtView.setVisibility(View.GONE);
                                ReviewsRecyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

                reviewsTxtView.setVisibility(View.GONE);
                ReviewsLoading.setVisibility(View.GONE);
            }
        });

        /*{

            /*
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", AppConst.APIKey );
                params.put("language", AppConst.MoviesDBAPILanguage);
                return params;
            }



        };
    */

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            sqLiteHandler.addMovie(selectedMovie);
        }
        else {
            sqLiteHandler.deleteMovie(selectedMovie);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        /**
         * Share menu button
         * https://developer.android.com/reference/android/support/v7/widget/ShareActionProvider.html
         * */

        // Inflate menu resource file.
        menuInflater.inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if(trailerArrayList.size() > 0) {
            item.setVisible(true);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailerArrayList.get(0).getKey());

            setShareIntent(shareIntent);
        }
        else {
            item.setVisible(false);
        }

    }


    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
