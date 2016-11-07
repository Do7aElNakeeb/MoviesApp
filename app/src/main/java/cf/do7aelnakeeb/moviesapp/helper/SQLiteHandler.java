package cf.do7aelnakeeb.moviesapp.helper;

/**
 * Created by Nakeeb PC on 7/23/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cf.do7aelnakeeb.moviesapp.app.Movie;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "movies.db";

    // Login table name
    public static final String TABLE_NAME = "favMovies";

    // Login Table Columns names
    public static final String ID = "movie_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RATING = "rating";
    public static final String IMAGE = "image";
    public static final String RELEASE_DATE = "release_date";
    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";

    private SQLiteDatabase db;

    private String[] cols = {ID, NAME, DESCRIPTION,
            RATING, IMAGE, RELEASE_DATE/*, TRAILERS, REVIEWS*/};


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        db = getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "CREATE TABLE " + TABLE_NAME + "( id integer primary key autoincrement, "
                + ID + " integer not null," + NAME + " TEXT,"
                + DESCRIPTION + " TEXT," + RATING + " TEXT,"
                + IMAGE + " TEXT," + RELEASE_DATE + " TEXT, unique(" + ID + "))";
        db.execSQL(CREATE_STATIONS_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addMovie(Movie movie) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, movie.getId());
        values.put(NAME, movie.getName());
        values.put(DESCRIPTION, movie.getDescription());
        values.put(RATING, movie.getRating());
        values.put(IMAGE, movie.getImage());
        values.put(RELEASE_DATE, movie.getReleaseDate());
        /*
        values.put(TRAILERS, queryValues.get(TRAILERS));
        values.put(REVIEWS, queryValues.get(REVIEWS));
        */

        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        //close(); // Closing database connection

        Log.d(TAG, "New movie inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */

    public List<Movie> getMoviesDetails(String args){
        List<Movie> movies = new ArrayList<Movie>();

        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, cols, args, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();

        return movies;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteMovies(){
        db.delete(SQLiteHandler.TABLE_NAME, null, null);
    }

    public void deleteMovie(Movie movie){
        db.delete(TABLE_NAME, ID  + "=" + movie.getId(), null);
        Log.d("delete movie", movie.getName());
    }

    private Movie cursorToMovie(Cursor cursor) {
        return new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));

    }

}
