package cf.do7aelnakeeb.moviesapp.helper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Created by NakeebMac on 11/9/16.
 */


/**
 * Content Provider
 * https://developer.android.com/guide/topics/providers/content-provider-creating.html
 * */

public class MoviesProvider extends ContentProvider {

    static final String PROVIDER = "cf.do7aelnakeeb.moviesapp.helper.MoviesProvider";

    public static final String MOVIES_PATH = "movies";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/" +  MOVIES_PATH);

    SQLiteHandler sqLiteHandler;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        sqLiteHandler = new SQLiteHandler(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        sqLiteQueryBuilder.setTables(SQLiteHandler.TABLE_NAME);

        db = sqLiteHandler.getWritableDatabase();
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        db = sqLiteHandler.getWritableDatabase();
        long id = db.insert(SQLiteHandler.TABLE_NAME, null, values);
        Log.d("FavMovieAdded", "New movie inserted into sqlite: " + id);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(MOVIES_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = sqLiteHandler.getWritableDatabase();

        int rowsDeleted = db.delete(SQLiteHandler.TABLE_NAME, SQLiteHandler.ID  + "=" + selection, null);
        Log.d("delete movie", selection);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = { SQLiteHandler.ID,
                SQLiteHandler.NAME, SQLiteHandler.DESCRIPTION,
                SQLiteHandler.RATING, SQLiteHandler.IMAGE, SQLiteHandler.RELEASE_DATE };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
