import java.util.ArrayList;

public class Table {
    private static Table instance;
    private ArrayList<Card> yourCards;
    private ArrayList<Card> opponentCards;


    public ArrayList<Card> getYourCards() {
        return yourCards;
    }

    public void setYourCards(ArrayList<Card> yourCards) {
        this.yourCards = yourCards;
    }

    public ArrayList<Card> getOpponentCards() {
        return opponentCards;
    }

    public void setOpponentCards(ArrayList<Card> opponentCards) {
        this.opponentCards = opponentCards;
    }

    public Table(){
        yourCards = new ArrayList<Card>();
        opponentCards = new ArrayList<Card>();
    }

    public ArrayList<Card> getAll() {
        ArrayList<Card> toGet = new ArrayList<Card>();
        toGet.addAll(opponentCards);
        toGet.addAll(yourCards);
        return toGet;
    }

    public void addCard(Card card, String side) {
        if(side.equals("Your"))
            yourCards.add(card);
        else
            opponentCards.add(card);
    }

    public void resetTable() {
        yourCards.clear();
        opponentCards.clear();
    }
}
