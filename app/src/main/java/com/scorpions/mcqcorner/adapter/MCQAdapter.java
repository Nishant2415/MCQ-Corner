package com.scorpions.mcqcorner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.model.McqModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MCQAdapter extends RecyclerView.Adapter<MCQAdapter.ViewHolder> {

    private List<McqModel> postedMCQ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MCQAdapter(List<McqModel> postedMCQ) {
        this.postedMCQ = postedMCQ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mcq_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MCQAdapter.ViewHolder holder, int position) {
        final McqModel mcqModel = postedMCQ.get(position);

        db.collection(Global.PROFILE).document(mcqModel.getUserId()).get()
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy h:mm a", Locale.US);
        String mcqTime = sdf.format(mcqModel.getTime());
        holder.time.setText(mcqTime);
        holder.question.setText(mcqModel.getQuestion());
        holder.optionA.setText(String.format("A. %s", mcqModel.getOptionA()));
        holder.optionB.setText(String.format("B. %s", mcqModel.getOptionB()));
        holder.optionC.setText(String.format("C. %s", mcqModel.getOptionC()));
        holder.optionD.setText(String.format("D. %s", mcqModel.getOptionD()));
        holder.answer.setText(String.format("ANSWER : %s", mcqModel.getAnswer()));
    }

    @Override
    public int getItemCount() {
        return postedMCQ.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, time, question, optionA, optionB, optionC, optionD, answer;
        private ImageView imgProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.rvAdapter_username);
            time = itemView.findViewById(R.id.rvAdapter_timestamp);
            question = itemView.findViewById(R.id.rvAdapter_MCQ_Question);
            optionA = itemView.findViewById(R.id.rvAdapter_MCQ_OptionA);
            optionB = itemView.findViewById(R.id.rvAdapter_MCQ_OptionB);
            optionC = itemView.findViewById(R.id.rvAdapter_MCQ_OptionC);
            optionD = itemView.findViewById(R.id.rvAdapter_MCQ_OptionD);
            answer = itemView.findViewById(R.id.rvAdapter_MCQ_Answer);
            imgProfilePic = itemView.findViewById(R.id.rvAdapter_user_image);
        }
    }
}