import java.util.ArrayList;

public class Table {
    private ArrayList<Card> cardsAttack;
    private ArrayList<Card> cardsDefense;
    private Card currentCard;

    public Table(){
        cardsAttack = new ArrayList<Card>();
        cardsDefense = new ArrayList<Card>();
    }
}
