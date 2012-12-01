package edu.wvu.hpvracer;

import android.content.SharedPreferences;

public class AppData {
	
	public static int riderID = 0;
	public static int raceID = 0;
	public static int lapStartTime = 0;
	public static int raceLapNumber = 0;
	public static int riderLapNumber = 0;
	public static long ghostSpeed = 0;
	public static long ghostCadence = 80;
	public static SharedPreferences settings;	//refers to local android device storage

	/**
	 * @param args
	 */
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
