package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.codepath.runnershigh.PagerAdapter;
import com.codepath.runnershigh.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

//This fragment holds the TabLayout and ViewPager to switch b/w StatsFragment and GraphsFragment

public class ResultsFragment extends Fragment {
    ViewPager2 viewPager;   //Viewpager that will facilitate switching b/w graphs/stats
    TabLayout tabLayout;    //Tab layout to switch b/w fragments
    PagerAdapter pagerAdapter;

    public ResultsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapter(this);

        viewPager = view.findViewById(R.id.vpMI);
        tabLayout = view.findViewById(R.id.tlMI);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {

            //Set up the tab titles
            switch (position) {
                case 0:
                    tab.setText("Stats");
                    break;
                case 1:
                    tab.setText("Graphs");
                    break;
                case 2:
                    tab.setText("Comments");
                    break;
                default:
                    tab.setText("X-AE-12");
            }
        }).attach();
    }
}
