package edu.wvu.hpvracer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LocalRaceDataActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_race_data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_local_race_data, menu);
		return true;
	}

}
