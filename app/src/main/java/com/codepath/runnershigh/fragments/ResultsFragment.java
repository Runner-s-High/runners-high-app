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
import com.codepath.runnershigh.RunData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

public class ResultsFragment extends Fragment {
    ViewPager2 viewPager;
    TabLayout tabLayout;
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
//        therun = Parcels.unwrap(getArguments().getParcelable("run"));

        pagerAdapter = new PagerAdapter(this);
        viewPager = view.findViewById(R.id.vpMI);
        tabLayout = view.findViewById(R.id.tlMI);
        viewPager.setAdapter(pagerAdapter);

        //IDK WHAT THIS DOES
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> tab.setText("OBJECT " + (position + 1))
//        ).attach();

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Stats");
                        break;
                    case 1:
                        tab.setText("Graphs");
                        break;
                    default:
                        tab.setText("X-AE-12");
                }
            }
        }).attach();
    }

//    public Bundle getStatsBundle() {
//        Bundle statsBundle = new Bundle();
//
//        statsBundle.putString("time", therun.getRunTime());
//        statsBundle.putDouble("distance", therun.getRunDistance());
//        statsBundle.putDouble("calories", therun.getRunCalories());
//        statsBundle.putString("note", therun.getRunNote());
//        statsBundle.putInt("premood", therun.getPreRunMood());
//        statsBundle.putInt("postmood", therun.getPostRunMood());
//
//        return statsBundle;
//    }
//
//    public Bundle getGraphsBundle() {
//        Bundle graphsBundle = new Bundle();
//
//        graphsBundle.putInt("premood", therun.getPreRunMood());
//        graphsBundle.putInt("postmood", therun.getPostRunMood());
//
//        return graphsBundle;
//    }
}
