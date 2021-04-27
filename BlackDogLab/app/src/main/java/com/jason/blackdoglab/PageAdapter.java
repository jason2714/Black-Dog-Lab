package com.jason.blackdoglab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jason.blackdoglab.utils.Utils;

class PageAdapter extends FragmentStateAdapter {


    private Player player;
    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public PageAdapter(@NonNull FragmentActivity fragmentActivity,Player player) {
        super(fragmentActivity);
        this.player = player;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new CalendarFragment();
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
