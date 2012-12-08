package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        ((TextView)findViewById(R.id.currentSettings)).setText(AppData.ToString());
        
    }
        
    public void clearSettings(View view){
    	AppData data = new AppData(getPreferences(0));
    	data.EndRace();
    	((TextView)findViewById(R.id.currentSettings)).setText(AppData.ToString());
    	Toast toast = Toast.makeText(this, "Settings cleared", Toast.LENGTH_SHORT);
    	toast.show();
    }

    public void goHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}
    
    public void cloudRaceData(View view) {
    	Intent intent = new Intent(this, CloudRaceData.class);
    	startActivity(intent);
    }
    
    public void aboutUs(View view) {
    	Intent intent = new Intent(this, AboutUs.class);
    	startActivity(intent);
    }
    
}
