/*
 *
 *   Created by Nishu Sharma on 02/11/22, 8:04 AM
 *   Copyright Ⓒ 2022. All rights reserved Ⓒ 2022 http://freefuninfo.com/
 *   Last modified: 02/11/22, 8:04 AM
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    EditText inputEmail,password,confirmPassW,userName;
    Button register;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView btn = findViewById(R.id.LoginTxtBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
        inputEmail = findViewById(R.id.UserName1);
        password = findViewById(R.id.Password1);
        userName = findViewById(R.id.UserN);
        confirmPassW = findViewById(R.id.Password2);
        register = findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }
    private void PerformAuth() {
        String email = inputEmail.getText().toString();
        String UserName = userName.getText().toString();
        String password1 = password.getText().toString();
        String password2 = confirmPassW.getText().toString();

        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter Valid Email");
            inputEmail.requestFocus();
        }
        else if(password1.isEmpty() || password1.length() <6){
            password.setError("Enter Proper Password");
        }
        else if(!password1.equals(password2)){
            confirmPassW.setError("Password does not match at both fields");
        }
        else{
            progressDialog.setMessage("Please wait while registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegistrationActivity.this, "Registration Successful ☺☺", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegistrationActivity.this,Invoice.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



    }
}