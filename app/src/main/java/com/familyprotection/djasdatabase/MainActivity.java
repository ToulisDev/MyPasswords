package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
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

        Boolean isRemembered = checkSavedCreds();

        handler = new Handler();
        handler.postDelayed(() -> {
                Intent dsp = new Intent(MainActivity.this, LoginActivity.class);
                if(isRemembered)
                    dsp = new Intent(MainActivity.this, listView.class);
                startActivity(dsp);
                finish();
        }, 4000);

    }

    private Boolean checkSavedCreds() {
        SharedPreferences sp = getSharedPreferences("creds",MODE_PRIVATE);
        if(sp.contains("username") && sp.contains("password")){
            ConnectionHelper.username = sp.getString("username",null);
            ConnectionHelper.password = sp.getString("password",null);
            return true;
        }
        return false;
    }


}