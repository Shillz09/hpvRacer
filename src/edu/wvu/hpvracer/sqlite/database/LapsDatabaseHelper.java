package edu.wvu.hpvracer.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LapsDatabaseHelper extends SQLiteOpenHelper {

  public LapsDatabaseHelper(Context context) {
    super(context, SQLConstants.LAPS_DATABASE_NAME, null, SQLConstants.DATABASE_VERSION);
  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    LapsTable.onCreate(database);
  }

  // Method is called during an upgrade of the database,
  // e.g. if you increase the database version
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    LapsTable.onUpgrade(database, oldVersion, newVersion);
  }
} 