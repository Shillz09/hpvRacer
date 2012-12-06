package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import edu.wvu.hpvracer.sqlite.contentProvider.QueryHelper;
import edu.wvu.hpvracer.sqlite.database.LapsTable;
import edu.wvu.hpvracer.sqlite.database.RacesTable;

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
         
        ((TextView)findViewById(R.id.title)).setText(raceName + " -- " + raceID);
        ((TextView)findViewById(R.id.speed_display)).setText(getString(R.string.speed) + " " + getString(R.string.speed_units));
        ((TextView)findViewById(R.id.cadence_display)).setText(getString(R.string.cadence) + " " + getString(R.string.cadence_units));
        
        testSqlite();
    }
    
    protected void testSqlite() {
    	
    	//laps table
    	long unixTime = System.currentTimeMillis() / 1000L;
    	QueryHelper.Tables l = LapsTable.TABLE_LAPS;
    	Bundle e = new Bundle();
    	e.putString(QueryHelper.TABLENAME, l.toString());
    	e.putInt(LapsTable.COLUMN_LAP_NUMBER , 1);
    	e.putLong(LapsTable.COLUMN_LAP_START_TIME, unixTime);
    	
    	 Intent lapsUpdateIntent = new Intent(this, QueryHelper.class);
    	 lapsUpdateIntent.putExtras(e);
    	 startService(lapsUpdateIntent);
    	 
    	 //race data table (endurance or drag)
    	 QueryHelper.Tables r = RacesTable.TABLE_RACE_DATA;
    	 unixTime = System.currentTimeMillis() / 1000L;
    	 e.clear();
    	 e.putString(QueryHelper.TABLENAME,  r.toString());
    	 e.putString(RacesTable.COLUMN_KEY, "Speed");
    	 e.putInt(RacesTable.COLUMN_VALUE, 20);
    	 e.putLong(RacesTable.COLUMN_READING_TIME, unixTime);
    	 
    	 Intent raceDataAddIntent = new Intent(this, QueryHelper.class);
    	 raceDataAddIntent.putExtras(e);
    	 startService(raceDataAddIntent);
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
