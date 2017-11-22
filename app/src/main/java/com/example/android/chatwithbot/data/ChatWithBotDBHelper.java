package com.example.android.chatwithbot.data;

/**
 * Created by safwanx on 11/22/17.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for chat data.
 */
public class ChatWithBotDBHelper extends SQLiteOpenHelper {

    //Constant representing the name of the database
    public static final String DATABASE_NAME = "chat.db";
    //Initial version for the database, set it to one
    public static final int DATABASE_VERSION = 1;

    public ChatWithBotDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Build sql query to build all the required tables
        final String SQL_CREATE_CHAT_TABLE = "CREATE TABLE " +
                ChatContract.Chat.TABLE_NAME + " (" +
                ChatContract.Chat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChatContract.Chat._MESSAGE + " TEXT NOT NULL, " +
                ChatContract.Chat._TYPE + " INTEGER NOT NULL, " +
                ChatContract.Chat.TIME_STAMP + " INTEGER NOT NULL " +
                ");";

        //Execute these queries
        db.execSQL(SQL_CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChatContract.Chat.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
