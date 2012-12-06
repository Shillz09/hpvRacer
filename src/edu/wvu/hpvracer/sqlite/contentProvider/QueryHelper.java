package edu.wvu.hpvracer.sqlite.contentProvider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import edu.wvu.hpvracer.AppData;
import edu.wvu.hpvracer.sqlite.database.LapsTable;
import edu.wvu.hpvracer.sqlite.database.RacesTable;


/* USAGE
 * 
    	long unixTime = System.currentTimeMillis() / 1000L;
    	QueryHelper.Tables t = LapsTable.TABLE_LAPS;
    	Bundle e = new Bundle();
    	e.putString(QueryHelper.TABLENAME, t.toString());
    	e.putInt(LapsTable.COLUMN_LAP_NUMBER , 1);
    	e.putLong(LapsTable.COLUMN_LAP_START_TIME, unixTime);
    	
    	 Intent queryIntent = new Intent(this, QueryHelper.class);
    	 queryIntent.putExtras(e);
    	 startService(queryIntent);
 * 
 */

// extends IntentService to run in separate thread and avoid blocking main process
public class QueryHelper extends IntentService {

	public static final String TABLENAME = "tableName";
	public static final String EXTRAS = "e";
	
	public enum Tables {
		hpvRaceData, hpvLaps
	}
	
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
    	
    	String sel = extras.getString(TABLENAME);
    	Tables selection = Tables.valueOf(sel); 
    			
    	switch (selection) {
    	
    		case hpvLaps:
    			LapsInsert(extras);
    			break;
    			
    		case hpvRaceData:
    			RaceDataInsert(extras);
    			break;
    	}
    	
	}
	
	
	private void LapsInsert(Bundle d) { 
		ContentValues values = new ContentValues();
	    values.put(LapsTable.COLUMN_LAP_NUMBER, d.getInt(LapsTable.COLUMN_LAP_NUMBER));
	    values.put(LapsTable.COLUMN_LAP_START_TIME, d.getLong(LapsTable.COLUMN_LAP_START_TIME));
		Uri uri = getContentResolver().insert(LapsContentProvider.CONTENT_URI, values);
		Log.i("URI", uri.toString());
	}
	
	
	private void RaceDataInsert(Bundle i) {
		
		AppData d = new AppData();

		ContentValues values = new ContentValues();
	    values.put(RacesTable.COLUMN_VALUE, i.getInt(RacesTable.COLUMN_VALUE));
	    values.put(RacesTable.COLUMN_KEY, (String)i.getString(RacesTable.COLUMN_KEY));
	    values.put(RacesTable.COLUMN_READING_TIME, i.getLong(RacesTable.COLUMN_READING_TIME));
	    
	    values.put(RacesTable.COLUMN_RACE_ID, d.RaceID() );
	    values.put(RacesTable.COLUMN_RACE_LAP, d.RaceLap() );
	    values.put(RacesTable.COLUMN_RIDER_ID, d.RiderID() );
	    values.put(RacesTable.COLUMN_RIDER_LAP, d.RiderLap() );
	    values.put(RacesTable.COLUMN_UPLOAD_STATUS, "local");
	    
		Uri uri = getContentResolver().insert(RacesContentProvider.CONTENT_URI, values);
		
		RaceDataPostCount++;
		
		if (RaceDataPostCount > uploadThreshold) {
			//TODO: query local DB, bundle data into JSON and send to cloud
			//TODO: on completion of upload, remove local data from db
		}
	}

}
