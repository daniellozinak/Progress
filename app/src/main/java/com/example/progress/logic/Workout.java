package com.example.progress.logic;


import android.annotation.SuppressLint;
import android.util.Log;

import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.logic.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Workout extends Entity {
    private WorkoutRow workoutRow = null;
    private ArrayList<Exercise> exerciseList = null;

    /**
     * Workout constructor
     */
    public Workout()
    {
        exerciseList = new ArrayList<>();
    }

    /**
     * Workout constructor
     * @param clientRow ClientRow instance, set as WorkoutRow ClientRow
     * @param start set as WorkoutRow Start
     * @param name set as WorkoutRow name
     */
    public Workout(ClientRow clientRow, long start, String name)
    {
        workoutRow = new WorkoutRow(clientRow, start, name);
        exerciseList = new ArrayList<>();
    }

    /**
     * Workout constructor
     * @param workoutRow WorkoutRow instance, set as WorkoutRow
     */
    public Workout(WorkoutRow workoutRow)
    {
        this.workoutRow = workoutRow;
    }

    /**
     * Adds ExerciseRow to WorkoutRow
     * @param type  Type of the Exercise
     * @param exerciseName  Name of the Exercise
     * @param reps  Reps
     * @param weight  Weight [kg]
     * @return
     */
    public boolean addExercise(ExerciseType type, String exerciseName, int reps, int weight) {
        if (workoutRow == null) {
            return false;
        }
        Exercise newExercise = new Exercise(this, type, exerciseName, reps, weight);
        exerciseList.add(newExercise);

        Log.d("debug", "Exercise added to list size:" + exerciseList.size());
        return true;
    }

    /**
     * Removes the Exercise
     * @param exercise Exercise instance
     * @return True if removed, False if not
     */
    public boolean removeExercise(Exercise exercise) {
        return exerciseList.remove(exercise);
    }

    /**
     * Removes the Exercise
     * @param index Exercise Index in ExerciseList
     * @return True if removed, False if not
     */
    public boolean removeExercise(int index) {
        try {
            exerciseList.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }


    /**
     *
     * @return time that passed since the Workout started in milliseconds
     */
    public long getCurrentTime() {
        assert (this.workoutRow != null);
        return (new Date().getTime() - this.workoutRow.getStart());
    }

    /**
     *
     * @return Duration of the Workout in milliseconds
     */
    public long getDuration(){ return this.workoutRow.getEnd() - this.workoutRow.getStart();}

    /**
     * WorkoutName getter
     * @return WorkoutRow Name
     */
    public String getWorkoutName() {
        assert (this.workoutRow != null);
        return this.workoutRow.getName();
    }

    /**
     *  WorkoutRow getter
     * @return  WorkoutRow instance
     */
    public WorkoutRow getWorkoutRow() {
        assert (workoutRow != null);
        return workoutRow;
    }

    /**
     * ExerciseList getter
     * @return ExerciseList instance
     */
    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }

    /**
     * Saves Workout into the Database
     * @return True if saved, False if not
     */
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

    /**
     * Loads Workout from Database
     * @param id ID of Workout
     * @return True if loaed, False if not
     */
    @Override
    public boolean load(int id) {
        this.workoutRow = WorkoutTable.getInstance().findWorkout(id, Settings.getInstance().getHelper());
        return this.workoutRow != null;
    }

    /**
     * Deletes Workout from Database
     * @return True if deleted, False if not
     */
    @Override
    public boolean delete() {
        return WorkoutTable.getInstance().deleteWorkout(workoutRow,Settings.getInstance().getHelper());
    }

    /**
     * ToString
     * @return WorkoutRow Name date: [WorkoutRow Start as Date] duration:[ Workout getDuration in hh:mm:ss format]
     */
    @Override
    public String toString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
        return this.workoutRow.getName() + " date:[" + new Date(this.workoutRow.getStart()) + "] duration:[" + sdt.format(this.getDuration()) + "]";
    }
}
