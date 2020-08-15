package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.OTPVerificationActivity;


public class RegisterMobileFragment extends Fragment {

    public RegisterMobileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_mobile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNext = view.findViewById(R.id.nextMobileNumber);
        final EditText edtMobileNo = view.findViewById(R.id.fRegisterMobile_edtMobileNo);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMobileNo.getText().toString().isEmpty()) {
                    startActivity(new Intent(view.getContext(), OTPVerificationActivity.class)
                            .putExtra("mobileNo", edtMobileNo.getText().toString().trim()));
                } else {
                    Toast.makeText(view.getContext(), "Please enter mobile number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}