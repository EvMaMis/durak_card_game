import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    static private final Deck currentDeck = new Deck();
    static private boolean yourTurn = true;
    static private Opponent opponent;

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        currentDeck.shuffleCards(100);
        Hand yourHand = new Hand(currentDeck.pullCards(6));
        opponent = Opponent.getInstance(new Hand(currentDeck.pullCards(6)));

        Printer printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        boolean correctBeat;

        Card trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);

        currentDeck.pushCard(trump);
        printer.printCard(trump);

        while (!yourHand.getCards().isEmpty() && !opponent.getHand().getCards().isEmpty()) {
            correctBeat = false;
            printer.printHand(opponent.getHand(), "Opponent's");
            printer.printHand(yourHand, "Your");
            if(yourTurn) {
                System.out.println("Choose what to play");
                try {
                    int toPlayIndex = scanner.nextInt();
                    Card playedCard = yourHand.playCard(toPlayIndex - 1);
                    System.out.println("You played: ");
                    printer.printCard(playedCard);
                    try {
                        Card beat = opponent.beat(playedCard);
                        System.out.println("Enemy played: ");
                        printer.printCard(beat);
                        opponent.getHand().playCard(opponent.getHand().getCards().indexOf(beat));
                        yourTurn = !yourTurn;
                    } catch (IncapableCardException e) {
                        opponent.takeAll();
                    }
                } catch (Exception e) {
                    System.out.println();
                }
            } else {
                Card toBeat = opponent.attack();
                opponent.getHand().getCards().remove(toBeat);
                System.out.println("Enemy played: ");
                printer.printCard(toBeat);

                while (!correctBeat){
                    int toPlayIndex = scanner.nextInt();
                    if(toPlayIndex == 0) {

                    }
                    Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                    System.out.println("You played: ");
                    printer.printCard(playedCard);
                    if(checkBeat(toBeat, playedCard)){
                        correctBeat = true;
                        yourHand.playCard(toPlayIndex - 1);
                    } else {
                        System.out.println("You have played wrong card");
                    }
                }
                yourTurn = !yourTurn;
            }
            checkTurn(yourHand, opponent.getHand());
        }
    }

    private static void checkTurn(Hand you, Hand opponentCards) {
        try {
            you.addCards(currentDeck.pullCards(6 - you.getCards().size()));
            opponentCards.addCards(currentDeck.pullCards(6 - opponentCards.getCards().size()));
            opponent.setHand(opponentCards);
        } catch(Exception e) {
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
