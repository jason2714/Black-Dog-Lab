package com.dji.artproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainPage extends AppCompatActivity {

    public class TabPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 3;

        public TabPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ViewPager2 mViewPager = findViewById(R.id.view_pager);
        TabLayout mTabLayout = findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new PageAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Main");
                        tab.setIcon(R.drawable.icon_recording);
                        break;
                    case 1:
                        tab.setText("Calender");
                        tab.setIcon(R.drawable.icon_shoot_photo);
                        break;
                    case 2:
                        tab.setText("Note");
                        tab.setIcon(R.drawable.icon_record_video);
                        break;
                    case 3:
                        tab.setText("User");
                        tab.setIcon(R.drawable.icon_recording);
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();
        mTabLayout.setElevation(15);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}