package com.example.progress.backend.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.WorkoutRow;

import java.util.ArrayList;

public class WorkoutTable {
    private static WorkoutTable instance;
    private ArrayList<WorkoutRow> cachedWorkoutRows;

    //SQL commands for Workout table
    public static final String SQL_TABLE_NAME ="Workout";
    public static final String SQL_COLUMN_WORKOUT_ID = "workoutID";
    public static final String SQL_COLUMN_CLIENT_ID = ClientTable.SQL_COLUMN_CLIENT_ID;
    public static final String SQL_COLUMN_START = "start";
    public static final String SQL_COLUMN_END = "end";
    public static final String SQL_COLUMN_NAME = "name";


    //SELECT
    public static final String SQL_QUERY_ALL = "SELECT * FROM " + SQL_TABLE_NAME;
    public static final String SQL_QUERY_ONE_BY_ID = "SELECT * FROM " + SQL_TABLE_NAME + " WHERE " + SQL_COLUMN_WORKOUT_ID + " = ";
    public static final String SQL_QUERY_BY_CLIENT = "SELECT * FROM " + SQL_TABLE_NAME + " WHERE " + ClientTable.SQL_COLUMN_CLIENT_ID  + " = ";

    //DELETE
    public static final String SQL_DELETE_ALL = "DELETE FROM " + SQL_TABLE_NAME;
    public static final String SQL_DELETE_BY_CLIENT = "DELETE FROM " + SQL_TABLE_NAME + "WHERE " + SQL_COLUMN_CLIENT_ID + " = ";
    public static final String SQL_DELETE_BY_ID = "DELETE FROM " + SQL_TABLE_NAME + "WHERE " + SQL_COLUMN_WORKOUT_ID + " = ";


    public static final String SQL_TABLE_CREATE = " CREATE TABLE " + SQL_TABLE_NAME + " (" +
            SQL_COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SQL_COLUMN_CLIENT_ID + " INTEGER,"+
            SQL_COLUMN_NAME + " VARCHAR(30), " +
            SQL_COLUMN_START + " INTEGER," +
            SQL_COLUMN_END + " INTEGER ," +
            "FOREIGN KEY(" + SQL_COLUMN_CLIENT_ID + ") REFERENCES " + ClientTable.SQL_TABLE_NAME + "(" + SQL_COLUMN_CLIENT_ID + "))";


    //db
    public boolean insertWorkout(WorkoutRow workoutRow, DatabaseHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(WorkoutTable.SQL_COLUMN_CLIENT_ID, workoutRow.getClientRow().getClientID());
        contentValues.put(WorkoutTable.SQL_COLUMN_NAME, workoutRow.getName());
        contentValues.put(WorkoutTable.SQL_COLUMN_START, workoutRow.getStart());
        contentValues.put(WorkoutTable.SQL_COLUMN_END, workoutRow.getEnd());

        long id = db.insert(WorkoutTable.SQL_TABLE_NAME,null,contentValues);
        return (id!=-1);
    }

    public WorkoutRow findWorkout(int workoutID,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;
        WorkoutRow workoutRow = null;
        try{
            cursor = database.rawQuery(WorkoutTable.SQL_QUERY_ONE_BY_ID + workoutID ,null);
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                int clientID = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_CLIENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_NAME));
                long start = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_START));
                long end = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_END));

                ClientTable instane = ClientTable.getInstance();
                ClientRow client = instane.findClient(clientID,helper);
                workoutRow = new WorkoutRow(client,start,end,name);
                workoutRow.setWorkoutID(workoutID);
            }
        }finally {
            cursor.close();
        }

        return workoutRow;
    }

    public ArrayList<WorkoutRow> findClientWorkouts(ClientRow clientRow,DatabaseHelper helper)
    {
        //initialize array list
        ArrayList<WorkoutRow> toReturnArray  = new ArrayList<WorkoutRow>();

        //get instance of database
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //create cursor
            cursor = database.rawQuery(WorkoutTable.SQL_QUERY_BY_CLIENT + clientRow.getClientID(), null);
            cursor.moveToFirst();

            //loop trough
            while (!cursor.isAfterLast()) {
                //get attributes
                int workoutID = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_WORKOUT_ID));
                int clientID = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_CLIENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_NAME));
                long start = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_START));
                long end = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_END));

                ClientTable instance = ClientTable.getInstance();

                //create and store client
                WorkoutRow workoutRow = new WorkoutRow(clientRow, start, end, name);
                workoutRow.setWorkoutID(workoutID);
                toReturnArray.add(workoutRow);
                cursor.moveToNext();
            }
        }
        finally {
            assert cursor != null;
            cursor.close();
        }
        cachedWorkoutRows = toReturnArray;
        return toReturnArray;
    }

    public ArrayList<WorkoutRow> findAllWorkouts(DatabaseHelper helper)
    {
        //initialize array list
        ArrayList<WorkoutRow> toReturnArray  = new ArrayList<WorkoutRow>();

        //get instance of database
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //create cursor
            cursor = database.rawQuery(WorkoutTable.SQL_QUERY_ALL, null);
            cursor.moveToFirst();

            //loop trough
            while (!cursor.isAfterLast()) {
                //get attributes
                int workoutID = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_WORKOUT_ID));
                int clientID = cursor.getInt(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_CLIENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_NAME));
                long start = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_START));
                long end = cursor.getLong(cursor.getColumnIndex(WorkoutTable.SQL_COLUMN_END));

                ClientTable instane = ClientTable.getInstance();
                ClientRow clientRow = instane.findClient(clientID,helper);

                //create and store client
                WorkoutRow workoutRow = new WorkoutRow(clientRow, start, end, name);
                workoutRow.setWorkoutID(workoutID);
                toReturnArray.add(workoutRow);
                cursor.moveToNext();
            }
        }
        finally {
            assert cursor != null;
            cursor.close();
        }
        cachedWorkoutRows = toReturnArray;
        return toReturnArray;
    }

    public int updateWorkout(WorkoutRow workoutRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkoutTable.SQL_COLUMN_CLIENT_ID, workoutRow.getClientRow().getClientID());
        contentValues.put(WorkoutTable.SQL_COLUMN_NAME, workoutRow.getName());
        contentValues.put(WorkoutTable.SQL_COLUMN_START, workoutRow.getStart());
        contentValues.put(WorkoutTable.SQL_COLUMN_END, workoutRow.getEnd());

        return database.update(WorkoutTable.SQL_TABLE_NAME,contentValues,
                WorkoutTable.SQL_COLUMN_WORKOUT_ID + " = " + workoutRow.getWorkoutID(),null);
    }

    public int deleteWorkout(WorkoutRow workoutRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ExerciseTable exerciseTable = ExerciseTable.getInstance();
        exerciseTable.deleteWorkoutExercise(workoutRow,helper);

        return database.delete(WorkoutTable.SQL_TABLE_NAME,
                WorkoutTable.SQL_COLUMN_WORKOUT_ID + " = " + workoutRow.getWorkoutID(),null);
    }

    public int deleteClientWorkout(ClientRow clientRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        ArrayList<WorkoutRow> workoutsToDelete = findClientWorkouts(clientRow,helper);

        ExerciseTable exerciseInstance = ExerciseTable.getInstance();

        for(WorkoutRow workout : workoutsToDelete)
        {
            exerciseInstance.deleteWorkoutExercise(workout,helper);
        }

        return database.delete(WorkoutTable.SQL_TABLE_NAME,
                ClientTable.SQL_COLUMN_CLIENT_ID + " = " + clientRow.getClientID(),null);
    }

    //singleton stuff
    private WorkoutTable()
    {
        cachedWorkoutRows = new ArrayList<WorkoutRow>();
    }

    public static WorkoutTable getInstance()
    {
        if(instance == null)
        {
            instance = new WorkoutTable();
        }
        return instance;
    }
}
