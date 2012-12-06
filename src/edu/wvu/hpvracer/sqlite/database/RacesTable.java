package edu.wvu.hpvracer.sqlite.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RacesTable {
  
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
	  + SQLConstants.TABLE_RACE_DATA + "(" 
	  + SQLConstants.COLUMN_KEY + " text not null, "
	  + SQLConstants.COLUMN_RACE_ID + " integer not null, "
	  + SQLConstants.COLUMN_RACE_LAP + " integer not null, "
	  + SQLConstants.COLUMN_RIDER_ID + " integer not null, "
	  + SQLConstants.COLUMN_READING_TIME + " integer not null, "
	  + SQLConstants.COLUMN_RIDER_LAP + " integer not null, "
	  + SQLConstants.COLUMN_UPLOAD_STATUS + " text not null, "
	  + SQLConstants.COLUMN_VALUE + " integer not null, "
	  + "PRIMARY KEY (" + SQLConstants.COLUMN_KEY + ", " + SQLConstants.COLUMN_READING_TIME + ")"
	  + ");";
  
  
  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_RACE_DATA);
	  database.execSQL(DATABASE_CREATE);
  }

  
  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  Log.w(RacesTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
	  database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_RACE_DATA);
	  onCreate(database);
  }
  
}