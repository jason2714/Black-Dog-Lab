package com.jason.blackdoglab;

import android.icu.text.Edits;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jason.blackdoglab.fragment.CalendarFragment;
import com.jason.blackdoglab.fragment.MainFragment;
import com.jason.blackdoglab.fragment.NoteFragment;
import com.jason.blackdoglab.fragment.UserFragment;
import com.jason.blackdoglab.utils.Utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class PageAdapter extends FragmentStateAdapter {


    private Player player;
    private HashSet<DailyMoods> dailyMoodsSet;
    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public PageAdapter(@NonNull FragmentActivity fragmentActivity,Player player,HashSet<DailyMoods> dailyMoodsSet) {
        super(fragmentActivity);
        this.player = player;
        this.dailyMoodsSet = dailyMoodsSet;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new CalendarFragment(dailyMoodsSet);
            case 2:
                return new NoteFragment();
            case 3:
                return new UserFragment(player);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
