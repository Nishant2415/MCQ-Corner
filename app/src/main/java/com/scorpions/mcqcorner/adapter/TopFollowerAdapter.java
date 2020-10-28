package com.scorpions.mcqcorner.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.MainActivity;
import com.scorpions.mcqcorner.activity.UserProfileActivity;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.fragment.ProfileFragment;
import com.scorpions.mcqcorner.model.FollowerModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopFollowerAdapter extends RecyclerView.Adapter<TopFollowerAdapter.ViewHolder> {

    private static final String TAG = "TopFollowerAdapter";
    private final List<FollowerModel> followerModels;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TopFollowerAdapter(List<FollowerModel> topFollowers) {
            this.followerModels = topFollowers;
    }

    @NonNull
    @Override
    public TopFollowerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_top_followers, parent, false);
            return new TopFollowerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FollowerModel followerModel = followerModels.get(position);

        db.collection(Global.PROFILE).document(followerModel.getUserId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        holder.userName.setText(documentSnapshot.getString(Global.USERNAME));
                        Glide.with(holder.itemView)
                                .load(documentSnapshot.getString(Global.PROFILE_PIC))
                                .placeholder(R.drawable.user)
                                .into(holder.imgProfilePic);
                    }
                });

        holder.userName.setText(followerModel.getUsername());
        if(followerModel.getUserId().equals(Preference.getString(holder.itemView.getContext(),Global.USER_ID)))
        {
            holder.follow.setVisibility(View.GONE);
        }
        db.collection(Global.PROFILE).document(Preference.getString(holder.itemView.getContext(),Global.USER_ID)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(String following:(List<String>)documentSnapshot.get(Global.FOLLOWING))
                        {
                            if(following.equals(followerModel.getUserId()))
                            {
                                holder.follow.setText("Following");
                                holder.follow.setEnabled(false);
                            }
                        }
                    }
                });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Map<String, Object> followMap = new HashMap<>();
                followMap.put(Global.FOLLOWING, FieldValue.arrayUnion(followerModel.getUserId()));
                db.collection(Global.PROFILE).document(Preference.getString(view.getContext(), Global.USER_ID)).update(followMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> followerMap = new HashMap<>();
                            followerMap.put(Global.FOLLOWERS, FieldValue.arrayUnion(Preference.getString(view.getContext(), Global.USER_ID)));
                            followerMap.put(Global.FOLLOERCOUNT,FieldValue.increment(1));
                            db.collection(Global.PROFILE).document(followerModel.getUserId()).update(followerMap);
                            holder.follow.setText("Following");
                            holder.follow.setEnabled(false);
                        } else {
                            Log.e(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            }
        });
        holder.imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!followerModel.getUserId().equals(Preference.getString(view.getContext(),Global.USER_ID))) {
                    Intent i = new Intent(view.getContext(), UserProfileActivity.class);
                    i.putExtra(Global.USER_ID, followerModel.getUserId());
                    view.getContext().startActivity(i);
                }
                else
                {
                    Context context=view.getContext();
                    if (context.getClass().equals(MainActivity.class)) {
                    BottomNavigationView bnv=((MainActivity) context).findViewById(R.id.aMain_bottomNavigationView);
                    bnv.setSelectedItemId(R.id.mNavigation_profile);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment()).commit();

                    }
                }
            }
        });
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),UserProfileActivity.class);
                i.putExtra(Global.USER_ID,followerModel.getUserId());
                view.getContext().startActivity(i);
            }
        });


    }

        @Override
        public int getItemCount() {
            return followerModels.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView userName;
            private Button follow;
            private ImageView imgProfilePic;

            public ViewHolder(final View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.lTopFollower_userName);
                follow = itemView.findViewById(R.id.lTopFollower_btnFollow);
                imgProfilePic = itemView.findViewById(R.id.lTopFollower_imgProfilePic);
            }
        }
    }