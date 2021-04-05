package com.jason.blackdoglab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jason.blackdoglab.utils.Utils;

public class MainPage extends AppCompatActivity {

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

    private ViewPager2 mViewPager;
    private  TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Utils.hideNavigationBar(MainPage.this);

        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        initListener();
    }

    private void initListener(){
        mViewPager.setAdapter(new PageAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                mTabLayout, mViewPager, (tab, position) -> {
            tab.setId(position);
            switch (position) {
                case 0:
//                    tab.setText("Main");
                    tab.setIcon(R.drawable.icon_recording);
                    break;
                case 1:
//                    tab.setText("Calender");
                    tab.setIcon(R.drawable.icon_shoot_photo);
                    break;
                case 2:
//                    tab.setText("Note");
                    tab.setIcon(R.drawable.icon_record_video);
                    break;
                case 3:
//                    tab.setText("User");
                    tab.setIcon(R.drawable.icon_recording);
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
                mTabLayout.animate().setStartDelay(500).translationY(10);
                Utils.setLog(tab.getId() + " select");
                tab.setIcon(R.drawable.open_book);
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

}