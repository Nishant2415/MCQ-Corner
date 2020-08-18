package com.scorpions.mcqcorner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.model.LoginModel;

public class SetUsernameActivity extends AppCompatActivity {

    private TextInputLayout edtUsername, edtPassword;
    private Button btnNext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String flag, mEmail, mPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        init();

        flag = getIntent().getStringExtra(Global.FLAG);
        mEmail = getIntent().getStringExtra(Global.EMAIL);
        mPassword = getIntent().getStringExtra(Global.PASSWORD);

        if (flag != null && flag.equals(Global.FROM_EMAIL)) {
            edtPassword.setVisibility(View.GONE);
        }

        checkingUsername();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsername.getEditText() != null & edtPassword.getEditText() != null) {
                    final String username = edtUsername.getEditText().getText().toString().trim();
                    String password = edtPassword.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(username)) {
                        edtUsername.setError("Please enter username!");
                    } else if (flag.equals(Global.FROM_OTP_VERIFICATION)) {
                        if (TextUtils.isEmpty(password)) {
                            edtPassword.setError("Please enter password!");
                        } else if (password.length() < 6) {
                            edtPassword.setError("Please enter strong password!");
                        } else {
                            setUsername(username, password);
                        }
                    } else {
                        mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                                        setUsername(username, mPassword);
                                    } else {
                                        Global.showCustomDialog(new Global.OnDialogClickListener() {
                                            @Override
                                            public void OnOkClicked() {
                                                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                                                startActivity(intent);
                                            }
                                        }, SetUsernameActivity.this, "Please verify email address!");
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void init() {
        edtUsername = findViewById(R.id.aSetUsername_edtUsername);
        edtPassword = findViewById(R.id.aSetUsername_edtPassWord);
        btnNext = findViewById(R.id.aSetUsername_btnNext);

        mAuth = FirebaseAuth.getInstance();

        btnNext.setEnabled(false);
    }

    private void checkingUsername() {
        if (edtUsername.getEditText() != null) {

            edtUsername.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence word, int i, int i1, int i2) {
                    final String username = edtUsername.getEditText().getText().toString().trim();
                    if (!TextUtils.isEmpty(username)) {
                        db.collection(Global.PROFILE).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots != null) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (documentSnapshot.exists()) {
                                            if (username.equals(documentSnapshot.getString(Global.USERNAME))) {
                                                edtUsername.setError("Username already taken!");
                                                return;
                                            } else {
                                                edtUsername.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                                edtUsername.setEndIconDrawable(R.drawable.ic_check);
                                                edtUsername.setError(null);
                                                btnNext.setEnabled(true);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        edtUsername.setEndIconDrawable(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void setUsername(String username, String password) {
        LoginModel loginModel = new LoginModel();
        loginModel.username = username;
        loginModel.password = password;
        if (mAuth.getCurrentUser() != null) {
            db.collection(Global.PROFILE).document(mAuth.getCurrentUser().getUid())
                    .set(loginModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(SetUsernameActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
