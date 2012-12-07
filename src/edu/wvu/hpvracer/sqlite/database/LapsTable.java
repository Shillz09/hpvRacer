package edu.wvu.hpvracer.sqlite.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LapsTable {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + SQLConstants.TABLE_LAPS
      + "(" 
      + SQLConstants.COLUMN_LAP_NUMBER + " integer primary key, "  
      + SQLConstants.COLUMN_LAP_START_TIME + " integer not null, "
      + SQLConstants.COLUMN_UPLOAD_STATUS + " integer not null"
      + ");";

  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_LAPS);
	  database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(LapsTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_LAPS);
    onCreate(database);
  }
}