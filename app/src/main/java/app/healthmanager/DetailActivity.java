package app.healthmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DetailActivity extends AppCompatActivity {
    ImageButton btn1, btn2, btn3, btn4;
    FloatingActionButton addmedmtn;
    private MedAdapter mAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#62bfad"));
        }
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
        final TextView text = layout.findViewById(R.id.text);
        final Toast toast = new Toast(DetailActivity.this);
        text.setTextSize(15);
        text.setTextColor(Color.BLACK);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        btn1 = (ImageButton) findViewById(R.id.btnhome);
        btn2 = (ImageButton) findViewById(R.id.btndetail);
        btn3 = (ImageButton) findViewById(R.id.btncalendar);
        btn4 = (ImageButton) findViewById(R.id.btnoption);
        addmedmtn = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        final ListView listview = findViewById(R.id.list);
        getMedCursor();
        mAdapter = new MedAdapter(DetailActivity.this, cursor);
        registerForContextMenu(listview);
        listview.setAdapter(mAdapter);

        addmedmtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //Creative AlertDialog for Add Your Medicien
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.alertdialog_add, null, false);
                view.setBackgroundColor(Color.parseColor("#f8f3eb"));
                builder.setView(view);
                final EditText etname = (EditText) view.findViewById(R.id.etname);
                final EditText etwhere = (EditText) view.findViewById(R.id.etwhere);
                final EditText ethowmany = (EditText) view.findViewById(R.id.ethowmany);
                final Button okbtn = (Button) view.findViewById(R.id.okbtn);
                final Button canclebtn = (Button) view.findViewById(R.id.canclebtn);
                final AlertDialog alertDialog = builder.create();
                etname.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_ENTER;
                    }
                });
                etwhere.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_ENTER;
                    }
                });
                ethowmany.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_ENTER;
                    }
                });

                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aimg = "default";
                        String aname = etname.getText().toString();
                        String awhere = etwhere.getText().toString();
                        String ahowmany = ethowmany.getText().toString();
                        if (aname.equals("")) {
                            etname.setFocusableInTouchMode(true);
                            etname.requestFocus();
                            text.setText("이름을 정확히 입력해주세요");
                            toast.show();
                        } else if (awhere.equals("")) {
                            etwhere.setFocusableInTouchMode(true);
                            etwhere.requestFocus();
                            text.setText("보관장소를 정확히 입력해주세요");
                            toast.show();
                        } else if (ahowmany.equals("")) {
                            ethowmany.setFocusableInTouchMode(true);
                            ethowmany.requestFocus();
                            text.setText("복용횟수를 정확히 입력해주세요");
                            toast.show();
                        } else {
                            ListView listview = findViewById(R.id.list);
                            mAdapter = new MedAdapter(DetailActivity.this, cursor);
                            listview.setAdapter(mAdapter);
                            getMedCursor();

                            //DB에 Data 저장
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MedContract.MedEntry.COLUMN_NAME_IMAGES, aimg);
                            contentValues.put(MedContract.MedEntry.COLUMN_NAME_TITLE, aname);
                            contentValues.put(MedContract.MedEntry.COLUMN_NAME_CONTENTS, awhere);
                            contentValues.put(MedContract.MedEntry.COLUMN_NAME_CONTENTS2, ahowmany);

                            SQLiteDatabase db = MedDBHelper.getInstance(DetailActivity.this).getWritableDatabase();
                            db.insert(MedContract.MedEntry.TABLE_NAME, null, contentValues); //db에 insert
                            mAdapter.swapCursor(cursor); //Cursor Swap하여 최신화
                            text.setText("약이 추가되었습니다!");
                            toast.show();
                            alertDialog.dismiss();
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailActivity.this, MedInfoActivity.class);

                //Hand over the key value for detail activity
                String nameinfo = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_TITLE));
                String whereinfo = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_CONTENTS));
                String howmanyinfo = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_CONTENTS2));
                Integer cursor_position = Integer.parseInt(String.valueOf(mAdapter.getItemId(cursor.getPosition())));
                String img_string = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_IMAGES));

                intent.putExtra("nameinfo", nameinfo);
                intent.putExtra("whereinfo", whereinfo);
                intent.putExtra("howmanyinfo", howmanyinfo);
                intent.putExtra("position", cursor_position);
                intent.putExtra("imgstr", img_string);
                startActivity(intent);
                finish();
            }
        });

        //Bottom Navegation Menu Click Event
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void getMedCursor() {
        MedDBHelper dbHelper = MedDBHelper.getInstance(DetailActivity.this);
        cursor = dbHelper.getReadableDatabase().query(MedContract.MedEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    //DB에 저장할 ListItem Adapter
    private class MedAdapter extends CursorAdapter {
        public MedAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            TextView nameText = view.findViewById(R.id.txname);
            TextView whereText = view.findViewById(R.id.txwhere);
            TextView howmanyText = view.findViewById(R.id.txhowmany);
            ImageView deletelist = view.findViewById(R.id.delete_list);
            final LayoutInflater inflater = getLayoutInflater();
            final View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
            final TextView text = layout.findViewById(R.id.text);
            final Toast toast = new Toast(DetailActivity.this);
            text.setTextSize(15);
            text.setTextColor(Color.BLACK);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);

            //휴지통 클릭 시 리스트 삭제
            deletelist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("항목 제거").setMessage("약을 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MedAdapter mAdapter = new MedAdapter(context, cursor);
                            SQLiteDatabase db = MedDBHelper.getInstance(context).getWritableDatabase();
                            db.delete(MedContract.MedEntry.TABLE_NAME, MedContract.MedEntry._ID + " = " + mAdapter.getItemId(cursor.getPosition()), null); //DB Delete
                            for (int i = 0; i <= 1; i++) {
                                if (i == 1) {
                                    text.setText("정상적으로 삭제되었습니다.");
                                    toast.show();
                                    dialog.dismiss();
                                    Intent intent = getIntent(); //화면 이동(액티비티 이동)set get
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.show();
                }
            });
            nameText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_TITLE)));
            whereText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_CONTENTS)));
            howmanyText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_NAME_CONTENTS2)));
        }
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