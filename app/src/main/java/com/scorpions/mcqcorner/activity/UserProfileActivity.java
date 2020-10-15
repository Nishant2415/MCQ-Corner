package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.adapter.MCQAdapter;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.model.McqModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private TextView txtUsername, txtWebsite, txtPostCount, txtFollowingCount, txtFollowerCount;
    private RecyclerView rvPosts;
    private List<McqModel> mcqModelList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFollow;
    private String userID;
    private ImageView imgProfilePic;
    private long postsCount;
    private static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();

        setProfile();

        setRecyclerView();

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0)
                    followUser();
                else
                    unFollowUser();
            }
        });
    }

    private void init() {
        txtUsername = findViewById(R.id.aUserProfile_txtUsername);
        txtWebsite = findViewById(R.id.aUserProfile_txtWebsite);
        txtPostCount = findViewById(R.id.aUserProfile_txtPostCount);
        txtFollowingCount = findViewById(R.id.aUserProfile_txtFollowingCount);
        txtFollowerCount = findViewById(R.id.aUserProfile_txtFollowerCount);
        rvPosts = findViewById(R.id.aUserProfile_recyclerView);
        rvPosts.setNestedScrollingEnabled(false);
        mcqModelList = new ArrayList<>();
        btnFollow = findViewById(R.id.aUserProfile_btnFollow);
        imgProfilePic = findViewById(R.id.aUserProfile_imgProfilePic);
        userID = getIntent().getStringExtra(Global.USER_ID);
    }

    private void setProfile() {
        db.collection(Global.PROFILE).document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Glide.with(getApplicationContext())
                            .load(documentSnapshot.getString(Global.PROFILE_PIC))
                            .placeholder(R.drawable.user)
                            .into(imgProfilePic);
                    txtUsername.setText(documentSnapshot.getString(Global.USERNAME));
                    txtWebsite.setText(documentSnapshot.getString(Global.WEBSITE));
                    ArrayList<String> following = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWING);
                    ArrayList<String> followers = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWERS);
                    txtFollowingCount.setText(String.valueOf(following.size()));
                    txtFollowerCount.setText(String.valueOf(followers.size()));
                    postsCount = documentSnapshot.getLong(Global.POSTS);
                    txtPostCount.setText(String.valueOf(postsCount));
                    if (followers.contains(Preference.getString(UserProfileActivity.this, Global.USER_ID))) {
                        btnFollow.setText(Global.UNFOLLOW);
                        flag = 1;
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        db.collection(Global.MCQ).orderBy(Global.TIME, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            if (userID.equals(ds.getString(Global.USER_ID))) {
                                McqModel mcqModel = ds.toObject(McqModel.class);
                                mcqModelList.add(mcqModel);
                            }
                        }
                        MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                        rvPosts.setLayoutManager(new LinearLayoutManager(UserProfileActivity.this));
                        rvPosts.setAdapter(recyclerViewAdapter);
                    }
                });
    }

    private void followUser() {
        Map<String, Object> followMap = new HashMap<>();
        followMap.put(Global.FOLLOWING, FieldValue.arrayUnion(userID));
        db.collection(Global.PROFILE).document(Preference.getString(this, Global.USER_ID)).update(followMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> followerMap = new HashMap<>();
                    followerMap.put(Global.FOLLOWERS, FieldValue.arrayUnion(Preference.getString(UserProfileActivity.this, Global.USER_ID)));
                    db.collection(Global.PROFILE).document(userID).update(followerMap);
                    flag = 1;
                    btnFollow.setText(Global.UNFOLLOW);
                } else {
                    Log.e(TAG, "onComplete: " + task.getException());
                }
            }
        });
    }

    private void unFollowUser() {
        Map<String, Object> followMap = new HashMap<>();
        followMap.put(Global.FOLLOWING, FieldValue.arrayRemove(userID));
        db.collection(Global.PROFILE).document(Preference.getString(this, Global.USER_ID)).update(followMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> followerMap = new HashMap<>();
                    followerMap.put(Global.FOLLOWERS, FieldValue.arrayRemove(Preference.getString(UserProfileActivity.this, Global.USER_ID)));
                    db.collection(Global.PROFILE).document(userID).update(followerMap);
                    flag = 0;
                    btnFollow.setText(Global.FOLLOW);
                } else {
                    Log.e(TAG, "onComplete: " + task.getException());
                }
            }
        });
    }
}