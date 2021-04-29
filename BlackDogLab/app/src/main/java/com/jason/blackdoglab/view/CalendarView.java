package com.jason.blackdoglab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.blackdoglab.CalendarAdapter;
import com.jason.blackdoglab.DailyMoods;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CalendarView extends LinearLayout implements View.OnClickListener {
    // calendar components
    LinearLayout header;
    ImageView btnPrev;
    ImageView btnNext;
    TextView tvDisplayYear;
    TextView tvDisplayMonth;
    GridView gridView;
    Calendar calendarDisplay;
    private HashSet<DailyMoods> dailyMoodsSet;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        calendarDisplay = Calendar.getInstance();
        //updateCalendar here will make UI thread overoveroverload
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.btn_calendar_left);
        btnNext = findViewById(R.id.btn_calendar_right);
        tvDisplayYear = findViewById(R.id.date_display_year);
        tvDisplayMonth = findViewById(R.id.date_display_month);
        gridView = findViewById(R.id.calendar_grid);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_calendar, this);
        assignUiElements();
    }

    public void setDailyMoods(HashSet<DailyMoods> dailyMoodsSet) {
        this.dailyMoodsSet = dailyMoodsSet;
    }

    public void updateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        HashSet<DailyMoods> dailyMoodsSetDisplay = (HashSet) dailyMoodsSet.clone();
        Calendar calendarInit = (Calendar) calendarDisplay.clone();
        // determine the cell for current month's beginning
        calendarInit.set(Calendar.DAY_OF_MONTH, 1);
        // -1->start at Sunday,-2->start at Monday
        int monthBeginningCell = calendarInit.get(Calendar.DAY_OF_WEEK) - 1;
        // move calendar backwards to the beginning of the week
        calendarInit.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        int DAYS_COUNT = 42;
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendarInit.getTime());
            calendarInit.add(Calendar.DAY_OF_MONTH, 1);
        }
        dailyMoodsSetDisplay.retainAll(cells);

        // update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), cells, calendarDisplay, dailyMoodsSetDisplay));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateToday = sdf.format(calendarDisplay.getTime()).split("-");
        tvDisplayYear.setText(dateToday[0] + "年");
        tvDisplayMonth.setText(dateToday[1] + "月");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calendar_left:
                calendarDisplay.add(Calendar.MONTH, -1);
                updateCalendar();
                break;
            case R.id.btn_calendar_right:
                calendarDisplay.add(Calendar.MONTH, 1);
                updateCalendar();
                break;
            default:
                break;
        }
    }
}