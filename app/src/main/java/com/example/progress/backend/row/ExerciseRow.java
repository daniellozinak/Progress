package com.example.progress.backend.row;


public class ExerciseRow {
    private int exerciseID;
    private WorkoutRow workoutRow;
    private String type;
    private String name;
    private int reps;
    private int weight;

    public ExerciseRow(WorkoutRow workoutRow, String type, String name, int reps,int weight) {
        this.workoutRow = workoutRow;
        this.type = type;
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }

    public ExerciseRow(int exerciseID, WorkoutRow workoutRow, String type, String name, int reps, int weight) {
        this.exerciseID = exerciseID;
        this.workoutRow = workoutRow;
        this.type = type;
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return this.name + " [" + this.type + "] " + this.reps + " x " + this.weight + "kg";
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public WorkoutRow getWorkoutRow() {
        return workoutRow;
    }

    public void setWorkoutRow(WorkoutRow workoutRow) {
        this.workoutRow = workoutRow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() { return weight; }

    public void setWeight(int weight) { this.weight = weight; }
}
