package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnRegister;
    private TextView txtSignIn;
    private FirebaseAuth mAuth;
    private EditText edtConfirmPassword, edtMobileNo;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String mobileNo = edtMobileNo.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                int valid = 0;

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Invalid");
                    edtEmail.requestFocus();
                    valid = 1;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Invalid");
                    edtPassword.requestFocus();
                    valid = 1;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    edtConfirmPassword.setError("Invalid");
                    edtConfirmPassword.requestFocus();
                    valid = 1;
                }

                if (!confirmPassword.equals(password)) {
                    edtConfirmPassword.setError("Password didn't match!");
                    edtConfirmPassword.requestFocus();
                    valid = 1;
                }

                if (!Patterns.PHONE.matcher(mobileNo).matches()) {
                    edtMobileNo.setError("Contact is not valid");
                    edtMobileNo.requestFocus();
                    valid = 1;
                }

                if (mobileNo.length() != 10) {
                    edtMobileNo.setError("Enter valid contact number");
                    edtMobileNo.requestFocus();
                    valid = 1;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("Email address is not valid");
                    edtEmail.requestFocus();
                    valid = 1;
                }

                if (valid == 0) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        sp = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);

        edtEmail = findViewById(R.id.aRegister_edtEmail);
        edtPassword = findViewById(R.id.aRegister_edtPassword);
        btnRegister = findViewById(R.id.aRegister_btnRegister);
        txtSignIn = findViewById(R.id.aRegister_txtSignIn);
        edtConfirmPassword = findViewById(R.id.aRegister_edtConfirmPassword);
        edtMobileNo = findViewById(R.id.aRegister_edtMobileNo);

        mAuth = FirebaseAuth.getInstance();
    }
}
