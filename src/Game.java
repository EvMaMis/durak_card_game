import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    static private final Deck currentDeck = new Deck();
    static private boolean yourTurn = true;
    static private Opponent opponent;
    static private Table table = new Table();

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
                    table.getCardsAttack().add(playedCard);
                    System.out.println("You played: ");
                    printer.printCard(playedCard);
                    try {
                        Card beat = opponent.beat(playedCard);
                        System.out.println("Enemy played: ");
                        table.getCardsDefense().add(beat);
                        printer.printCard(beat);
                        opponent.getHand().playCard(opponent.getHand().getCards().indexOf(beat));
                        yourTurn = !yourTurn;
                    } catch (IncapableCardException e) {
                        opponent.getHand().takeAll(table.getAll());
                    }
                } catch (Exception e) {
                    System.out.println();
                }
            } else {
                Card toBeat = opponent.attack();
                table.getCardsAttack().add(toBeat);
                opponent.getHand().getCards().remove(toBeat);
                System.out.println("Enemy played: ");
                printer.printCard(toBeat);

                while (!correctBeat) {
                    int toPlayIndex = scanner.nextInt();
                    if (toPlayIndex == 0) {
                        yourHand.takeAll(table.getAll());
                        break;
                    } else {
                        Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                        System.out.println("You played: ");
                        printer.printCard(playedCard);
                        if (checkBeat(toBeat, playedCard)) {
                            correctBeat = true;
                            yourHand.playCard(toPlayIndex - 1);
                            yourTurn = !yourTurn;
                        } else {
                            System.out.println("You have played wrong card");
                        }
                    }
                }
            }
            checkTurn(yourHand, opponent.getHand());
        }
    }

    private static void checkTurn(Hand yourCards, Hand opponentCards) {
        table.resetTable();
        int youToPull = 6 - yourCards.getCards().size();
        int oppToPull = 6 - opponentCards.getCards().size();
        if (!currentDeck.getCards().isEmpty()) {
            if (currentDeck.getCards().size() >= youToPull + oppToPull) {
                yourCards.addCards(currentDeck.pullCards(youToPull));
                opponentCards.addCards(currentDeck.pullCards(oppToPull));
                opponent.setHand(opponentCards);
            } else {
                while(!currentDeck.getCards().isEmpty()){
                    if(youToPull > 0) {
                        yourCards.addCards(currentDeck.pullCards(1));
                        youToPull--;
                    }
                    if(oppToPull > 0 && !currentDeck.getCards().isEmpty()){
                        opponentCards.addCards(currentDeck.pullCards(1));
                        opponent.setHand(opponentCards);
                        oppToPull--;
                    }
                }
            }
        }
    }
    private static boolean checkBeat(Card toBeat, Card beating) {
        if(toBeat.getSuit() == beating.getSuit()) {
            return toBeat.getValue().ordinal() < beating.getValue().ordinal();
        } else if (!toBeat.getSuit().getTrump() && beating.getSuit().getTrump()) {
            return true;
        }
        return false;
    }
}
