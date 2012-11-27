package edu.wvu.hpvracer;

import android.content.SharedPreferences;

public class AppData {
	
	public static int riderID = 0;
	protected static int raceID = 0;
	protected static int riderLapNumber = 0;
	public static int lapStartTime = 0;
	protected static int raceLapNumber = 0;
	protected static SharedPreferences settings;

	/**
	 * @param args
	 */
	public static int Rider() {
		//TODO: test if id is set, if not, get next from sqLiteDB
		//query select max(riderID) from db
		return riderID;
	}
	
	public static int Race() {
		//TODO: test if race# is set, if not, get from cloud
		//query: race of [type] on [date]; if more than 1, choose; if none, create empty
		return raceID;
	}
	
	public static int RiderLap() {
		//TODO: test if lap # is set, if not, get from sqLiteDB
		//query db for lap # for rider #; if none, lap = 1
		return riderLapNumber;
	}
	
	public static int RaceLap() {
		//TODO: test if lap # is set, if not, get from sqLiteDB
		//query db for lap # for rider #; if none, lap = 1
		return raceLapNumber;
	}
	
	public static void NewRider() {
		riderID++;
		riderLapNumber = 1;
		//TODO: store new IDs in database
	}
	
	public static void EndRace() {
		raceID = 0;
		raceLapNumber = 0;
		riderLapNumber = 0;
		riderID = 0;
	}

}
