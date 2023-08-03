package com.example.audi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Slider extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;


    public Slider(Context context){
        this.context= context;
    }


    public int[] images = {
            R.drawable.parth1,
            R.drawable.parth2,
            R.drawable.parth3

    };

    public String[] names = {
            "Mr.Parth Wadke",
            "Mr.Vijay Munde",
            "Ms.Glen Dhingra"

    };


    public String[] post = {
            "Principal,\n" +
                    "                            VES Polytechnic,\n" +
                    "                      Chembur.",
            "Principal1,\n" +
                    "                            VES Polytechnic,\n" +
                    "                      Chembur.",
            "Principal2,\n" +
                    "                            VES Polytechnic,\n" +
                    "                      Chembur."

    };

    public String[] des = {
            "bsrbqeraberhberahberbwerberberbebrreberberbserberberrrrrrrrrrrrrrrrrrrrrrrrrrrwevvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv",
            "bsrbqeraberhberahberbwerberberbebrreberberbserberberrrrrrrrrrrrrrrrrrrrrrrrrrrwevvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv",
            "bsrbqeraberhberahberbwerberberbebrreberberbserberberrrrrrrrrrrrrrrrrrrrrrrrrrrwevvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"

    };



    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider, container, false);

        ImageView sliderimage = (ImageView) view.findViewById(R.id.imageView1);
        TextView slidername = (TextView) view.findViewById(R.id.textView1);
        TextView sliderpost = (TextView) view.findViewById(R.id.textView2);
        TextView sliderdes = (TextView) view.findViewById(R.id.textView3);

        sliderimage.setImageResource(images[position]);
        slidername.setText(names[position]);
        sliderpost.setText(post[position]);
        sliderdes.setText(des[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
