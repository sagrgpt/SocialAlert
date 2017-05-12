package com.example.socialalert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class SettingActivity extends AppCompatActivity {

    EditText username, message, contact1, contact2, contact3, contact4;
    private static final int CONTACT1_REQUEST_CODE = 201;
    private static final int CONTACT2_REQUEST_CODE = 202;
    private static final int CONTACT3_REQUEST_CODE = 203;
    private static final int CONTACT4_REQUEST_CODE = 204;

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
        SharedPreferences sharedPreferences = getSharedPreferences("settings",Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("IsSet",false))     fillContent(sharedPreferences);
    }

    private void fillContent(SharedPreferences sharedPreferences) {
        username.setText(sharedPreferences.getString("Name",null));
        message.setText(sharedPreferences.getString("Message",null));
        contact1.setText(sharedPreferences.getString("Contact1",null));
        contact2.setText(sharedPreferences.getString("Contact2",null));
        contact3.setText(sharedPreferences.getString("Contact3",null));
        contact4.setText(sharedPreferences.getString("Contact4",null));
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
        if(contact1.getText().toString().length()!=13 &&
                contact1.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact1!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact2.getText().toString().length()!=13 &&
                contact2.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact2!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact3.getText().toString().length()!=13 &&
                contact3.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact3!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(contact4.getText().toString().length()!=13 &&
                contact4.getText().toString().length()!=10){
            Toast.makeText(this,"Invalid Contact4!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(message.getText().toString().length() >= 42){
            Toast.makeText(this,"Invalid message!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onPickContact1(View view) {
        Intent contactPicker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPicker,CONTACT1_REQUEST_CODE);
    }

    public void onPickContact2(View view) {
        Intent contactPicker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPicker,CONTACT2_REQUEST_CODE);
    }

    public void onPickContact3(View view) {
        Intent contactPicker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPicker,CONTACT3_REQUEST_CODE);
    }

    public void onPickContact4(View view) {
        Intent contactPicker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPicker,CONTACT4_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CONTACT1_REQUEST_CODE:{
                    contactPicked(data,1);
                    break;
                }
                case CONTACT2_REQUEST_CODE: {
                    contactPicked(data,2);
                    break;
                }
                case CONTACT3_REQUEST_CODE: {
                    contactPicked(data,3);
                    break;
                }
                case CONTACT4_REQUEST_CODE: {
                    contactPicked(data,4);
                    break;
                }
                default:
                    Toasty.error(this,"Contact not available").show();
            }
        }else Toasty.info(this,"Contacts not selected!").show();
    }

    private void contactPicked(Intent data,int contactIndex) {
        Cursor cursor =null;
        try{
            String phoneNo;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(index);
            switch (contactIndex){
                case 1: {
                    contact1.setText(phoneNo);
                    break;
                }
                case 2: {
                    contact2.setText(phoneNo);
                    break;
                }
                case 3: {
                    contact3.setText(phoneNo);
                    break;
                }
                case 4: {
                    contact4.setText(phoneNo);
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
