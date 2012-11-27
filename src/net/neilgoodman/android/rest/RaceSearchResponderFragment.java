package net.neilgoodman.android.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.wvu.hpvracer.DragRaceSelectorActivity;
import edu.wvu.hpvracer.ListObject;

public class RaceSearchResponderFragment extends RESTResponderFragment {
    private static String TAG = RaceSearchResponderFragment.class.getName();
    
    // We cache our stored races here so that we can return right away
    // on multiple calls to setRaces() during the Activity lifecycle events (such
    // as when the user rotates their device). In a real application we would want
    // to cache this data in a more sophisticated way, probably using SQLite and
    // Content Providers, but for the demo and simple apps this will do.
    private List<ListObject> mRaces;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // This gets called each time our Activity has finished creating itself.
        setRaces();
    }

    private void setRaces() {
        final DragRaceSelectorActivity activity = (DragRaceSelectorActivity) getActivity();
        
        if (mRaces == null && activity != null) {
            // This is where we make our REST call to the service. We also pass in our ResultReceiver
            // defined in the RESTResponderFragment super class.
            
            // We will explicitly call our Service since we probably want to keep it as a private
            // component in our app. You could do this with Intent actions as well, but you have
            // to make sure you define your intent filters correctly in your manifest.
            Intent intent = new Intent(activity, RESTService.class);
            intent.setData(Uri.parse("http://50weasels.com/HCI/getRaces.php"));
            
            // Here we are going to place our REST call parameters. Note that
            // we could have just used Uri.Builder and appendQueryParameter()
            // here, but I wanted to illustrate how to use the Bundle params.
            Bundle params = new Bundle();
            params.putString("d", "2012/11/24");
            params.putString("t", "Drag");
            
            intent.putExtra(RESTService.EXTRA_PARAMS, params);
            intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, getResultReceiver());
            
            // Here we send our Intent to our RESTService.
            activity.startService(intent);
        }
        else if (activity != null) {
            // Here we check to see if our activity is null or not.
            // We only want to update our views if our activity exists.
            
            ArrayAdapter<ListObject> adapter = activity.getArrayAdapter();
            
            // Load our list adapter with our Races.
            adapter.clear();
            for (ListObject race : mRaces) {
                adapter.add(race);
            }
            
            // Add a listener for clicks on the list (rm)
            ListFragment myList = activity.getListFragment();
            final ListView lv = myList.getListView();
            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListObject selectedFromList =(ListObject) (lv.getItemAtPosition(position));
                    Log.i("debug", selectedFromList.title);
                    activity.StartRace(selectedFromList);
                }

            });

        }
    }
    
    @Override
    public void onRESTResult(int code, String result) {
        // Here is where we handle our REST response. This is similar to the 
        // LoaderCallbacks<D>.onLoadFinished() call from the previous tutorial.
        
        // Check to see if we got an HTTP 200 code and have some data.
        if (code == 200 && result != null) {
            
            // For really complicated JSON decoding I usually do my heavy lifting
            // with Gson and proper model classes, but for now let's keep it simple
            // and use a utility method that relies on some of the built in
            // JSON utilities on Android.
            mRaces = getRacesFromJson(result);
            ListObject l = new ListObject();
            l.id = 0;
            l.title = "None of these";
            mRaces.add(l);
            setRaces();
        }
        else {
            Activity activity = getActivity();
            if (activity != null) {
                Toast.makeText(activity, "Failed to load Race data. Check your internet settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private static List<ListObject> getRacesFromJson(String json) {
        ArrayList<ListObject> raceList = new ArrayList<ListObject>();
        
        try {
            JSONObject racesWrapper = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray  races        = racesWrapper.getJSONArray("results");
            
            for (int i = 0; i < races.length(); i++) {
                JSONObject race = races.getJSONObject(i);
                ListObject l = new ListObject();
                l.id = race.getInt("RaceID");
                l.title = race.getString("RaceName");
                raceList.add(l);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON. " + e.getMessage(), e);
        }
        
        return raceList;
    }

}
