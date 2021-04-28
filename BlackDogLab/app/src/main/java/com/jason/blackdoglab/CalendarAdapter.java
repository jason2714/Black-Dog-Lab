package com.jason.blackdoglab;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private HashSet<Date> eventDays;
    private Context context;
    private Calendar calendarDisplay;

    public CalendarAdapter(Context context, ArrayList<Date> days)
    {
        super(context, R.layout.custom_calendar_day, days);
        this.context = context;
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);
    }

    public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
    {
        super(context, R.layout.custom_calendar_day, days);
        this.context = context;
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);
    }

    public CalendarAdapter(Context context, ArrayList<Date> days, Calendar calendarDisplay)
    {
        super(context, R.layout.custom_calendar_day, days);
        this.context = context;
        this.calendarDisplay = calendarDisplay;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.custom_calendar_day, parent, false);
        // clear styling
        TextView mTvCalendarDate = view.findViewById(R.id.tv_calendar_date);
        mTvCalendarDate.setTypeface(null, Typeface.NORMAL);
        mTvCalendarDate.setTextColor(context.getResources().getColor(R.color.brown3));
        if (month != calendarDisplay.get(Calendar.MONTH) || year != calendarDisplay.get(Calendar.YEAR)) {
            // if this day is outside current month, grey it out
//            mTvCalendarDate.setTextColor(Color.parseColor("#E0E0E0"));
            mTvCalendarDate.setTextColor(context.getResources().getColor(R.color.grey4));
        } else if (year == calendarToday.get(Calendar.YEAR) &&
                month == calendarToday.get(Calendar.MONTH) &&
                day == calendarToday.get(Calendar.DATE)) {
            // if it is today, set it to blue/bold
            mTvCalendarDate.setTextColor(Color.WHITE);
            view.setBackgroundResource(R.color.brown3);
        }

        // set text
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        mTvCalendarDate.setText(sdf.format(calendar.getTime()));

        return view;
    }
}