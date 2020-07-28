package app.healthmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    ImageButton btn1, btn2, btn3, btn4;
    WebView webView;
    TextView foldtx, appname;
    Button webView_foodbtn, mask, medicien;
    LinearLayout titlelinear;
    ObjectAnimator animation;
    Boolean isExitFlag = false;

    @SuppressLint("SetJavaScriptEnabled")
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
        mask = (Button) findViewById(R.id.mask);
        medicien = (Button) findViewById(R.id.medicien);
        webView = (WebView) findViewById(R.id.webView);
        appname = (TextView) findViewById(R.id.appname);
        titlelinear = (LinearLayout) findViewById(R.id.titlelinear);
        webView_foodbtn = (Button) findViewById(R.id.webView_food_btn);
        foldtx = (TextView) findViewById(R.id.foldingtx);
        WebSettings webSettings = webView.getSettings();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치기반 서비스 동의
        webSettings.setAppCacheEnabled(false); // 캐시파일 경로 저장
        webSettings.setJavaScriptEnabled(true); // 자바스크립트 허용
        webSettings.setLoadWithOverviewMode(true); // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        webView.setWebViewClient(new WebViewClient()); // 새창 뜨지 않게 하기위해서
        animation = ObjectAnimator.ofFloat(titlelinear, "translationY", 0, -650);

        //페이지 로드 완료 후
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        foldtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(titlelinear, "translationY", -650, 0);
                animation.setDuration(700);
                animation.start();
                webView.setVisibility(GONE);
                foldtx.setVisibility(GONE);
            }
        });
        //마스크&약국 검색
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.getVisibility() == View.VISIBLE) {
                    animation.setDuration(0);
                    webView.loadUrl("https://m.map.kakao.com/actions/searchView?q=공적마스크판매처&viewmap=true");
                    webview_change();
                } else {
                    animation.setDuration(700);
                    webview_change();
                    webView.loadUrl("https://m.map.kakao.com/actions/searchView?q=공적마스크판매처&viewmap=true");
                }
            }
        });
        medicien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.getVisibility() == View.VISIBLE) {
                    animation.setDuration(0);
                    webView.loadUrl("https://m.map.kakao.com/actions/searchView?q=약국&viewmap=true");
                    webview_change();
                } else {
                    animation.setDuration(700);
                    webview_change();
                    webView.loadUrl("https://m.map.kakao.com/actions/searchView?q=약국&viewmap=true");
                }
            }
        });

        webView_foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.getVisibility() == View.VISIBLE) {
                    animation.setDuration(0);
                    webView.loadUrl("http://api.nongsaro.go.kr/sample/ajax/recomendDiet/recomendDietList.html");
                    webview_change();
                } else {
                    animation.setDuration(700);
                    webView.loadUrl("http://api.nongsaro.go.kr/sample/ajax/recomendDiet/recomendDietList.html");
                    webview_change();
                }
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

    @Override
    public void onBackPressed() {
        if(isExitFlag){
            finish();
        } else {
            isExitFlag = true;
            Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.",  Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExitFlag = false;
                }
            }, 2000);
        }
    }

    public void webview_change() {
        foldtx.setText("-- > 페이지 숨기기 < --");
        animation.start();
        webView.setVisibility(View.VISIBLE);
        foldtx.setVisibility(View.VISIBLE);
    }
}
