package edu.wvu.hpvracer.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RaceDataTable {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + SQLConstants.TABLE_RACEDATA + "(" 
	  + SQLConstants.COLID + " integer primary key autoincrement, "
      + SQLConstants.KEY + " text not null, "
	  + SQLConstants.RACEID + " integer not null, "
      + SQLConstants.RACELAP + " integer not null, "
      + SQLConstants.RIDERID + " integer not null, "
      + SQLConstants.READINGTIME + " integer not null, "
      + SQLConstants.RIDERLAP + " integer not null, "
      + SQLConstants.UPLOADSTATUS + " text not null, "
	  + SQLConstants.VALUE + " integer not null"
      +");";


  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  Log.w(RaceDataTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
      database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_RACEDATA);
      onCreate(database);
  }

} 

