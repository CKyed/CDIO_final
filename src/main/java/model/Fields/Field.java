package model.Fields;
import static Utilities.PathExpert.*;
import static Utilities.FileReader.getFieldData;
import static Utilities.FileReader.readFile;


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

    public abstract int getGroup(); //TODO lav attribut group her, som de andre kan nedarve
    public abstract String getType(); //TODO lav attribut group her, som de andre kan nedarve

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
