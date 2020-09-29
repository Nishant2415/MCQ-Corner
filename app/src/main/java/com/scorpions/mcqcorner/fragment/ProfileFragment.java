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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.EditProfile;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView cimageView;
    FirebaseFirestore db;
    String userid;
    Button btnedt;
    TextView txtUserName, txtUserWebsite;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cimageView = view.findViewById(R.id.profile);
        btnedt = view.findViewById(R.id.btnedit);
        txtUserName = view.findViewById(R.id.lUserName_profile);
        txtUserWebsite = view.findViewById(R.id.lUserWebsite_profile);
        btnedtclick();

        getProfile(view);
    }

    private void getProfile(View view) {
        db = FirebaseFirestore.getInstance();

        userid = Preference.getString(view.getContext(), Global.USER_ID);

        db.collection(Global.PROFILE).document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    txtUserName.setText(documentSnapshot.getString(Global.USERNAME));
                    txtUserWebsite.setText(documentSnapshot.getString(Global.WEBSITE));
                }
            }
        });
    }

    private void btnedtclick() {
        btnedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
    }
}
