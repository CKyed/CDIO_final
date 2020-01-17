package controller;

import model.Board;
import model.Fields.*;
import model.Fields.OwnableFile.Brewery;
import model.Fields.OwnableFile.Ferry;
import model.Fields.OwnableFile.*;

public class BoardController {
    private Board board;
    private int[] totalNumberOfStreetsInSeries;

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

        //gets the total number of streets in each series
        this.totalNumberOfStreetsInSeries = new int[15];
        for (int i =0;i<board.getFields().length;i++){
            //Increments numberOfStreetsInSeries in the right place
            this.totalNumberOfStreetsInSeries[board.getFields()[i].getGroup()]++;
        }
    }

    public int[] getBuildableStreetIds(int playerId, int playerBalance){
        boolean[] buildableArray = new boolean[board.getFields().length];
        int counter =0;
       //Loops through all fields, checking if they are buildable
        for (int i=0;i<board.getFields().length;i++){
            buildableArray[i] = isBuildable(i,playerBalance, playerId);
            if (buildableArray[i])
                counter++;
        }
        //Makes array of size counter for the ids
        int[] buildAbleStreetIds=new int[counter];

        counter =0;
        for (int i = 0; i < board.getFields().length; i++) {
            if (buildableArray[i])
                buildAbleStreetIds[counter++] = i;
        }

        return buildAbleStreetIds;
    }

    public int[] getSellableStreetIds(int playerId){
        boolean[] sellableArray = new boolean[board.getFields().length];
        int counter =0;
        //Loops through all fields, checking if they are buildable
        for (int i=0;i<board.getFields().length;i++){
            sellableArray[i] = isHouseSellable(i,playerId);
            if (sellableArray[i])
                counter++;
        }
        //Makes array of size counter for the ids
        int[] sellAbleStreetIds=new int[counter];

        counter =0;
        for (int i = 0; i < board.getFields().length; i++) {
            if (sellableArray[i])
                sellAbleStreetIds[counter++] = i;
        }
        return sellAbleStreetIds;
    }

    public void buildHouses(int fieldId,int numberOfHouses){
        ((Street)this.board.getFields()[fieldId]).buildHouses(numberOfHouses);
        updateStreetRent(fieldId);
    }

    public void sellHouses(int fieldId,int numberOfHouses){
        ((Street)this.board.getFields()[fieldId]).removeHouses(numberOfHouses);
        updateStreetRent(fieldId);
    }

    public void updateStreetRent(int fieldId){
        //Gets the updated rent
        int houseLevel = ((Street)this.board.getFields()[fieldId]).getHouseLevel();
        int rent = ((Street)this.board.getFields()[fieldId]).getRentLevels()[houseLevel];
        if (houseLevel==0 && ownsAllInSeries(fieldId)){
            rent *=2;
        }
        //Assigns as the actual rent
        ((Street)this.board.getFields()[fieldId]).setRent(rent);
    }

    public void updateAllRents(){
        //Updates the rent of all ownables
        int rent;
        for (int i=0;i<board.getFields().length;i++){
            switch (board.getFields()[i].getType()){
                case "street":
                    updateStreetRent(i);
                    break;
                case "ferry":
                    //Gets number of ferries owned
                    int numberOfFerriesOwned = getNumberOfOwnablesOwnedInGroup(i);
                    // gets actual rent
                    rent = ((Ferry)board.getFields()[i]).getRentLevels()[numberOfFerriesOwned];

                    //sets rent
                    ((Ownable)board.getFields()[i]).setRent(rent);
                    break;
                case "brew":
                    int numberOfBreweriesOwned = getNumberOfOwnablesOwnedInGroup(i);
                    rent = ((Brewery)board.getFields()[i]).getRentLevels()[numberOfBreweriesOwned];
                    //sets rent
                    ((Ownable)board.getFields()[i]).setRent(rent);
                    break;
            }
        }
    }

    public int getNumberOfOwnablesOwnedInGroup(int fieldId){
        //Gets number of ownables in group, that is owned by the owner of the fieldId-field
        //Only returns true, if no ownables are pawned

        int numberOfOwnables =0;
        boolean correctOwner;
        boolean notBank;
        boolean notPawned;

        //loops through all fields and increments numberOfOwnables if groups and owners match
        for (int i=0;i<board.getFields().length;i++){
            //If group is the same
            if (board.getFields()[fieldId].getGroup()==board.getFields()[i].getGroup()){
                //Gets all three booleans
                correctOwner = ((Ownable)board.getFields()[fieldId]).getOwnerId()==((Ownable)board.getFields()[i]).getOwnerId();
                notBank = ((Ownable)board.getFields()[i]).getOwnerId() != -1;
                notPawned = !((Ownable)board.getFields()[i]).isPledged();

                //Only increments numberOfOwnables if all three are true
                if (correctOwner && notBank && notPawned) {
                    numberOfOwnables++;
                }
            }
        }
        return numberOfOwnables;
    }

    public boolean ownsAllInSeries(int fieldId){
        //Gets number of ownables owned in the group - only the ones that are not pawned
        int numberOwned = getNumberOfOwnablesOwnedInGroup(fieldId);

        //Checks if number is the same as totalNumber
        return numberOwned == totalNumberOfStreetsInSeries[board.getFields()[fieldId].getGroup()];
    }

    public boolean isBuildable(int fieldId, int playerBalance, int playerId){
        boolean isStreet = board.getFields()[fieldId].getType().equals("street");
        boolean hasAllInseries = false;
        boolean notHotelLevel= false;
        boolean canAfford= false;
        boolean correctOwner = false;
        boolean evenBuild= false;
        boolean notPawned = false;

        if (isStreet) {
            hasAllInseries = ownsAllInSeries(fieldId);
            notHotelLevel = ((Street) board.getFields()[fieldId]).getHouseLevel() != 5;
            canAfford = ((Street) board.getFields()[fieldId]).getHousePrice() <= playerBalance;
            correctOwner = ((Street) board.getFields()[fieldId]).getOwnerId() == playerId;
            evenBuild = evenBuild(fieldId,1);
            notPawned = !((Street) board.getFields()[fieldId]).isPledged();
        }

        //Only returns true, if all 5 conditions are satisfied
        return isStreet && hasAllInseries && notHotelLevel && canAfford && evenBuild && correctOwner && notPawned;
    }

    public boolean isHouseSellable(int fieldId, int playerId){
        boolean isStreet = board.getFields()[fieldId].getType().equals("street");
        boolean correctOwner = false;
        boolean hasAllInseries = false;
        boolean notHouse0 = false;
        boolean evenBuild= false;

        if (isStreet) {
            hasAllInseries = ownsAllInSeries(fieldId);
            notHouse0 = ((Street) board.getFields()[fieldId]).getHouseLevel() != 0;
            correctOwner = ((Street) board.getFields()[fieldId]).getOwnerId() == playerId;
            evenBuild = evenBuild(fieldId,-1);
        }

        //Only returns true, if all 5 conditions are satisfied
        return isStreet && hasAllInseries && notHouse0 && evenBuild && correctOwner;
    }

    public int[] getPawnableOrUnpawnableStreetIds(int playerId, boolean pawnable, int playerBalance){
        //pawnable==true means get pawnableStreetIds
        //pawnable==false means get unpawnableStreetIds

        //initializing
        boolean owned;
        boolean correctPawnStatus;
        boolean noHousesBuiltInSeries;
        boolean canAfford;
        int numberOfPawnables=0;
        boolean[] streetsPawnable = new boolean[board.getFields().length];

        //runs through all fields and saves their pawnable-boolean in streetsPawnable (array)
        for (int i = 0; i <board.getFields().length ; i++) {
            streetsPawnable[i] = false;
            noHousesBuiltInSeries =true;
            owned = false;
            correctPawnStatus = false;
            canAfford = true;
            //If it is an ownable
            if (board.getFields()[i].getType()=="street" || board.getFields()[i].getType()=="ferry" || board.getFields()[i].getType()=="brew"){
                owned = ((Ownable)board.getFields()[i]).getOwnerId()==playerId;
                //Only changes canAfford if we are looking for unPawning -not pawnning
                if (!pawnable)
                canAfford = playerBalance >= (int)((((Ownable)board.getFields()[i]).getPrice()/2)*1.1);

                //only checks if houses are built, if it is a street
                if (board.getFields()[i].getType()=="street"){
                    noHousesBuiltInSeries = noHousesBuiltInSeries(i);
                }

                //If the pawning-status of the ownable is the same as the pawnable variable, correctPawnStatus==false
                //If the pawning-status of the ownable is different from the pawnable variable, correctPawnStatus==true
                correctPawnStatus = ! (((Ownable) board.getFields()[i]).isPledged() == pawnable);
            }
            //If it is owned and not already pawned and player can afford
            if (owned && correctPawnStatus && noHousesBuiltInSeries && canAfford){
                //increments number of pawnables
                numberOfPawnables++;
                streetsPawnable[i] = true;
            }
        }

        //initializes array of correct size
        int[] pawnableOrUnpawnableStreetIds = new int[numberOfPawnables];
        int counter =0;

        //Puts the correct ids in the pawnableStreetIds array
        for (int i = 0; i < board.getFields().length; i++) {
            if (streetsPawnable[i]){
                pawnableOrUnpawnableStreetIds[counter] = i;
                counter++;
            }
        }

        return pawnableOrUnpawnableStreetIds;
    }

    public boolean evenBuild(int fieldId, int houseIncrement){
        //Returns true if the increment in number of houses is allowed
        boolean allowed = true;

        //Makes an array same length as number of streets in the group
        int streetGroup = board.getFields()[fieldId].getGroup();
        int[] houseLevelsInGroup = new int[totalNumberOfStreetsInSeries[streetGroup]];

        int counter =0;

        //Loops through all fields
        for (int i = 0; i < board.getFields().length; i++) {

            if (board.getFields()[i].getGroup()==streetGroup){ //If it is correct group
                //Gets house level
                houseLevelsInGroup[counter] = ((Street) board.getFields()[i]).getHouseLevel();
                if (fieldId==i){ //If it is the field called in the method, adds increment
                    houseLevelsInGroup[counter] += houseIncrement;
                }
                counter++;
            }
        }

        //houseLevelsInGroup now contains houselevels for the desired build
        //build is not allowed, if the difference between any two houseLevels is greater than 1

        int houseLevelDiff;
        for (int i = 0; i < houseLevelsInGroup.length; i++) {
            for (int j = 0; j < houseLevelsInGroup.length; j++) {
                houseLevelDiff = Math.abs(houseLevelsInGroup[i]-houseLevelsInGroup[j]);
                if(houseLevelDiff>1)
                    allowed=false;
            }
        }

        return allowed;
    }


    public boolean noHousesBuiltInSeries(int fieldId){
        int groupId = board.getFields()[fieldId].getGroup();
        int errors =0;
        for (int i = 0; i < board.getFields().length; i++) {
            //If it is street
            if (board.getFields()[i].getType().equals("street")){
                //If it is in correct group
                if (board.getFields()[i].getGroup()==groupId){
                    //If house level is not 0, it is an error
                    if (((Street)board.getFields()[i]).getHouseLevel() !=0){
                        errors++;
                    }
                }
            }
        }
        //Returns true if there were no errors
        return errors==0;
    }
}