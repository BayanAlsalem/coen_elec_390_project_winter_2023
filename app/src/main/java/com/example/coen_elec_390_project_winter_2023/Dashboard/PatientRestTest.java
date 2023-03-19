package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

import java.util.Locale;

public class PatientRestTest extends AppCompatActivity {

    private static final long START_TIME_IN_SECONDS = 30000;
    private TextView Countdown;
    private Button Start_test, Redo, next_test;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeLeft = START_TIME_IN_SECONDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instruction_resting_data);

        Start_test = findViewById(R.id.btn_StartRest);
        Redo = findViewById(R.id.btn_redo);
        next_test = findViewById(R.id.btn_Contracting);
        Countdown = findViewById(R.id.text_view_countdown);


        Start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        updateCountDownText();
                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;
                        Redo.setVisibility(View.VISIBLE);
                        next_test.setVisibility(View.VISIBLE);

                    }
                }.start();
                mTimerRunning = true;
                Start_test.setVisibility(View.INVISIBLE);
            }
        });

        next_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientRestTest.this, PatientContractTest.class);
                startActivity(intent);
            }
        });

        Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeft = START_TIME_IN_SECONDS;
                updateCountDownText();
                Redo.setVisibility(View.INVISIBLE);
                Start_test.setVisibility(View.VISIBLE);
                next_test.setVisibility(View.INVISIBLE);
            }
        });
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        Countdown.setText(timeLeftFormatted);
    }

}
