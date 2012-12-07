package edu.wvu.hpvracer.sqlite.contentProvider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import edu.wvu.hpvracer.AppData;
import edu.wvu.hpvracer.RaceDataUploadManager;
import edu.wvu.hpvracer.sqlite.database.SQLConstants;
import edu.wvu.hpvracer.sqlite.database.SQLConstants.DatabaseActions;
import edu.wvu.hpvracer.sqlite.database.SQLConstants.Tables;


/* USAGE :: INSERT INTO TABLES
 * NOTE: Race data only requires "key", "value" and "reading time" the rest of the values are collected from AppData
 * 
 * long unixTime = System.currentTimeMillis() / 1000L;
 * QueryHelper.Tables t = LapsTable.TABLE_LAPS;
 * Bundle e = new Bundle();
 * e.putString(QueryHelper.DBACTION, SQLConstants.DatabaseActions.insert.toString()); 
 * e.putString(QueryHelper.TABLENAME, t.toString());
 * e.putInt(LapsTable.COLUMN_LAP_NUMBER , 1);
 * e.putLong(LapsTable.COLUMN_LAP_START_TIME, unixTime);
 * 
 * Intent queryIntent = new Intent(this, QueryHelper.class);
 * queryIntent.putExtras(e);
 * startService(queryIntent);
 * 
 */

// extends IntentService to run in separate thread and avoid blocking main process
public class QueryHelper extends IntentService {

	public static final String TABLENAME = "tableName";
	public static final String DBACTION = "dbAction";
	public static final String SELECTFILTER = "selectWhere";
	
	private final int uploadThreshold = 3;  //TODO: make this a configurable user setting
	private static final String TAG = QueryHelper.class.getName();
	private static int RaceDataPostCount; 
	private static final String UPLOADKEY = "uploadKey";
	
	public QueryHelper() {
		super(TAG);
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
    	if (extras == null) {
    	    return;
	    }
    	
    	DatabaseActions dothis = DatabaseActions.valueOf(extras.getString(DBACTION)); 
    	Tables tableSelection = Tables.valueOf(extras.getString(TABLENAME));
    	
    	switch (dothis) {
    	case select:
    		// SELECT RECORDS FROM TABLES
    		switch (tableSelection) {
    		case hpvLaps:
    			Cursor lapsRS = LapsSelect(extras);
    			break;
    		case hpvRaceData:
    			Cursor raceDataRS = RaceDataSelect(extras);
    			break;
        	} // end insert table choice switch
    		break;
    		
    	case insert:
        	// INSERT INTO TABLES
    		switch (tableSelection) {
    		case hpvLaps:
    			LapsInsert(extras);
    			break;
    		case hpvRaceData:
    			RaceDataInsert(extras);
    			break;
        	} // end insert table choice switch
    		break;
    		
    	case update:
    		// UPDATE RECORDS IN TABLES
    		switch (tableSelection) {
    		case hpvLaps:
    			LapsUpdate(extras);
    			break;
    		case hpvRaceData:
    			RaceDataUpdate(extras);
    			break;
        	} // end insert table choice switch
    		break;
    		
    	case delete:
    		// DELETE RECORDS FROM TABLES
    		switch (tableSelection) {
    		case hpvLaps:
    			LapsDelete(extras);
    			break;
    		case hpvRaceData:
    			RaceDataDelete(extras);
    			break;
        	} // end insert table choice switch
    		break;
    	}
	}
	
	
	private void LapsInsert(Bundle d) { 
		// LapsContentProvider --> (Uri) insert(Uri uri, ContentValues values) {
		ContentValues values = new ContentValues();
	    values.put(SQLConstants.COLUMN_LAP_NUMBER, d.getInt(SQLConstants.COLUMN_LAP_NUMBER));
	    values.put(SQLConstants.COLUMN_LAP_START_TIME, d.getLong(SQLConstants.COLUMN_LAP_START_TIME));
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, "local");
		Uri uri = getContentResolver().insert(LapsContentProvider.LAPS_CONTENT_URI, values);
		Log.i("QueryHelper:LapsInsert", uri.toString());
	}

	private void LapsUpdate(Bundle d) { 
		// REQUIRES "UPLOADKEY" = value in status that should be put into the table
		//			"SELECTFILTER" = value to find; the matching records will be updated

		ContentValues values = new ContentValues();
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, d.getString(UPLOADKEY));
		String where = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] selectionArgs = new String[] { d.getString(SELECTFILTER) };
		
		int updateCount = getContentResolver().update(LapsContentProvider.LAPS_CONTENT_URI, values, where, selectionArgs);
		Log.i("QueryHelper:LapsUpdate", ""+updateCount);
	}
	
	private Cursor LapsSelect(Bundle d) { 
		// REQURIES "SELECTFILTER" = value in status that should be selected (probably "local")
		String[] Columns = { SQLConstants.COLUMN_LAP_NUMBER, SQLConstants.COLUMN_LAP_START_TIME };
		String selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] Parameters = new String[] { d.getString(SELECTFILTER)};
		String Sort = null;
		
		Cursor c = getContentResolver().query(LapsContentProvider.LAPS_CONTENT_URI,  Columns, selection,  Parameters,  Sort);
		return c;

	}
	
	private void LapsDelete(Bundle d) { 
		// REQURIES "SELECTFILTER" = value in status that should be selected (probably "local")
		int rowsDeleted = 0;
		String selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] Parameters = new String[] { d.getString(SELECTFILTER)};
		
		rowsDeleted = getContentResolver().delete(LapsContentProvider.LAPS_CONTENT_URI, selection, Parameters);
		Log.i("QueryHelper:LapsDelete", ""+rowsDeleted);
	}
	
	
	private void RaceDataInsert(Bundle i) {
		
		AppData d = new AppData();

		ContentValues values = new ContentValues();
		
		//raceIdValue = key + : + unixtime, e.g. SPEED:123894725
		String raceIdValue = i.getString(SQLConstants.COLUMN_KEY) + ":" + i.getLong(SQLConstants.COLUMN_READING_TIME); 
		values.put(SQLConstants.COLUMN_ID, raceIdValue);
		values.put(SQLConstants.COLUMN_VALUE, i.getInt(SQLConstants.COLUMN_VALUE));
	    values.put(SQLConstants.COLUMN_KEY, i.getString(SQLConstants.COLUMN_KEY.toString()));
	    values.put(SQLConstants.COLUMN_READING_TIME, i.getLong(SQLConstants.COLUMN_READING_TIME));
	    
	    values.put(SQLConstants.COLUMN_RACE_ID, d.RaceID() );
	    values.put(SQLConstants.COLUMN_RACE_LAP, d.RaceLap() );
	    values.put(SQLConstants.COLUMN_RIDER_ID, d.RiderID() );
	    values.put(SQLConstants.COLUMN_RIDER_LAP, d.RiderLap() );
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, "local");
	    
		Uri uri = getContentResolver().insert(RacesContentProvider.RACE_CONTENT_URI, values);
		Log.i("QueryHelper:RacesURI", uri.toString());
		RaceDataPostCount++;
		
		if (RaceDataPostCount > uploadThreshold) {
			RaceDataPostCount = 0;
			
			Intent UploadRaceData = new Intent(this, RaceDataUploadManager.class);
	    	startService(UploadRaceData);
			
		}
	}
	
	private void RaceDataUpdate(Bundle d) { 
		// REQUIRES "UPLOADKEY" = value in status that should be put into the table
		//			"SELECTFILTER" = value to find; the matching records will be updated

		ContentValues values = new ContentValues();
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, d.getString(UPLOADKEY));
		String where = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] selectionArgs = new String[] { d.getString(SELECTFILTER) };
		
		int updateCount = getContentResolver().update(RacesContentProvider.RACE_CONTENT_URI, values, where, selectionArgs);
		Log.i("QueryHelper:LapsUpdate", ""+updateCount);
	}
	
	private Cursor RaceDataSelect(Bundle d) { 
		// REQURIES "SELECTFILTER" = value in status that should be selected (probably "local")
		String[] Columns = { SQLConstants.COLUMN_KEY, SQLConstants.COLUMN_RACE_ID, SQLConstants.COLUMN_RACE_LAP,
				SQLConstants.COLUMN_READING_TIME, SQLConstants.COLUMN_RIDER_ID, SQLConstants.COLUMN_RIDER_LAP,
				SQLConstants.COLUMN_VALUE };
		String selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] Parameters = new String[] { d.getString(SELECTFILTER)};
		String Sort = null;
		
		Cursor c = getContentResolver().query(RacesContentProvider.RACE_CONTENT_URI,  Columns, selection,  Parameters,  Sort);
		return c;
	}
	
	private void RaceDataDelete(Bundle d) { 
		// REQURIES "SELECTFILTER" = value in status that should be selected (probably "local")
		int rowsDeleted = 0;
		String selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] Parameters = new String[] { d.getString(SELECTFILTER)};
		
		rowsDeleted = getContentResolver().delete(RacesContentProvider.RACE_CONTENT_URI, selection, Parameters);
		Log.i("QueryHelper:LapsDelete", ""+rowsDeleted);
	}

}
