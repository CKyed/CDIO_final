package model.Fields;
import static controller.PathExpert.namePath;
import static controller.TextController.getFieldData;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;


public abstract class Field {
    private String color;
    private String name;
    private String description;
    private int id;

    public Field(String state){
        this.id = getFieldData(state,"id");
        this.name = readFile(namePath,state);
    }
}
