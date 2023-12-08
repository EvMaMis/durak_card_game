import javax.swing.*;
import java.util.ArrayList;

public class Opponent {
    private static Opponent instance;
    private Hand hand;
    private int difficulty = 1;
    private Opponent(Hand hand) {
        this.hand = hand;
    }
    public static Opponent getInstance(Hand hand) {
        if (instance == null)
            instance = new Opponent(hand);
        return instance;
    }
    public Hand getHand() {
        return hand;
    }
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Card beat(Card toBeat) throws IncapableCardException {
        ArrayList<Card> trumps = hand.getTrumps();
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
            for(Card card:hand.getCards()) {
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

    public Card attack() {
        ArrayList<Card> possibleTurn = new ArrayList<Card>();
        for(Card card:hand.getCards()) {
            if(!card.getSuit().getTrump())
                possibleTurn.add(card);
        }
        if(!possibleTurn.isEmpty())
            return getMin(possibleTurn);
        return getMin(hand.getCards());
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
}
