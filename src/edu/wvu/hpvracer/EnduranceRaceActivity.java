package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.wvu.hpvracer.sqlite.contentProvider.QueryHelper;
import edu.wvu.hpvracer.sqlite.database.SQLConstants;

public class EnduranceRaceActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_endurance_race);
        
        Intent intent = getIntent();
        
        String raceName = intent.getStringExtra(RaceSelectorActivity.RACETITLE);
        
        if (raceName == null || raceName.isEmpty()) {
        	raceName = "New Race";
        }
        
         int raceID = intent.getIntExtra(RaceSelectorActivity.RACEID, 0);
         AppData raceData = new AppData(getPreferences(0));
         raceData.RaceID(raceID);
         if (raceData.RaceLap() == 0) {
        	 raceData.RaceLap(1);
        	 raceData.RiderLap(1);
        	 raceData.NewRider();
         }
         
        ((TextView)findViewById(R.id.title)).setText(raceName + " -- " + raceID);
        ((TextView)findViewById(R.id.speed_display)).setText(getString(R.string.speed) + " " + getString(R.string.speed_units));
        ((TextView)findViewById(R.id.cadence_display)).setText(getString(R.string.cadence) + " " + getString(R.string.cadence_units));
        
        testSqlite();
    }
    
    protected void testSqlite() {
    	
    	long unixTime = Utilities.unixtime();
    	Bundle e = new Bundle();
    	
    	// LAPS TABLE
    	e.putString(QueryHelper.DBACTION, SQLConstants.DatabaseActions.insert.toString());
    	e.putString(QueryHelper.TABLENAME, SQLConstants.TABLE_LAPS);
    	e.putInt(SQLConstants.COLUMN_LAP_NUMBER , Constants.myRand.nextInt( Integer.MAX_VALUE ) + 1);
    	e.putLong(SQLConstants.COLUMN_LAP_START_TIME, unixTime);
    	
    	Intent lapsUpdateIntent = new Intent(this, QueryHelper.class);
    	lapsUpdateIntent.putExtras(e);
    	startService(lapsUpdateIntent);
    	
    	 // RACE DATA TABLE (endurance or drag)
    	 e.clear();
    	 e.putString(QueryHelper.DBACTION, SQLConstants.DatabaseActions.insert.toString());
    	 e.putString(QueryHelper.TABLENAME,  SQLConstants.TABLE_RACE_DATA);
    	 e.putString(SQLConstants.COLUMN_KEY, "Speed");
    	 e.putInt(SQLConstants.COLUMN_VALUE, 20);
    	 e.putLong(SQLConstants.COLUMN_READING_TIME, unixTime);
    	 Log.i("Race data extras", e.toString());
    	 
    	 Intent raceDataAddIntent = new Intent(this, QueryHelper.class);
    	 raceDataAddIntent.putExtras(e);
    	 startService(raceDataAddIntent);
    	 Log.i("EnduranceRaceActivity", "startService(raceDataAddIntent) is called");
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
    	AppData d  = new AppData(getPreferences(0));
    	d.EndRace();
    }
    
}
