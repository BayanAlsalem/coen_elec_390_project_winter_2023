package com.example.coen_elec_390_project_winter_2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout mySplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        mySplash = findViewById(R.id.splashId);
        mySplash.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

            //What to do when we click anywhere on the screen?
            //we create an object of the class Intent
            //The object has the following parameters:
            //1- Which class are we in?
            //2- Which class we want to jump/go to?

            Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(myIntent);

        }
    }


