package com.example.android.chatwithbot.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * * <code>{@link ChatContract}</code> is the contract class used for the chat database.
 * Created by safwanx on 11/22/17.
 */

public class ChatContract {

    /*Add content provider constants to this contact. Clients need to know how to access
    the data. It is our job to provide content URIs for different paths and following data:
     1) Content Authority
     2) Base Content Uri
     3) Path(s) to different directories (tables/multiple rows)
     4) Content URIs for different tables(inner classes).
     */

    public static final String SCHEME = "content://";

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.chatwithbot";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_CHAT = "chat";




    //Create all the required tables for the chat database. They are in the form of inner classes.
    //Each inner class represents a table in the database. For chat app we need one table for.

    public static final class Chat implements BaseColumns {
        //Content Uri for chat table (directory)
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT).build();

        //Different MIME types used by getType method of content provider
        //We are working with two types of data:
        //1) a directory and 2) a single row of data.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAT;


        //Name of the table
        public static final String TABLE_NAME = "chats";
        //Names of different columns in this table
        public static final String _MESSAGE = "message";
        public static final String _TYPE = "type";
        public static final String TIME_STAMP = "time";
    }

}

