package com.jason.blackdoglab;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    private boolean isPopupWindowExist = false;

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
        this.dailyMoodsSetDisplay = (HashSet) dailyMoodsSetDisplay.clone();
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
                    view.setOnClickListener(v -> {
                        if (!isPopupWindowExist)
                            setPopupWindow(v, dailyMoods.getMood(), dailyMoods.getDiary());
                    });
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

        return view;
    }

    private void setPopupWindow(View v, int mood, String diary) {
        isPopupWindowExist = true;
        String[] moodsStr = {"0", "1", "2", "3", "4"};
        int[] moodsDotDrawable = {R.drawable.dot_mood1, R.drawable.dot_mood2,
                R.drawable.dot_mood3, R.drawable.dot_mood4, R.drawable.dot_mood5};

        //initial view
        View view = LayoutInflater.from(context).inflate(R.layout.view_calendar_popup_window, null);
        ImageView mDot = view.findViewById(R.id.img_dot);
        TextView mTvDiary = view.findViewById(R.id.tv_diary);
        TextView mTvMoodTitle = view.findViewById(R.id.tv_mood_title);
        TextView mTvClose = view.findViewById(R.id.tv_close);
        mTvDiary.setText(diary);
        mTvMoodTitle.setText(moodsStr[mood]);
        mDot.setImageResource(moodsDotDrawable[mood]);

        //initial popup window
        Utils.setLog("getY = " + v.getY());
        Utils.setLog("getTranslationY = " + v.getTranslationY());
        Utils.setLog("getBottom = " + v.getBottom());
        Utils.setLog("ParentClass = " + v.getParent().getClass());
        PopupWindow popupWindow = new PopupWindow(view,
                context.getResources().getDimensionPixelOffset(R.dimen.calendar_width),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.bg_note_box));
        popupWindow.setElevation(Utils.convertDpToPixel(context, 5));
//        popupWindow.showAsDropDown(v, 0, 0);
        //don't know why have 10 offset
//        popupWindow.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, v.getTop() - 5);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(() -> {
            Utils.setLog("dismiss");
            Utils.showBackgroundAnimator(context,0.5f, 1.0f);
            isPopupWindowExist = false;
        });
        Utils.showBackgroundAnimator(context,1.0f, 0.5f);
        mTvClose.setClickable(true);
        mTvClose.setOnClickListener(viewClick -> popupWindow.dismiss());
    }

}