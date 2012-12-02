package edu.wvu.hpvracer.sqlite.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RacesTable {
	
  // Database table
  public static final String TABLE_RACE_DATA = "hpvRaceData";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_READING_TIME = "category";
  public static final String COLUMN_KEY = "summary";
  public static final String COLUMN_VALUE = "summary";
  public static final String COLUMN_RIDER_ID = "summary";
  public static final String COLUMN_RACE_ID = "summary";
  public static final String COLUMN_RIDER_LAP = "summary";
  public static final String COLUMN_RACE_LAP = "summary";
  public static final String COLUMN_UPLOAD_STATUS = "summary";
  
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
	  + TABLE_RACE_DATA + "(" 
	  + COLUMN_ID + " integer primary key autoincrement, "
	  + COLUMN_KEY + " text not null, "
	  + COLUMN_RACE_ID + " integer not null, "
	  + COLUMN_RACE_LAP + " integer not null, "
	  + COLUMN_RIDER_ID + " integer not null, "
	  + COLUMN_READING_TIME + " integer not null, "
	  + COLUMN_RIDER_LAP + " integer not null, "
	  + COLUMN_UPLOAD_STATUS + " text not null, "
	  + COLUMN_VALUE + " integer not null"
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