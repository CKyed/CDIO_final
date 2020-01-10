package controller;

import model.Board;
import model.Fields.*;
import model.Fields.OwnableFile.Brewery;
import model.Fields.OwnableFile.Ferry;
import model.Fields.OwnableFile.Street;

public class BoardController {
    private Board board;


    public BoardController(){
        this.board = new Board();
        setup();
    }

    public Board getBoard() {
        return board;
    }

    public void setup(){
        this.board.getFields()[0] = new Start("start");
        this.board.getFields()[1] = new Street("field01");
        this.board.getFields()[2] = new Chance("chance01");
        this.board.getFields()[3] = new Street("field02");
        this.board.getFields()[4] = new IncomeTax("tax01");
        this.board.getFields()[5] = new Ferry("ferry01");
        this.board.getFields()[6] = new Street("field03");
        this.board.getFields()[7] = new Chance("chance02");
        this.board.getFields()[8] = new Street("field04");
        this.board.getFields()[9] = new Street("field05");
        this.board.getFields()[10] = new Visit("visit");
        this.board.getFields()[11] = new Street("field06");
        this.board.getFields()[12] = new Brewery("brew01");
        this.board.getFields()[13] = new Street("field07");
        this.board.getFields()[14] = new Street("field08");
        this.board.getFields()[15] = new Ferry("ferry02");
        this.board.getFields()[16] = new Street("field09");
        this.board.getFields()[17] = new Chance("chance03");
        this.board.getFields()[18] = new Street("field10");
        this.board.getFields()[19] = new Street("field11");
        this.board.getFields()[20] = new Parking("carpark");
        this.board.getFields()[21] = new Street("field12");
        this.board.getFields()[22] = new Chance("chance04");
        this.board.getFields()[23] = new Street("field13");
        this.board.getFields()[24] = new Street("field14");
        this.board.getFields()[25] = new Ferry("ferry03");
        this.board.getFields()[26] = new Street("field15");
        this.board.getFields()[27] = new Street("field16");
        this.board.getFields()[28] = new Brewery("brew02");
        this.board.getFields()[29] = new Street("field17");
        this.board.getFields()[30] = new Prison("prison");
        this.board.getFields()[31] = new Street("field18");
        this.board.getFields()[32] = new Street("field19");
        this.board.getFields()[33] = new Chance("chance05");
        this.board.getFields()[34] = new Street("field20");
        this.board.getFields()[35] = new Ferry("ferry04");
        this.board.getFields()[36] = new Chance("chance06");
        this.board.getFields()[37] = new Street("field21");
        this.board.getFields()[38] = new OrdinaryTax("tax02");
        this.board.getFields()[39] = new Street("field22");
    }

    public int[] getStreetIdsOwnedByPlayer(int playerId){
        int numberOfStreetsOwned=0;

        //counts how many streets player owns
        for(int i=0;i<board.getFields().length;i++){
            if (board.getFields()[i].getType()=="street"){
                if (((Street)board.getFields()[i]).getOwnerId()==playerId){
                    numberOfStreetsOwned++;
                }

            }
        }
        int[] streetsOwnedIds = new int[numberOfStreetsOwned];

        //Same loop again to add id's to the array
        int counter =0;
        for(int i=0;i<board.getFields().length;i++){
            if (board.getFields()[i].getType()=="street"){
                if (((Street)board.getFields()[i]).getOwnerId()==playerId){
                    streetsOwnedIds[counter] = i;
                    counter++;
                }
            }
        }
        return streetsOwnedIds;
    }

}
