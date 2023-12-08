import java.util.ArrayList;

public class Table {
    private ArrayList<Card> cardsAttack;
    private ArrayList<Card> cardsDefense;

    public ArrayList<Card> getCardsAttack() {
        return cardsAttack;
    }

    public void setCardsAttack(ArrayList<Card> cardsAttack) {
        this.cardsAttack = cardsAttack;
    }

    public ArrayList<Card> getCardsDefense() {
        return cardsDefense;
    }

    public void setCardsDefense(ArrayList<Card> cardsDefense) {
        this.cardsDefense = cardsDefense;
    }

    public Table(){
        cardsAttack = new ArrayList<Card>();
        cardsDefense = new ArrayList<Card>();
    }

    public ArrayList<Card> getAll() {
        ArrayList<Card> toGet = new ArrayList<Card>();
        toGet.addAll(cardsDefense);
        toGet.addAll(cardsAttack);
        return toGet;
    }

    public void addCard(Card card, String side) {
        if(side.equals("Your"))
            cardsAttack.add(card);
        else
            cardsDefense.add(card);
    }

    public void resetTable() {
        cardsAttack.clear();
        cardsDefense.clear();
    }
}
