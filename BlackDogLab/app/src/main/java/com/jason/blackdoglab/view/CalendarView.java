package com.jason.blackdoglab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.blackdoglab.CalendarAdapter;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarView extends LinearLayout {
    // calendar components
    LinearLayout header;
    ImageView btnPrev;
    ImageView btnNext;
    TextView tvDisplayYear;
    TextView tvDisplayMonth;
    GridView gridView;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.img_calendar_left);
        btnNext = findViewById(R.id.img_calendar_right);
        tvDisplayYear = findViewById(R.id.date_display_year);
        tvDisplayMonth = findViewById(R.id.date_display_month);
        gridView = findViewById(R.id.calendar_grid);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_calendar, this);
        assignUiElements();
    }

    //    public void updateCalendar(HashSet<Date> events) {
//        ArrayList<Date> cells = new ArrayList<>();
//        Calendar currentDate = Calendar.getInstance();
//        Calendar calendar = (Calendar) currentDate.clone();
//        // determine the cell for current month's beginning
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2;
//
//        // move calendar backwards to the beginning of the week
//        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
//
//        // fill cells
//        int DAYS_COUNT = 7;
//        while (cells.size() < DAYS_COUNT) {
//            cells.add(calendar.getTime());
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
//
//        // update grid
//        gridView.setAdapter(new CalendarAdapter(getContext(), cells, events));
//
//        // update title
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d MMM,yyyy");
//        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
//        txtDateDay.setText(dateToday[0]);
//        txtDisplayDate.setText(dateToday[1]);
//        txtDateYear.setText(dateToday[2]);
//    }
    public void updateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.MONTH,2);
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Utils.setLog(monthBeginningCell+"");
        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        int DAYS_COUNT = 42;
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), cells));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM");
        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
        tvDisplayYear.setText(dateToday[0] + "年");
        tvDisplayMonth.setText(dateToday[1] + "月");
    }
}