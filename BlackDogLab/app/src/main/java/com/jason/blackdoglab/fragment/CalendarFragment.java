package com.jason.blackdoglab.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jason.blackdoglab.customclass.DailyMoods;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;
import com.jason.blackdoglab.view.CalendarView;

import java.util.Calendar;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView mBgMainCalendar;
    private HashSet<DailyMoods> dailyMoodsSet;
    private CalendarView cvCalendar;
    private Calendar calendarDisplay;

    public CalendarFragment() {
        // Required empty public constructor
        dailyMoodsSet = new HashSet<DailyMoods>();
    }

    public CalendarFragment(HashSet<DailyMoods> dailyMoodsSet) {
        this.dailyMoodsSet = dailyMoodsSet;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView(View view) {
        cvCalendar = view.findViewById(R.id.cv_calendar);
        mBgMainCalendar = view.findViewById(R.id.bg_main_calendar);
    }

    @Override
    protected ImageView getBgImgView() {
        return mBgMainCalendar;
    }

    @Override
    protected int getBgDrawableID() {
        return Utils.getAttrID(getContext(),R.attr.bg_calendar,Utils.RESOURCE_ID);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO initial view here
        //initial view

        //initial listener
        cvCalendar.setDailyMoods(dailyMoodsSet);
        cvCalendar.updateCalendar();
        Utils.setLog("daily mood old");
        for (DailyMoods dailyMoods : dailyMoodsSet) {
            Utils.setLog(dailyMoods.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onClick(View v) {

    }
}