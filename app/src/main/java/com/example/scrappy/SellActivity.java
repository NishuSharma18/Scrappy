/*
 *   Created by Abhinav Pandey on 06/05/23, 12:46 AM
 */

package com.example.scrappy;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SellActivity extends AppCompatActivity {

    TextView Date, Time;
    EditText Location, Contact;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    Double latitude, longitude;
    Button next, locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Date = findViewById(R.id.date);
        Time = findViewById(R.id.time);
        Contact = findViewById(R.id.contact);
        Location = findViewById(R.id.location);
        next = findViewById(R.id.continueBtn);
        locationBtn = findViewById(R.id.locationBtn);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        Location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();

                if (length < 30) {
                    Location.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    Location.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                }
            }
        });

        String date = Date.getText().toString();
        String time = Time.getText().toString();
        String contact = Contact.getText().toString();
        String location = Location.getText().toString();

        Intent intent=new Intent(SellActivity.this,imageUpload.class);

        double[] quantities= getIntent().getDoubleArrayExtra("quantity");

        intent.putExtra("quantity",quantities);
        intent.putExtra("date",date);
        intent.putExtra("time",time);
        intent.putExtra("lat",latitude);
        intent.putExtra("lon",longitude);
        intent.putExtra("address",location);
        intent.putExtra("contact",contact);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Date.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        Time.setText(time);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(SellActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {

                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    longitude = addresses.get(0).getLongitude();
                                    latitude = addresses.get(0).getLatitude();
                                    Location.setText(addresses.get(0).getAddressLine(0));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {

                                Toast.makeText(SellActivity.this, "Turn on your GPS, please", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SellActivity.this, MainActivity.class));
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(SellActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
