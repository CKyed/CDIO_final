package model;

import model.Fields.Field;
import model.Fields.Start;

public class Board {
    private Field[] fields;

    public Board(){
        fields = new Field[40];
    }

    public Field[] getFields() {
        return fields;
    }
}
