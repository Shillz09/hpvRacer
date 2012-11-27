package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	final String TAG="edu.wvu.hpvracer MainActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // GCM (google cloud messaging)
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);	// this is needed for testing; can be removed when published
        String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
        	GCMRegistrar.register(this, Constants.SENDER_ID);
        } else {
        	Log.v(TAG, "Already registered");
        }
        final String FinalRegId = GCMRegistrar.getRegistrationId(this);
        storeGlobalVariable(Constants.GCMREGID, FinalRegId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void storeGlobalVariable(String varName, String value) {
    	SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(varName, value);
        editor.commit();
    }
    
    public void startDragRace(View view){
    	Intent intent = new Intent(this, DragRaceSelectorActivity.class);
    	startActivity(intent);
    }
    
    public void startEnduranceRace(View view){
    	Intent intent = new Intent(this, EnduranceRaceActivity.class);
    	startActivity(intent);
    }
    
    public void startSettings(View view){
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }

}
