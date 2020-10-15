package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.LoginActivity;
import com.scorpions.mcqcorner.activity.MainActivity;
import com.scorpions.mcqcorner.activity.PostMcqActivity;
import com.scorpions.mcqcorner.activity.UserProfileActivity;
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
    private MainActivity mainActivity;

    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.fHome_toolbar);
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setTitle("MCQ Corner");
        setHasOptionsMenu(true);
        return view;
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

    private void setRecyclerView(final View view) {
        db.collection(Global.PROFILE).document(Preference.getString(view.getContext(), Global.USER_ID)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            final List<String> followingList = (ArrayList<String>) documentSnapshot.get(Global.FOLLOWING);
                            final List<String> trimmedFollowingList = new ArrayList<>();
                            for (String s : followingList) {
                                trimmedFollowingList.add(s.trim());
                            }
                            if (trimmedFollowingList.size() != 0) {
                                db.collection(Global.MCQ).orderBy(Global.TIME, Query.Direction.DESCENDING).get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                                    if (trimmedFollowingList.contains(ds.getString("userId"))) {
                                                        McqModel mcqModel = ds.toObject(McqModel.class);
                                                        mcqModelList.add(mcqModel);
                                                    }
                                                }
                                                MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                                                rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                                rvPosts.setAdapter(recyclerViewAdapter);

                                                recyclerViewAdapter.usernameClicked(new MCQAdapter.setOnUsernameClickedListener() {
                                                    @Override
                                                    public void OnUsernameClick(String userId) {
                                                        if(getActivity()!=null) {
                                                            if(!userId.equals(Preference.getString(getActivity().getApplicationContext(),Global.USER_ID))) {
                                                                getActivity().startActivity(new Intent(getActivity(), UserProfileActivity.class)
                                                                        .putExtra(Global.USER_ID, userId));
                                                            }
                                                            else
                                                            {
                                                                BottomNavigationView bnv=getActivity().findViewById(R.id.aMain_bottomNavigationView);
                                                                bnv.setSelectedItemId(R.id.mNavigation_profile);
                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment()).commit();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout && getActivity() != null) {
            startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            FirebaseAuth.getInstance().signOut();
            Preference.setBoolean(getActivity(), Global.IS_LOGGED_IN, false);
            getActivity().finish();
            return true;
        }
        return false;
    }
}