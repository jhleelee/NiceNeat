package jacklee_entertainment.niceneat.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.sql.SQLException;

/**
 * Created by user on 2015-05-27.
 */
public class FriendsDB_Adapter extends SimpleCursorAdapter {

    public FriendsDB_Adapter(Context context, int layout, Cursor c,
                           String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get reference to the row
        View view = super.getView(position, convertView, parent);
        //check for odd or even to set alternate colors to the row background
        if(position % 2 == 0){
            view.setBackgroundColor(Color.rgb(238, 233, 233));
        }
        else {
            view.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        return view;
    }


}
