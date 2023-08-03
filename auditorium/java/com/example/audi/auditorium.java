package com.example.audi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import androidx.appcompat.app.AppCompatActivity;

public class auditorium extends AppCompatActivity {

    SliderLayout sliderLayout;
    Button button,logout,feature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditorium);
        feature=findViewById(R.id.features);
        feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBill();
            }
        });
        button=findViewById(R.id.check);
        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheck();
            }
        });
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(3);
        setSliderViews();

    }



    /* image sliders for loop */
    private void setSliderViews() {
        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.background);
                    sliderView.setDescription(" 1" );
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.background2);
                    sliderView.setDescription(" 2" );
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.background3);
                    sliderView.setDescription(" 3" );
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.background);
                    sliderView.setDescription(" 4" );
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            /* sliderView.setDescription("setDescription " + (i + 1)); */
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(auditorium.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });


            sliderLayout.addSliderView(sliderView);

        }
    }

    public void openCheck(){
        Intent intent =  new Intent(this,avail.class);
        startActivity(intent);
    }


    public void openBill(){
        Intent intent =  new Intent(this,bill.class);
        startActivity(intent);
    }
}
