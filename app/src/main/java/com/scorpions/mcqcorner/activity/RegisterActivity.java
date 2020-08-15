package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.scorpions.mcqcorner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.scorpions.mcqcorner.fragment.RegisterEmailFragment;
import com.scorpions.mcqcorner.fragment.RegisterMobileFragment;


public class RegisterActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView txtSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        getSupportFragmentManager().beginTransaction().replace(R.id.register_container, new RegisterMobileFragment()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            Fragment fragment;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        fragment = new RegisterMobileFragment();
                        break;
                    case 1:
                        fragment = new RegisterEmailFragment();
                        break;
                    default:
                        fragment = null;
                        break;
                }
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.register_container, fragment).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {

        tabLayout = findViewById(R.id.aRegister_tabLayout);
        txtSignIn = findViewById(R.id.aRegister_txtSignIn);
        txtSignIn.setText(Html.fromHtml("Already registered? <b><font color='#DC1414'>Sign In</font</b>"));
    }
}