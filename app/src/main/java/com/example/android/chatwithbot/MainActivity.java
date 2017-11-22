package com.example.android.chatwithbot;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.chatwithbot.data.ChatContract;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "ChatWithBot";

    //Projection string to be used for cursor loader
    private static final String[] PROJECTION_CHAT = {
            ChatContract.Chat._ID,
            ChatContract.Chat._MESSAGE,
            ChatContract.Chat._TYPE,
            ChatContract.Chat.TIME_STAMP
    };

    //Based on above projections the index for columns. So if the projection changes
    //this too have to be changed. The cursor returned will have columns in this order
    //and follow these indices for columns
    static final int COLUMN_ID = 0;
    static final int COLUMN_MESSAGE = 1;
    static final int COLUMN_TYPE = 2;
    static final int COLUMN_TIME_STAMP = 3;


    // Cursor adapter instance
    private ChatAdapter chatAdapter;
    // Loader id
    private final int loaderId = 0;

    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    // Length of the random string
    public static final int MAX_LENGTH = 20;
    public static final int MIN_LENGTH = 10;

    //These are used to decide if it is a message or a response
    private final int TYPE_MESSAGE = 1; // for VIEW_TYPE_RIGHT
    private final int TYPE_RESPONSE = 0; // for VIEW_TYPE_LEFT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find out send button view
        buttonSend = (Button) findViewById(R.id.send);
        // Find out edit text view
        chatText = (EditText) findViewById(R.id.msg);

        // Attach a listener to button, when the button is clicked
        // the chat should be added to database and later updated
        // in list view.
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        // we are using supportV4 library to implement loader manager callback
        getSupportLoaderManager().initLoader(loaderId, savedInstanceState, this);

        // Create the adapter
        chatAdapter = new ChatAdapter(getApplicationContext(), null, 0);

        // Find the list view
        listView = (ListView) findViewById(R.id.msgview);

        // Attach the adapter to list view
        listView.setAdapter(chatAdapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    /**
     * This is a helper method to save conversation in pair.
     * @return
     */
    private boolean sendChatMessage() {
        // Take out the message from edit text
        String myMessage = chatText.getText().toString();

        if(myMessage.length() != 0) {
            // Generate a random message for bot
            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();
            int randomLength = generator.nextInt(MAX_LENGTH);

            if(randomLength == 0){
                randomLength = MIN_LENGTH;
            }
            char tempChar;
            for (int i = 0; i < randomLength; i++) {
                tempChar = (char) (generator.nextInt(96) + 32);
                randomStringBuilder.append(tempChar);
            }

            // Save the bot message
            String botMessage = randomStringBuilder.toString();

            // TODO Instead add the data to data base
            ContentValues values = new ContentValues();

            // Add values to content values
            values.put(ChatContract.Chat._MESSAGE, myMessage);
            values.put(ChatContract.Chat._TYPE, TYPE_MESSAGE);
            values.put(ChatContract.Chat.TIME_STAMP, System.currentTimeMillis());

            Uri uri = null;
            uri = ChatContract.Chat.CONTENT_URI;

            // Insert in database
            getContentResolver().insert(uri, values);

            // Similarly add the response from bot
            values.clear();
            values.put(ChatContract.Chat._MESSAGE, botMessage);
            values.put(ChatContract.Chat._TYPE, TYPE_RESPONSE);
            values.put(ChatContract.Chat.TIME_STAMP, System.currentTimeMillis());

            // Insert in database
            getContentResolver().insert(uri, values);

            chatText.setText("");
        }


        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;

        //Sort order: Ascending by time stamp
        String sortOrder = ChatContract.Chat.TIME_STAMP + " ASC";

        cursorLoader = new CursorLoader(this,
                ChatContract.Chat.CONTENT_URI,
                PROJECTION_CHAT,
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() != 0) {
            chatAdapter.swapCursor((Cursor) data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        chatAdapter.swapCursor(null);
    }
}
