package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.MainActivity;
import com.scorpions.mcqcorner.activity.UserProfileActivity;
import com.scorpions.mcqcorner.adapter.MCQAdapter;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.model.McqModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {

    private SearchView edtSearch;
    private List<McqModel> mcqModelList;
    private RecyclerView rvSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MainActivity mainActivity;
    private Toolbar toolbar;

    public SearchFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = view.findViewById(R.id.fSearch_toolbar);
        mainActivity.setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtSearch = toolbar.findViewById(R.id.janghiyo);
        rvSearch = view.findViewById(R.id.fSearch_rvSearch);
        edtSearch.setIconifiedByDefault(false);
        mcqModelList = new ArrayList<>();

        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
                    final String searchString = s.toLowerCase();
                    db.collection(Global.MCQ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mcqModelList.clear();
                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                String question = ds.getString("question");
                                String category = ds.getString("category");
                                if (question != null && category != null) {
                                    question = question.toLowerCase();
                                    category = category.toLowerCase();
                                    if (question.contains(searchString) || category.contains(searchString)) {
                                        McqModel mcqModel = ds.toObject(McqModel.class);
                                        mcqModelList.add(mcqModel);
                                    }
                                }
                            }
                            if (mcqModelList.size() > 0) {
                                MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                                rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rvSearch.setAdapter(recyclerViewAdapter);

                                recyclerViewAdapter.usernameClicked(new MCQAdapter.setOnUsernameClickedListener() {
                                    @Override
                                    public void OnUsernameClick(String userId) {
                                        if (getActivity() != null) {
                                            getActivity().startActivity(new Intent(getActivity(), UserProfileActivity.class)
                                                    .putExtra(Global.USER_ID, userId));
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please type a word more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}
