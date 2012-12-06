package edu.wvu.hpvracer.sqlite.database;

import edu.wvu.hpvracer.sqlite.contentProvider.QueryHelper.Tables;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RacesTable {
	
  // Database table
  public static final Tables TABLE_RACE_DATA = Tables.hpvRaceData;
  public static final String COLUMN_READING_TIME = "readingTime";
  public static final String COLUMN_KEY = "key";
  public static final String COLUMN_VALUE = "value";
  public static final String COLUMN_RIDER_ID = "riderID";
  public static final String COLUMN_RACE_ID = "raceID";
  public static final String COLUMN_RIDER_LAP = "riderLap";
  public static final String COLUMN_RACE_LAP = "raceLap";
  public static final String COLUMN_UPLOAD_STATUS = "uploadStatus";
  
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
	  + TABLE_RACE_DATA + "(" 
	  + COLUMN_KEY + " text not null, "
	  + COLUMN_RACE_ID + " integer not null, "
	  + COLUMN_RACE_LAP + " integer not null, "
	  + COLUMN_RIDER_ID + " integer not null, "
	  + COLUMN_READING_TIME + " integer not null, "
	  + COLUMN_RIDER_LAP + " integer not null, "
	  + COLUMN_UPLOAD_STATUS + " text not null, "
	  + COLUMN_VALUE + " integer not null, "
	  + "PRIMARY KEY (" + COLUMN_KEY + ", " + COLUMN_READING_TIME + ")"
	  +");";
  
  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(RacesTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_RACE_DATA);
    onCreate(database);
  }
}