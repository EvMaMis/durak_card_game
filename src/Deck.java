import java.sql.SQLOutput;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    public ArrayList<Card> getCards() {
        return cards;
    }
    public Deck() {
        cards = new ArrayList<Card>();
        Value[] values = Value.values();
        Suit[] suits = Suit.values();
        for(Value value:values) {
            for(Suit suit:suits) {
                cards.add(new Card(value, suit));
            }
        }
    }

    public void shuffleCards(int numberOfShuffle) {
        for(int i = 0; i < numberOfShuffle; i++) {
            int first = (int) (Math.random() * 36);
            int second =(int) (Math.random() * 36);
            Card temp;
            temp = cards.get(first);
            cards.set(first, cards.get(second));
            cards.set(second, temp);
        }
    }
    public ArrayList<Card> pullCards(int numberOfPulls) {
        if(!cards.isEmpty()){
            ArrayList<Card> cardsToGet = new ArrayList<>();
            for(int i = 0; i < numberOfPulls; i++) {
                cardsToGet.add(cards.get(cards.size() -1));
                cards.remove(cards.size()-1);
            }
            return cardsToGet;
        } else {
            return null;
        }
    }

    public void pushCard(Card cardtoPush) {
        cards.add(0, cardtoPush);
    }

}
