package com.example.progress.logic;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ExerciseTable;

public class Exercise {
    private ExerciseRow exerciseRow;

    public Exercise(WorkoutRow workoutRow,ExerciseType type,String name,int reps,int weight)
    {
        exerciseRow = new ExerciseRow(workoutRow,type.toString(),name,reps,weight);
    }

    public ExerciseRow getExerciseRow() {
        return exerciseRow;
    }

    public boolean insertExercise(DatabaseHelper helper)
    {
        assert (exerciseRow!=null);
        return ExerciseTable.getInstance().insertExercise(exerciseRow,helper);
    }

    public void setExerciseRow(ExerciseRow exerciseRow) {
        this.exerciseRow = exerciseRow;
    }

    @Override
    public String toString() {
        return exerciseRow.toString();
    }
}
