/*
 *   Created by Abhinav Pandey on 06/05/23, 1:00 AM
 */
package com.example.scrappy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class imageUpload extends AppCompatActivity {

    public Bitmap bitmap;
    public EditText caption;
    public Button addPhoto, post;
    public ViewPager imagePager;
    public List<Uri> imageUris;
    private static int PICK_IMAGES_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        caption = findViewById(R.id.caption);
        addPhoto = findViewById(R.id.addPhoto);
        post = findViewById(R.id.upload);
        imagePager = findViewById(R.id.images);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }

        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(imageUpload.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                        withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST);
                                imagePager.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();


            }

        });
        caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();

                if (length < 20) {
                    caption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    caption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
            }
        });
    }

    private void createPost() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String Caption = caption.getText().toString();
        Double lat = getIntent().getDoubleExtra("lat", 25.473034);
        Double lon = getIntent().getDoubleExtra("lon", 81.878357);
        String address = getIntent().getStringExtra("address");

        List<String> imageUrls = new ArrayList<>();
        for (Uri uris : imageUris) {

            String imageName = UUID.randomUUID().toString();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

            storageRef.putFile(uris).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the image
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    String imageUrl = uri.toString();
                    imageUrls.add(imageUrl);

                    // If all images have been uploaded, create the post
                    if (imageUrls.size() == imageUris.size()) {

                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
                        String postId = postsRef.push().getKey();
                        long timestamp = System.currentTimeMillis();

                        model post = new model(postId, userId, Caption, lat, lon, address, timestamp, imageUrls);

                        postsRef.child(postId).setValue(post).addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
                imageHandler adapter = new imageHandler(this, imageUris);
                ViewPager imagePager = findViewById(R.id.images);
                imagePager.setAdapter(adapter);

            } else {
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                imageHandler adapter = new imageHandler(this, imageUris);
                ViewPager imagePager = findViewById(R.id.images);
                imagePager.setAdapter(adapter);
            }
        }
    }
}
