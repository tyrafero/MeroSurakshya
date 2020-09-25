package com.example.ms3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent welcomeIntent= new Intent(MainActivity.this, otp.class);
                    startActivity(welcomeIntent);
                }

            }

        };
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    public void send_sms (View view ) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            //MyMessage();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, 0);
        }


    }


}