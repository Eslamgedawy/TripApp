package com.example.tripapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegisterActivity extends AppCompatActivity implements AsyncCallback<BackendlessUser> {

    EditText username , email , password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }


    public void register(View view) {

        BackendlessUser user = new BackendlessUser();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setProperty("name",username.getText().toString());


        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty())
            Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();

        else
            Backendless.UserService.register(user,this);


    }

    @Override
    public void handleResponse(BackendlessUser response) {
        Intent in = new Intent(this,LoginActivity.class);
        startActivity(in);
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleFault(BackendlessFault fault) {
        if(fault.getCode().equals("3033"))
            Toast.makeText(this, "user exist", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, fault.getCode() + fault.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
