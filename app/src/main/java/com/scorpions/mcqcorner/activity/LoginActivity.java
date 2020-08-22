package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;
import com.scorpions.mcqcorner.model.UserModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText edtUsername, edtPassword;
    private Button btnSignIn;
    private TextView txtSignUp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if (mAuth.getCurrentUser() != null &&
                mAuth.getCurrentUser().isEmailVerified() ||
                Preference.getBoolean(getApplicationContext(), Global.IS_LOGGED_IN)) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.showLoadingDialog(LoginActivity.this);
                    userModel = new UserModel(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());

                    if (TextUtils.isEmpty(userModel.getUsername())) {
                        edtUsername.setError(Global.INVALID);
                    } if (TextUtils.isEmpty(userModel.getPassword())) {
                        edtPassword.setError(Global.INVALID);
                    } else {
                        if (userModel.getUsername().contains("@")) {
                            mAuth.signInWithEmailAndPassword(userModel.getUsername(), userModel.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Global.dismissDialog();
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, Global.INVALID_EMAIL, Toast.LENGTH_SHORT).show();
                                        if (task.getException() != null)
                                            Log.e(TAG, "onComplete: " + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            db.collection(Global.PROFILE)
                                    .whereEqualTo(Global.USERNAME, userModel.getUsername()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Global.dismissDialog();
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        Toast.makeText(LoginActivity.this, Global.INVALID_USERNAME, Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                            String password = documentSnapshot.getString(Global.PASSWORD);
                                            if(userModel.getPassword().equals(password)) {
                                                Preference.setBoolean(getApplicationContext(), Global.IS_LOGGED_IN, true);
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, Global.INVALID_USERNAME, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

            txtSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
        }
    }

    private void init() {
        edtUsername = findViewById(R.id.aLogin_edtUsername);
        edtPassword = findViewById(R.id.aLogin_edtPassword);
        btnSignIn = findViewById(R.id.aLogin_btnLogin);
        txtSignUp = findViewById(R.id.aLogin_txtSignUp);
        txtSignUp.setText(Html.fromHtml("Not registered? <b><font color='#DC1414'>Sign up</font</b>"));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
}
