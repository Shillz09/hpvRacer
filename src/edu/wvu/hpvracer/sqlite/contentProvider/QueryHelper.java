package edu.wvu.hpvracer.sqlite.contentProvider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import edu.wvu.hpvracer.AppData;
import edu.wvu.hpvracer.sqlite.database.SQLConstants.*;
import edu.wvu.hpvracer.sqlite.database.SQLConstants;


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
	
	private final int uploadThreshold = 50;
	private static final String TAG = QueryHelper.class.getName();
	private static int RaceDataPostCount; 
	
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
    		break;
    		
    	case insert:
        	// insert into one of the tables...
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
    		break;
    		
    	case delete:
    		break;
    		
    	}
    	
	}
	
	
	private void LapsInsert(Bundle d) { 
		ContentValues values = new ContentValues();
	    values.put(SQLConstants.COLUMN_LAP_NUMBER, d.getInt(SQLConstants.COLUMN_LAP_NUMBER));
	    values.put(SQLConstants.COLUMN_LAP_START_TIME, d.getLong(SQLConstants.COLUMN_LAP_START_TIME));
		Uri uri = getContentResolver().insert(LapsContentProvider.LAPS_CONTENT_URI, values);
		Log.i("QueryHelper:LapsURI", uri.toString());
	}
	
	
	private void RaceDataInsert(Bundle i) {
		
		AppData d = new AppData();

		ContentValues values = new ContentValues();
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
			//TODO: query local DB, bundle data into JSON and send to cloud
			//TODO: on completion of upload, remove local data from db
		}
	}

}
