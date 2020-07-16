package app.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class OptionActivity extends AppCompatActivity {


    ImageButton btn1,btn2,btn3,btn4;
    TextView versiontx, engineertx, usertx;
    RadioGroup themetx, textsizetx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62bfad"));
        }

        btn1 = (ImageButton) findViewById(R.id.btnhome);
        btn2 = (ImageButton) findViewById(R.id.btndetail);
        btn3 = (ImageButton) findViewById(R.id.btncalendar);
        btn4 = (ImageButton) findViewById(R.id.btnoption);
        versiontx = findViewById(R.id.version_code);
        engineertx = findViewById(R.id.engineer_code1);
        themetx = findViewById(R.id.theme_radiogroup);
        usertx = findViewById(R.id.user_call);
        textsizetx = findViewById(R.id.textsize_radiogroup);


        findViewById(R.id.versiontx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (versiontx.getVisibility() == View.GONE){
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    versiontx.setAnimation(animation);
                    versiontx.setVisibility(View.VISIBLE);
                }else{
                    versiontx.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.engineertx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (engineertx.getVisibility() == View.GONE){
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    engineertx.setAnimation(animation);
                    engineertx.setVisibility(View.VISIBLE);
                }else{
                    engineertx.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.themetx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (themetx.getVisibility() == View.GONE){
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    themetx.setAnimation(animation);
                    themetx.setVisibility(View.VISIBLE);
                }else{
                    themetx.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.usertx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertx.getVisibility() == View.GONE){
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    usertx.setAnimation(animation);
                    usertx.setVisibility(View.VISIBLE);
                }else{
                    usertx.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.theme1_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.theme2_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.textsizetx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textsizetx.getVisibility() == View.GONE){
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(500);
                    textsizetx.setAnimation(animation);
                    textsizetx.setVisibility(View.VISIBLE);
                }else{
                    textsizetx.setVisibility(View.GONE);
                }
            }
        });

        //Bottom Navegation Menu Click Event
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
