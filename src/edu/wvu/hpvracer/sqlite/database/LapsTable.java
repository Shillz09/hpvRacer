package edu.wvu.hpvracer.sqlite.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LapsTable {
	
  // Database table
  public static final String TABLE_LAPS = "hpvLaps";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_LAP_NUMBER = "category";
  public static final String COLUMN_LAP_START_TIME = "summary";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_LAPS
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_LAP_NUMBER + " integer not null, " 
      + COLUMN_LAP_START_TIME + " integer not null " 
      + ");";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(LapsTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_LAPS);
    onCreate(database);
  }
}