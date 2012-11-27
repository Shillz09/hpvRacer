package edu.wvu.hpvracer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String message = "you're in the settings component";
        
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        
        setContentView(textView);

    }
    
    public void changeRace(View view){
    	//TODO: put new race record in table, get ID, use toast to display success, return home
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }

}
