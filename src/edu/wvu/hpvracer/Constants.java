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
	
	// USED IN GOOGLE CLOUD MESSAGING
	public static final String SENDER_ID = "678175558305";
	
	// USED IN SQLITE DATABASE (LOCAL DB)
	public static final int SQLDATABASE_VERSION = 1;
	public static final String SQLROWID = "_id";
	public static final String SQLTABLENAME = "hpvSensorData";
	public static final String SQLREADINGTIME = "readTime";
	public static final String SQLKEY = "readingType";
	public static final String SQLVALUE = "value";
	public static final String SQLRACER = "RiderID";
	public static final String SQLRACE = "RaceID";
	public static final String SQLRIDERLAP = "RiderLapNumber";
	public static final String SQLRACELAP = "RaceLapNumber";
	public static final String SQLUPLOADSTATUS = "status";
	public static enum SQLKEYOPTIONS {
		SPEED, CADENCE
	}

}
