package edu.wvu.hpvracer.sqlite.contentProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LapsTableQuery extends Activity {

    private int lapNum;
    private int lapStart;
	
	// constructor
	public LapsTableQuery(int lapNumberIn, int lapStartIn) {
		lapNum = lapNumberIn;
		lapStart = lapStartIn;
		storeLap();
	}
	
    public void storeLap() {
    	
    	Activity activity = new Activity();
    	Intent intent = new Intent(this, QueryService.class);
        
        Bundle params = new Bundle();
        params.putInt("num", lapNum);
        params.putInt("start", lapStart);
        
        intent.putExtra("input", params);
        
        activity.startService(intent);
        
    }
	
}
