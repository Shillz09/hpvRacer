package edu.wvu.hpvracer;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
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
	
    private boolean uploadSuccessAll;
    private String uploadKeyResponse;
    ArrayList<JsonResultDetail> uploadResultDetail = new ArrayList<JsonResultDetail>();
    
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
			String jsonObject = JsonMaker();
			//Log.i("RaceDataUploadManager", jsonObject);
			
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
	
	private int SetLocalStatus(String key) {
		
		// key can be "all" (upload all records where the upload_status column = uploadKeyResponse
		// key can be a row ID for a particular row in the races data table
		ContentValues values = new ContentValues();
		String where;
		String[] parameters;
	    values.put(SQLConstants.COLUMN_UPLOAD_STATUS, SQLConstants.UPLOAD_STATUS_LOCAL);
	    
		if (key == "all") {
			//used on upload failure; sets Upload_Status column back to "local" for records with uploadKey
		    where = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
		    parameters = new String[] { uploadKeyResponse };
		} else {
		    where = SQLConstants.COLUMN_ID + " = ?";
		    parameters = new String[] { key };			
		}
	    
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
	
	private String JsonMaker() {
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
		
		if (code == 200 && result != null) {
			
			// http success result, see what the JSON says
			if (decodeJson(result)) {
				
				// JSON decoded; see if all records, or just some were posted
				if (uploadSuccessAll) {
					handleRestSuccess();
				} else {
					handleRestPartialFailure();
				}
				
			// JSON decode failure; could be in 1st or 2nd JSON array; see if we have a total result success so we can proceed 
			} else if (uploadSuccessAll) {
				handleRestSuccess();
				
			// JSON decode failure; could not find any information from JSON reply
			} else {
				handleRestFailure();
			}

		} else {
			
			handleRestFailure();
        }
	}
	
    private boolean decodeJson(String json) {
        // gets values out of JSON string and stores in class-wide variables;
    	// returns true for successful decode; false for failure at ANY point.
    	// expects JSON object containing 2 arrays "results" and "uploadResult"
    	
    	boolean decodeSuccess = false;
    	JSONArray individualRecordResults;
        JSONObject uploadSuccess;
        
        try {
        	
            JSONObject resultWrapper = (JSONObject) new JSONTokener(json).nextValue();
            
            individualRecordResults = resultWrapper.getJSONArray("results");
            JSONArray temp = resultWrapper.getJSONArray("uploadResult");
            uploadSuccess = temp.getJSONObject(0);
            
            uploadSuccessAll = Boolean.parseBoolean(uploadSuccess.getString("success"));
            uploadKeyResponse = uploadSuccess.getString("uploadKey");

            // go from 1st rec to penultimate; last record is "end" with value boolean
            for (int i = 0; i < individualRecordResults.length()-1; i++) {
            	JsonResultDetail detail = new JsonResultDetail();
                JSONObject rec = individualRecordResults.getJSONObject(i);
                detail.raceDataTableRowID = rec.getString("rowKey");
                detail.postSuccess = Boolean.parseBoolean(rec.getString("success"));
                uploadResultDetail.add(detail);
            }
            
            decodeSuccess = true;
        }
        
        catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON. " + e.getMessage(), e);
        }
        
        return decodeSuccess;
    }
    
    private void handleRestFailure() {
    	// total failure; mark all data as local
    	SetLocalStatus("all");
    }
    
    private void handleRestPartialFailure() {
    	// iterate through results and handle various success and failures
    	for (JsonResultDetail r : uploadResultDetail) {
    	    if(r.postSuccess){
    	        deleteUploaded(r.raceDataTableRowID);
    	    } else {
    	    	SetLocalStatus(r.raceDataTableRowID);
    	    }
    	}
    }
    
    private void handleRestSuccess() {
		// delete local records matching upload key
    	deleteUploaded("all");
    	
		// start GCM to notify tablet of new data
    	
    }
    
    private int deleteUploaded(String key) {
		// key can be "all" (upload all records where the upload_status column = uploadKeyResponse
		// key can be a row ID for a particular row in the races data table
    	String selection;
    	String[] Parameters;
		int rowsDeleted = 0;
		
		if (key == "all") {
			selection = SQLConstants.COLUMN_UPLOAD_STATUS + " = ?";
			Parameters = new String[] { uploadKeyResponse };
			
		} else {
			selection = SQLConstants.COLUMN_ID + " = ?";
			Parameters = new String[] { key };
			
		}
	    
		rowsDeleted = getContentResolver().delete(RacesContentProvider.RACE_CONTENT_URI, selection, Parameters);
		//Log.i("QueryHelper:LapsDelete", ""+rowsDeleted);
		return rowsDeleted;

    }

}
