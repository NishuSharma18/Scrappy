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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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
import java.util.Objects;

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
     Bitmap bmp,scaledBitmap,bmp2,scaledBitmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Objects.requireNonNull(getSupportActionBar()).hide();

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.img1);
        scaledBitmap = Bitmap.createScaledBitmap(bmp,70,70,false);
        bmp2 = BitmapFactory.decodeResource(getResources(),R.drawable.header2);
        scaledBitmap2 = Bitmap.createScaledBitmap(bmp2,250,80,false);
        callFindViewById();
        callOnClickListener1();
        callOnClickListener2();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = 367845+ snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void callOnClickListener2() {
        Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Invoice.this,FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }


    private void callOnClickListener1() {
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
                Toast.makeText(Invoice.this, "Invoice Generated and downloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        linePaint.setColor(Color.rgb(0,0,0));
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledBitmap2,0,0,paint);
        canvas.drawBitmap(scaledBitmap,160,280,paint);

        paint.setTextSize(18f);
        paint.setColor(Color.rgb(0,0,0));

        canvas.drawText("Welcome To the Scrap Store",15,75,paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Room no -204, SVBH Hostel, MNNIT Allahabad,",5,90,paint);
        canvas.drawText("Teliyarganj, 210679, Uttar Pradesh",5,105,paint);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));

        canvas.drawLine(10,120,240,120,linePaint);

        paint.setColor(Color.rgb(128,0,128));
        canvas.drawText("Customer Name :  "+ name.getText(),20,135,paint);
        paint.setColor(Color.rgb(0,50,250));
        canvas.drawLine(10,145,240,145,linePaint);
        paint.setTextSize(10.5f);
        canvas.drawText("Purchase :" ,20,160,paint);
        paint.setTextSize(8.5f);
        canvas.drawText(spinner.getSelectedItem().toString(),20,190,paint);
        canvas.drawText(quantity.getText()+" kgs",120,190,paint);
        // amount calculation
        double amount = itemPrice[spinner.getSelectedItemPosition()]*Double.parseDouble(quantity.getText().toString());
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(decimalFormat.format(amount)),230,190,paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("+%" ,20,220,paint);
        canvas.drawText("Tax 8%",120,220,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format(amount*8/100),230,220,paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(10,235,240,235,linePaint);
        paint.setTextSize(12f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("Total :",120,265,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format(amount*8/100 + amount),230,265,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.5f);

        // fot date and time
        canvas.drawText("Date: " +datePatternFormat.format(new Date().getTime()),10,305,paint );
        canvas.drawText("Invoice No: ",10,320,paint);
        canvas.drawText(String.valueOf(invoiceNo+1),55,320,paint);

        paint.setColor(Color.rgb(128,0,128));
        canvas.drawText("Payment Successful",10,335,paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(14f);
        paint.setColor(Color.rgb(0,100,0));
        canvas.drawText("Thank You!",canvas.getWidth()/2,370,paint);


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