package cf.do7aelnakeeb.moviesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import cf.do7aelnakeeb.moviesapp.helper.DividerItemDecoration;
import cf.do7aelnakeeb.moviesapp.helper.MoviesAdapter;
import cf.do7aelnakeeb.moviesapp.helper.ReviewsAdapter;
import cf.do7aelnakeeb.moviesapp.helper.TrailersAdapter;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MovieDetails extends Fragment {

    private static final String TAG = MovieDetails.class.getSimpleName();
    private ProgressDialog pDialog;

    TextView Title;
    TextView ReleaseDate;
    TextView Rating;
    TextView Description;
    ImageView Image;

    RecyclerView ReviewsRecyclerView;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Movie.Review> reviewArrayList;

    RecyclerView TrailersRecyclerView;
    TrailersAdapter trailersAdapter;
    ArrayList<Movie.Trailer> trailerArrayList;

    Movie selectedMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            selectedMovie = savedInstanceState.getParcelable("selectedMovie");
            //Log.d("ccc", selectedMovie.getName());
        }

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        ReviewsRecyclerView = (RecyclerView) getView().findViewById(R.id.reviewsRecyclerView);
        ReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


/*
        reviewsAdapter = new ReviewsAdapter(getActivity(), reviewArrayList);
        ReviewsRecyclerView.setAdapter(reviewsAdapter);
*/
        TrailersRecyclerView = (RecyclerView) getView().findViewById(R.id.trailersRecyclerView);
        TrailersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        TrailersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        trailersAdapter = new TrailersAdapter(getActivity(), trailerArrayList);
        TrailersRecyclerView.setAdapter(trailersAdapter);

//        syncTrailers();



  //      syncReviews();



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
        selectedMovie = outState.getParcelable("selectedMovie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.movie_details, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setMovie(Movie movie){
        Log.d("Movie Name4", movie.getName());
        selectedMovie = movie;
    }

    public void updateMovieDetails(Movie movie){
        selectedMovie = movie;
        Log.d("Movie Name3", movie.getName());
        Picasso.with(getActivity()).load(AppConst.MoviesDBImageURL + movie.getImage()).into(Image);
        Title.setText(movie.getName());
        Description.setText(movie.getDescription());
        ReleaseDate.setText(movie.getReleaseDate().substring(0,4));
        Rating.setText(movie.getRating() + "/10");
        syncTrailers();
//        syncReviews();
    }

    private void syncTrailers(){

        // Tag used to cancel the request
        String tag_string_req = "req_trailers";

        pDialog.setMessage("Loading Trailers ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.GET, AppConst.MoviesDBURL + selectedMovie.getId() + "/videos?api_key=" + AppConst.APIKey,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        syncReviews();
                        Log.d(TAG, "MoviesDB Response: " + response);
                        hideDialog();

                        trailerArrayList = new ArrayList<Movie.Trailer>();

                        try {
                            // Extract JSON array from the response
                            JSONObject arr = new JSONObject(response);
                            JSONArray resultsArr = arr.getJSONArray("results");
                            System.out.println(arr.length());
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
                                //hideDialog();

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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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

        pDialog.setMessage("Loading Reviews ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.GET, AppConst.MoviesDBURL + selectedMovie.getId() + "/reviews?api_key=" + AppConst.APIKey,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "MoviesDB Response: " + response);
                        hideDialog();

                        reviewArrayList = new ArrayList<Movie.Review>();

                        try {
                            // Extract JSON array from the response
                            JSONObject arr = new JSONObject(response);
                            JSONArray resultsArr = arr.getJSONArray("results");
                            System.out.println(arr.length());
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
                                //hideDialog();

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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
