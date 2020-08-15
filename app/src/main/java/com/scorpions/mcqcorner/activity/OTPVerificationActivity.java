package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private EditText edtVerificationCode;
    private Button btnVerify;
    private TextView txtTimer;
    private FirebaseAuth mAuth;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        init();

        final String mobileNo = "+91" + getIntent().getStringExtra("mobileNo");

        sendVerificationCode(mobileNo);

        txtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(mobileNo, mResendToken);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtVerificationCode.getText().toString().isEmpty()) {
                    verifyVerificationCode(edtVerificationCode.getText().toString());
                    /*Intent intent = new Intent(OTPVerificationActivity.this, SetUsernameActivity.class);
                    intent.putExtra(Global.FLAG, Global.FROM_OTP_VERIFICATION);
                    startActivity(intent);
                    finish();*/
                }
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();

                    if (code != null) {
                        edtVerificationCode.setText(code);
                        verifyVerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(OTPVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken resendingToken) {
                    super.onCodeSent(s, resendingToken);
                    mVerificationId = s;
                    mResendToken = resendingToken;

                    txtTimer.setEnabled(false);

                    time = 60;

                    new CountDownTimer(62000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            txtTimer.setText(("Resend code in  0:" + checkDigit(time)));
                            time--;
                        }

                        public void onFinish() {
                            txtTimer.setText(("Resend OTP"));
                            txtTimer.setEnabled(true);
                        }

                    }.start();

                }
            };

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void verifyVerificationCode(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(OTPVerificationActivity.this, SetUsernameActivity.class);
                            intent.putExtra(Global.FLAG, Global.FROM_OTP_VERIFICATION);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OTPVerificationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void init() {
        edtVerificationCode = findViewById(R.id.aOTPVerification_edtOTP);
        txtTimer = findViewById(R.id.aOTPVerification_txtTimer);
        btnVerify = findViewById(R.id.aOtpVerification_btnOTPVerify);

        Toolbar toolbar = findViewById(R.id.aOTPVerification_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.otp_verification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        mAuth = FirebaseAuth.getInstance();
    }
}