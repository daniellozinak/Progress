package com.example.progress.logic;

import android.os.Build;
import android.widget.ArrayAdapter;

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

    public Client(String nickname)
    {
        clientRow = new ClientRow(nickname);
    }

    public Client(ClientRow client)
    {
        this.clientRow = client;
    }


    public boolean startWorkout(long start,String name)
    {
        //creates new Workout object
        assert(clientRow!=null);
        currentWorkout = new Workout(this.clientRow,start,name);
        return true;
    }

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
    public boolean isCurrentWorkout() {return currentWorkout !=null;}


    //getters, setters
    public ClientRow getClientRow() {
        return clientRow;
    }

    public void setClientRow(ClientRow clientRow) {
        this.clientRow = clientRow;
    }

    public Workout getCurrentWorkout()
    {
        assert (currentWorkout!=null);
        return currentWorkout;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Client> getAllClients()
    {
        ArrayList<ClientRow> clientRows = ClientTable.getInstance().findAllClients(Settings.getInstance().getHelper());
        ArrayList<Client> clients = new ArrayList<>();
        clientRows.forEach(element-> clients.add(new Client(element)));

        return clients;
    }

    public void setCurrentWorkout(Workout currentWorkout) { this.currentWorkout = currentWorkout; }

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

    @Override
    public String toString() {
        return this.clientRow.getNickname();
    }

    //Entity
    @Override
    public boolean save() {
        assert(clientRow!=null);
        return ClientTable.getInstance().insertClient(clientRow,Settings.getInstance().getHelper());
    }

    @Override
    public boolean load(int id) {
        clientRow = ClientTable.getInstance().findClient(id, Settings.getInstance().getHelper());
        return clientRow !=null;
    }

    @Override
    public boolean delete() {
        return ClientTable.getInstance().deleteClient(clientRow,Settings.getInstance().getHelper()) > 0;
    }
}
