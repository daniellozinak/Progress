package com.example.progress.logic;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.WorkoutTable;

import java.util.Date;

public class Workout  {
    private WorkoutRow currentWorkout = null;
    private DatabaseHelper helper=null;

    public Workout(DatabaseHelper helper)
    {
        this.helper = helper;
    }

    public boolean startWorkout(ClientRow client,String name)
    {
        assert(helper!=null);
        currentWorkout = new WorkoutRow(client,new Date().getTime(),name);
        return WorkoutTable.getInstance().insertWorkout(currentWorkout,helper);
    }

    public boolean endWorkout()
    {
        assert(helper!=null);
        assert(currentWorkout!=null);
        currentWorkout.setEnd(new Date().getTime());
        int rowsEffected = WorkoutTable.getInstance().updateWorkout(currentWorkout,helper);
        if(rowsEffected > 0)
        {
            currentWorkout = null;
            return true;
        }
        return false;
    }

    public boolean addExercise()
    {
        return false;
    }

    public long getCurrentTime()
    {
        assert(this.currentWorkout != null);
        return (new Date().getTime() - this.currentWorkout.getStart());
    }

    public boolean isOngoingWorkout()
    {
        return (this.currentWorkout !=null);
    }

    public String getWorkoutName()
    {
        assert(this.currentWorkout != null);
        return this.currentWorkout.getName();
    }

}
