package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DragRaceActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_race);
        
        Intent intent = getIntent();
        String raceName = intent.getStringExtra(RaceSelectorActivity.RACETITLE);
        int raceID = intent.getIntExtra(RaceSelectorActivity.RACEID, 0);
        
        ((TextView)findViewById(R.id.title)).setText(raceName + " -- " + raceID);
        ((TextView)findViewById(R.id.speed_display)).setText(getString(R.string.speed) + " " + getString(R.string.speed_units));
        ((TextView)findViewById(R.id.cadence_display)).setText(getString(R.string.cadence) + " " + getString(R.string.cadence_units));
    }
     
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    public void endRace(View view){
    	updateGlobals();
    	//TODO:  do final server upload
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    
    private void updateGlobals() {
    	SharedPreferences prefs = getPreferences(0);
    	AppData d  = new AppData(prefs);
    	d.EndRace();
    }
    
}
