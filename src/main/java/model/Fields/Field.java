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

    /**
     * Fields take state as parameter from the namePath file, and is able to return name and id
     * @param state
     */
    public Field(String state){
        this.name = readFile(namePath,state);
        this.id = getFieldData(state,"id");
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
