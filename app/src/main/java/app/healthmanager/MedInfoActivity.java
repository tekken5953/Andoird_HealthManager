package app.healthmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MedInfoActivity extends AppCompatActivity {
    TextView edit1, edit2, edit3, spinnertx, sptxh, sptxm;
    ImageButton alarm, backpress;
    ArrayList<LinearLayout> alltable = new ArrayList<LinearLayout>();
    ArrayList<EditText> arrayedith = new ArrayList<EditText>();
    ArrayList<EditText> arrayeditm = new ArrayList<EditText>();
    String st1, st2, st3, st4 = null;
    EditText edith, editm;
    LinearLayout spinnerRow;
    ImageView imginfo;
    Switch a_switch;
    ArrayAdapter<String> spadh, spadm;
    private AlarmManager alarmManager;
    Integer count = 0;
    private static final int REQUEST_CODE = 0;
    public static final int REQUEST_CODE2 = 1;
    private String imageFilePath;
    private Uri photoUri;

    @Override
    protected void onResume() {
        super.onResume();
        //갤러리 이미지를 DB에서 읽어와 교체 (없으면 기본이미지로 set)
        try {
            Uri imguri = Uri.parse(getIntent().getExtras().getString("imgstr"));
            String imgstring = imguri.toString();
            InputStream is = getContentResolver().openInputStream(Uri.parse(imgstring));
            if (is != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                float height = 512;
                float width = 512;
                Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
                imginfo.setImageBitmap(resizedBmp);
                imginfo.setColorFilter(Color.parseColor("#00ff0000"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            imginfo.setImageResource(R.drawable.galary);
            imginfo.setColorFilter(Color.parseColor("#3f48cc"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DetailActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medinfo);
        tedPermission();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62bfad"));
        }
        backpress = (ImageButton) findViewById(R.id.backpressbtn);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        edit1 = (TextView) findViewById(R.id.edit1info);
        edit2 = (TextView) findViewById(R.id.edit2info);
        edit3 = (TextView) findViewById(R.id.edit3info);
        alarm = (ImageButton) findViewById(R.id.alarm);
        imginfo = (ImageView) findViewById(R.id.imginfo);
        a_switch = (Switch) findViewById(R.id.onoffswitch);
        a_switch.setChecked(true);
        imginfo.setColorFilter(Color.parseColor("#3f48cc"));
        edit1.setText(getIntent().getExtras().getString("nameinfo"));
        edit2.setText(getIntent().getExtras().getString("whereinfo"));
        edit3.setText(getIntent().getExtras().getString("howmanyinfo"));
        ArrayList<String> listh = new ArrayList<>();
        for (int hour = 1; hour <= 24; hour++) {
            listh.add(String.valueOf(hour));
        }
        ArrayList<String> listm = new ArrayList<>();
        for (int min = 0; min <= 59; min++) {
            listm.add(String.valueOf(min));
        }
        spadh = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listh);
        spadm = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listm);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
        final TextView text = layout.findViewById(R.id.text);
        final Toast toast = new Toast(MedInfoActivity.this);
        text.setTextSize(15);
        text.setTextColor(Color.BLACK);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        onResume(); //이미지 설정

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedInfoActivity.this, DetailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //카메라 이미지 클릭시 갤러리로 이동
        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"갤러리에서 가져오기", "카메라로 촬영하기"};
                new AlertDialog.Builder(MedInfoActivity.this)
                        .setIcon(R.drawable.galary)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                if (item == 0) {
                                    //갤러리 호출
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType
                                            (MediaStore.Images.Media.CONTENT_TYPE);
                                    imginfo.setColorFilter(null);
                                    startActivityForResult(intent, REQUEST_CODE);
                                } else if (item == 1) {
                                    //카메라로 찍기
                                    sendTakePhotoIntent();
                                }
                            }
                        })
                        .show();
            }
        });

        a_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (a_switch.isChecked()) {
                    if (count >= 1) {
                    } else {
                        text.setText("설정 된 알림이 없습니다");
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        toast.show();
                        setAlarmSetting();
                    }
                } else {
                    NotificationManagerCompat.from(MedInfoActivity.this).cancel(1);
                    text.setText("해당 약의 알림을 삭제했습니다");
                    toast.show();
                    count = 0;
                }
            }
        });

        //알림버튼 클릭 시 알림정보설정 이벤트
        alarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                arrayedith.clear();
                arrayeditm.clear();
                if (count >= 1) {
                    //이미 알림이 설정되어있을 때 알림삭제
                    NotificationManagerCompat.from(MedInfoActivity.this).cancel(1);
                    count = 0;
                    text.setText("기존알림이 초기화되었습니다.\n다시 설정해주세요");
                    toast.show();
                }
                setAlarmSetting();
            }
        });
    }

    //알림 설정
    public void NotificationSomethings() {
        Intent intent = new Intent(MedInfoActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1 + count, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st3));
        calendar.set(Calendar.MINUTE, Integer.parseInt(st4));
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            Toast.makeText(this, "내일 " + st3 + "시 " + st4 + "분 으로 알림설정완료", Toast.LENGTH_SHORT).show();
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        }
    }

    public void setAlarmSetting() {
        final AlertDialog builder = new AlertDialog.Builder(MedInfoActivity.this).create();
        View view = LayoutInflater.from(MedInfoActivity.this).inflate(R.layout.alertdialog_alarm, null, false);
        view.setBackgroundColor(Color.parseColor("#f8f3eb"));
        final Button okbtn = (Button) view.findViewById(R.id.alarmokbtn); //okbutton
        final Button nobtn = (Button) view.findViewById(R.id.alarmnobtn);  //nobutton
        final Switch alarmswitch = (Switch) view.findViewById(R.id.onoffswitch); //alarm on/off switch
        final EditText edit_count = (EditText) view.findViewById(R.id.edit_count); //alarm count
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.spinnerlinear);
        edit_count.setHint("6이하의 숫자만 입력!");
        edit_count.setTextSize(12);
        linearLayout.removeAllViews();
        builder.setView(view);
        builder.show();
        //알림 횟수 설정에 따른 시간설정 Row 추가
        edit_count.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Text 변경 전
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Text 변경 되었을 때
                linearLayout.removeAllViews();
                alltable.clear();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void afterTextChanged(Editable s) {
                //Text 변경 후
                st1 = edit_count.getText().toString();
                if ((st1.equals(""))) {
                    st1 = "0";
                } else if (!(st1.equals("1") | st1.equals("2") | st1.equals("3") | st1.equals("4") | st1.equals("5") | st1.equals("6") | st1.equals("7") | st1.equals("8") | st1.equals("9") | st1.equals("0"))) {
                    Toast.makeText(MedInfoActivity.this, "공백 또는 특수문자는 입력이 불가능합니다", Toast.LENGTH_SHORT).show();
                    edit_count.setText(null);
                } else if (st1.equals("0")) {
                    Toast.makeText(MedInfoActivity.this, "0보다 큰 숫자를 입력하세요", Toast.LENGTH_SHORT).show();
                    edit_count.setText(null);
                }
                //알람시간 설정열기
                for (int z = 0; z < (Integer.parseInt(st1)); z++) {
                    spinnerRow = new LinearLayout(linearLayout.getContext());
                    spinnertx = new TextView(spinnerRow.getContext());
                    spinnerRow.setPadding(15, 35, 0, 0);
                    sptxh = new TextView(spinnerRow.getContext());
                    sptxm = new TextView(spinnerRow.getContext());
                    edith = new EditText(spinnerRow.getContext());
                    editm = new EditText(spinnerRow.getContext());
                    LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(180, 150);
                    edith.setLayoutParams(pp);
                    edith.setGravity(Gravity.CENTER);
                    editm.setGravity(Gravity.CENTER);
                    editm.setLayoutParams(pp);
                    sptxh.setText(" 시  ");
                    sptxh.setTextSize(12);
                    sptxm.setText(" 분  ");
                    sptxm.setTextSize(12);
                    st2 = String.valueOf(z + 1);
                    spinnertx.setText(st2 + "번째 알림받을 시간   ");
                    spinnertx.setTextSize(12);
                    spinnerRow.setId(z);
                    alltable.add(spinnerRow);
                    arrayedith.add(edith);
                    arrayeditm.add(editm);
                    spinnerRow.addView(spinnertx);
                    spinnerRow.addView(edith);
                    spinnerRow.addView(sptxh);
                    spinnerRow.addView(editm);
                    spinnerRow.addView(sptxm);
                    linearLayout.addView(spinnerRow);
                    linearLayout.setPadding(37, 0, 0, 0);
                    //editText에 5이상의 숫자가 입력되었을 때
                    if (Integer.parseInt(st1) > 6) {
                        Toast.makeText(MedInfoActivity.this, "6이하의 숫자만 입력해주세요!", Toast.LENGTH_SHORT).show();
                        edit_count.setText(null);
                        linearLayout.removeAllViews();
                        alltable.clear();
                        break;
                    }
                }
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st1 = edit_count.getText().toString();
                if (st1.equals("")) {
                    Toast.makeText(MedInfoActivity.this, "약 먹을 횟수를 입력해주세요", Toast.LENGTH_SHORT).show();
                    // edit에 값 입력 안됐을 경우 edit로 focus
                    edit_count.setFocusableInTouchMode(true);
                    edit_count.requestFocus();
                    //focus 후 키보드 올리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    //Time Setting 값 받아오기
                    for (int z = 0; z < Integer.parseInt(st1); z++) {
                        st3 = arrayedith.get(z).getText().toString();
                        st4 = arrayeditm.get(z).getText().toString();
                        if (st3.equals("") || st4.equals("")) {
                            Toast.makeText(MedInfoActivity.this, "올바른 시간이 아닙니다", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(st3) > 23 || Integer.parseInt(st3) < 0) {
                            Toast.makeText(MedInfoActivity.this, "올바른 시간이 아닙니다", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(st4) > 59 || Integer.parseInt(st4) < 0) {
                            Toast.makeText(MedInfoActivity.this, "올바른 시간이 아닙니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("hour" + count, Integer.parseInt(st3));
                            intent.putExtra("min" + count, Integer.parseInt(st4));
                            count++;
                            NotificationSomethings();
                            Toast.makeText(MedInfoActivity.this, st3 + " 시 " + st4 + " 분에 " + count + " 번째 알림 발송", Toast.LENGTH_SHORT).show();
                            builder.dismiss();
                        }
                    }
                }
                a_switch.setChecked(true); //알림 자동설정
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                a_switch.setChecked(false);
            }
        });
    }

    //갤러리 불러오기 함수
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SQLiteDatabase db = MedDBHelper.getInstance(MedInfoActivity.this).getWritableDatabase();
        if (requestCode == REQUEST_CODE) {
            try {
                Uri img_uri = data.getData();
                assert img_uri != null;
                String img_string = img_uri.toString();
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                //selected Image`s size is bigger than imginfo`s one
                float bmpHeight = 512;
                float bmpWidth = 512;
                Bitmap resizedBmp = Bitmap.createScaledBitmap(img, (int) bmpWidth, (int) bmpHeight, true); //image size resize 후 등록
                assert in != null;
                in.close();
                //선택된 이미지를 DB TABLE 에 update
                ContentValues contentValues = new ContentValues();
                contentValues.put(MedContract.MedEntry.COLUMN_NAME_IMAGES, img_string);
                db.update(MedContract.MedEntry.TABLE_NAME, contentValues, MedContract.MedEntry._ID + "=" + (Objects.requireNonNull(getIntent().getExtras())).getInt("position"), null);
                imginfo.setImageBitmap(resizedBmp);
                Intent intent = new Intent(MedInfoActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                Toast.makeText(this, "사진 저장을 완료했습니다!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            onResume();
        } else if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK) {
            //카메라로 찍은 사진을 Bitmap으로 받아와서 교체하고 Uri로 parse 후 DB에 Update하는 코드
            //TODO
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
                imginfo.setImageBitmap(bitmap);
                Intent intent = new Intent(MedInfoActivity.this, DetailActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                Toast.makeText(this, "사진 저장을 완료했습니다!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SQLiteDatabase db = MedDBHelper.getInstance(MedInfoActivity.this).getWritableDatabase();
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                String img_string = photoUri.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //선택된 이미지를 DB TABLE 에 update
                ContentValues contentValues = new ContentValues();
                contentValues.put(MedContract.MedEntry.COLUMN_NAME_IMAGES, img_string);
                db.update(MedContract.MedEntry.TABLE_NAME, contentValues, MedContract.MedEntry._ID + "=" + (Objects.requireNonNull(getIntent().getExtras())).getInt("position"), null);
                startActivityForResult(takePictureIntent, REQUEST_CODE2);
            }
        }
    }

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setDeniedMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}