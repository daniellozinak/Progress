package com.example.progress.backend.row;

public class WorkoutRow {
    private int workoutID;
    private ClientRow clientRow;
    private long start;
    private long end;
    private String name;

    /**
     * WorkoutRow constructor
     * @param clientRow set as WorkoutRow ClientRow
     * @param start set as WorkoutRow Start
     * @param end set as WorkoutRow End
     * @param name set as WorkoutRow Name
     */
    public WorkoutRow(ClientRow clientRow, long start, long end, String name) {
        this.clientRow = clientRow;
        this.start = start;
        this.end = end;
        this.name = name;
    }

    /**
     * WorkoutRow constructor
     * @param clientRow set as WorkoutRow ClientRow
     * @param start set as WorkoutRow Start
     * @param name set as WorkoutRow Name
     */
    public WorkoutRow(ClientRow clientRow, long start, String name) {
        this.clientRow = clientRow;
        this.start = start;
        this.name = name;
    }

    /**
     * Workout ToString
     * @return  Workout {Client=,Start=,End=,Name=}
     */
    @Override
    public String toString() {
        return "Workout{" +
                "Client=" + clientRow +
                ", Start=" + start +
                ", End=" + end +
                ", Name='" + name + '\'' +
                '}';
    }

    /**
     * WorkoutRow ID setter
     * @param workoutID set as WorkoutRow ID
     */
    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    /**
     * WorkoutRow ID getter
     * @return  WorkoutRow ID
     */
    public int getWorkoutID() {
        return workoutID;
    }

    /**
     * WorkoutRow ClientRow getter
     * @return  WorkoutRow ClientRow
     */
    public ClientRow getClientRow() {
        return clientRow;
    }

    /**
     * WorkoutRow ClientRow setter
     * @param clientRow set as WorkoutRow ClientRow
     */
    public void setClientRow(ClientRow clientRow) {
        this.clientRow = clientRow;
    }

    /**
     * WorkoutRow Start getter
     * @return  WorkoutRow Start
     */
    public long getStart() {
        return start;
    }

    /**
     * WorkoutRow Start setter
     * @param start set as WorkoutRow Start
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * WorkoutRow End getter
     * @return  WorkoutRow End
     */
    public long getEnd() {
        return end;
    }

    /**
     * WorkoutRow End setter
     * @param end set as WorkoutRow End
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * WorkoutRow Name getter
     * @return  WorkoutRow Name
     */
    public String getName() {
        return name;
    }

    /**
     * WorkoutRow Name setter
     * @param name set as WorkoutRow Name
     */
    public void setName(String name) {
        this.name = name;
    }
}
