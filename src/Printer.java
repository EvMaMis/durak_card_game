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

    public void printTable(Table table, Card trump) {
        System.out.println("\n----------------------TABLE----------------------");
        printPartOfTable(table.getCardsDefense());
        printCard(trump);
        printPartOfTable(table.getCardsAttack());
        System.out.println("\n-------------------------------------------------");
    }

    private void printPartOfTable(ArrayList<Card> cards) {
        System.out.println();
        for(Card card:cards){
            System.out.printf("%-8s", "_____");
        }
        System.out.println();
        for(Card card:cards){
            System.out.printf(" %3s    ", suitsChars.get(card.getSuit()) + valuesChars.get(card.getValue()));
        }
        System.out.println();
        for(Card card:cards){
            System.out.printf("%-8s", "-----");
        }
        System.out.println();
    }


    public void printCard(Card card) {
        System.out.printf("%50s","-----");
        System.out.println();
        System.out.printf("%50s", suitsChars.get(card.getSuit()) + valuesChars.get(card.getValue()));
        System.out.println();
        System.out.printf("%50s","-----");
    }

    public void printGame(Opponent opponent, Table table, Card trump, Hand yourHand) {
        printHand(opponent.getHand(), "Opponent's");
        printTable(table, trump);
        printHand(yourHand, "Your");
        System.out.println("Choose what to play");
    }
}
