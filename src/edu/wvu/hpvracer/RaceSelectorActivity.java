package edu.wvu.hpvracer;

import net.neilgoodman.android.rest.RaceSearchResponderFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class RaceSelectorActivity extends FragmentActivity {
	public final static String RACETITLE = "edu.wvu.hpvracer.RACETITLE";
	public final static String RACEID = "edu.wvu.hpvracer.RACEID";
	public  String RaceType;
    private ArrayAdapter<ListObject> mAdapter;
    private ListFragment list;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// GET INTENT EXTRAS
    	Bundle extras = getIntent().getExtras();
    	if (extras == null) {
    	    return;
	    }
    	// Get data via the key
    	//RaceType = extras.getString(MainActivity.RACETYPE.toString());
    	RaceType = extras.getString(MainActivity.RACETYPE);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_selector);
        
        mAdapter = new ArrayAdapter<ListObject>(this, R.layout.item_label_list);
        
        // set default race in case of network failure
        ListObject l = new ListObject();
        l.id = Constants.myRand.nextInt();
        l.title = "New Race";
        mAdapter.add(l);

        FragmentManager     fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        // Since we are using the Android Compatibility library
        // we have to use FragmentActivity. So, we use ListFragment
        // to get the same functionality as ListActivity.
        list = new ListFragment();

        ft.add(R.id.race_list, list);
        
        // Let's set our list adapter to a simple ArrayAdapter.
        list.setListAdapter(mAdapter);
        
        // RESTResponderFragments call setRetainedInstance(true) in their onCreate() method. So that means
        // we need to check if our FragmentManager is already storing an instance of the responder.
        RaceSearchResponderFragment responder = (RaceSearchResponderFragment) fm.findFragmentByTag("RESTResponder");
        if (responder == null) {
            responder = new RaceSearchResponderFragment();
            
            // We add the fragment using a Tag since it has no views. It will make the Race REST call
            // for us each time this Activity is created.
            ft.add(responder, "RESTResponder");
        }

        // Make sure you commit the FragmentTransaction or your fragments
        // won't get added to your FragmentManager. Forgetting to call ft.commit()
        // is a really common mistake when starting out with Fragments.
        ft.commit();
        
    }
    

    public ArrayAdapter<ListObject> getArrayAdapter() {
        return mAdapter;
    }
    
    public ListFragment getListFragment() {
    	return list;
    }
    
    public void StartRace(ListObject selectedRace) {
    	
    	Intent intent = new Intent();
    	Boolean ok = false;
    	
    	if (RaceType.equals(MainActivity.DRAGRACE)) {
    		ok = true;
    		intent = new Intent(this, DragRaceActivity.class);
    			
    	} else if (RaceType.equals(MainActivity.ENDURANCERACE)) {
    		ok = true;
        	intent = new Intent(this, EnduranceRaceActivity.class);
    			
    	} else {
    		ok = false;
    		Toast t = Toast.makeText(this, "Race Type Not Found.", Toast.LENGTH_LONG);
    		t.show();
    	}
    	
    	if (ok) {
	    	// store chosen race ID
	    	AppData d = new AppData(getPreferences(0));
	        d.RaceID(selectedRace.id);
	        if (d.RiderLap() == 0) {
	        	d.RiderLap(1);
	        }
	        if (d.RaceLap() == 0){
	        	d.RaceLap(1);
	        }
	        
	        //TODO if race == "none of these", upload ID to cloud with temp name, current dt and race type

	        // set extras and start next activity
	    	intent.putExtra(RACEID, selectedRace.id);
	    	intent.putExtra(RACETITLE, selectedRace.title);
	    	startActivity(intent);
    	}
    }

}
