package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    Handler handler;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        ObjectAnimator objAn = ObjectAnimator.ofPropertyValuesHolder(
                img,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );
        objAn.setDuration(1000);
        objAn.setRepeatCount(ValueAnimator.INFINITE);
        objAn.setRepeatMode(ValueAnimator.REVERSE);
        objAn.start();


        handler = new Handler();
        handler.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }, 4000);

    }


}