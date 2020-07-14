package app.healthmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MedDBHelper extends SQLiteOpenHelper {
    private static MedDBHelper sInstance;
    private static final int DB_VERSION = 7; //Default = 0  +++++++
    private static final String DB_NAME = "Med.db";
    //DB TABLE FORMAT
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MedContract.MedEntry.TABLE_NAME + " (" + MedContract.MedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MedContract.MedEntry.COLUMN_NAME_TITLE + " TEXT,"
            + MedContract.MedEntry.COLUMN_NAME_CONTENTS + " TEXT,"
            + MedContract.MedEntry.COLUMN_NAME_IMAGES + " TEXT,"
            + MedContract.MedEntry.COLUMN_NAME_CONTENTS2 + " TEXT)";

    //DB Delete and Create
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MedContract.MedEntry.TABLE_NAME;

    public static MedDBHelper getInstance(Context context) {
        if (sInstance == null){
            sInstance = new MedDBHelper(context);
        }return sInstance;
    }

    private MedDBHelper(@Nullable Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES); //TABLE CREATE
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
