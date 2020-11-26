package com.example.progress.backend.table;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.WorkoutRow;

import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

/**
 * Singleton class
 */
public class ClientTable {

    private static ClientTable instance = null;
    private ArrayList<ClientRow> cachedClients;

    //SQL commands for Client table
    public static final String SQL_TABLE_NAME = "Client";
    public static final String SQL_COLUMN_CLIENT_ID = "clientID";
    public static final String SQL_COLUMN_NICKNAME = "nickname";
    public static final String SQL_TABLE_CREATE = "CREATE TABLE " + SQL_TABLE_NAME + "("
            + SQL_COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SQL_COLUMN_NICKNAME + " VARCHAR(30))";
    public static final String SQL_QUERY_ALL = "SELECT * FROM " + SQL_TABLE_NAME;
    public static final String SQL_DELETE_ALL = "DELETE  FROM " + SQL_TABLE_NAME;
    public static final String SQL_QUERY_ONE_BY_ID = "SELECT * FROM " + SQL_TABLE_NAME + " WHERE " + SQL_COLUMN_CLIENT_ID + " = ";

    public static final int NAME_LIMIT = 30;


    /**
     * Inserts Client into the Database
     * @param clientRow ClientRow instance
     * @param helper DatabaseHelper instance
     * @return  True if inserted, False if not
     */
    public boolean insertClient(ClientRow clientRow, DatabaseHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientTable.SQL_COLUMN_NICKNAME, clientRow.getNickname());

        long id = db.insert(ClientTable.SQL_TABLE_NAME,null,contentValues);

        clientRow.setClientID(getLastID(helper));

        return (id!=-1);
    }

    /**
     * Finds ClientRow in the Database
     * @param clientID ClientRow ID
     * @param helper DatabaseHelper instance
     * @return  ClientRow instance
     */
    public ClientRow findClient(int clientID, DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = null;
        ClientRow clientRow = null;
        try{
            cursor = database.rawQuery(ClientTable.SQL_QUERY_ONE_BY_ID +clientID ,null);
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(ClientTable.SQL_COLUMN_CLIENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ClientTable.SQL_COLUMN_NICKNAME));
                clientRow = new ClientRow(id,name);
            }
        }finally {
            assert cursor != null;
            cursor.close();
        }

        return clientRow;
    }

    /**
     * Deletes all ClientRows from the Database
     * @param helper DatabaseHelper instance
     */
    public void deleteClients(DatabaseHelper helper)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(ClientTable.SQL_DELETE_ALL);
        db.execSQL(WorkoutTable.SQL_DELETE_ALL);
        db.execSQL(ExerciseTable.SQL_DELETE_ALL);
    }

    /**
     * Finds all ClientRows in the Database
     * @param helper DatabaseHelper instance
     * @return  all ClientRows
     */
    public ArrayList<ClientRow> findAllClients(DatabaseHelper helper)
    {
        //initialize array list
        ArrayList<ClientRow> toReturnArray  = new ArrayList<ClientRow>();

        //get instance of database
        SQLiteDatabase database = helper.getReadableDatabase();

        //create cursor
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery(ClientTable.SQL_QUERY_ALL,null);
        cursor.moveToFirst();

        //loop trough
        while(!cursor.isAfterLast())
        {
            //get attributes
            int clientID = cursor.getInt(cursor.getColumnIndex(ClientTable.SQL_COLUMN_CLIENT_ID));
            String nickname = cursor.getString(cursor.getColumnIndex(ClientTable.SQL_COLUMN_NICKNAME));

            //create and store client
            ClientRow clientRow = new ClientRow(clientID,nickname);
            toReturnArray.add(clientRow);
            cursor.moveToNext();
        }
        cachedClients = toReturnArray;
        return toReturnArray;
    }

    /**
     * Updates ClientRow in the Database
     * @param clientRow ClientRow instance
     * @param helper DatabaseHelper instance
     * @return  True if updated, False if not
     */
    public boolean updateClient(ClientRow clientRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientTable.SQL_COLUMN_CLIENT_ID,clientRow.getClientID());
        contentValues.put(ClientTable.SQL_COLUMN_NICKNAME, clientRow.getNickname());


        return database.update(ClientTable.SQL_TABLE_NAME,contentValues,
                ClientTable.SQL_COLUMN_CLIENT_ID+ " = " + clientRow.getClientID(),null) == 1;
    }

    /**
     * Deletes ClientRow in the Database
     * @param clientRow ClientRow instance
     * @param helper DatabaseHelper instance
     * @return  True if deleted, False if not
     */
    public boolean deleteClient(ClientRow clientRow,DatabaseHelper helper)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        WorkoutTable workoutInstance   = WorkoutTable.getInstance();

        workoutInstance.deleteClientWorkout(clientRow,helper);

        return database.delete(ClientTable.SQL_TABLE_NAME,
                ClientTable.SQL_COLUMN_CLIENT_ID+ " = " + clientRow.getClientID(),null) == 1;
    }


    /**
     * ClientTable constructor
     */
    private ClientTable() {
        cachedClients = new ArrayList<ClientRow>();
    }

    /**
     *  ClientTable instance getter
     * @return ClientTable static instance
     */
    public static ClientTable getInstance()
    {
        if(instance == null)
        {
            instance = new ClientTable();
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
