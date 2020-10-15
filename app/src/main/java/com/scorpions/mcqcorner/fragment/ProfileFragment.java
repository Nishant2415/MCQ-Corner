package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.EditProfile;
import com.scorpions.mcqcorner.adapter.MCQAdapter;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.model.McqModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView cimageView;
    String userid;
    Button btnedt;
    TextView txtUserName, txtUserWebsite;
    private TextView txtEmpty, txtPostCount, txtFollowingCount, txtFollowerCount;
    private RecyclerView rvPosts;
    private List<McqModel> mcqModelList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private long postsCount;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cimageView = view.findViewById(R.id.fProfile_imgProfilePic);
        btnedt = view.findViewById(R.id.fProfile_btnEditProfile);
        txtUserName = view.findViewById(R.id.fProfile_txtUsername);
        txtUserWebsite = view.findViewById(R.id.fProfile_txtWebsite);

        txtPostCount = view.findViewById(R.id.fProfile_txtPostCount);
        txtFollowingCount = view.findViewById(R.id.fProfile_txtFollowingCount);
        txtFollowerCount = view.findViewById(R.id.fProfile_txtFollowerCount);
        txtEmpty = view.findViewById(R.id.fProfile_txtEmpty);

        rvPosts = view.findViewById(R.id.fProfile_recyclerView);
        rvPosts.setNestedScrollingEnabled(false);
        mcqModelList = new ArrayList<>();

        btnedtclick();

        getProfile(view);

        setRecyclerView(view);

        txtUserWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink();
            }
        });
    }

    private void getProfile(View view) {
        db = FirebaseFirestore.getInstance();

        userid = Preference.getString(view.getContext(), Global.USER_ID);

        db.collection(Global.PROFILE).document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && getActivity() != null) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(documentSnapshot.getString(Global.PROFILE_PIC))
                            .placeholder(R.drawable.user)
                            .into(cimageView);
                    txtUserName.setText(documentSnapshot.getString(Global.USERNAME));
                    txtUserWebsite.setText(documentSnapshot.getString(Global.WEBSITE));
                    ArrayList<String> followingCount = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWING);
                    ArrayList<String> followerCount = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWERS);
                    txtFollowingCount.setText(String.valueOf(followingCount.size()));
                    txtFollowerCount.setText(String.valueOf(followerCount.size()));
                    postsCount = documentSnapshot.getLong(Global.POSTS);
                    txtPostCount.setText(String.valueOf(postsCount));
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

    private void setRecyclerView(final View view) {
        db.collection(Global.MCQ).orderBy(Global.TIME, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            if (Preference.getString(view.getContext(), Global.USER_ID).equals(ds.getString(Global.USER_ID))) {
                                McqModel mcqModel = ds.toObject(McqModel.class);
                                mcqModelList.add(mcqModel);
                            }
                        }
                        if (postsCount == 0) {
                            rvPosts.setVisibility(View.INVISIBLE);
                            txtEmpty.setVisibility(View.VISIBLE);
                        } else {
                            MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                            rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvPosts.setAdapter(recyclerViewAdapter);
                        }
                    }
                });
    }

    private void openLink() {
        String website = txtUserWebsite.getText().toString();
        if (!TextUtils.isEmpty(website)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + website));
            startActivity(browserIntent);
        }
    }
}
