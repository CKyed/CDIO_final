package model.Fields;
import static controller.PathExpert.namePath;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;


public abstract class Field {
    private String color;
    private String name;
    private String description;
    private int id;

    public Field(String state){
        int[] data = getFieldData(state);

        this.name = readFile(namePath,state);
        this.id = data[1];

    }
    public int[] getFieldData(String state){
        String data = readFile(fieldAttributesPath,state);
        String[] stringData = data.split(",");
        int[] intData = new int[stringData.length];
        for (int i=0;i<stringData.length;i++){
            intData[i] = Integer.parseInt(stringData[i]);
        }
        return intData;
    }


}
