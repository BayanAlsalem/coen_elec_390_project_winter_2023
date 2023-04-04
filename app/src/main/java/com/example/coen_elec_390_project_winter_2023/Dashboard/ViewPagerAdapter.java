package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.coen_elec_390_project_winter_2023.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int image[]={

            R.drawable.image0,
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5
    };

    int headings[] = {

            R.string.step0Heading,
            R.string.step1Heading,
            R.string.step2Heading,
            R.string.step3Heading,
            R.string.step4Heading,
            R.string.step5Heading,

    };

    int description[] = {

            R.string.step0_description,
            R.string.step1_description,
            R.string.step2_description,
            R.string.step3_description,
            R.string.step4_description,
            R.string.step5_description,
    };

    public ViewPagerAdapter(Context context)

    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container, false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.imageView0);
        TextView slideHeading = (TextView) view.findViewById(R.id.step0_Heading);

        TextView slidedescription = (TextView) view.findViewById(R.id.step0_discription);

        slidetitleimage.setImageResource(image[position]);

        slideHeading.setText(headings[position]);

        //description array movement
        slidedescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }
}
