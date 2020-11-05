package com.example.progress.backend.row;

import com.example.progress.backend.table.ClientTable;

public class WorkoutRow {
    private int workoutID;
    private ClientRow clientRow;
    private long start;
    private long end;
    private String name;

    public WorkoutRow(ClientRow clientRow, long start, long end, String name) {
        this.clientRow = clientRow;
        this.start = start;
        this.end = end;
        this.name = name;
    }
    public WorkoutRow(ClientRow clientRow, long start, String name) {
        this.clientRow = clientRow;
        this.start = start;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "client=" + clientRow +
                ", start=" + start +
                ", end=" + end +
                ", name='" + name + '\'' +
                '}';
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public ClientRow getClientRow() {
        return clientRow;
    }

    public void setClientRow(ClientRow clientRow) {
        this.clientRow = clientRow;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
