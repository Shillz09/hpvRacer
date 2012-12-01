package edu.wvu.hpvracer.sqlite;


public class SQLConstants {

	// TABLE NAMES
	public static final String TABLE_RACEDATA = "hpvSensorData";
	public static final String TABLE_LAPTABLE = "hpvLaps";
	
	// RACEDATA COLUMN NAMES
	public static final String COLID = "_id";
	public static final String READINGTIME = "readTime";
	public static final String KEY = "readingType";
	public static final String VALUE = "value";
	public static final String RIDERID = "riderID";
	public static final String RACEID = "raceID";
	public static final String RIDERLAP = "riderLapNumber";
	public static final String RACELAP = "raceLapNumber";
	public static final String UPLOADSTATUS = "status";
	
	// ENUMERATOR FOR ACCEPTED "SQLKEY" VALUES
	public enum KEYOPTIONS {
		SPEED, CADENCE
	}
	
	// ENUMERATOR FOR "UPLOADSTATUS" VALUES
	public enum UPLOADSTATUSES {
		LOCAL, UPLOADING, COMPLETE
	}
	
	// COLUMN NAMES
	public enum RACEDATACOLUMNS {
		COLID, READINGTIME, KEY, VALUE, RIDERID, RACEID, RIDERLAP, RACELAP, UPLOADSTATUS
	}

	// LAP TABLE COLUMN NAMES
	public static final String LAPNUMBER = "lapNumber";
	public static final String LAPSTARTTIME = "lapStartTime";
	public enum LAPTABLECOLUMNS {
		LAPNUMBER, LAPSTARTTIME
	}
		
}
