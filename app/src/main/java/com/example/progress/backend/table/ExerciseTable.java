package com.example.progress.backend.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;

import java.util.ArrayList;

/**
 * Singleton class
 */
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
    public static final String SQL_COLUMN_WEIGHT        = "weight";

    public static final String SQL_QUERY_ALL = "SELECT * FROM " + SQL_TABLE_NAME;
    public static final String SQL_QUERY_ONE_BY_ID = "SELECT * FROM " + SQL_TABLE_NAME + " WHERE " + SQL_COLUMN_EXERCISE_ID + " = ";
    public static final String SQL_DELETE_ALL = "DELETE FROM " + SQL_TABLE_NAME;

    public static final String SQL_TABLE_CREATE = "CREATE TABLE " + SQL_TABLE_NAME + " ( " +
            SQL_COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SQL_COLUMN_TYPE + " VARCHAR(30), " +
            SQL_COLUMN_NAME + " VARCHAR(30), " +
            SQL_COLUMN_REPS + " INTEGER," +
            SQL_COLUMN_WEIGHT + " INTEGER," +
            SQL_COLUMN_WORKOUT_ID + " INTEGER, " +
            "FOREIGN KEY(" + SQL_COLUMN_WORKOUT_ID + ") REFERENCES " + WorkoutTable.SQL_TABLE_NAME + " ( " + SQL_COLUMN_WORKOUT_ID + "))";

    /**
     * Inserts ExerciseRow into the Database
     * @param exerciseRow ExerciseRow instance
     * @param helper DatabaseHelper instance
     * @return True if inserted, False if not
     */
    public boolean insertExercise(ExerciseRow exerciseRow, DatabaseHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ExerciseTable.SQL_COLUMN_WORKOUT_ID,exerciseRow.getWorkoutRow().getWorkoutID());
        contentValues.put(ExerciseTable.SQL_COLUMN_NAME,exerciseRow.getName());
        contentValues.put(ExerciseTable.SQL_COLUMN_TYPE,exerciseRow.getType());
        contentValues.put(ExerciseTable.SQL_COLUMN_REPS,exerciseRow.getReps());
        contentValues.put(ExerciseTable.SQL_COLUMN_WEIGHT,exerciseRow.getWeight());

        long id = db.insert(ExerciseTable.SQL_TABLE_NAME,null,contentValues);

        exerciseRow.setExerciseID(getLastID(helper));

        return (id!=-1);
    }

    /**
     * Finds ExerciseRow in the Database
     * @param exerciseID ExerciseRow ID
     * @param helper DatabaseHelper instance
     * @return ExerciseRow instance
     */
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
                int weight     = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_WEIGHT));

                WorkoutTable instance = WorkoutTable.getInstance();
                WorkoutRow workoutRow = instance.findWorkout(workoutID,helper);

                //create and store client
                exerciseRow = new ExerciseRow(exerciseID,workoutRow,type,name,reps,weight);
            }
        }finally {
            assert cursor != null;
            cursor.close();
        }

        return exerciseRow;
    }

    /**
     * Finds all ExerciseRows in the Database
     * @param helper DatabaseHelper instance
     * @return All ExerciseRow instances
     */
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
                int weight     = cursor.getInt(cursor.getColumnIndex(ExerciseTable.SQL_COLUMN_WEIGHT));

                WorkoutTable instance = WorkoutTable.getInstance();
                WorkoutRow workoutRow = instance.findWorkout(workoutID,helper);

                //create and store client
                ExerciseRow exerciseRow = new ExerciseRow(exerciseID,workoutRow,type,name,reps,weight);
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

    /**
     * Deletes all ExerciseRows in the Database
     * @param helper DatabaseHelper instance
     */
    public void deleteExercises(DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(ExerciseTable.SQL_DELETE_ALL);
    }

    /**
     * Updates ExerciseRow in the Database
     * @param exerciseRow ExerciseRow instance
     * @param helper DatabaseHelper instance
     * @return True if updated, False if not
     */
    public boolean updateExercise(ExerciseRow exerciseRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ExerciseTable.SQL_COLUMN_WORKOUT_ID,exerciseRow.getWorkoutRow().getWorkoutID());
        contentValues.put(ExerciseTable.SQL_COLUMN_NAME,exerciseRow.getName());
        contentValues.put(ExerciseTable.SQL_COLUMN_TYPE,exerciseRow.getType());
        contentValues.put(ExerciseTable.SQL_COLUMN_REPS,exerciseRow.getReps());

        return database.update(ExerciseTable.SQL_TABLE_NAME,contentValues,
                ExerciseTable.SQL_COLUMN_EXERCISE_ID + " = " + exerciseRow.getExerciseID(),null) == 1;
    }

    /**
     * Deletes ExerciseRow from the Databse
     * @param exerciseRow ExerciseRow instance
     * @param helper DatabaseHelper instance
     * @return True if deleted, False if not
     */
    public boolean deleteExercise(ExerciseRow exerciseRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        return database.delete(ExerciseTable.SQL_TABLE_NAME,
                ExerciseTable.SQL_COLUMN_EXERCISE_ID + " = " + exerciseRow.getExerciseID(),null) == 1;
    }

    /**
     * Deletes WorkoutRow ExerciseRows
     * @param workoutRow WorkoutRow instance
     * @param helper DatabaseHelper instance
     * @return number of rows effected
     */
    public int deleteWorkoutExercise(WorkoutRow workoutRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        return database.delete(ExerciseTable.SQL_TABLE_NAME,
                WorkoutTable.SQL_COLUMN_WORKOUT_ID + " = " + workoutRow.getWorkoutID(),null);
    }

    /**
     * ExerciseTable constructor
     */
    private ExerciseTable()
    {
        this.cachedExercises = new ArrayList<ExerciseRow>();
    }

    /**
     * ExerciseTable instance getter
     * @return ExerciseTable static instance
     */
    public static ExerciseTable getInstance()
    {
        if(instance == null)
        {
            instance = new ExerciseTable();
        }
        return instance;
    }

    /**
     * Finds last ID inserted into the Database
     * @param helper DatabaseHelper instance
     * @return Last ID inserted
     */
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
