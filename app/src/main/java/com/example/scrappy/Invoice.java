/*
 *
 *   Created by Nishu Sharma on 26/10/22, 12:46 AM
 *   Copyright Ⓒ 2022. All rights reserved Ⓒ 2022 http://freefuninfo.com/
 *   Last modified: 26/10/22, 12:32 AM
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Date;

public class Invoice extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("record");
    DataObj dataobj = new DataObj();
    Button saveAndPrint , Print;
    EditText name, quantity;
    Spinner spinner;
    String[]  itemList;
    double[] itemPrice;
    ArrayAdapter<String> adapter;
    long invoiceNo = 0;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        callFindViewById();
        callOnClickListener();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = 3678+ snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void callOnClickListener() {
        saveAndPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dataobj.invoiceNo = invoiceNo+1;
            dataobj.customerName = String.valueOf(name.getText());
            dataobj.date = new Date().getTime();
            dataobj.scrapType = spinner.getSelectedItem().toString();
            dataobj.Quantity = Double.parseDouble(String.valueOf(quantity.getText()));
            dataobj.amt = Double.valueOf(decimalFormat.format(dataobj.getQuantity()*itemPrice[spinner.getSelectedItemPosition()]));

            myRef.child(String.valueOf(invoiceNo+1)).setValue(dataobj);
            }
        });
    }

    private void callFindViewById() {
        saveAndPrint = findViewById(R.id.btnSaveAndPrint);
        Print = findViewById(R.id.btnPrint);
        name = findViewById(R.id.editTextName);
        quantity = findViewById(R.id.editTextQty);
        spinner = findViewById(R.id.spinner);
        itemList = new String[]{"Cardboard","Plastic","Metal"};
        itemPrice = new double[]{38.75,47.5,130.25};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,itemList);
        spinner.setAdapter(adapter);
    }
}