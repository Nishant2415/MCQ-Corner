package com.scorpions.mcqcorner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.model.McqModel;

public class PostMcqActivity extends AppCompatActivity {
    private RadioGroup rgOption;
    private Spinner spCategory;
    private EditText edtQuestion, edtOptionA, edtOptionB, edtOptionC, edtOptionD;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_mcq);
        init();

        findViewById(R.id.lPostMcq_btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rgOption.getCheckedRadioButtonId();
                final String question = edtQuestion.getText().toString();
                final String optionA = edtOptionA.getText().toString();
                final String optionB = edtOptionB.getText().toString();
                final String optionC = edtOptionC.getText().toString();
                final String optionD = edtOptionD.getText().toString();
                final String category = spCategory.getSelectedItem().toString();
                if(TextUtils.isEmpty(question) || TextUtils.isEmpty(optionA) || TextUtils.isEmpty(optionB) || TextUtils.isEmpty(optionC) || TextUtils.isEmpty(optionD) || TextUtils.isEmpty(category) || selectedId == -1)
                {
                    Toast.makeText(PostMcqActivity.this, "fields can not be blank", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String answer;
                    if(selectedId == R.id.aPostMcq_rbOptionA)
                    {
                       answer = "A";
                    }
                    else if(selectedId == R.id.aPostMcq_rbOptionB)
                    {
                        answer = "B";
                    }
                    else if(selectedId == R.id.aPostMcq_rbOptionC)
                    {
                        answer = "C";
                    }
                    else
                    {
                        answer = "D";
                    }
                    postMCQ(question,optionA,optionB,optionC,optionD,answer,category);
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
    }

    private void postMCQ(String question,String optionA, String optionB, String optionC, String optionD, String answer,String category) {
        McqModel mcqModel = new McqModel(question,optionA,optionB,optionC,optionD,answer,category);
        db.collection(Global.MCQ).document().set(mcqModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(PostMcqActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PostMcqActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}