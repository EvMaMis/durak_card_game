import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getSize() {
        return cards.size();
    }

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
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

    public void playCard(Card card) {
        try {
            cards.remove(card);
        } catch(Exception e) {
            System.out.println("No such card");
            e.printStackTrace();
        }
    }

    public void takeAll(ArrayList<Card> cardsToTake) {
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
