package edu.wvu.hpvracer;

public class Constants {

	/**
	 * @param args
	 */

	public static final int RUNNING = 0;
	public static final int FINISHED = 1;
	public static final int ERROR = -1;
	
	// USED IN SHARED PREFERENCES
	public static final String GLOBAL_VARS = "hpvRacerPreferencesFile";
	public static final String RIDERID = "RiderID";
	public static final String RACEID = "RaceID";
	public static final String RIDERLAP = "RiderLapNumber";
	public static final String RACELAP = "RaceLapNumber";
	public static final String GCMREGID = "GoogleCloudMessagingDeviceRegistrationId";
	public static final String GHOSTSPEED = "ghostSpeed";
	public static final String GHOSTCADENCE = "ghostCadence";
	
	// USED IN GOOGLE CLOUD MESSAGING
	public static final String SENDER_ID = "678175558305";


}
