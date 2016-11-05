package cf.do7aelnakeeb.moviesapp.helper;

/**
 * Created by Nakeeb PC on 7/23/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final String TABLE_NAME = "stations";

    // Login Table Columns names
    public static final String ID = "movie_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RATING = "rating";
    public static final String IMAGE = "image";
    public static final String RELEASE_DATE = "release_date";
    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";
    public static final String MOG95 = "MOG95";
    public static final String Diesel = "diesel";
    public static final String MOBILMART = "MobilMart";
    public static final String ONTHERUN = "OnTheRun";
    public static final String THEWAYTOGO = "TheWayToGo";
    public static final String CARWASH = "CarWash";
    public static final String Lubricants = "lubricants";
    public static final String PHONE = "phone";

    private SQLiteDatabase db;

    private String[] cols = {ID, NAME, DESCRIPTION, RATING,
            RATING, IMAGE, RELEASE_DATE, TRAILERS, REVIEWS, SQLiteHandler.Diesel, SQLiteHandler.MOBILMART, SQLiteHandler.ONTHERUN,
            SQLiteHandler.THEWAYTOGO, SQLiteHandler.CARWASH, SQLiteHandler.Lubricants, SQLiteHandler.PHONE};


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "CREATE TABLE " + TABLE_NAME + "( id integer primary key autoincrement, "
                + ID + " TEXT," + NAME + " TEXT,"
                + DESCRIPTION + " TEXT," + RATING + " TEXT,"
                + IMAGE + " TEXT," + RELEASE_DATE + " TEXT,"
                + TRAILERS + " TEXT," + REVIEWS + " TEXT,"
                + MOG95 + " TEXT," + Diesel + " TEXT,"
                + MOBILMART + " TEXT," + ONTHERUN + " TEXT,"
                + THEWAYTOGO + " TEXT," + CARWASH + " TEXT,"
                + Lubricants + " TEXT," + PHONE + " TEXT" + ")";
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
    public void addMovie(HashMap<String, String> queryValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, queryValues.get(ID));
        values.put(NAME, queryValues.get(NAME));
        values.put(DESCRIPTION, queryValues.get(DESCRIPTION));
        values.put(RATING, queryValues.get(RATING));
        values.put(IMAGE, queryValues.get(IMAGE));
        values.put(RELEASE_DATE, queryValues.get(RELEASE_DATE));
        values.put(TRAILERS, queryValues.get(TRAILERS));
        values.put(REVIEWS, queryValues.get(REVIEWS));
        values.put(MOG95, queryValues.get(MOG95));
        values.put(Diesel, queryValues.get(Diesel));
        values.put(MOBILMART, queryValues.get(MOBILMART));
        values.put(ONTHERUN, queryValues.get(ONTHERUN));
        values.put(THEWAYTOGO, queryValues.get(THEWAYTOGO));
        values.put(CARWASH, queryValues.get(CARWASH));
        values.put(Lubricants, queryValues.get(Lubricants));
        values.put(PHONE, queryValues.get(PHONE));

        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New station inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */

    public List<Movie> getMoviesDetails(String args){
        List<Movie> markers = new ArrayList<Movie>();

        Cursor cursor = db.query(TABLE_NAME, cols, args, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
            markers.add(movie);
            cursor.moveToNext();
        }
        cursor.close();

        return markers;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteMarkers(){
        db.delete(SQLiteHandler.TABLE_NAME, null, null);
    }


    private Movie cursorToMarker(Cursor cursor) {
        return new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));

    }

}
