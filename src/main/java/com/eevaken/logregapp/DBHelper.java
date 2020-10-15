package com.eevaken.logregapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1; // версия дб
    public static final String DATABASE_NAME = "userDB"; // название дб
    public static final String TABLE_USERS = "users"; // название таблицы

    //поля таблицы юзерс
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //конструктор вызова
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //создаем таблицу
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + "("+ KEY_ID + " integer primary key,"
                + KEY_NAME + " text ,"
                + KEY_SURNAME + " text,"
                + KEY_EMAIL + " text,"
                + KEY_USERNAME + " text,"
                + KEY_PASSWORD + " text)");
    }

    //обновляем таблицу
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // сначала удаляем
        db.execSQL("drop table if exists " + TABLE_USERS);
        // пересоздаем
        onCreate(db);
    }
}
