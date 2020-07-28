package app.healthmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {

    ImageButton btn1, btn2, btn3, btn4;
    Button memobtn;
    String yearstr;
    String monthstr;
    String daystr;
    String selectdate;
    TextView memo_contents;
    TextView memotx;
    ArrayList<String> memoarray = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(Calendar.getInstance().getTimeInMillis());


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62bfad"));
        }

        btn1 = (ImageButton) findViewById(R.id.btnhome);
        btn2 = (ImageButton) findViewById(R.id.btndetail);
        btn3 = (ImageButton) findViewById(R.id.btncalendar);
        btn4 = (ImageButton) findViewById(R.id.btnoption);
        memobtn = (Button) findViewById(R.id.memodiagbtn);
        memotx = (TextView) findViewById(R.id.memotx);
        memo_contents = (TextView) findViewById(R.id.memo_contents);

        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        final String year = yearFormat.format(currentTime);
        final String month = monthFormat.format(currentTime);
        final String day = dayFormat.format(currentTime);
        memotx.setText(year + "년 " + month + "월 " + day + "일의 메모내용");

        // Making CalendarView Instance
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
        //To regist Listner
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                yearstr = String.valueOf(year);
                monthstr = String.valueOf(month + 1);
                daystr = String.valueOf(dayOfMonth);
                selectdate = yearstr + "년 " + monthstr + "월 " + daystr + "일의 메모내용";
                memotx.setText(selectdate);
                date = yearstr + "-" + monthstr + "-" + daystr;

                myRef.child(date).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            memo_contents.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                        } catch (Exception e) {
                            memo_contents.setText("메모없음");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        //TODO
        //버그 : FireBase에 리스트형태로 데이터 저장 시 0번인덱스부터 중첩저장현상 발생

        memobtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                View view = LayoutInflater.from(CalendarActivity.this).inflate(R.layout.alertdialog_memoadd, null, false);
                view.setBackgroundColor(Color.parseColor("#f8f3eb"));
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                final Button addbtn = (Button) view.findViewById(R.id.memoaddok);
                final Button canclebtn = (Button) view.findViewById(R.id.memoaddno);
                final EditText memoedit = (EditText) view.findViewById(R.id.memoedit);
                final TextView title = (TextView) view.findViewById(R.id.titletext);
                title.setText(year + "년 " + month + "월 " + day + "일의 메모");
                if (yearstr != null || monthstr != null || daystr != null) {
                    title.setText(yearstr + "년 " + monthstr + "월 " + daystr + "일의 메모");
                }
                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//DB에 해당 날짜 메모 insert
                        try {
                            if (memoedit.getText().toString().equals("")) {
                                // edit에 값 입력 안됐을 경우 edit로 focus
                                memoedit.setFocusableInTouchMode(true);
                                memoedit.requestFocus();
                                //focus 후 키보드 올리기
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                Toast.makeText(CalendarActivity.this, "메모를 입력해주세요", Toast.LENGTH_SHORT).show();
                            } else {
                                memoarray.add(memoedit.getText().toString());
                                Log.e("memoarray", memoarray.get(memoarray.size() -1));
                                Log.e("memoarray", memoarray.toString());
                                myRef.child(date).setValue(memoarray);
                                alertDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                canclebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        myRef.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    memo_contents.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Bottom Navegation Menu Click Event
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
