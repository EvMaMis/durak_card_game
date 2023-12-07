import java.util.ArrayList;

public class Game {
    static private Deck currentDeck = new Deck();

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        currentDeck.shuffleCards(100);
        Hand yourHand = new Hand(currentDeck.pullCards(6));
        Hand opponentsHand = new Hand(currentDeck.pullCards(6));
        Printer printer = new Printer();

        Card trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);
        currentDeck.pushCard(trump);



        while (!currentDeck.getCards().isEmpty()) {
            yourHand.playCard(0);
            checkTurn(yourHand, opponentsHand);
        }
        printer.printDeck(currentDeck);
    }

    private static void checkTurn(Hand you, Hand opponent) {
        try {
            you.addCards(amountCheck(you));
            opponent.addCards(amountCheck(opponent));
        } catch(Exception e){

        }
    }
    private static ArrayList<Card> amountCheck(Hand hand) {
        ArrayList<Card> cards = currentDeck.pullCards(6 - hand.getCards().size());
        return cards;
    }
}
