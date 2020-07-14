package app.healthmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class OptionActivity extends AppCompatActivity {


    ImageButton btn1,btn2,btn3,btn4;

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

        findViewById(R.id.versiontx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.version_code).getVisibility() == View.GONE){
                    findViewById(R.id.version_code).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.version_code).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.engineertx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.engineer_code1).getVisibility() == View.GONE){
                    findViewById(R.id.engineer_code1).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.engineer_code1).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.themetx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.theme_radiogroup).getVisibility() == View.GONE){
                    findViewById(R.id.theme_radiogroup).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.theme_radiogroup).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.usertx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.user_call).getVisibility() == View.GONE){
                    findViewById(R.id.user_call).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.user_call).setVisibility(View.GONE);
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
                if (findViewById(R.id.textsize_radiogroup).getVisibility() == View.GONE){
                    findViewById(R.id.textsize_radiogroup).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.textsize_radiogroup).setVisibility(View.GONE);
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
