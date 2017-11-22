package com.example.android.chatwithbot;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ChatAdapter} exposes a list of chat messages
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 * Created by safwanx on 11/22/17.
 */

public class ChatAdapter extends CursorAdapter {

    //These are used to decide which type of view to be inflated in newView
    private final int VIEW_TYPE_LEFT = 0;
    private final int VIEW_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    // Helper class for view holder, each view will have one object of this type.
    // While using ListView with adapter, the newView method is not called as many
    // times as there are list items, because the adapter intelligently recycles views
    // that are outside the screen.
    // But when an older view is being reused the bindView method is called. So if
    // we use methods like findViewById there then it will slow down the performance.
    //
    // Hence we find the views inside view holder and instantiating view holder object
    // inside newView.

    private static class ViewHolder
    {
        private TextView messageView;

        public ViewHolder(View view)
        {
            messageView = (TextView) view.findViewById(R.id.msgr);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ( (position%2) == 0 ) ? VIEW_TYPE_RIGHT : VIEW_TYPE_LEFT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*
     Remember that these views are reused as needed.
    */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Find the layout type (left/right)
        int layoutType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        //Determine layout id from view type
        if(layoutType == VIEW_TYPE_LEFT)
        {
            layoutId = R.layout.left;
        }
        else
        {
            layoutId = R.layout.right;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        //Create view holder for this view
        ViewHolder viewHolder = new ViewHolder(view);

        //Save it inside view
        view.setTag(viewHolder);

        return view;
    }


    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        //TextView tv = (TextView)view;
        //tv.setText(convertCursorRowToUXFormat(cursor));

        //Get the associated view holder object
        ViewHolder viewHolder = (ViewHolder) view.getTag();


        //Find the message from the cursor
        String message = cursor.getString(MainActivity.COLUMN_MESSAGE);
        //Set the text for this view
        viewHolder.messageView.setText(message);

    }
}
