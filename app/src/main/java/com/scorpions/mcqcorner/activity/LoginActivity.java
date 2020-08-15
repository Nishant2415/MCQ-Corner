package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    private EditText edtUsername, edtPassword;
    private Button btnSignIn;
    private TextView txtSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = edtUsername.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();

                    if(TextUtils.isEmpty(username)) {
                        edtUsername.setError("Invalid");
                        edtUsername.requestFocus();
                    } else if(TextUtils.isEmpty(password)) {
                        edtPassword.setError("Invalid");
                        edtPassword.requestFocus();
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
        edtUsername =findViewById(R.id.aLogin_edtUsername);
        edtPassword =findViewById(R.id.aLogin_edtPassword);
        btnSignIn=findViewById(R.id.aLogin_btnLogin);
        txtSignUp =findViewById(R.id.aLogin_txtSignUp);
        txtSignUp.setText(Html.fromHtml("Not registered? <b><font color='#DC1414'>Sign up</font</b>"));
        mAuth = FirebaseAuth.getInstance();
    }
}
