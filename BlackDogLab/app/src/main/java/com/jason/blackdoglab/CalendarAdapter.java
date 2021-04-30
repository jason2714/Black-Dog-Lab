package com.jason.blackdoglab;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.blackdoglab.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private HashSet<DailyMoods> dailyMoodsSetDisplay;
    private Context context;
    private Calendar calendarDisplay;
    private int[] dateMoodBgColors = {R.color.blue2, R.color.blue4,
            R.color.green3, R.color.brown4, R.color.brown2};

    public CalendarAdapter(Context context, ArrayList<Date> days) {
        super(context, R.layout.custom_calendar_day, days);
        this.context = context;
        this.calendarDisplay = Calendar.getInstance();
        this.dailyMoodsSetDisplay = new HashSet<DailyMoods>();
        inflater = LayoutInflater.from(context);
    }

    public CalendarAdapter(Context context, ArrayList<Date> days, Calendar calendarDisplay, HashSet<DailyMoods> dailyMoodsSetDisplay) {
        super(context, R.layout.custom_calendar_day, days);
        this.context = context;
        this.calendarDisplay = calendarDisplay;
        this.dailyMoodsSetDisplay = dailyMoodsSetDisplay;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
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
        mTvCalendarDate.setTextColor(Utils.getAttrID(context, R.attr.colorPrimary, Utils.DATA));

        if (month != calendarDisplay.get(Calendar.MONTH) || year != calendarDisplay.get(Calendar.YEAR)) {
            // if this day is outside current month, grey it out
            mTvCalendarDate.setTextColor(context.getResources().getColor(R.color.grey4));
        } else if (year == calendarDisplay.get(Calendar.YEAR) &&
                month == calendarDisplay.get(Calendar.MONTH)) {
            for (DailyMoods dailyMoods : dailyMoodsSetDisplay) {
                if (dailyMoods.getDate().equals(dateStr)) {
                    view.setBackgroundResource(dateMoodBgColors[dailyMoods.getMood()]);
                    mTvCalendarDate.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        }
//        else if (year == calendarToday.get(Calendar.YEAR) &&
//                month == calendarToday.get(Calendar.MONTH) &&
//                day == calendarToday.get(Calendar.DATE)) {
//            // if it is today, set it to blue/bold
//            mTvCalendarDate.setTextColor(Color.WHITE);
//            view.setBackgroundColor(Utils.getAttrID(context,R.attr.calendar_color,Utils.DATA));
//        }

        // set text
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        mTvCalendarDate.setText(sdf.format(calendar.getTime()));

        view.setOnClickListener(v -> {
            Utils.setLog(v.getHeight() + " " + v.getWidth());
            Utils.setLog(v.getX() + " " + v.getY());
            Utils.setLog(((LinearLayout)v.getParent().getParent()).getWidth() + "");
        });
        return view;
    }
}