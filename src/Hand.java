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
            return null;
        }
    }

    public void addCards(ArrayList<Card> cardstToAdd) {
        cards.addAll(cardstToAdd);
    }
}
