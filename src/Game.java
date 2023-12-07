import java.util.ArrayList;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        Card trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);
        currentDeck.pushCard(trump);
        System.out.println(trump.getSuit());

        System.out.println(opponentsHand.getTrumps());

        while (!yourHand.getCards().isEmpty() && !opponentsHand.getCards().isEmpty()) {
            printer.printHand(opponentsHand, "Opponent's");
            printer.printHand(yourHand, "Your");
            System.out.println("Choose what to play");
            try{
                int toPlayIndex = scanner.nextInt();
                Card playedCard = yourHand.playCard(toPlayIndex-1);
                try {
                    Card beat = opponentsHand.opponentBeat(playedCard);
                    System.out.println("You have played");
                    opponentsHand.playCard(opponentsHand.getCards().indexOf(beat));
                    System.out.println("Bro beats");
                } catch(IncapableCardException e) {
                    opponentsHand.takeAll();
                }
                checkTurn(yourHand, opponentsHand);
            } catch (Exception e){
                System.out.println();
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
