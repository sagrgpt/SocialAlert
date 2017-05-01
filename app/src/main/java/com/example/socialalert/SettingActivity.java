package com.example.socialalert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    EditText username, message, contact1, contact2, contact3, contact4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        username = (EditText) findViewById(R.id.username);
        message = (EditText) findViewById(R.id.message);
        contact1 = (EditText) findViewById(R.id.contact1);
        contact2 = (EditText) findViewById(R.id.contact2);
        contact3 = (EditText) findViewById(R.id.contact3);
        contact4 = (EditText) findViewById(R.id.contact4);
    }

    public void SavePreference(View view) {

        if(validate()){
            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("IsSet",true);
            edit.putString("Name",username.getText().toString());
            edit.putString("Contact1",contact1.getText().toString());
            edit.putString("Contact2",contact2.getText().toString());
            edit.putString("Contact3",contact3.getText().toString());
            edit.putString("Contact4",contact4.getText().toString());
            edit.putString("Message",message.getText().toString());
            edit.apply();
            startActivity(new Intent(SettingActivity.this,AlertActivity.class));
            finish();
        }

    }

    private boolean validate() {
        if(username.getText().toString().length()<3) {
            Toast.makeText(this,"Invalid username!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact1.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact1!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact2.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact2!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact3.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact3!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact4.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact4!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(message.getText().toString().length()==10){
            Toast.makeText(this,"Invalid message!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
