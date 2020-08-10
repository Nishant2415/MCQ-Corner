package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpions.mcqcorner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scorpions.mcqcorner.activity.PostMcqActivity;


public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostMcqActivity.class));
            }
        });
    }
}