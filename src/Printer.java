import java.util.ArrayList;
import java.util.HashMap;

public class Printer {
    private HashMap<Value, String> valuesChars;
    private HashMap<Suit, String> suitsChars;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
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
        if(owner.equals("Opponent's"))
            System.out.println("Cards available " + hand.getCards().size());
        else {

            for (Card card : hand.getCards()) {
                System.out.print(valuesChars.get(card.getValue()) + suitsChars.get(card.getSuit()) + "\t");
            }
            System.out.println();
            if (owner.equals("Your")) {
                for (int i = 1; i <= hand.getCards().size(); i++)
                    System.out.printf("%-4s", i);
                System.out.println(ANSI_RED + " 0 - End turn " + ANSI_RESET + "<--- Controls \n");
            }
        }
    }

    public void printTable(Table table, Card trump, int cardsLeft) {
        System.out.println("\n----------------------TABLE----------------------");
        printPartOfTable(table.getOpponentCards());
        printCard(trump, cardsLeft);
        printPartOfTable(table.getYourCards());
        System.out.println("\n-------------------------------------------------");
    }

    public void printTake() {
        System.out.println("========================");
        System.out.println(ANSI_RED + "CARDS BEEN TAKEN" + ANSI_RESET);
        System.out.println("========================");

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


    public void printCard(Card card, int cardsLeft) {
        System.out.printf("%50s","-----");
        System.out.println();
        System.out.printf("%50s", suitsChars.get(card.getSuit()) + valuesChars.get(card.getValue()));
        System.out.printf("   Deck: " + cardsLeft);
        System.out.println();
        System.out.printf("%50s","-----");
    }

    public void printGame(Table table, Opponent opponent, Hand yourHand, Deck deck, Card trump) {
        System.out.print("\033\143");
        printHand(opponent.getHand(), "Opponent's");
        printTable(table, trump, deck.getSize());
        printHand(yourHand, "Your");
        System.out.println(ANSI_GREEN + "Choose what to play" + ANSI_RESET);
    }

    public void printResults(Hand yourHand, Opponent opponent) {
        if(yourHand.isEmpty() && opponent.getHand().isEmpty()) {
            System.out.println("DRAW!");
        } else if (yourHand.isEmpty() && !opponent.getHand().isEmpty()) {
            System.out.println(ANSI_GREEN + "YOU WON!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "YOU LOSE!" + ANSI_RESET);
        }
    }

    public void printException(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
}
