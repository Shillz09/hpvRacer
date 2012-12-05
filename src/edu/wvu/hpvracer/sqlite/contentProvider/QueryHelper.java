package edu.wvu.hpvracer.sqlite.contentProvider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import edu.wvu.hpvracer.AppData;
import edu.wvu.hpvracer.MainActivity;
import edu.wvu.hpvracer.sqlite.database.LapsTable;
import edu.wvu.hpvracer.sqlite.database.RacesTable;

// extends IntentService to run in separate thread and avoid blocking main process
public class QueryHelper extends IntentService {

	public static final String TABLENAME = "tableName";
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
    	
    	Tables selection = (Tables)extras.get(TABLENAME);
    			
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
	    values.put(LapsTable.COLUMN_LAP_START_TIME, d.getInt(LapsTable.COLUMN_LAP_START_TIME));
		Uri uri = getContentResolver().insert(LapsContentProvider.CONTENT_URI, values);
	}
	
	
	private void RaceDataInsert(Bundle i) {
		
		AppData d = new AppData();
		
		ContentValues values = new ContentValues();
	    values.put(RacesTable.COLUMN_VALUE, i.getInt(RacesTable.COLUMN_VALUE));
	    values.put(RacesTable.COLUMN_KEY, i.getString(RacesTable.COLUMN_KEY));
	    values.put(RacesTable.COLUMN_READING_TIME, i.getInt(RacesTable.COLUMN_READING_TIME));
	    
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
