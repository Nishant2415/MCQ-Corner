package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.fragment.HomeFragment;
import com.scorpions.mcqcorner.fragment.ProfileFragment;
import com.scorpions.mcqcorner.fragment.SearchFragment;
import com.scorpions.mcqcorner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(this)).commit();

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.mNavigation_home:
                        fragment = new HomeFragment(MainActivity.this);
                        break;
                    case R.id.mNavigation_search:
                        fragment = new SearchFragment(MainActivity.this);
                        break;
                    case R.id.mNavigation_profile:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = null;
                        break;
                }

                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();

                return true;
            }
        });
    }

    private void init() {
        bnv = findViewById(R.id.aMain_bottomNavigationView);
    }


}
