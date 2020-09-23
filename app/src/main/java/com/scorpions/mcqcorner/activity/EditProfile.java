package com.scorpions.mcqcorner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.config.Global;
import com.scorpions.mcqcorner.config.Preference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";
    private EditText edtUsername, edtWebsite, edtMobileNo, edtEmail;
    private FirebaseFirestore db;
    private final int PICK_IMAGE_REQUEST = 24;
    private ImageView imgProfilePic;
    private Button btnUpdate;
    private Bitmap bitmap;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();

        // Close button click
        findViewById(R.id.lUpdateProfile_imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setupProfile();

        // Update profile in database
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String website = edtWebsite.getText().toString().trim();
                String mobileNo = edtMobileNo.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                if (!username.isEmpty()) {
                    Map<String, Object> profileMap = new HashMap<>();
                    profileMap.put(Global.USERNAME, username);
                    profileMap.put(Global.WEBSITE, website);
                    profileMap.put(Global.MOBILE_NO, mobileNo);
                    profileMap.put(Global.EMAIL, email);

                    db.collection(Global.PROFILE).document(currentUserId).update(profileMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (bitmap != null) {
                                            uploadImageToStorage();
                                        }
                                        Toast.makeText(EditProfile.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(EditProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "onComplete: " + task.getException());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(EditProfile.this, "Please enter username!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void init() {
        edtUsername = findViewById(R.id.aEditProfile_edtUsername);
        edtWebsite = findViewById(R.id.aEditProfile_edtWebsite);
        edtMobileNo = findViewById(R.id.aEditProfile_edtMobileNo);
        edtEmail = findViewById(R.id.aEditProfile_edtEmail);
        imgProfilePic = findViewById(R.id.aEditProfile_imgProfilePic);
        btnUpdate = findViewById(R.id.lUpdateProfile_btnUpdate);

        db = FirebaseFirestore.getInstance();
        currentUserId = Preference.getString(EditProfile.this, Global.USER_ID);
    }

    // Setup all fields from database
    private void setupProfile() {
        db.collection(Global.PROFILE).document(currentUserId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.e(TAG, "onSuccess: " + "setupProfile");
                            edtUsername.setText(documentSnapshot.getString(Global.USERNAME));
                            edtWebsite.setText(documentSnapshot.getString(Global.WEBSITE));
                            edtMobileNo.setText(documentSnapshot.getString(Global.MOBILE_NO));
                            edtEmail.setText(documentSnapshot.getString(Global.EMAIL));
                            Glide.with(getApplicationContext()).load(documentSnapshot.getString(Global.PROFILE_PIC)).placeholder(R.drawable.user).into(imgProfilePic);
                        }
                    }
                });
    }

    // Uploading image to storage
    private void uploadImageToStorage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("ProfilePictures")
                .child(currentUserId + ".jpg");

        storageRef.putBytes(baos.toByteArray()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Getting download link from storage
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Add image link in database
                            Map<String, Object> map = new HashMap<>();
                            map.put(Global.PROFILE_PIC, uri.toString());
                            db.collection(Global.PROFILE).document(currentUserId).update(map);
                        }
                    });
                } else {
                    Toast.makeText(EditProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onComplete: " + task.getException());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}