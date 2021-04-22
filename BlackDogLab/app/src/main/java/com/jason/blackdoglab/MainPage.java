package com.jason.blackdoglab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

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
    private ImageButton mImgBtLeft, mImgBtRight;
    private int mThemeColor = R.style.Theme_BlackDogLab_Blue;


    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_main_page;
    }

    @Override
    protected int getThemeID() {
        return mThemeColor;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mImgBtLeft = findViewById(R.id.imgbt_left);
        mImgBtRight = findViewById(R.id.imgbt_right);
    }

    @Override
    protected void initListener() {
        mViewPager.setAdapter(new PageAdapter(this));
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
                    tab.setIcon(R.drawable.icon_main);
                    break;
            }
        }
        );
        tabLayoutMediator.attach();
        mTabLayout.setElevation(15);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //from current position
//                mTabLayout.animate().translationYBy(10);
                //from initial position
//                mTabLayout.animate().translationY(10);
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
        //TODO get theme color
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbt_left:

                break;
            case R.id.imgbt_right:
                break;
            default:
                break;
        }
    }
}