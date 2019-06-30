package Services;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GoogleCalendar {

    private long calendarID;
    Context mContext;

    private static final String ACCOUNT_NAME = "mohdosama962@gmail.com";
    private TableLayout tableLayout;

    public GoogleCalendar(Context mContext) {
        this.mContext = mContext;
    }

    public List <String>  Configuration(){
        this.calendarID = getCalendarID();
        System.out.println("CAlendar ID"+ this.calendarID);
        List <String> events = getEvents(calendarID);
         return events ;


    }

    public long getCalendarID() {
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME};
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                        CalendarContract.Calendars.NAME + "=? or " +
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                new String[]{ACCOUNT_NAME, ACCOUNT_NAME,
                        ACCOUNT_NAME}, null);

        if (cursor.moveToFirst()) {

            if (cursor.getString(1).equals(ACCOUNT_NAME))
                return cursor.getInt(0);


        }
        return -1;
    }




    private List<String> getEvents(long calendarID)  {
      //  ArrayList<HashMap<String, String>> events = new ArrayList<>();
        String[] projection = new String[] { CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.ALL_DAY, CalendarContract.Events.EVENT_LOCATION };
        Calendar startTime = Calendar.getInstance();

        startTime.set(Calendar.HOUR_OF_DAY,0);
        startTime.set(Calendar.MINUTE,0);
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime= Calendar.getInstance();
        endTime.add(Calendar.DATE, 1);
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ) AND ( deleted != 1 ))";
        Cursor cursor = mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                projection, selection , null, null);

        List<String> event = new ArrayList<>();
        if (cursor!=null&&cursor.getCount()>0&&cursor.moveToFirst()) {
            do {
                event.add(cursor.getString(1));
            } while ( cursor.moveToNext());
        }
        cursor.close();
       return event;

    }



}
