package edu.wvu.hpvracer.sqlite;

import edu.wvu.hpvracer.Constants;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LapTableManager {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + Constants.SQLLAPTABLE + "(" 
	  + Constants.SQLLAPNUMBER + " integer primary key, "
      + Constants.SQLLAPSTARTTIME + " integer not null, "
      + Constants.SQLUPLOADSTATUS + " text not null, "
      +");";


  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  Log.w(SensorTableManager.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
      database.execSQL("DROP TABLE IF EXISTS " + Constants.SQLLAPTABLE);
      onCreate(database);
  }

} 

