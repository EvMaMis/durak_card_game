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

    public ArrayList<Card> getTrumps() {
        ArrayList<Card> cardsToReturn = new ArrayList<Card>();
        for(Card card:cards) {
            if(card.getSuit().getTrump()) {
                cardsToReturn.add(card);
            }
        }
        return cardsToReturn;
    }

    public void addCards(ArrayList<Card> cardstToAdd) {
        cards.addAll(cardstToAdd);
    }

    public Card opponentBeat(Card toBeat) throws IncapableCardException {
        ArrayList<Card> trumps = getTrumps();
        if (toBeat.getSuit().getTrump()) {
            if (!trumps.isEmpty()) {
                ArrayList<Card> possibleTrumps = new ArrayList<Card>();
                for(Card trump:trumps) {
                    if(trump.getValue().ordinal() > toBeat.getValue().ordinal()){
                        possibleTrumps.add(trump);
                    }
                }
                if(!possibleTrumps.isEmpty())
                    return getMin(possibleTrumps);
                else
                    throw new IncapableCardException("Don't have card to beat");
            } else {
                throw new IncapableCardException("Don't have card to beat");
            }
        } else {
            ArrayList<Card> cardsSameSuit = new ArrayList<>();
            for(Card card:cards) {
                if(card.getSuit() == toBeat.getSuit() && card.getValue().ordinal() > toBeat.getValue().ordinal()) {
                    cardsSameSuit.add(card);
                }
            }
            if(!cardsSameSuit.isEmpty()) {
                return getMin(cardsSameSuit);
            } else if(!trumps.isEmpty()){
                return getMin(trumps);
            } else {
                throw new IncapableCardException("Don't have card to beat");
            }
        }
    }

    private Card canBeatTrump(ArrayList<Card> trumps, Card toBeat) {
        for(Card trump:trumps) {
            if(trump.getValue().ordinal() > toBeat.getValue().ordinal()) {
                return trump;
            }
        }
        return new Card(Value.EIGHT, Suit.DIAMONDS);
    }

    private Card getMin(ArrayList<Card> cardsToChoose) {
        if (!cardsToChoose.isEmpty()) {
            Card min = cardsToChoose.get(0);
            if(cardsToChoose.size() >= 2){
                for(Card card:cardsToChoose) {
                    if (card.getValue().ordinal() < min.getValue().ordinal()){
                        min = card;
                    }
                }
            }
            return min;
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    public void takeAll() {
        System.out.println("I'll take it");
    }
}
