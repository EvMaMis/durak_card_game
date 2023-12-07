import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    static private final Deck currentDeck = new Deck();
    static private boolean yourTurn = true;

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        currentDeck.shuffleCards(100);
        Hand yourHand = new Hand(currentDeck.pullCards(6));
        Hand opponentsHand = new Hand(currentDeck.pullCards(6));
        Printer printer = new Printer();
        Scanner scanner = new Scanner(System.in);

        Card trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);
        currentDeck.pushCard(trump);
        printer.printCard(trump);

        while (!yourHand.getCards().isEmpty() && !opponentsHand.getCards().isEmpty()) {
            printer.printHand(opponentsHand, "Opponent's");
            printer.printHand(yourHand, "Your");
            if(yourTurn) {
                System.out.println("Choose what to play");
                try {
                    int toPlayIndex = scanner.nextInt();
                    Card playedCard = yourHand.playCard(toPlayIndex - 1);
                    System.out.println("You played: ");
                    printer.printCard(playedCard);
                    try {
                        Card beat = opponentsHand.opponentBeat(playedCard);
                        System.out.println("Enemy played: ");
                        printer.printCard(beat);
                        opponentsHand.playCard(opponentsHand.getCards().indexOf(beat));
                        yourTurn = !yourTurn;
                    } catch (IncapableCardException e) {
                        opponentsHand.takeAll();
                    }
                    checkTurn(yourHand, opponentsHand);
                } catch (Exception e) {
                    System.out.println();
                }
            } else {
                Card toBeat = opponentsHand.opponentAttack();
                printer.printCard(toBeat);
                yourTurn = !yourTurn;
            }
        }
    }

    private static void checkTurn(Hand you, Hand opponent) {
        try {
            you.addCards(currentDeck.pullCards(6 - you.getCards().size()));
            opponent.addCards(currentDeck.pullCards(6 - opponent.getCards().size()));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private static boolean checkBeat(Card toBeat, Card beating) {
        if(toBeat.getSuit() == beating.getSuit() || toBeat.getSuit().getTrump() && beating.getSuit().getTrump()) {
            return toBeat.getValue().ordinal() > beating.getValue().ordinal();
        } else if (toBeat.getSuit().getTrump() && !beating.getSuit().getTrump()) {
            return true;
        }
        return false;
    }
}
