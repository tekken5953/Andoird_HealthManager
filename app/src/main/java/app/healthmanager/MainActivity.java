package app.healthmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageButton btn1, btn2, btn3, btn4;
    TextView appname;
    Button webView_foodbtn, mask, medicien, search, go_detail;
    LinearLayout titlelinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62bfad"));
        }

        btn1 = (ImageButton) findViewById(R.id.btnhome);
        btn2 = (ImageButton) findViewById(R.id.btndetail);
        btn3 = (ImageButton) findViewById(R.id.btncalendar);
        btn4 = (ImageButton) findViewById(R.id.btnoption);
        mask = (Button) findViewById(R.id.webView_mask);
        medicien = (Button) findViewById(R.id.webView_medicien);
        search = (Button) findViewById(R.id.webView_search);
        appname = (TextView) findViewById(R.id.appname);
        titlelinear = (LinearLayout) findViewById(R.id.titlelinear);
        webView_foodbtn = (Button) findViewById(R.id.webView_food_btn);
        go_detail = (Button) findViewById(R.id.go_detail_btn);

        go_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //마스크&약국 검색
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog("https://m.map.kakao.com/actions/searchView?q=공적마스크판매처&viewmap=true");
            }
        });
        medicien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog("https://m.map.kakao.com/actions/searchView?q=약국&viewmap=true");
            }
        });

        webView_foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog("http://api.nongsaro.go.kr/sample/ajax/recomendDiet/recomendDietList.html");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog("http://dikmobile.health.kr/main.do");
            }
        });

        //Bottom Navegation Menu Click Event
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void alertDialog(String s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View v = LayoutInflater.from(this).inflate(R.layout.webview,null,false);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        Window window = alertDialog.getWindow();
        if(window != null) {
            // 백그라운드 투명
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = window.getAttributes();
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
            // UI 정렬
            window.setGravity(Gravity.CENTER);
        }

        final WebView webView = v.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치기반 서비스 동의
        webSettings.setAppCacheEnabled(false); // 캐시파일 경로 저장
        webSettings.setJavaScriptEnabled(true); // 자바스크립트 허용
        webSettings.setLoadWithOverviewMode(true); // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        webView.setWebViewClient(new WebViewClient()); // 새창 뜨지 않게 하기위해서

        //페이지 로드 완료 후
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        //webview 에서 GPS location 권한을 사용하도록 추가
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });

        webView.loadUrl(s);

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Health Manager")
                .setMessage("정말 종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(MainActivity.this);
                    }

                })
                .setNegativeButton("아니오", null)
                .show();
    }
}
