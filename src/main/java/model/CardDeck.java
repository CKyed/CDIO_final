package model;

public class CardDeck {
    private ChanceCard[] chanceCards;

    //Takes a String array of chance card texts and an int number, and creates the chance card objects.
    public CardDeck(String[] chanceCardTexts,int numberOfCards){
        for (int i=0;i<numberOfCards;i++){
            chanceCards[i] = new ChanceCard(chanceCardTexts[i],i);
        }
    }

    //takes two chance card and swaps there position in the chance card array
    public void swap(int a, int b){
        ChanceCard cardA = chanceCards[a];
        ChanceCard cardB = chanceCards[b];
        chanceCards[a] = cardB;
        chanceCards[b] = cardA;
    }

    //Shuffels the chance card deck
    public void shuffle(){
        for (int i=0; i<1000; i++ ){
            int a= (int) (Math.random()*chanceCards.length);
            int b= (int) (Math.random()*chanceCards.length);
            swap(a,b);
        }
    }

    //Draws the card in the top, and after that puts it in the bottom
    public ChanceCard draw(){
        ChanceCard upper= chanceCards[0];
        for (int i=0; i<chanceCards.length-1;i++){
            chanceCards[i] =chanceCards [i+1];
        }
        chanceCards[chanceCards.length-1]=upper;
        return upper;
    }

    public ChanceCard[] getChanceCards() {
        return chanceCards;
    }
}