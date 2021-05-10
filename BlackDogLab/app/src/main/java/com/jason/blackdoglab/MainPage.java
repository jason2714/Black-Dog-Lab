package com.jason.blackdoglab;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.jason.blackdoglab.adapter.PageAdapter;
import com.jason.blackdoglab.customclass.DailyMoods;
import com.jason.blackdoglab.customclass.Player;
import com.jason.blackdoglab.fragment.CalendarFragment;
import com.jason.blackdoglab.fragment.MainFragment;
import com.jason.blackdoglab.fragment.NoteFragment;
import com.jason.blackdoglab.fragment.UserFragment;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainPage extends BaseActivity {

//    public class TabPagerAdapter extends FragmentPagerAdapter {
//        private static final int PAGE_COUNT = 3;
//
//        public TabPagerAdapter(@NonNull FragmentManager fm, int behavior) {
//            super(fm, behavior);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return PAGE_COUNT;
//        }
//    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageButton mImgBtTabTriangle;
    private int themeColor;
    private boolean isTabTriangleShow;
    private Player player;
    private HashSet<DailyMoods> dailyMoodsSet;
    private List<Fragment> fragments;
    private ImageView[] tabIconImages;


    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_main_page;
    }

    @Override
    protected int setThemeColor() {
        return themeColor;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mImgBtTabTriangle = findViewById(R.id.imgbt_tab_triangle);
        mImgBtTabTriangle.animate().alpha(0);
        isTabTriangleShow = false;

        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new CalendarFragment(dailyMoodsSet));
        fragments.add(new NoteFragment());
        fragments.add(new UserFragment(player));

        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setupWithViewPager(mViewPager);
        //must below setupWithViewPager or program will crush
        initTabDrawable();
    }

    @Override
    protected void initListener() {
        mImgBtTabTriangle.setOnClickListener(this);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utils.hideNavigationBar(MainPage.this);
                if (tab.getId() != 3)
                    ((ImageView) tab.getCustomView()).setColorFilter
                            (Utils.getAttrID(MainPage.this, R.attr.colorPrimary, Utils.DATA), PorterDuff.Mode.SRC_IN);
                if (!isTabTriangleShow) {
                    mTabLayout.animate().translationY(Utils.convertDpToPixel(MainPage.this, 55)).setStartDelay(500).setDuration(500);
                    mImgBtTabTriangle.animate().alpha(1).setStartDelay(1000);
                    mTabLayout.setBackgroundResource(R.drawable.bg_tab_down);
                }
                //from current position
//                mTabLayout.animate().translationYBy(10);
                //from initial position
//                  mTabLayout.animate().translationY(10);
                //to that position
//                mTabLayout.animate().y(10);
//                mTabLayout.animate().setStartDelay(500).translationY(10);
//                Utils.setLog(tab.getId() + " select");
//                tab.setIcon(R.drawable.open_book);
//                hide Text Label
//                tab.setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getId() != 3)
                    ((ImageView) tab.getCustomView()).clearColorFilter();
                Utils.setLog(tab.getId() + " unselect");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Utils.setLog(tab.getId() + " reselect");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflater fail 的原因是activity開太多 刪掉一些就好了
        //是因為out of memory 調整圖片大小也可解決
//        ActivityUtils.getInstance().cleanActivityBesidesDialog(this);
        ActivityUtils.getInstance().printActivity();
        int initPage = 0;
        if (initPage != 3)
            tabIconImages[initPage].setColorFilter(Utils.getAttrID
                    (MainPage.this, R.attr.colorPrimary, Utils.DATA), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(initPage).select();
    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        dailyMoodsSet = new HashSet<DailyMoods>();
        fcBasicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fcDailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        fcLoginDate = new FileController(this, getResources().getString(R.string.login_date));
        try {
            //set basic theme color
            String[] splitFileData = fcDailyMood.readFileSplit();
            for (String lineData : splitFileData) {
                String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                dailyMoodsSet.add(new DailyMoods(lineDataArray[0], Integer.parseInt(lineDataArray[1]), lineDataArray[2]));
                if (lineDataArray[0].equals(fcLoginDate.readFile())) {
                    Utils.setLog("Mood Type = " + lineDataArray[1]);
                    switch (Integer.parseInt(lineDataArray[1])) {
                        case 3:
                        case 4:
                            themeColor = R.style.Theme_BlackDogLab_Blue;
                            break;
                        case 2:
                            themeColor = R.style.Theme_BlackDogLab_Green;
                            break;
                        case 0:
                        case 1:
                            themeColor = R.style.Theme_BlackDogLab_Brown;
                            break;
                        default:
                            themeColor = R.style.Theme_BlackDogLab_Default;
                            Utils.setLog("Not Set Today's Mood Yet");
                            break;
                    }
                }
            }
            Utils.setLog("Set Theme Success");
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
        try {
            String[] playerBasicInfo = fcBasicInfo.readLineSplit();
            if (playerBasicInfo.length == 3) {
                player = new Player(playerBasicInfo[0], playerBasicInfo[1], Integer.parseInt(playerBasicInfo[2]));
                Utils.setLog("Get Player Info Success");
            } else
                Utils.setLog("Player Basic Info Format Wrong");

        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbt_tab_triangle:
                mTabLayout.animate().translationY(0).setInterpolator(new LinearInterpolator());
                mImgBtTabTriangle.animate().alpha(0);
                mTabLayout.setBackgroundResource(R.drawable.bg_tab);
                isTabTriangleShow = false;
                break;
            default:
                break;
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void initTabDrawable() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_icon, null);

        tabIconImages = new ImageView[]{
                view.findViewById(R.id.icon_tab_main), view.findViewById(R.id.icon_tab_calendar),
                view.findViewById(R.id.icon_tab_note), view.findViewById(R.id.icon_tab_portrait)
        };
        int[] tabIconDrawables = new int[]{
                R.drawable.icon_main, R.drawable.icon_calender,
                R.drawable.icon_note, player.getCharacterDrawable()
        };
        for (int i = 0; i < tabIconImages.length; i++) {
            int imagePadding = (int) Utils.convertDpToPixel(this, 17);
            tabIconImages[i].setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            setImageDrawableFit(tabIconImages[i], tabIconDrawables[i]);
            mTabLayout.getTabAt(i).setCustomView(tabIconImages[i]);
            mTabLayout.getTabAt(i).setId(i);
        }
    }

}