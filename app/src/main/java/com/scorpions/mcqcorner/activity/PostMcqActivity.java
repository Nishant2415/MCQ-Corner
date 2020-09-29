package com.scorpions.mcqcorner.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.model.McqModel;

public class PostMcqActivity extends AppCompatActivity {

    private RadioGroup rgOption;
    private Spinner spCategory;
    private EditText edtQuestion, edtOptionA, edtOptionB, edtOptionC, edtOptionD;
    private ImageView imgProfilePic;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_mcq);
        init();

        db.collection(Global.PROFILE).document(Preference.getString(PostMcqActivity.this, Global.USER_ID)).get()
                .addOnSuccessListener(PostMcqActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(getApplicationContext()).load(documentSnapshot.getString(Global.PROFILE_PIC)).into(imgProfilePic);
                    }
                });

        findViewById(R.id.lPostMcq_btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rgOption.getCheckedRadioButtonId();
                String question = edtQuestion.getText().toString();
                String optionA = edtOptionA.getText().toString();
                String optionB = edtOptionB.getText().toString();
                String optionC = edtOptionC.getText().toString();
                String optionD = edtOptionD.getText().toString();
                String category = spCategory.getSelectedItem().toString();

                if (TextUtils.isEmpty(question) || TextUtils.isEmpty(optionA) || TextUtils.isEmpty(optionB) || TextUtils.isEmpty(optionC) || TextUtils.isEmpty(optionD) || TextUtils.isEmpty(category) || selectedId == -1) {
                    Toast.makeText(PostMcqActivity.this, "Fields can not be blank", Toast.LENGTH_SHORT).show();
                } else {
                    String answer = "N/A";
                    if (selectedId == R.id.aPostMcq_rbOptionA) {
                        answer = "A";
                    } else if (selectedId == R.id.aPostMcq_rbOptionB) {
                        answer = "B";
                    } else if (selectedId == R.id.aPostMcq_rbOptionC) {
                        answer = "C";
                    } else if (selectedId == R.id.aPostMcq_rbOptionD){
                        answer = "D";
                    }
                    McqModel mcqModel = new McqModel(Preference.getString(PostMcqActivity.this, Global.USER_ID),question, optionA, optionB, optionC, optionD, answer, category, System.currentTimeMillis());
                    postMCQ(mcqModel);
                }
            }
        });

        findViewById(R.id.lPostMcq_imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        edtQuestion = findViewById(R.id.aPostMcq_edtQuestion);
        edtOptionA = findViewById(R.id.aPostMcq_edtOptionA);
        edtOptionB = findViewById(R.id.aPostMcq_edtOptionB);
        edtOptionC = findViewById(R.id.aPostMcq_edtOptionC);
        edtOptionD = findViewById(R.id.aPostMcq_edtOptionD);
        rgOption = findViewById(R.id.aPostMcq_rgOption);
        spCategory = findViewById(R.id.aPostMcq_category);
        imgProfilePic = findViewById(R.id.aPostMcq_imgProfilePic);
    }

    private void postMCQ(McqModel mcqModel) {
        db.collection(Global.MCQ).document(Preference.getString(PostMcqActivity.this, Global.USER_ID)).collection(Global.MCQ).document().set(mcqModel)
                .addOnCompleteListener(PostMcqActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PostMcqActivity.this, "MCQ uploaded!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostMcqActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}