package com.example.progress.backend.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;

import java.util.ArrayList;

public class ExerciseTable {
    private static ExerciseTable instance;
    private ArrayList<ExerciseRow> cachedExercises;

    //SQL commands for Exercise table
    public static final String SQL_TABLE_NAME         = "Exercise";
    public static final String SQL_COLUMN_EXERCISE_ID = "exerciseID";
    public static final String SQL_COLUMN_WORKOUT_ID  = "workoutID";
    public static final String SQL_COLUMN_TYPE        = "type";
    public static final String SQL_COLUMN_NAME        = "name";
    public static final String SQL_COLUMN_REPS        = "reps";

    public static final String SQL_QUERY_ALL = "SELECT * FROM " + SQL_TABLE_NAME;
    public static final String SQL_QUERY_ONE_BY_ID = "SELECT * FROM " + SQL_TABLE_NAME + " WHERE " + SQL_COLUMN_EXERCISE_ID + " = ";
    public static final String SQL_DELETE_ALL = "DELETE FROM " + SQL_TABLE_NAME;

    public static final String SQL_TABLE_CREATE = "CREATE TABLE " + SQL_TABLE_NAME + " ( " +
            SQL_COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SQL_COLUMN_WORKOUT_ID + " INTEGER, " +
            SQL_COLUMN_TYPE + " VARCHAR(30), " +
            SQL_COLUMN_NAME + " VARCHAR(30), " +
            SQL_COLUMN_REPS + " INTEGER," +
            "FOREIGN KEY(" + SQL_COLUMN_WORKOUT_ID + ") REFERENCES " + WorkoutTable.SQL_TABLE_NAME + " ( " + SQL_COLUMN_WORKOUT_ID + "))";

    public boolean insertExercise(ExerciseRow exerciseRow, DatabaseHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ExerciseTable.SQL_COLUMN_WORKOUT_ID,exerciseRow.getWorkoutRow().getWorkoutID());
        contentValues.put(ExerciseTable.SQL_COLUMN_NAME,exerciseRow.getName());
        contentValues.put(ExerciseTable.SQL_COLUMN_TYPE,exerciseRow.getType());
        contentValues.put(ExerciseTable.SQL_COLUMN_REPS,exerciseRow.getReps());

        long id = db.insert(ExerciseTable.SQL_TABLE_NAME,null,contentValues);

        exerciseRow.setExerciseID(getLastID(helper));

        return (id!=-1);
    }

    public ExerciseRow findExercise(int exerciseID,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;
        ExerciseRow exerciseRow = null;
        try{
            cursor = database.rawQuery(ExerciseTable.SQL_QUERY_ONE_BY_ID + exerciseID ,null);
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                int id         = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_EXERCISE_ID));
                int workoutID  = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_WORKOUT_ID));
                String type    = cursor.getString(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_TYPE));
                String name    = cursor.getString(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_NAME));
                int reps       = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_REPS));

                WorkoutTable instance = WorkoutTable.getInstance();
                WorkoutRow workoutRow = instance.findWorkout(workoutID,helper);

                //create and store client
                exerciseRow = new ExerciseRow(exerciseID,workoutRow,type,name,reps);
            }
        }finally {
            assert cursor != null;
            cursor.close();
        }

        return exerciseRow;
    }

    public ArrayList<ExerciseRow> findAllExercises(DatabaseHelper helper)
    {
        //initialize array list
        ArrayList<ExerciseRow> toReturnArray  = new ArrayList<ExerciseRow>();

        //get instance of database
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //create cursor
            cursor = database.rawQuery(ExerciseTable.SQL_QUERY_ALL, null);
            cursor.moveToFirst();

            //loop trough
            while (!cursor.isAfterLast()) {
                //get attributes
                int exerciseID = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_EXERCISE_ID));
                int workoutID  = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_WORKOUT_ID));
                String type    = cursor.getString(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_TYPE));
                String name    = cursor.getString(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_NAME));
                int reps       = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_REPS));

                WorkoutTable instance = WorkoutTable.getInstance();
                WorkoutRow workoutRow = instance.findWorkout(workoutID,helper);

                //create and store client
                ExerciseRow exerciseRow = new ExerciseRow(exerciseID,workoutRow,type,name,reps);
                toReturnArray.add(exerciseRow);
                cursor.moveToNext();
            }
        }
        finally {
            assert cursor != null;
            cursor.close();
        }
        cachedExercises = toReturnArray;
        return toReturnArray;
    }

    public void deleteExercises(DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(ExerciseTable.SQL_DELETE_ALL);
    }


    public int updateExercise(ExerciseRow exerciseRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ExerciseTable.SQL_COLUMN_WORKOUT_ID,exerciseRow.getWorkoutRow().getWorkoutID());
        contentValues.put(ExerciseTable.SQL_COLUMN_NAME,exerciseRow.getName());
        contentValues.put(ExerciseTable.SQL_COLUMN_TYPE,exerciseRow.getType());
        contentValues.put(ExerciseTable.SQL_COLUMN_REPS,exerciseRow.getReps());

        return database.update(ExerciseTable.SQL_TABLE_NAME,contentValues,
                ExerciseTable.SQL_COLUMN_EXERCISE_ID + " = " + exerciseRow.getExerciseID(),null);
    }

    public int deleteExercise(ExerciseRow exerciseRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        return database.delete(ExerciseTable.SQL_TABLE_NAME,
                ExerciseTable.SQL_COLUMN_EXERCISE_ID + " = " + exerciseRow.getExerciseID(),null);
    }

    public int deleteWorkoutExercise(WorkoutRow workoutRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        return database.delete(ExerciseTable.SQL_TABLE_NAME,
                WorkoutTable.SQL_COLUMN_WORKOUT_ID + " = " + workoutRow.getWorkoutID(),null);
    }

    //singleton stuff
    private ExerciseTable()
    {
        this.cachedExercises = new ArrayList<ExerciseRow>();
    }

    public static ExerciseTable getInstance()
    {
        if(instance == null)
        {
            instance = new ExerciseTable();
        }
        return instance;
    }

    private  int getLastID(DatabaseHelper helper) {
        SQLiteDatabase database = helper.getWritableDatabase();
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = database.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }
}
