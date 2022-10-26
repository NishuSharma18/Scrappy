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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Build;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
            dataobj.invoiceNo = invoiceNo+1;
            dataobj.customerName = String.valueOf(name.getText());
            dataobj.date = new Date().getTime();
            dataobj.scrapType = spinner.getSelectedItem().toString();
            dataobj.Quantity = Double.parseDouble(String.valueOf(quantity.getText()));
            dataobj.amt = Double.valueOf(decimalFormat.format(dataobj.getQuantity()*itemPrice[spinner.getSelectedItemPosition()]));

            myRef.child(String.valueOf(invoiceNo+1)).setValue(dataobj);

            printPdf();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        linePaint.setColor(Color.rgb(0,50,250));
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,350,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,50,250));

        canvas.drawText("Welcome To the Scrap Store",20,20,paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Room no -204, SVBH Hostel, MNNIT Allahabad,",20,40,paint);
        canvas.drawText("Teliyarganj, 210679, Uttar Pradesh",20,55,paint);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));

        canvas.drawLine(20,65,230,65,linePaint);

        canvas.drawText("Customer Name: "+ name.getText(),20,80,paint);
        canvas.drawLine(20,90,230,90,linePaint);

        canvas.drawText("Purchase:" ,20,105,paint);
        canvas.drawText(spinner.getSelectedItem().toString(),20,135,paint);
        canvas.drawText(quantity.getText()+" kgs",120,135,paint);
        // amount calculation
        double amount = itemPrice[spinner.getSelectedItemPosition()]*Double.parseDouble(quantity.getText().toString());
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(decimalFormat.format(amount)),230,135,paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("+%" ,20,175,paint);
        canvas.drawText("Tax 8%",120,175,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format(amount*8/100),230,175,paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(20,210,230,210,linePaint);
        paint.setTextSize(10f);
        canvas.drawText("Total",120,225,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format(amount*8/100 + amount),230,225,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.5f);

        // fot date and time
        canvas.drawText("Date: " +datePatternFormat.format(new Date().getTime()),20,260,paint );
        canvas.drawText("Invoice No:",20,275,paint);
        canvas.drawText(String.valueOf(invoiceNo+1),63,275,paint);
        canvas.drawText("Payment Successful",20,290,paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Thank You!",canvas.getWidth()/2,320,paint);


        // creating file
        myPdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"),"DroidRushInvoice.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();

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