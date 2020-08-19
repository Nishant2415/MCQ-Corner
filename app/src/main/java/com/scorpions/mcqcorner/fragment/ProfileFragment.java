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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.EditProfile;
import com.scorpions.mcqcorner.model.ProfileModel;

import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView cimageView;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userid;
    Button btnedt;
    TextView txtUserName, txtUserWebsite, txtUserBio;
    ProfileModel profileModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cimageView = getView().findViewById(R.id.profile);
        btnedt = getView().findViewById(R.id.btnedit);
        txtUserName  = getView().findViewById(R.id.lUserName_profile);
        txtUserWebsite = getView().findViewById(R.id.lUserWebsite_profile);
        //txtUserBio = getView().findViewById(R.id.luserBio_profile);
        btnedtclick();

    }
    /*private  void getProfile()
    {
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userid = fauth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("Profile").document(userid);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                txtUserName.setText(value.getString("userName"));
                txtUserWebsite.setText(value.getString("webSite"));
                txtUserBio.setText(value.getString("bio"));


            }
        });
    }
*/

    private void btnedtclick() {
        btnedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), EditProfile.class);
                startActivity(intent);
            }
        });
    }
}
