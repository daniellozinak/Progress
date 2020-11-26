package com.example.progress.logic;

import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.logic.settings.Settings;

public class Exercise extends Entity {
    private ExerciseRow exerciseRow;

    /**
     * Exercise constructor
     * @param workout Workout instance
     * @param type  Type of Exercise
     * @param name  Name of Exercise
     * @param reps  Reps
     * @param weight  Weight [kg]
     */
    public Exercise(Workout workout,ExerciseType type,String name,int reps,int weight)
    {
        exerciseRow = new ExerciseRow(workout.getWorkoutRow(),type.toString(),name,reps,weight);
    }

    /**
     * ExerciseRow getter
     * @return ExerciseRow instance
     */
    public ExerciseRow getExerciseRow() {
        return exerciseRow;
    }

    /**
     * ExerciseRow setter
     * @param exerciseRow ExerciseRow instance, set as Exercise ExerciseRow
     */
    public void setExerciseRow(ExerciseRow exerciseRow) {
        this.exerciseRow = exerciseRow;
    }

    /**
     * Saved Exercise into the Database
     * @return True if saved, False if not
     */
    @Override
    public boolean save()
    {
        assert (exerciseRow!=null);
        return ExerciseTable.getInstance().insertExercise(exerciseRow, Settings.getInstance().getHelper());
    }

    /**
     * Loads Exercise from the Database
     * @param id ID of Exercise
     * @return True if loaded, False if not
     */
    @Override
    public boolean load(int id) {
        exerciseRow = ExerciseTable.getInstance().findExercise(id,Settings.getInstance().getHelper());
        return exerciseRow != null;
    }

    /**
     * Deletes Exercise From Database
     * @return True if deleted, False if not
     */
    @Override
    public boolean delete() {
        return ExerciseTable.getInstance().deleteExercise(exerciseRow,Settings.getInstance().getHelper());
    }

    /**
     * ToString
     * @return ExerciseRow
     */
    @Override
    public String toString() {
        return exerciseRow.toString();
    }
}
