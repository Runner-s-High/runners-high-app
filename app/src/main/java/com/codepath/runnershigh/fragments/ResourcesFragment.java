package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.runnershigh.R;

/*
This fragment has multiple mental health resources in the form of website hyperlinks and phone
numbers that the user can access from within the app.
 */
public class ResourcesFragment extends Fragment {
    ResourcesFragmentInterface resourcesFragmentInterface;

    public ResourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvPhone1, tvPhone2, tvRes1, tvRes2, tvRes3;

        tvPhone1 = view.findViewById(R.id.tvPhone1);
        tvPhone2 = view.findViewById(R.id.tvPhone2);
        tvRes1 = view.findViewById(R.id.tvRes1);
        tvRes2 = view.findViewById(R.id.tvRes2);
        tvRes3 = view.findViewById(R.id.tvRes3);

        //Setting up links for web browser and phone app
        tvRes1.setMovementMethod(LinkMovementMethod.getInstance());
        tvRes2.setMovementMethod(LinkMovementMethod.getInstance());
        tvRes3.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(tvPhone1, Linkify.ALL);
        Linkify.addLinks(tvPhone2, Linkify.ALL);
    }

    //Attaching the interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RunningFragment.RunningFragmentInterface){
            resourcesFragmentInterface=(ResourcesFragment.ResourcesFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement ResourcesFragmentInterface");
        }
    }

    public interface ResourcesFragmentInterface {
        void openResourcesFragment();
    }
}