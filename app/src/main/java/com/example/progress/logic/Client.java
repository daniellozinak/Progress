package com.example.progress.logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.logic.settings.Settings;

import java.util.ArrayList;

public class Client extends Entity {
    private ClientRow clientRow;
    private Workout currentWorkout = null;

    /**
     * Client constructor
     * @param nickname set as ClientRow nickname
     */
    public Client(String nickname)
    {
        clientRow = new ClientRow(nickname);
    }

    /**
     * Client constructor
     * @param client ClientRow instance, set as ClientRow
     */
    public Client(ClientRow client)
    {
        this.clientRow = client;
    }

    /**
     * Client constructor
     * @param id ClientRow id
     */
    public Client(int id)
    {
        this.clientRow = ClientTable.getInstance()
                .findClient(id,Settings.getInstance().getHelper());
    }

    /**
     * Starts new Workout
     * @param start timestamp
     * @param name Workout name
     * @return True if Workout started, False if not
     */
    public boolean startWorkout(long start,String name)
    {
        //creates new Workout object
        if(clientRow == null) {return false;}
        currentWorkout = new Workout(this.clientRow,start,name);
        return true;
    }

    /**
     * Ends Workout
     * @return True if Workout ended, False if not
     */
    public boolean endWorkout()
    {
        if(currentWorkout.save())
        {
            currentWorkout = null;
            return true;
        }
        return  false;
    }


    //returns true if workout is happening right now

    /**
     *
     * @return True if Workout is happening right now, False if not
     */
    public boolean isCurrentWorkout() {return currentWorkout !=null;}


    /**
     * ClientRow getter
     * @return ClientRow instance
     */
    public ClientRow getClientRow() {
        return clientRow;
    }

    /**
     * ClientRow setter
     * @param clientRow set as ClientRow
     */
    public void setClientRow(ClientRow clientRow) {
        this.clientRow = clientRow;
    }

    /**
     * Current Workout getter
     * @return Current Workout instance
     */
    public Workout getCurrentWorkout()
    {
        assert (currentWorkout!=null);
        return currentWorkout;
    }

    /**
     * Current Workout setter
     * @param currentWorkout set as Current Workout
     */
    public void setCurrentWorkout(Workout currentWorkout) { this.currentWorkout = currentWorkout; }

    /**
     * Static Clients getter
     * @return all Clients in the Database
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Client> getAllClients()
    {
        ArrayList<ClientRow> clientRows = ClientTable.getInstance().findAllClients(Settings.getInstance().getHelper());
        ArrayList<Client> clients = new ArrayList<>();
        clientRows.forEach(element-> clients.add(new Client(element)));

        return clients;
    }

    /**
     * Finished Workouts getter
     * @return All Workouts that hs been finished so far
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Workout> getClientFinishedWorkouts()
    {
        //select all workouts for client
        ArrayList<WorkoutRow> filtered = WorkoutTable.getInstance().findClientWorkouts(clientRow, Settings.getInstance().getHelper());
        //delete if workout is not finished (End timestamp == 0)
        filtered.removeIf(element -> element.getEnd() == 0);
        //create new List<Workout>
        ArrayList<Workout> toReturn = new ArrayList<>();
        //for each element in, add new Workout to List<Workout>
        filtered.forEach(element->toReturn.add(new Workout(element)));
        return toReturn;
    }

    /**
     * ToString
     * @return ClientRow Nickname
     */
    @Override
    public String toString() {
        return this.clientRow.getNickname();
    }

    /**
     * Saves Client into the Database
     * @return True if saved, False if not
     */
    @Override
    public boolean save() {
        if(clientRow == null) {return false;}
        return ClientTable.getInstance().insertClient(clientRow,Settings.getInstance().getHelper());
    }

    /**
     * Loads Client from Database
     * @param id ID of Client
     * @return True if loaded, False if not
     */
    @Override
    public boolean load(int id) {
        clientRow = ClientTable.getInstance().findClient(id, Settings.getInstance().getHelper());
        return clientRow !=null;
    }

    /**
     * Deletes Client
     * @return True if deleted, False if not
     */
    @Override
    public boolean delete() {
        return ClientTable.getInstance().deleteClient(clientRow,Settings.getInstance().getHelper());
    }
}
