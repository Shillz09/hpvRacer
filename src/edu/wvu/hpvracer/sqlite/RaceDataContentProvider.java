package edu.wvu.hpvracer.sqlite;

import java.util.Arrays;
import java.util.HashSet;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class RaceDataContentProvider extends ContentProvider {

	// database
	private SQLiteDatabaseHelper database;

	// Used for the UriMatcher
	private static final int RACES = 10;
	private static final int RACES_ID = 20;

	private static final String AUTHORITY = "edu.wvu.hpvracer.sqlite.contentprovider";

	private static final String BASE_PATH = "hpvRacer";
	public  static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public  static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/hpvRacer";
	public  static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/hpvRacer";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		static {
			sURIMatcher.addURI(AUTHORITY, BASE_PATH, RACES);
			sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", RACES_ID);
		}
	
	@Override
	public boolean onCreate() {
		database = new SQLiteDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	
		// Check if the caller has requested a column which does not exists
		checkColumns(projection);
	
		// Set the table
		queryBuilder.setTables(SQLConstants.TABLE_RACEDATA);
	
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case RACES:
			break;
			
			case RACES_ID:
				// Adding the ID to the original query
				queryBuilder.appendWhere(SQLConstants.COLID + "=" + uri.getLastPathSegment());
			break;
				
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

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
		int rowsDeleted = 0;
		long id = 0;
		
		switch (uriType) {
			case RACES:
				id = sqlDB.insert(SQLConstants.TABLE_RACEDATA, null, values);
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
				rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACEDATA, selection, selectionArgs);
			break;
				
			case RACES_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACEDATA, SQLConstants.COLID + "=" + id, null);
				} else {
					rowsDeleted = sqlDB.delete(SQLConstants.TABLE_RACEDATA, SQLConstants.COLID + "=" + id + " and " + selection, selectionArgs);
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
				rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACEDATA, values, selection, selectionArgs);
			break;
			
			case RACES_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACEDATA, values, SQLConstants.COLID + "=" + id, null);
				} else {
					rowsUpdated = sqlDB.update(SQLConstants.TABLE_RACEDATA, values, SQLConstants.COLID + "=" + id + " and " + selection, selectionArgs);
				}
			break;
			
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		
		String[] available = { SQLConstants.COLID, SQLConstants.READINGTIME, SQLConstants.KEY, 
				SQLConstants.VALUE, SQLConstants.RIDERID, SQLConstants.RACEID, SQLConstants.RIDERLAP, 
				SQLConstants.RACELAP, SQLConstants.UPLOADSTATUS };
		
		if (projection != null) {
			
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns) ) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
			
		}
		
	}

} 