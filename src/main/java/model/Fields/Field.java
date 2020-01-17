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
    protected int group;

    public Field(String state){
        this.name = readFile(namePath,state);
        this.id = getFieldData(state,"id");
        this.description = readFile(descriptionMessagesPath,state);
    }

    public int getGroup(){
        return this.group;
    }
    public String getType(){
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }
}
