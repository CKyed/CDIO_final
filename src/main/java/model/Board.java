package model;

import model.Fields.Field;
import model.Fields.Start;

public class Board {
    private Field[] fields;

    /**
     * Makes an array that consists of 40 fields
     */
    public Board(){
        fields = new Field[40];
    }

    public Field[] getFields() {
        return fields;
    }
}
