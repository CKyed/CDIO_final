package model;

import model.Fields.Field;
import model.Fields.Start;

public class Board {
    private Field[] fields;

    public Board(){
        fields = new Field[40];

        for (int i=0; i <40;i++){
            fields[i] = new Start("start");

        }

    }

    public Field[] getFields() {
        return fields;
    }
}
