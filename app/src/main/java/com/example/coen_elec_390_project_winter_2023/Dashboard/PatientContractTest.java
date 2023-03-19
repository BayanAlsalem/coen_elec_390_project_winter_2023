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

public class PatientContractTest extends AppCompatActivity {

    private static final long START_TIME_IN_SECONDS = 30000;

    private TextView CountdownContract;
    private Button Start_Flex, Redo2, end_test;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeLeft = START_TIME_IN_SECONDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instruction_flexing_data);

        Start_Flex = findViewById(R.id.btn_StartFlex);
        Redo2 = findViewById(R.id.btn_redo2);
        end_test = findViewById(R.id.btn_end);
        CountdownContract = findViewById(R.id.text_view_countdown2);


        Start_Flex.setOnClickListener(new View.OnClickListener() {
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
                        Redo2.setVisibility(View.VISIBLE);
                        end_test.setVisibility(View.VISIBLE);

                    }
                }.start();
                mTimerRunning = true;
                Start_Flex.setVisibility(View.INVISIBLE);
            }
        });

        end_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientContractTest.this, PatientDashboardActivity.class);
                startActivity(intent);
            }
        });

        Redo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeft = START_TIME_IN_SECONDS;
                updateCountDownText();
                Redo2.setVisibility(View.INVISIBLE);
                Start_Flex.setVisibility(View.VISIBLE);
                end_test.setVisibility(View.INVISIBLE);
            }
        });
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        CountdownContract.setText(timeLeftFormatted);
    }

}