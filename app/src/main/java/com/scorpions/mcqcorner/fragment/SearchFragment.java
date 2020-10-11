package com.scorpions.mcqcorner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.adapter.MCQAdapter;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.model.McqModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private List<McqModel> mcqModelList;
    private RecyclerView rvSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchView = view.findViewById(R.id.fSearch_searchView);
        rvSearch = view.findViewById(R.id.fSearch_rvSearch);
        mcqModelList = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mcqModelList.clear();
                if (s.length() > 2) {
                    final String searchString = s.toLowerCase();
                    db.collection(Global.MCQ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                String question = ds.getString("question");
                                String category = ds.getString("category");
                                assert category != null;
                                category = category.toLowerCase();
                                assert question != null;
                                question = question.toLowerCase();
                                if (question.contains(searchString) || category.contains(searchString)) {
                                    McqModel mcqModel = ds.toObject(McqModel.class);
                                    mcqModelList.add(mcqModel);
                                }
                            }
                            if (mcqModelList.size() > 0) {
                                MCQAdapter recyclerViewAdapter = new MCQAdapter(mcqModelList);
                                rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rvSearch.setAdapter(recyclerViewAdapter);
                            } else {
                                Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}
