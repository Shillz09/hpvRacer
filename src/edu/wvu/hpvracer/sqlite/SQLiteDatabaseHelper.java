package edu.wvu.hpvracer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

  // USED IN SQLITE DATABASE (LOCAL DB)
  private static final int SQLDATABASE_VERSION = 1;
  private static final String SQLDATABASENAME = "hpvData.db";


  public SQLiteDatabaseHelper(Context context) {
    super(context, SQLDATABASENAME, null, SQLDATABASE_VERSION);
  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    RaceDataTable.onCreate(database);
    LapTable.onCreate(database);
  }

  // Method is called during an upgrade of the database
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    RaceDataTable.onUpgrade(database, oldVersion, newVersion);
    LapTable.onCreate(database);
  }
} 
