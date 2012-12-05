package edu.wvu.hpvracer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AppData extends Activity {
	
	private static int riderID;
	private static int raceID;
	private static int raceLapNumber;
	private static int riderLapNumber;
	private static int ghostSpeed;
	private static int ghostCadence;
	private static SharedPreferences settings;
	private boolean readOnly;
	//= getPreferences(0);
	
	public AppData() {
		// empty constructor used to access static values -- can not init from SharedPreferences
		readOnly = true;
	}
	public AppData(SharedPreferences p) {
		settings = p;
		riderID = settings.getInt(Constants.RIDERID, -1);
		raceID = settings.getInt(Constants.RACEID, -1);
		raceLapNumber = settings.getInt(Constants.RACELAP, 0);
		riderLapNumber = settings.getInt(Constants.RIDERLAP, 0);
		ghostCadence = settings.getInt(Constants.GHOSTCADENCE, 80);
		ghostSpeed = settings.getInt(Constants.GHOSTSPEED, 35);
		readOnly = false;
	}
	
	public void RiderID(int i) {
		if (!readOnly) {
			riderID = i;
			storeGlobalVarInt(Constants.RIDERID, i);
		} else {
			MakeToast("Rider ID");
		}
	}
	public int RiderID() {
		return riderID;
	}
	
	public void RaceID(int i) {
		if (!readOnly) {
			raceID = i;
			storeGlobalVarInt(Constants.RACEID, i);
		} else {
			MakeToast("Race ID");
		}
	}
	public int RaceID() {
		return raceID;
	}
	
	public void RaceLap(int i) {
		if (!readOnly) {
			raceLapNumber = i;
			storeGlobalVarInt(Constants.RACELAP , i);
		} else {
			MakeToast("Race lap");
		}
	}
	public int RaceLap() {
		return raceLapNumber;
	}
	
	public void RiderLap(int i){
		if (!readOnly) {
			riderLapNumber = i;
			storeGlobalVarInt(Constants.RIDERLAP, i);
		} else {
			MakeToast("Rider Lap");
		}
	}
	public int RiderLap() {
		return riderLapNumber;
	}
	
	public void GhostCadence(int i) {
		if (!readOnly) {
			ghostCadence = i;
			storeGlobalVarInt(Constants.GHOSTCADENCE, i);
		} else {
			MakeToast("Ghost Cadence");
		}
	}
	public int GhostCadence() {
		return ghostCadence;
	}
	
	public void GhostSpeed(int i){
		if (!readOnly) {
			ghostSpeed = i;
			storeGlobalVarInt(Constants.GHOSTSPEED, i);
		} else {
			MakeToast("Ghost Speed");
		}
	}
	public int GhostSpeed() {
		return ghostSpeed;
	}
    
    public void IncrementLap(boolean sameRider){
    	if (!readOnly) {
	    	raceLapNumber++;
	    	if (sameRider) {
	    		riderLapNumber++;
	    	} else {
	    		NewRider();
	    	}
	    	storeGlobalVarInt(Constants.RIDERLAP, riderLapNumber);
			storeGlobalVarInt(Constants.RACELAP, raceLapNumber);
    	} else {
    		MakeToast("New Lap");
    	}
    }
    
	public void NewRider() {
		if (!readOnly) {
			riderID++;
			riderLapNumber = 1;
			storeGlobalVarInt(Constants.RIDERID, riderID);
			storeGlobalVarInt(Constants.RIDERLAP, riderLapNumber);
		} else {
			MakeToast("New Rider");
		}
	}
	
	public void EndRace() {
		if (!readOnly) {
			raceID = 0;
			raceLapNumber = 0;
			riderLapNumber = 0;
			riderID = riderID + 1;
			storeGlobalVarInt(Constants.RACEID, raceID);
			storeGlobalVarInt(Constants.RIDERID, riderID);
			storeGlobalVarInt(Constants.RIDERLAP, riderLapNumber);
			storeGlobalVarInt(Constants.RACELAP, raceLapNumber);
		} else {
			MakeToast("End Race");
		}
	}
	
	public static String ToString() {
		String data = null;
		
		data = "Rider ID = " + riderID;
		data += "\nRider Lap Number = " + riderLapNumber;
		data += "\nRace ID = " + raceID;
		data += "\nRace Lap Number = " + raceLapNumber;
		data += "\nGhost Speed = " + ghostSpeed;
		data += "\nGhost Cadence = " + ghostCadence;
		
		return data;
	}
	

    private void storeGlobalVarInt(String varName, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(varName, value);
        editor.commit();
    }
    
    private void MakeToast(String d) {
    	Toast t = Toast.makeText(this, "Can not save data, " + d + ": AppData is in readOnly mode. Create using 'AppData(getPreferences(0))' to edit.", Toast.LENGTH_LONG);
		t.show();
    }

    /*
    private void storeGlobalVarString(String varName, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(varName, value);
        editor.commit();
    }
    private void storeGlobalVarLong(String varName, long value) {
    	SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(varName, value);
        editor.commit();
    }
	*/
}
