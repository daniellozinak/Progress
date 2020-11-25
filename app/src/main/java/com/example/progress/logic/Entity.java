package com.example.progress.logic;

public abstract class Entity {

    /**
     * Saves Entity to Database
     * @return true if Entity was saved, false if not
     */
    public abstract boolean save();

    /**
     * Loads Entity from Database
     * @param id ID of Entity
     * @return true if Entity was found, false if not
     */
    public abstract boolean load(int id);

    /**
     * Deletes Enetity from Database
     * @return true if Entity was deleted, false if not
     */
    public abstract boolean delete();

}
