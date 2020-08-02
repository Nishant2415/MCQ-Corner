package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpions.mcqcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private TextView txtSignUp;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if(mAuth.getCurrentUser() != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();

                    if(TextUtils.isEmpty(email)) {
                        edtEmail.setError("Invalid");
                        edtEmail.requestFocus();
                    } else if(TextUtils.isEmpty(password)) {
                        edtPassword.setError("Invalid");
                        edtPassword.requestFocus();
                    } else {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        mAuth = FirebaseAuth.getInstance();
        edtEmail =findViewById(R.id.aLogin_edtEmail);
        edtPassword =findViewById(R.id.aLogin_edtPassword);
        btnSignIn=findViewById(R.id.aLogin_btnLogin);
        txtSignUp =findViewById(R.id.aLogin_txtSignUp);
        sp = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
    }
}
