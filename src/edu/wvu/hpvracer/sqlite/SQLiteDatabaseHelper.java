package edu.wvu.hpvracer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.wvu.hpvracer.Constants;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

  public SQLiteDatabaseHelper(Context context) {
    super(context, Constants.SQLDATABASENAME, null, Constants.SQLDATABASE_VERSION);
  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    SensorTableManager.onCreate(database);
    LapTableManager.onCreate(database);
  }

  // Method is called during an upgrade of the database
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    SensorTableManager.onUpgrade(database, oldVersion, newVersion);
    LapTableManager.onCreate(database);
  }
} 
