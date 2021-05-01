package com.jason.blackdoglab;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    private long exitTime = 0;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private ImageButton mImgBtTabTriangle;
    private int themeColor;
    private boolean isTabTriangleShow;
    private Player player;
    private HashSet<DailyMoods> dailyMoodsSet;


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
    }

    @Override
    protected void initListener() {
        mImgBtTabTriangle.setOnClickListener(this);
        mViewPager.setAdapter(new PageAdapter(this, player, dailyMoodsSet));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                mTabLayout, mViewPager, (tab, position) -> {
            tab.setId(position);
            switch (position) {
                case 0:
//                    tab.setText("Main");
                    tab.setIcon(R.drawable.icon_main);
                    break;
                case 1:
//                    tab.setText("Calender");
                    tab.setIcon(R.drawable.icon_calender);
                    break;
                case 2:
//                    tab.setText("Note");
                    tab.setIcon(R.drawable.icon_note);
                    break;
                case 3:
//                    tab.setText("User");
                    tab.setIcon(R.drawable.icon_select_character1);
                    //keep icon colorful
                    tab.getIcon().setTintMode(PorterDuff.Mode.DST);
                    break;
                default:
                    tab.setIcon(R.drawable.icon_right_triangle);
                    break;
            }
        });
        tabLayoutMediator.attach();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!isTabTriangleShow) {
                    mTabLayout.animate().translationY(Utils.convertDpToPixel(MainPage.this,55)).setStartDelay(500).setDuration(500);
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
        ActivityUtils.getInstance().cleanActivity(this);
        ActivityUtils.getInstance().printActivity();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityUtils.getInstance().printActivity();
        // 監聽返回键，點兩次退出process
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("真的要離開嗎QQ");
                exitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.getInstance().exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initBasicInfo() {
        dailyMoodsSet = new HashSet<DailyMoods>();
        fc_basicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fc_dailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        fc_loginDate = new FileController(this, getResources().getString(R.string.login_date));
        try {
            //set basic theme color
            String[] splitFileData = fc_dailyMood.readFileSplit();
            for (String lineData : splitFileData) {
                String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                dailyMoodsSet.add(new DailyMoods(lineDataArray[0], Integer.parseInt(lineDataArray[1]), lineDataArray[2]));
                if (lineDataArray[0].equals(fc_loginDate.readFile())) {
                    Utils.setLog("Mood Type = " + lineDataArray[1]);
                    switch (Integer.parseInt(lineDataArray[1])) {
                        case 0:
                        case 1:
                            themeColor = R.style.Theme_BlackDogLab_Blue;
                            break;
                        case 2:
                            themeColor = R.style.Theme_BlackDogLab_Green;
                            break;
                        case 3:
                        case 4:
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
            String[] playerBasicInfo = fc_basicInfo.readLineSplit();
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
            case R.id.imgbt_left:

                break;
            case R.id.imgbt_right:
                break;
            case R.id.imgbt_tab_triangle:
                mTabLayout.animate().translationY(0);
                mImgBtTabTriangle.animate().alpha(0);
                mTabLayout.setBackgroundResource(R.drawable.bg_tab);
                isTabTriangleShow = false;
                break;
            default:
                break;
        }
    }

    public ViewPager2 getViewPager() {
        return mViewPager;
    }

}