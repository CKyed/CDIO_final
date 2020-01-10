package model.Fields;
import static controller.PathExpert.*;
import static controller.TextController.getFieldData;
import static controller.TextController.readFile;


public abstract class Field {
    protected String color;
    protected String name;
    protected String description;
    protected int id;
    protected String type;

    public Field(String state){
        this.name = readFile(namePath,state);
        this.id = getFieldData(state,"id");
        this.description = readFile(descriptionMessagesPath,state);
    }

    public abstract int getGroup();
    public abstract String getType();

    public String getName() {
        return this.name;
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
