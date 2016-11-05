package cf.do7aelnakeeb.moviesapp.app;

import android.util.Log;
import android.widget.Toast;

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

import cf.do7aelnakeeb.moviesapp.helper.MoviesAdapter;

/**
 * Created by NakeebMac on 10/21/16.
 */

public class AppConst {


    // MoviesDB API Url
    public static String MoviesDBURL = "https://api.themoviedb.org/3/movie/";

    // MoviesDB API Key
    public static String APIKey = "3d1810975f896dad9c6108860b09be3d";

    // MoviesDB API Language
    public static String MoviesDBAPILanguage = "en-US";

    // MoviesDB Images Url
    public static String MoviesDBImageURL = "https://image.tmdb.org/t/p/w185";

    // MoviesDB Images Url
    public static String TrailerThumbnail = "http://img.youtube.com/vi/";


    // MoviesDB Categories
    public static String[] MoviesDBCategories = {"popular", "top_rated"};


    public static ArrayList<String> movieData = new ArrayList<String>(){{
        add("id"); add("original_title"); add("overview"); add("original_title"); add("vote_average");
        add("poster_path"); add("release_date");
    }};

    public static ArrayList<String> movieReviewsData = new ArrayList<String>(){{
        add("id"); add("author"); add("content"); add("url");
    }};

    public static ArrayList<String> movieTrailersData = new ArrayList<String>(){{
        add("id"); add("key"); add("name"); add("type"); add("site");
    }};

    Map<String, String> APImovieParams = new HashMap<String, String>(){{
        put("api_key", APIKey);
        put("language", MoviesDBAPILanguage);
    }};

}
