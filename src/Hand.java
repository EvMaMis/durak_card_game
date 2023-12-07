import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Card playCard(int index) {
        try {
            Card cardToReturn = cards.get(index);
            cards.remove(index);
            return cardToReturn;
        } catch(IndexOutOfBoundsException e) {
            System.out.println("You has wrote a wrong number");
            throw new IndexOutOfBoundsException();
        }
    }
    public void takeAll(ArrayList<Card> cardsToTake) {
        System.out.println("I'll take it");
        cards.addAll(cardsToTake);
    }


    public ArrayList<Card> getTrumps() {
        ArrayList<Card> cardsToReturn = new ArrayList<Card>();
        for(Card card:cards) {
            if(card.getSuit().getTrump()) {
                cardsToReturn.add(card);
            }
        }
        return cardsToReturn;
    }

    public void addCards(ArrayList<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }
}
