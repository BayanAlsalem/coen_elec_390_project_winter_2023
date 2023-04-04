package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.coen_elec_390_project_winter_2023.R;
import com.google.api.Distribution;

import android.view.View;
import android.widget.TextView;

public class PatientInstructionsActivity extends AppCompatActivity{
     Button nextButton;
     Button backButton;
    String userID;
    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_instruction_layout);

//        if (getIntent().getExtras() != null) {
//            userID=getIntent().getStringExtra("userID");
//        }else{
//            System.out.println("Intent Failed");
//            return;
//        }

           nextButton = findViewById(R.id.nextbtn);
           backButton = findViewById(R.id.backbtn);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(PatientInstructionsActivity.this, PatientInstructionsStep1.class);
//                intent.putExtra("userID",userID);
//                startActivity(intent);

                if(getitem(0)<5) {
                    mSlideViewPager.setCurrentItem(getitem(1), true);
                }
                else {

                    int takeReadings = getIntent().getIntExtra("TakeReadings", 0);
                    int instruction = getIntent().getIntExtra("Instruction", 0);

                    if(takeReadings == 1){
                        Intent i = new Intent(PatientInstructionsActivity.this,BluetoothConnectionActivity.class);
                        startActivity(i);
                        finish();
                    }else if(instruction == 1) {
                        Intent i = new Intent(PatientInstructionsActivity.this, PatientDashboard.class);
                        startActivity(i);
                        finish();
                    }
                }

            }

           });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PatientInstructionsActivity.this, PatientDashboard.class);
//                intent.putExtra("userID",userID);
//                startActivity(intent);

                if(getitem(0)>0){

                    mSlideViewPager.setCurrentItem(getitem(-1),true);
                }
            }
        });

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);
        mSlideViewPager.setAdapter(viewPagerAdapter);

    }

//    public void setUpindicator(int position){
//
//        dots = new TextView[6];
//        mDotLayout.removeAllViews();
//
//        for(int i=0; i<dots.length; i++)
//        {
//            dots[i]=new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
//            mDotLayout.addView(dots[i]);
//        }
//
//        dots[position].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
//
//    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //setUpindicator(position);

            //if user is on the first page, user cannot go back
            if(position > 0){

                backButton.setVisibility(View.VISIBLE);
            }else {

                backButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //give us the position of the viewpager
    private int getitem(int i){

        return mSlideViewPager.getCurrentItem()+i;
    }

}
