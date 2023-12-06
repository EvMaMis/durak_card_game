import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void printHand() {
        for(Card card: cards) {
            System.out.println(card);
        }
    }
}
