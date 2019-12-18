package com.example.tripapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;


public class LoginActivity extends AppCompatActivity implements TextWatcher , CompoundButton.OnCheckedChangeListener {

    private static String APPID = "E250A2F2-A4E3-ABBF-FF4B-5AB19D36B100";
    private static String APIKEY = "1392D548-4976-417C-95F0-9B08710FFAE3";
    String email;
    String password;
//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    //sharedPref
    private  EditText loginEmail , loginPass;
    private CheckBox rem_userpass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_password);
        rem_userpass =findViewById(R.id.checkBox);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //keep user login

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        loginEmail.setText(sharedPreferences.getString(KEY_USERNAME,""));
        loginPass.setText(sharedPreferences.getString(KEY_PASS,""));

        loginEmail.addTextChangedListener(this);
        loginPass.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);

        Backendless.initApp(this, APPID, APIKEY);

//        email = loginEmail.getText().toString();
//
//        password = loginPass.getText().toString();
        //keep user logged in

//        Backendless.UserService.login(
//                email,
//                password,
//                new AsyncCallback<BackendlessUser>() {
//                    @Override
//                    public void handleResponse(BackendlessUser response) {
////                        Toast.makeText(getContext(), "User has been logged in", Toast.LENGTH_LONG).show();
//                        Intent in = new Intent(LoginActivity.this,HomeActivity.class);
//                        startActivity(in);
//                    }
//
//                    @Override
//                    public void handleFault(BackendlessFault fault) {
//                        Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
    }
    public Context getContext() {
        return this;
    }


    public void login(View view) {

        email = loginEmail.getText().toString();

        password = loginPass.getText().toString();

//        editor.putString("email",email);
//        editor.putString("password",password);
//        editor.commit();

//        if(preferences.contains(email) && preferences.contains(password)){
//            Intent in = new Intent(LoginActivity.this,HomeActivity.class);
//            startActivity(in);
//        }

        if(!(email.equals("") || password.equals(""))){

            Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();

                    String userID = response.getObjectId();

                    Intent in = new Intent(LoginActivity.this,HomeActivity.class);
                    in.putExtra("userID",userID);
                    startActivity(in);



                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    if(fault.getCode().equals("3003")){
                        Toast.makeText(LoginActivity.this, "invalid data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
            Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();

//        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
//            @Override
//            public void handleResponse(Boolean response) {
//                if(response){
//                    String userObjectID = UserIdStorageFactory.instance().getStorage().get();
//                    Backendless.Data.of(BackendlessUser.class).findById(userObjectID, new AsyncCallback<BackendlessUser>() {
//                        @Override
//                        public void handleResponse(BackendlessUser response) {
//                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
//                            LoginActivity.this.finish();
//                        }
//
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//
//                            Toast.makeText(LoginActivity.this, ""+fault.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toast.makeText(LoginActivity.this, ""+fault.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }




    public void register(View view) {

        Intent in = new Intent(this,RegisterActivity.class);
        startActivity(in);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        managePrefs();
    }

    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(KEY_USERNAME, loginEmail.getText().toString().trim());
            editor.putString(KEY_PASS, loginPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
}
