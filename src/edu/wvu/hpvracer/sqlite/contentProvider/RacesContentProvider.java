package edu.wvu.hpvracer.sqlite.contentProvider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import edu.wvu.hpvracer.sqlite.database.RacesDatabaseHelper;
import edu.wvu.hpvracer.sqlite.database.SQLConstants;

public class RacesContentProvider extends ContentProvider {

  // database
  private RacesDatabaseHelper database;

  // Used for the UriMatcher
  private static final int RACES = 10;
  private static final int RACE_ID = 20;

  private static final String AUTHORITY = "edu.wvu.hpvracer.sqlite.RacesContentProvider";
  private static final String BASE_PATH = "hpvRacer";
  public static final Uri RACE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, RACES);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", RACE_ID);
  }

  @Override
  public boolean onCreate() {
    database = new RacesDatabaseHelper(getContext());
    return false;
  }

  public Cursor rawQuery(String q){
	  SQLiteDatabase db = database.getWritableDatabase();
	  Cursor cursor = db.rawQuery(q, null);
	    
	  Log.d("RacesContentProvider", "rawQuery executed");
	    
	  return cursor;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Using SQLiteQueryBuilder instead of query() method
	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(SQLConstants.TABLE_RACE_DATA.toString());

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
	    
	    case RACES:
	      break;
	      
	    case RACE_ID:
	      // Adding the ID to the original query
	      queryBuilder.appendWhere(SQLConstants.COLUMN_ID + "=" + uri.getLastPathSegment());
	      break;
	    
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    
    Log.d("RacesContentProvider", queryBuilder.buildQuery(projection, selection, null, null, null, null));
    
    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    long id = 0;
    switch (uriType) {
    case RACES:
      id = sqlDB.insert(SQLConstants.TABLE_RACE_DATA.toString(), null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case RACES:
      rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACE_DATA.toString(), selection,
          selectionArgs);
      break;
    case RACE_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACE_DATA.toString(),
        		SQLConstants.COLUMN_READING_TIME + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACE_DATA.toString(),
        		SQLConstants.COLUMN_READING_TIME + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case RACES:
      rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACE_DATA.toString(), 
          values, 
          selection,
          selectionArgs);
      break;
    case RACE_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACE_DATA.toString(), 
            values,
            SQLConstants.COLUMN_READING_TIME + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACE_DATA.toString(), 
            values,
            SQLConstants.COLUMN_READING_TIME + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = { SQLConstants.COLUMN_READING_TIME, SQLConstants.COLUMN_KEY,
    		SQLConstants.COLUMN_VALUE, SQLConstants.COLUMN_RIDER_ID, SQLConstants.COLUMN_RACE_ID,
    		SQLConstants.COLUMN_RIDER_LAP, SQLConstants.COLUMN_RACE_LAP, SQLConstants.COLUMN_UPLOAD_STATUS,
    		SQLConstants.COLUMN_READING_TIME };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }

}