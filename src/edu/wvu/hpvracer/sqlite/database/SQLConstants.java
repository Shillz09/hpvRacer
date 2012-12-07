package edu.wvu.hpvracer.sqlite.database;

import android.content.ContentResolver;

public class SQLConstants {
	
	
	public static final int DATABASE_VERSION = 1;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/hpvRacer";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/hpvRacer";
	public static final String SELECT = "select";
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	
	// Races Database table
	public static final String RACES_DATABASE_NAME = "hpvRaceStatsDatabase.db";
	public static final String TABLE_RACE_DATA = Tables.hpvRaceData.toString();
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_READING_TIME = "readingTime";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_RIDER_ID = "riderID";
	public static final String COLUMN_RACE_ID = "raceID";
	public static final String COLUMN_RIDER_LAP = "riderLap";
	public static final String COLUMN_RACE_LAP = "raceLap";
	public static final String COLUMN_UPLOAD_STATUS = "uploadStatus";
	
	
	// Laps Database table
	public static final String LAPS_DATABASE_NAME = "hpvLapsDatabase.db";
	public static final String TABLE_LAPS = Tables.hpvLaps.toString();
	public static final String COLUMN_LAP_NUMBER = "_id";
	public static final String COLUMN_LAP_START_TIME = "startTime";
	
	public enum Tables {
		hpvRaceData, hpvLaps
	}
	
	public enum DatabaseActions {
		select, insert, update, delete
	}

}
