import java.sql.SQLOutput;
import java.util.ArrayList;
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
    public void printHand(Hand hand, String owner) {
        System.out.println("----- " + owner + " Hand -----");
        for (Card card : hand.getCards()) {
            System.out.print(valuesChars.get(card.getValue()) + suitsChars.get(card.getSuit()) + "\t");
        }
        System.out.println();
        if(owner.equals("Your")) {

            for(int i = 1; i <= hand.getCards().size(); i++)
                System.out.printf("%-4s", i);
            System.out.println(" 0 - Take cards <--- Controls \n");
        }
    }
    public void printDeck(Deck deck) {
        System.out.println(deck.getCards().size());
        for(int i = 0; i < deck.getCards().size(); i++) {
            System.out.println(deck.getCards().get(i));
        }
    }

    public void printCard(Card card) {
        System.out.print("-----\n|" + valuesChars.get(card.getValue()) + suitsChars.get(card.getSuit()) + " |\n-----\n");
    }
}
