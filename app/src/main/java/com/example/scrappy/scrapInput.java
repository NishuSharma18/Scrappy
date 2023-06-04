/*
 *   Created by Abhinav Pandey on 19/05/23, 5:19 PM
 */

package com.example.scrappy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class scrapInput extends AppCompatActivity {

    EditText metallic, plastic, paper, electronic, glass, textile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_input);

        metallic = findViewById(R.id.metallic);
        plastic = findViewById(R.id.plastic);
        paper = findViewById(R.id.paper);
        electronic = findViewById(R.id.electronic);
        glass = findViewById(R.id.glass);
        textile = findViewById(R.id.textile);

        Intent intent = new Intent(scrapInput.this, SellActivity.class);
        Double metalQ = 0.0, plasticQ = 0.0, paperQ = 0.0, electronicQ = 0.0, glassQ = 0.0, textileQ = 0.0;

        if (!metallic.getText().toString().isEmpty())
            metalQ = Double.parseDouble(metallic.getText().toString());
        if (!plastic.getText().toString().isEmpty())
            plasticQ = Double.parseDouble(plastic.getText().toString());
        if (!paper.getText().toString().isEmpty())
            paperQ = Double.parseDouble(paper.getText().toString());
        if (!electronic.getText().toString().isEmpty())
            electronicQ = Double.parseDouble(electronic.getText().toString());
        if (!glass.getText().toString().isEmpty())
            glassQ = Double.parseDouble(glass.getText().toString());
        if (!textile.getText().toString().isEmpty())
            textileQ = Double.parseDouble(textile.getText().toString());


        Double quantities[] = {metalQ, plasticQ, paperQ, electronicQ, glassQ, textileQ};


        intent.putExtra("quantity", quantities);

        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


    }
}