/*
 *
 *   Created by Nishu Sharma on 03/11/22, 8:26 PM
 *   Copyright Ⓒ 2022. All rights reserved Ⓒ 2022 http://freefuninfo.com/
 *   Last modified: 03/11/22, 8:26 PM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.example.scrappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RatingInputActivity extends AppCompatActivity {

    private RatingBar ratingbar;
    private Button submit;
    private EditText review;
    private String DealerID;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_input);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        ratingbar = (RatingBar) findViewById(R.id.ratingBar1);
        submit = (Button) findViewById(R.id.button3);
        review = findViewById(R.id.review);

        firebaseAuth = FirebaseAuth.getInstance();
        // if user has written the review then load it;
        loadMyreview();

        //Input data
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
                inputData();


            }

        });
    }

    private void loadMyreview() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(DealerID).child("ratings").child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // my review is available
                            // so get review details
                            String uid = ""+snapshot.child("uid").getValue();
                            String ratings = ""+snapshot.child("ratings").getValue();
                            String reviewText = ""+snapshot.child("review").getValue();
                            String timestamp = ""+snapshot.child("timestamp").getValue();

                            // set review details to our ui
                            float myRating = Float.parseFloat(ratings);
                            ratingbar.setRating(myRating);
                            review.setText(reviewText);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void inputData() {
        String rating = ""+ratingbar.getRating();
        String reviewText = review.getText().toString().trim();
        String timeStamp = ""+ System.currentTimeMillis();
        // setup data in Hashmap
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",firebaseAuth.getUid());
        hashMap.put("ratings",rating);
        hashMap.put("review",reviewText);
        hashMap.put("timestamp",timeStamp);

        // inserting this to database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(DealerID).child("ratings").child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RatingInputActivity.this,"Review performed Successfully....",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RatingInputActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
