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

        Card trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);

        currentDeck.pushCard(trump);
        printer.printCard(trump);

        while (!yourHand.getCards().isEmpty() && !opponent.getHand().getCards().isEmpty()) {

            if (yourTurn) {
                yourTurn = yourTurnPlay(scanner, yourHand, printer);
            } else {
                yourTurn = opponentTurnPlay(scanner, yourHand, printer);
            }
            checkTurn(yourHand, opponent.getHand());
        }
        getResults(yourHand, opponent.getHand());
    }

    private static boolean yourTurnPlay(Scanner scanner, Hand yourHand, Printer printer) {
        boolean correctBeat = false;
        while (!correctBeat) {
            printer.printHand(opponent.getHand(), "Opponent's");
            printer.printHand(yourHand, "Your");
            System.out.println("Choose what to play");
            int toPlayIndex;
            try {
                toPlayIndex = Integer.parseInt(scanner.nextLine());
                if (toPlayIndex == 0){
                    correctBeat = true;
                } else if(toPlayIndex <= yourHand.getCards().size() && toPlayIndex > 0){
                    Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                    if (table.getCardsAttack().isEmpty() || canBeTossed(playedCard)) {
                        yourHand.playCard(toPlayIndex - 1);
                        table.getCardsAttack().add(playedCard);
                        System.out.println("You played: ");
                        printer.printCard(playedCard);
                        try {
                            Card beat = opponent.beat(playedCard);
                            System.out.println("Enemy played: ");
                            table.getCardsDefense().add(beat);
                            printer.printCard(beat);
                            opponent.getHand().playCard(opponent.getHand().getCards().indexOf(beat));
                        } catch (IncapableCardException e) {
                            opponent.getHand().takeAll(table.getAll());
                            break;
                        }
                    } else {
                        System.out.println("You can't play this card");
                    }
                }
            } catch(NumberFormatException e) {
                System.out.println("It's not a number");
            }
        }
        return !correctBeat;
    }

    private static boolean opponentTurnPlay(Scanner scanner, Hand yourHand, Printer printer) {
        boolean correctBeat = false;
        printer.printHand(opponent.getHand(), "Opponent's");
        printer.printHand(yourHand, "Your");
        Card toBeat = opponent.attack();
        table.getCardsAttack().add(toBeat);
        opponent.getHand().getCards().remove(toBeat);
        System.out.println("Enemy played: ");
        printer.printCard(toBeat);

        while (!correctBeat) {
            try {
                int toPlayIndex = scanner.nextInt();
                scanner.nextLine();
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
                    } else {
                        System.out.println("You have played wrong card");
                    }
                }
            } catch(Exception e) {
                System.out.println("Wrong value");
            }
        }
        return correctBeat;
    }

    private static void checkTurn(Hand yourCards, Hand opponentCards) {
        table.resetTable();
        System.out.println("++++++++" + currentDeck.getCards().size() + "+++++++");
        int youToPull = 6 - yourCards.getCards().size();
        int oppToPull = 6 - opponentCards.getCards().size();
        while (!currentDeck.getCards().isEmpty() && youToPull > 0 || oppToPull > 0) {
            if (youToPull > 0) {
                yourCards.addCards(currentDeck.pullCards(1));
                youToPull--;
            }
            if (oppToPull > 0 && !currentDeck.getCards().isEmpty()) {
                opponentCards.addCards(currentDeck.pullCards(1));
                opponent.setHand(opponentCards);
                oppToPull--;
            }
        }

    }

    private static void getResults(Hand yourHand, Hand oppHand) {
        if(yourHand.getCards().isEmpty() && oppHand.getCards().isEmpty()) {
            System.out.println("DRAW!");
        } else if (yourHand.getCards().isEmpty() && !oppHand.getCards().isEmpty()) {
            System.out.println("YOU WON!");
        } else {
            System.out.println("YOU LOSE!");
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

    private static boolean canBeTossed(Card cardToCheck) {
        if(!table.getAll().isEmpty()) {
            for (Card card : table.getAll()) {
                if (cardToCheck.getValue() == card.getValue())
                    return true;
            }
        }
        return false;
    }
}
