package edu.wvu.hpvracer;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class EnduranceRaceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String message = "you're in the endurance race component";
        
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        
        setContentView(textView);
    }

}
