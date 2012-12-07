package edu.wvu.hpvracer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RaceDataUploadManager extends IntentService {

	private static final String TAG = RaceDataUploadManager.class.getName();
	
	public RaceDataUploadManager() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		Log.i("RaceDataUploadManager", "HELLO!");
		
		//TODO: create upload key ("uploading:" + unixtime)
		//TODO: update status set to upload key
		//TODO: query for rows where status = upload key
		//TODO: pass cursor to JSON process & get back JSON object
		//TODO: call REST upload process, sending JSON
		//TODO: get JSON result from REST process, including upload key
		//TODO: delete local records matching upload key

	}

}
