/*
 *
 *   Created by Nishu Sharma on 28/10/22, 5:28 PM
 *   Copyright Ⓒ 2022. All rights reserved Ⓒ 2022 http://freefuninfo.com/
 *   Last modified: 28/10/22, 5:28 PM
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity {

    private TextView dealerName,totalRating;
    private ImageView profilePic;
    private RatingBar ratingBar;
    private RecyclerView reviews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // initialising all values
        dealerName = findViewById(R.id.dealerName);
        profilePic = findViewById(R.id.profilePic);
        ratingBar = findViewById(R.id.ratingBar);
        totalRating = findViewById(R.id.ratingsQnt);
        reviews = findViewById(R.id.reviewList);




    }
}