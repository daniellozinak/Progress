package com.example.progress.logic;


import android.util.Log;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.backend.table.WorkoutTable;

import java.util.ArrayList;
import java.util.Date;

public class Workout  {
    private WorkoutRow currentWorkout = null;
    private DatabaseHelper helper=null;
    private ArrayList<Exercise> exerciseList = null;

    public Workout(DatabaseHelper helper)
    {
        this.helper = helper;
        exerciseList = new ArrayList<>();
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

        for(Exercise exercise : exerciseList)
        {
            if(!exercise.insertExercise(helper)) {return false;}
        }
        this.exerciseList.clear();


        if(rowsEffected > 0)
        {
            currentWorkout = null;
            return true;
        }
        return false;
    }


    public boolean addExercise(ExerciseType type,String exerciseName,int reps)
    {
        if(!isOngoingWorkout()){return false;}
        Exercise newExercise = new Exercise(currentWorkout,type,exerciseName,reps);
        exerciseList.add(newExercise);
        Log.d("debug","Exercise added to list size:" + exerciseList.size());

        return true;
    }

    public boolean removeExercise(ExerciseRow exerciseRow)
    {
        return exerciseList.remove(exerciseRow);
    }

    public boolean removeExercise(int index)
    {
        try{
            exerciseList.remove(index);
        }
        catch(IndexOutOfBoundsException e)
        {
            return  false;
        }
        return true;
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

    public WorkoutRow getCurrentWorkout ()
    {
        assert(currentWorkout!=null);
        return currentWorkout;
    }

    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }
}
