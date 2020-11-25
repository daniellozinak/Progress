package com.example.progress.logic;

import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.logic.settings.Settings;

public class Exercise extends Entity {
    private ExerciseRow exerciseRow;

    public Exercise(WorkoutRow workoutRow,ExerciseType type,String name,int reps,int weight)
    {
        exerciseRow = new ExerciseRow(workoutRow,type.toString(),name,reps,weight);
    }

    public ExerciseRow getExerciseRow() {
        return exerciseRow;
    }
    public void setExerciseRow(ExerciseRow exerciseRow) {
        this.exerciseRow = exerciseRow;
    }

    //save ExerciseRow to database
    @Override
    public boolean save()
    {
        assert (exerciseRow!=null);
        return ExerciseTable.getInstance().insertExercise(exerciseRow, Settings.getInstance().getHelper());
    }

    @Override
    public boolean load(int id) {
        exerciseRow = ExerciseTable.getInstance().findExercise(id,Settings.getInstance().getHelper());
        return exerciseRow != null;
    }

    @Override
    public boolean delete() {
        return ExerciseTable.getInstance().deleteExercise(exerciseRow,Settings.getInstance().getHelper()) > 0;
    }

    @Override
    public String toString() {
        return exerciseRow.toString();
    }
}
