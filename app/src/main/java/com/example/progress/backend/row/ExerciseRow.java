package com.example.progress.backend.row;


public class ExerciseRow {
    private int exerciseID;
    private WorkoutRow workoutRow;
    private String type;
    private String name;
    private int reps;
    private int weight;

    /**
     * ExerciseRow constructor
     * @param workoutRow set as ExerciseRow WorkoutRow
     * @param type set as ExerciseRow Type
     * @param name set as ExerciseRow Name
     * @param reps set as ExerciseRow Reps
     * @param weight set as ExerciseRow Weight
     */
    public ExerciseRow(WorkoutRow workoutRow, String type, String name, int reps,int weight) {
        this.workoutRow = workoutRow;
        this.type = type;
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * ExerciseRow constructor
     * @param exerciseID set as ExerciseRow ID
     * @param workoutRow set as ExerciseRow WorkoutRow
     * @param type set as ExerciseRow Type
     * @param name set as ExerciseRow Name
     * @param reps set as ExerciseRow Reps
     * @param weight set as ExerciseRow Weight
     */
    public ExerciseRow(int exerciseID, WorkoutRow workoutRow, String type, String name, int reps, int weight) {
        this.exerciseID = exerciseID;
        this.workoutRow = workoutRow;
        this.type = type;
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * ExerciseRow ToString
     * @return  Name [Type] Reps x Weight [kg]
     */
    @Override
    public String toString() { return this.name + " [" + this.type + "] " + this.reps + " x " + this.weight + "kg"; }

    /**
     * ExerciseRow Reps getter
     * @return  ExerciseRow Reps
     */
    public int getReps() { return reps; }

    /**
     * ExerciseRow Reps setter
     * @param reps set as ExerciseRow Reps
     */
    public void setReps(int reps) { this.reps = reps; }

    /**
     * ExerciseRow ID getter
     * @return  ExerciseRow ID
     */
    public int getExerciseID() { return exerciseID; }

    /**
     * ExerciseRow ID setter
     * @param exerciseID set as ExerciseRow ID
     */
    public void setExerciseID(int exerciseID) { this.exerciseID = exerciseID; }

    /**
     * ExerciseRow WorkoutRow getter
     * @return  ExerciseRow WorkoutRow
     */
    public WorkoutRow getWorkoutRow() { return workoutRow; }

    /**
     * ExerciseRow WorkoutRow setter
     * @param workoutRow set as ExerciseRow WorkoutRow
     */
    public void setWorkoutRow(WorkoutRow workoutRow) { this.workoutRow = workoutRow; }

    /**
     * ExerciseRow Type getter
     * @return  ExerciseRow Type
     */
    public String getType() { return type; }

    /**
     * ExerciseRow Type setter
     * @param type set as ExerciseRow Type
     */
    public void setType(String type) { this.type = type; }

    /**
     * ExerciseRow Name getter
     * @return  ExerciseRow Name
     */
    public String getName() { return name; }

    /**
     * ExerciseRow Name setter
     * @param name set as ExerciseRow Name
     */
    public void setName(String name) { this.name = name; }

    /**
     * ExerciseRow Weight getter
     * @return  ExerciseRow Weight
     */
    public int getWeight() { return weight; }

    /**
     * ExerciseRow Weight setter
     * @param weight set as ExerciseRow Weight
     */
    public void setWeight(int weight) { this.weight = weight; }
}
