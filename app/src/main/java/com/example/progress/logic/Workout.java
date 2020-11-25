package com.example.progress.logic;


import android.annotation.SuppressLint;
import android.util.Log;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.logic.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Workout extends Entity {
    private WorkoutRow workoutRow = null;
    private ArrayList<Exercise> exerciseList = null;

    //constructors
    public Workout() {
        exerciseList = new ArrayList<>();
    }

    public Workout(ClientRow clientRow, long start, String name) {
        workoutRow = new WorkoutRow(clientRow, start, name);
        exerciseList = new ArrayList<>();
    }

    public Workout(WorkoutRow workoutRow)
    {
        this.workoutRow = workoutRow;
    }

    public boolean addExercise(ExerciseType type, String exerciseName, int reps, int weight) {
        if (workoutRow == null) {
            return false;
        }
        Exercise newExercise = new Exercise(workoutRow, type, exerciseName, reps, weight);
        exerciseList.add(newExercise);

        Log.d("debug", "Exercise added to list size:" + exerciseList.size());
        return true;
    }

    public boolean removeExercise(ExerciseRow exerciseRow) {
        return exerciseList.remove(exerciseRow);
    }

    public boolean removeExercise(int index) {
        try {
            exerciseList.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public long getDuration(){ return this.workoutRow.getEnd() - this.workoutRow.getStart();}


    //getters, setters
    public long getCurrentTime() {
        assert (this.workoutRow != null);
        return (new Date().getTime() - this.workoutRow.getStart());
    }


    public String getWorkoutName() {
        assert (this.workoutRow != null);
        return this.workoutRow.getName();
    }

    public WorkoutRow getCurrentWorkout() {
        assert (workoutRow != null);
        return workoutRow;
    }

    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }

    //Entity
    @Override
    public boolean save() {
        assert (workoutRow != null);
        //set end timestamp to workout and update the database
        workoutRow.setEnd(new Date().getTime());
        if (!WorkoutTable.getInstance().insertWorkout(workoutRow, Settings.getInstance().getHelper())) {
            return false;
        }

        //for all exercises in workout, save exercises
        for (Exercise exercise : exerciseList) {
            if (!exercise.save()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean load(int id) {
        this.workoutRow = WorkoutTable.getInstance().findWorkout(id, Settings.getInstance().getHelper());
        return this.workoutRow != null;
    }

    @Override
    public String toString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
        return this.workoutRow.getName() + " date:[" + new Date(this.workoutRow.getStart()) + "] duration:[" + sdt.format(this.getDuration()) + "]";
    }
}
