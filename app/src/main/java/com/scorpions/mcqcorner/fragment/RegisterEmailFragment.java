package com.scorpions.mcqcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.activity.SetUsernameActivity;
import com.scorpions.mcqcorner.config.Global;

public class RegisterEmailFragment extends Fragment {

    private EditText edtEmail, edtPassword;
    private Button btnNext;
    private FirebaseAuth mAuth;

    public RegisterEmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtEmail = view.findViewById(R.id.fRegisterEmail_edtEmailAddress);
        edtPassword = view.findViewById(R.id.fRegisterEmail_edtPassWord);
        btnNext = view.findViewById(R.id.fRegisterEmail_btnNext);
        mAuth = FirebaseAuth.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();

                if(!email.contains("@")) {
                    edtEmail.setError("Invalid email");
                } else if(TextUtils.isEmpty(email)) {
                    edtEmail.setError("Can not be blank");
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Can not be blank");
                } else if (password.length() < 6) {
                    edtPassword.setError("Use strong password");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful() & mAuth.getCurrentUser()!=null) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "Temporary toast", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), SetUsernameActivity.class);
                                        intent.putExtra(Global.FLAG, Global.FROM_EMAIL);
                                        intent.putExtra(Global.EMAIL, email);
                                        intent.putExtra(Global.PASSWORD, password);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}