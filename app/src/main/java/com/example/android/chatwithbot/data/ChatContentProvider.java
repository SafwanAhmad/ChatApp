package com.example.android.chatwithbot.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * In order to build a content provider of its own the should be a class that
 * extends <code>{@link ContentProvider}</code> and overload all the abstract methods
 * as done below.
 * Created by safwanx on 11/23/17.
 */

public class ChatContentProvider extends ContentProvider {
    //Define constants that are used by the Uri matcher.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int CHAT = 100;
    public static final int CHAT_ITEM = 101;

    //Define a static Uri matcher object
    private static UriMatcher sUriMatcher = buildUriMatcher();

    //Create a member variable for database helper
    SQLiteOpenHelper mDbHelper;

    ///These are different Selection clauses which are used to perform different
    //types of queries. The ? mark is replaced by the selection args provided
    //in the query.
    static final String sSelectionChat = ChatContract.Chat._ID + "=?";



    /**
     * The purpose of this method is to define all URIs that are valid for this content
     * provider. Further it helps to map these URIs to different constants defined in content
     * provider and later allow easy URI matching.
     * For the identification of a single row the _ID field representing a chat id is used.
     *
     * @return UriMatcher object.
     */
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Add URI for all rows inside popular table (complete directory)
        uriMatcher.addURI(ChatContract.CONTENT_AUTHORITY, ChatContract.PATH_CHAT, CHAT);
        //Add URI for a single row inside popular table(single item)
        uriMatcher.addURI(ChatContract.CONTENT_AUTHORITY, ChatContract.PATH_CHAT + "/#", CHAT_ITEM);

        return uriMatcher;
    }


    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        //Get the object using our own MovieDbHelper class
        mDbHelper = new ChatWithBotDBHelper(context);
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        //Use uri matcher to determine the type of the uri
        int code = sUriMatcher.match(uri);

        switch (code) {
            case CHAT: {
                return ChatContract.Chat.CONTENT_TYPE;
            }

            case CHAT_ITEM: {
                return ChatContract.Chat.CONTENT_ITEM_TYPE;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Get access to writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Number of rows updated
        int rowsUpdated = 0;

        int code = sUriMatcher.match(uri);

        switch (code) {

            case CHAT: {
                rowsUpdated = database.update(ChatContract.Chat.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }
            break;

            case CHAT_ITEM: {
                //Get the id passed in the uri
                String chatId = uri.getPathSegments().get(1);
                String[] mSelectionArgs = new String[]{chatId};

                rowsUpdated = database.update(ChatContract.Chat.TABLE_NAME,
                        values,
                        sSelectionChat,
                        mSelectionArgs);
            }
            break;

            default: {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }

        //We must notify about this change
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //Get access to writable database
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id;
        Uri returnedUri;

        //Use uri matcher to check the validity of passed uri.
        int code = sUriMatcher.match(uri);

        switch (code) {
            case CHAT: {
                id = database.insert(ChatContract.Chat.TABLE_NAME,
                        null,
                        values);
            }
            break;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        //Check if the values have been successfully inserted
        if (id > 0) {
            returnedUri = ContentUris.withAppendedId(uri, id);

            //Notify the resolver if the uri has been changed, and return the newly inserted URI
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Get the access to a writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Number of rows deleted
        int rowsDeleted;

        //Use UriMatcher to check if the uri is supported
        int code = sUriMatcher.match(uri);

        switch (code) {
            case CHAT: {
                rowsDeleted = database.delete(
                        //Name of the table containing the data
                        ChatContract.Chat.TABLE_NAME,
                        //Selection clause (where in sql)
                        selection,
                        //Arguments for the selection
                        selectionArgs
                );
            }
            break;

            case CHAT_ITEM: {
                //Get the movie ID for a specific movie
                String movieId = uri.getPathSegments().get(1);
                //Make the selection clause for this operation
                String[] mSelectionArgs = new String[]{movieId};

                rowsDeleted = database.delete(
                        ChatContract.Chat.TABLE_NAME,
                        sSelectionChat,
                        mSelectionArgs
                );
            }
            break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        //Don't forget to notify listener about the data change
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Get access to readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        //Cursor to be returned (if query is successful)
        Cursor cursor = null;

        //Find if the uri is valid
        int code = sUriMatcher.match(uri);

        switch (code) {
            case CHAT: {
                cursor = database.query(ChatContract.Chat.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            }
            break;

            //Based on this type of uri, provider will find the right selection and selection args
            //to be sent in the query.
            case CHAT_ITEM: {
                //Using Selection and Selection args
                //URI: content://<authority>/<path>/#

                String chatId = uri.getPathSegments().get(1);

                cursor = database.query(ChatContract.Chat.TABLE_NAME,
                        projection,
                        sSelectionChat,
                        new String[]{chatId},
                        null,
                        null,
                        sortOrder);
            }
            break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        //Also register to watch a content URI for changes.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }
}
