package cf.do7aelnakeeb.moviesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cf.do7aelnakeeb.moviesapp.R;
import cf.do7aelnakeeb.moviesapp.app.AppConst;
import cf.do7aelnakeeb.moviesapp.app.AppController;
import cf.do7aelnakeeb.moviesapp.app.Movie;
import cf.do7aelnakeeb.moviesapp.helper.MoviesAdapter;
import cf.do7aelnakeeb.moviesapp.helper.MoviesProvider;
import cf.do7aelnakeeb.moviesapp.helper.SQLiteHandler;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class MoviesGrid extends Fragment {

    private static final String TAG = MoviesGrid.class.getSimpleName();
    private ProgressDialog pDialog;

    OnMovieSelected onMovieSelected;

    RecyclerView MoviesRecyclerView;
    MoviesAdapter moviesAdapter;
    ArrayList<Movie> movieArrayList;

    int MoviesCategories = 0;

    // Deal with SQLite directly
    //SQLiteHandler sqLiteHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            MoviesCategories = savedInstanceState.getInt("MoviesCategory");
        }

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        MoviesRecyclerView = (RecyclerView) getView().findViewById(R.id.MovieRecyclerView);

        MoviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));

        MoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        syncMovies(MoviesCategories);

        moviesAdapter = new MoviesAdapter(getActivity(), movieArrayList, onMovieSelected);
        MoviesRecyclerView.setAdapter(moviesAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.movies_grid, container, false);

    }

    // Container Activity must implement this interface
    public interface OnMovieSelected {
        void onMovieSelected(Movie movie);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onMovieSelected = (OnMovieSelected) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnMovieSelected");
        }
    }

    private void syncMovies(){

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Loading Movies ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConst.MoviesDBURL + AppConst.MoviesDBCategories[MoviesCategories],
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "MoviesDB Response: " + response);
                        hideDialog();

                        movieArrayList = new ArrayList<Movie>();

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
                                    String name = obj.get("original_title").toString();
                                    String description = obj.get("overview").toString();
                                    String rating = obj.get("vote_average").toString();
                                    String image = obj.get("poster_path").toString();
                                    String release_date = obj.get("release_date").toString().substring(0, 4);

                                    movieArrayList.add(new Movie(id, name, description, rating, image, release_date));

                                }

                                moviesAdapter = new MoviesAdapter(getActivity(), movieArrayList, onMovieSelected);
                                MoviesRecyclerView.setAdapter(moviesAdapter);

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

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", AppConst.APIKey );
                params.put("language", AppConst.MoviesDBAPILanguage);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("MoviesCategory", MoviesCategories);
    }

    private void syncFavorite(){



        pDialog.setMessage("Loading Movies ...");
        showDialog();


        /*
        sqLiteHandler = new SQLiteHandler(getContext());
        try {
            sqLiteHandler.open();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), "Loading Failed", Toast.LENGTH_SHORT).show();
        }
        /*

         */
        movieArrayList = new ArrayList<Movie>();

        // Getting data using content provider
        Cursor cursor = getContext().getContentResolver().query(MoviesProvider.CONTENT_URI, SQLiteHandler.cols, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
            movieArrayList.add(movie);
            Log.d("FavMovie Added", movieArrayList.get(cursor.getPosition()).getImage());
            cursor.moveToNext();
        }
        cursor.close();

        // Getting data directly from SQlite
        //List<Movie> favMovies = sqLiteHandler.getMoviesDetails(null);

        if (movieArrayList.size() != 0) {
            moviesAdapter = new MoviesAdapter(getActivity(), movieArrayList, onMovieSelected);
            MoviesRecyclerView.setAdapter(moviesAdapter);
            hideDialog();

        }
        else {
            Toast.makeText(getActivity(), "No Favorite Movies Found", Toast.LENGTH_SHORT).show();
            hideDialog();
        }

    }

    private void syncMovies(int Category){

        getActivity().invalidateOptionsMenu();

        switch (Category){
            case 0:
                syncMovies();
                break;

            case 1:
                syncMovies();
                break;

            case 2:
                syncFavorite();
                break;

            default:
                syncMovies();
                break;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && menu.findItem(R.id.menu_item_share) != null){
            menu.findItem(R.id.menu_item_share).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular: {
                MoviesCategories = 0;
                syncMovies(0);
                return true;
            }

            case R.id.action_top_rated: {
                MoviesCategories = 1;
                syncMovies(1);
                return true;
            }
            case R.id.action_favorite: {
                MoviesCategories = 2;
                syncMovies(2);
                return true;
            }
            case R.id.refresh: {
                syncMovies(MoviesCategories);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
