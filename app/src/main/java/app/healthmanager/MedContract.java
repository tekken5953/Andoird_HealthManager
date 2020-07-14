package app.healthmanager;

import android.provider.BaseColumns;

public class MedContract {
    private MedContract() {

    }
    //CREATE DB TABLE INFORMATION
    public static class MedEntry implements BaseColumns{
        public static final String TABLE_NAME = "med";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUMN_NAME_CONTENTS2 = "contents2";
        public static final String COLUMN_NAME_IMAGES = "image";
    }
}
