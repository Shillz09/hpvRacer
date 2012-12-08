package edu.wvu.hpvracer;

import net.neilgoodman.android.rest.RESTService;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import edu.wvu.hpvracer.sqlite.contentProvider.QueryHelper;
import edu.wvu.hpvracer.sqlite.contentProvider.RacesContentProvider;
import edu.wvu.hpvracer.sqlite.database.SQLConstants;

public class RaceDataUploadManager extends IntentService {

	private static final String TAG = RaceDataUploadManager.class.getName();
	private String uploadKey = Utilities.uploadKey();
	private Cursor c;
    private ResultReceiver mReceiver = new ResultReceiver(new Handler()) {

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData != null && resultData.containsKey(RESTService.REST_RESULT)) {
                onRESTResult(resultCode, resultData.getString(RESTService.REST_RESULT));
            }
            else {
                onRESTResult(resultCode, null);
            }
        }
        
    };
	
	public RaceDataUploadManager() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		// update status set to upload key
		int recsToUpload = SetUploadStatus();
		
		if (recsToUpload > 0) {
			// query for rows where status = upload key
			c = SelectRowsForUpload();
				
			// pass cursor to JSON process & get back JSON object
			String jsonObject = JsonParser();
			Log.i("RaceDataUploadManager", jsonObject);
			
			intent = new Intent(this, RESTService.class);
	        intent.setData(Uri.parse("http://50weasels.com/HCI/addRaceData.php"));
	            
	        Bundle params = new Bundle();
	        params.putString("json", jsonObject);
	            
	        intent.putExtra(RESTService.EXTRA_PARAMS, params);
	        intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, mReceiver);
	           
	        startService(intent);
			
		}
	}
	
	private int SetUploadStatus() {
		// using RacesContentProvider directly since this manager is already
		// running externally to the main thread; direct calls prevent the 
		// need to create listeners to sequence events.
		
		ContentValues values = new ContentValues();
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, uploadKey);
	    String where = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
	    String[] parameters = new String[] { SQLConstants.UPLOAD_STATUS_LOCAL };
	    
		int updates = getContentResolver().update(RacesContentProvider.RACE_CONTENT_URI, values, where, parameters);
		return updates;
		
	}
	
	
	private Cursor SelectRowsForUpload() {
		String[] projection = { SQLConstants.COLUMN_KEY, SQLConstants.COLUMN_VALUE, SQLConstants.COLUMN_READING_TIME,
				SQLConstants.COLUMN_RIDER_ID, SQLConstants.COLUMN_RACE_ID, SQLConstants.COLUMN_RACE_LAP, 
				SQLConstants.COLUMN_RIDER_LAP };
		String selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		String[] parameters = { uploadKey };
		
		c = getContentResolver().query(RacesContentProvider.RACE_CONTENT_URI, projection, selection, parameters, null);
		
		return c;
	}
	
	private String JsonParser() {
		String json = "{\"" + QueryHelper.UPLOADKEY + "\":[{\"" + QueryHelper.UPLOADKEY + "\":\"" + uploadKey + "\"}], ";
			  json += "\"raceDataUpload\":[";
		String jsonEnd = "]}";
		
		c.moveToFirst();
		while (c.isAfterLast() == false) 
		{
			json += "{\"" + SQLConstants.COLUMN_KEY + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_KEY)) + "\",";
			json += "\"" + SQLConstants.COLUMN_VALUE + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_VALUE)) + "\",";
			json += "\"" + SQLConstants.COLUMN_READING_TIME + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_READING_TIME)) + "\",";
			json += "\"" + SQLConstants.COLUMN_RIDER_ID + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_RIDER_ID)) + "\",";
			json += "\"" + SQLConstants.COLUMN_RACE_ID + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_RACE_ID)) + "\",";
			json += "\"" + SQLConstants.COLUMN_RACE_LAP + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_RACE_LAP)) + "\",";
			json += "\"" + SQLConstants.COLUMN_RIDER_LAP + "\":\"" + c.getString(c.getColumnIndex(SQLConstants.COLUMN_RIDER_LAP)) + "\"}";

			if (!c.isLast()) {
				json += ",";
			}

		    c.moveToNext();
		}
		c.close();
		return json + jsonEnd;
	}
	
	public void onRESTResult(int code, String result) {
		
		Log.i("RaceDataUploadManager.onRESTResult", ""+code);
		Log.i("RaceDataUploadManager.onRESTResult", result);

		if (code == 200 && result != null) {
			
			//TODO: upon REST success result, delete local records matching upload key
			
			
			//TODO: upon REST success result, start GCM to notify tablet of new data

		} else {
			
			//TODO: upon REST fail result, set status of data back to 'local' where = uploadKey
			
        }
	}

}
