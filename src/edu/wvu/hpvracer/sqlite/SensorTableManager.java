package edu.wvu.hpvracer.sqlite;

import edu.wvu.hpvracer.Constants;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SensorTableManager {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + Constants.SQLRACEDATATABLE + "(" 
	  + Constants.SQLROWID + " integer primary key autoincrement, "
      + Constants.SQLKEY + " text not null, "
	  + Constants.SQLRACE + " integer not null, "
      + Constants.SQLRACELAP + " integer not null, "
      + Constants.SQLRACER + " integer not null, "
      + Constants.SQLREADINGTIME + " integer not null, "
      + Constants.SQLRIDERLAP + " integer not null, "
      + Constants.SQLUPLOADSTATUS + " text not null, "
	  + Constants.SQLVALUE + " integer not null"
      +");";


  public static void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  Log.w(SensorTableManager.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
      database.execSQL("DROP TABLE IF EXISTS " + Constants.SQLRACEDATATABLE);
      onCreate(database);
  }

} 

