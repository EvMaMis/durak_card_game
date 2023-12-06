import java.sql.SQLOutput;
import java.util.HashMap;

public class Printer {
    private HashMap<Value, String> valuesChars;
    private HashMap<Suit, String> suitsChars;
    public Printer() {
        valuesChars = new HashMap<>();
        suitsChars = new HashMap<>();
        String[] values = "6 7 8 9 10 J Q K A".split(" ");
        String[] suits = "♦ ♥ ♣ ♠".split(" ");
        int i = 0;
        for(Value value:Value.values()){
            valuesChars.put(value, values[i]);
            i++;
        }
        i = 0;
        for(Suit suit:Suit.values()) {
            suitsChars.put(suit, suits[i]);
            i++;
        }
    }
    public void printCards(Hand hand) {
        for(Card card:hand.getCards()){
            System.out.println(valuesChars.get(card.getValue()) + " " + suitsChars.get(card.getSuit()));
        }
    }
}
