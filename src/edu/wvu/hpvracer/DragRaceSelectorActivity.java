package edu.wvu.hpvracer;

import net.neilgoodman.android.rest.RaceSearchResponderFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
//TODO: note this loaded the list of races fine on home network. Why didn't races load at school?
// Can the lack of races as soon on campus be duplicated?
// Handle case of no network connection or slow network: show "none of these" before races load as well as after
public class DragRaceSelectorActivity extends FragmentActivity {
	public final static String RACETITLE = "edu.wvu.hpvracer.RACETITLE";
	public final static String RACEID = "edu.wvu.hpvracer.RACEID";
    private ArrayAdapter<ListObject> mAdapter;
    private ListFragment list;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_race_selector);
        
        mAdapter = new ArrayAdapter<ListObject>(this, R.layout.item_label_list);
        
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
    	Intent intent = new Intent(this, DragRaceActivity.class);
    	intent.putExtra(RACEID, selectedRace.id);
    	intent.putExtra(RACETITLE, selectedRace.title);
    	
    	startActivity(intent);
    }

}
