package edu.wvu.hpvracer.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LapTable {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + SQLConstants.TABLE_LAPTABLE + "(" 
	  + SQLConstants.LAPNUMBER + " integer primary key, "
      + SQLConstants.LAPSTARTTIME + " integer not null, "
      + SQLConstants.UPLOADSTATUS + " text not null, "
      +");";


  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  Log.w(RaceDataTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
      database.execSQL("DROP TABLE IF EXISTS " + SQLConstants.TABLE_LAPTABLE);
      onCreate(database);
  }

} 

