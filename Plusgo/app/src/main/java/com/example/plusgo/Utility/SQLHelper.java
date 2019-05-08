/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 4/17/19 1:40 PM
 *
 */

package com.example.plusgo.Utility;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "com.example.plusgo";
    public static final String TABLE_NAME = "USER";
    public static final String COL1_TASK = "";
    public static final String COL2_TASK = "";
    public static final String _ID = BaseColumns._ID;
    SQLiteDatabase sqLiteDatabase;


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String createTodoListTable = "CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_TASK + " TEXT, " +
                COL2_TASK + " TEXT)";
        sqlDB.execSQL(createTodoListTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }

}

