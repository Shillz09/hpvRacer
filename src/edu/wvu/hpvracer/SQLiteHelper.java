package edu.wvu.hpvracer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + Constants.SQLTABLENAME + "(" 
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

  public SQLiteHelper(Context context) {
    super(context, Constants.SQLTABLENAME, null, Constants.SQLDATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + Constants.SQLTABLENAME);
    onCreate(db);
  }

} 

