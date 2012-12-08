package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	final String TAG="edu.wvu.hpvracer MainActivity";
	public static final String RACETYPE = "RaceType";
	public static final String DRAGRACE = "Drag";
	public static final String ENDURANCERACE = "Endurance";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppData.init(getPreferences(0));
        
        setContentView(R.layout.activity_main);
        
        //TODO: implement this for the tablet MainActivity; not needed on phone
        //
        // GCM (google cloud messaging)
        // GCMRegistrar.checkDevice(this);
        // GCMRegistrar.checkManifest(this);	// this is needed for testing; can be removed when published
        // String regId = GCMRegistrar.getRegistrationId(this);
        // if (regId.equals("")) {
        // 	GCMRegistrar.register(this, Constants.SENDER_ID);
        // } else {
        // 	Log.v(TAG, "Already registered");
        // }
        // final String FinalRegId = GCMRegistrar.getRegistrationId(this);
        // AppData.storeGlobalVariable(Constants.GCMREGID, FinalRegId);
        
        // create tables
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void startDragRace(View view){
    	Intent intent = new Intent(this, RaceSelectorActivity.class);
    	intent.putExtra(RACETYPE, DRAGRACE);
    	startActivity(intent);
    }
    
    public void startEnduranceRace(View view){
    	Intent intent = new Intent(this, RaceSelectorActivity.class);
    	intent.putExtra(RACETYPE, ENDURANCERACE);
    	startActivity(intent);
    }
    
    public void startSettings(View view){
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }

}
