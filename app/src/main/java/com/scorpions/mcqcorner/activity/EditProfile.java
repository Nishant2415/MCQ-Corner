package com.scorpions.mcqcorner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hbb20.CountryCodePicker;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.model.ProfileModel;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText eduser,edwebsite,edphone,edemail;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userid;
    Button btnSave;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();
        getProfile();
        addUpdateProfile();
        //saveEditProfile();

    }



    private void init()
    {
        eduser = findViewById(R.id.aSetting_edtUsername);
        edwebsite = findViewById(R.id.aSetting_edtWebsite);
        edphone = findViewById(R.id.aSetting_edtMobileNo);
        edemail = findViewById(R.id.aSetting_edtEmail);
        countryCodePicker = findViewById(R.id.ccps);

        btnSave = findViewById(R.id.lUpdateProfile_img);

        findViewById(R.id.lUpdateProfile_imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private  void getProfile()
    {
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userid = fauth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("Profile").document(userid);
        documentReference.addSnapshotListener(EditProfile.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                eduser.setText(value.getString("username"));
                edwebsite.setText(value.getString("webSite"));
                edphone.setText(value.getString("mobileNo"));
                edemail.setText(value.getString("email"));

            }
        });
    }

    private void addUpdateProfile()
    {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProfileModel profileModel = new ProfileModel();

                if(eduser.getText().toString().trim().isEmpty() |
                        edphone.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(EditProfile.this, "Please enter username or phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    profileModel.setUsername(eduser.getText().toString().trim());
                    profileModel.setWebsite(edwebsite.getText().toString().trim());
                    profileModel.setMobileno(edphone.getText().toString().trim());
                    profileModel.setEmail(edemail.getText().toString().trim());

                    Toast.makeText(EditProfile.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                    userid = fauth.getCurrentUser().getUid();

                    final DocumentReference documentReference = fstore.collection("Profile").document(userid);
                    documentReference.addSnapshotListener(EditProfile.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                            Map<String, Object> profile = new HashMap<>();

                            profile.put("username", profileModel.getUsername());
                            profile.put("webSite", profileModel.getWebsite());
                            profile.put("mobileNo", profileModel.getMobileno());
                            profile.put("email", profileModel.getEmail());

                            fauth = FirebaseAuth.getInstance();
                            fstore = FirebaseFirestore.getInstance();

                            /*fstore.collection("Profile")
                                    .add(profile)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("tagy", "inserted successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("tagy", "error !! failed");
                                        }
                                    });*/


                        documentReference.update(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Log.d("tagy", "inserted successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Log.d("tagy", "error !! failed");
                                    }
                                });
                        }
                    });
                }
            }
        });
    }
}