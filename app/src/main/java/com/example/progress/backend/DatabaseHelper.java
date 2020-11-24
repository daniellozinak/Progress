package com.example.progress.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.progress.backend.table.ClientTable;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.backend.table.WorkoutTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Progress.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseHelper.DATABASE_NAME,
                null, DatabaseHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ClientTable.SQL_TABLE_CREATE);
        db.execSQL(WorkoutTable.SQL_TABLE_CREATE);
        db.execSQL(ExerciseTable.SQL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE Exercise ADD COLUMN weight INTEGER DEFAULT 0");
//        }
    }

}
