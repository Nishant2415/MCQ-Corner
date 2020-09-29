package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.PostMcqActivity;
import com.scorpions.mcqcorner.adapter.MCQAdapter;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.model.McqModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView rvPosts;
    private List<McqModel> mcqModelList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        rvPosts = view.findViewById(R.id.fHome_rvPost);
        mcqModelList = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostMcqActivity.class));
            }
        });
        setRecyclerView(view);
    }

    private void setRecyclerView(View view) {
        db.collection(Global.PROFILE).document(Preference.getString(view.getContext(), Global.USER_ID)).get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> following = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWING);
                            if (following != null) {
                                for (String follower : following) {
                                    db.collection(Global.MCQ).document(follower.trim()).collection(Global.MCQ).orderBy(Global.TIME, Query.Direction.DESCENDING).get()
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                                        McqModel mcqModel = ds.toObject(McqModel.class);
                                                        mcqModelList.add(mcqModel);
                                                    }
                                                    MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                                                    rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                    rvPosts.setAdapter(recyclerViewAdapter);
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }
}