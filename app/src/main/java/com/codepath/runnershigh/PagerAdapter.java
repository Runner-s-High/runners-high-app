package com.codepath.runnershigh;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.codepath.runnershigh.fragments.GraphsFragment;
import com.codepath.runnershigh.fragments.ResultsFragment;
import com.codepath.runnershigh.fragments.StatsFragment;

//Adapter for ViewPager used in ResultsFragment in MoreInfoActivity
public class PagerAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 2;

    Bundle args;

    public PagerAdapter(@NonNull Fragment fragment) {
        super(fragment);

        ResultsFragment parentFragment = (ResultsFragment) fragment;

        args = parentFragment.getArguments();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        //Get appropriate fragment with factory method
        switch (position) {
            case 0:
                return StatsFragment.newInstance(args);
            case 1:
                return GraphsFragment.newInstance(args);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
