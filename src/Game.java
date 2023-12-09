import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private final Table table;
    private final Deck currentDeck;
    private final Hand yourHand;
    private final Opponent opponent;
    private boolean yourTurn;
    private final Scanner scanner;
    private final Printer printer;
    private final Card trump;

    private Game(int numberOfShuffle) {
        table = new Table();
        currentDeck = new Deck();
        currentDeck.shuffleCards(numberOfShuffle);
        yourHand = new Hand(currentDeck.pullCards(6));
        opponent = Opponent.getInstance(new Hand(currentDeck.pullCards(6)));
        yourTurn = true;
        scanner = new Scanner(System.in);
        printer = new Printer();
        trump = currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);
        currentDeck.pushCard(trump);
    }

    public static void main(String[] args) {
        Game game = new Game(100);
        game.startGame();
    }

    private void startGame() {
        while (!checkFinished()) {
            if (yourTurn) {
                yourTurn = yourTurnPlay();
            } else {
                yourTurn = opponentTurnPlay();
            }
            checkTurn();
        }
        printer.printResults(yourHand, opponent);
    }

    private boolean yourTurnPlay() {
        boolean correctBeat = false;
        while (!correctBeat) {
            printer.printGame(table, opponent, yourHand, currentDeck, trump);
            try {
                int toPlayIndex = Integer.parseInt(scanner.nextLine());
                if(toPlayIndex > yourHand.getSize() || toPlayIndex < 0)
                    throw new NumberFormatException();
                if (toPlayIndex == 0) {
                    correctBeat = true;
                } else {
                    Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                    if (table.getYourCards().isEmpty() || canBeTossed(playedCard)) {
                        yourHand.playCard(toPlayIndex - 1);
                        table.addCard(playedCard, "Your");
                        try {
                            Card beat = opponent.beat(playedCard);
                            opponent.getHand().playCard(opponent.getHand().getCards().indexOf(beat));
                            table.addCard(beat, "Opponent");
                        } catch (IncapableCardException e) {
                            opponent.getHand().takeAll(table.getAll());
                            printer.printTake();
                            break;
                        }
                    } else {
                        printer.printException("You can't toss this card");
                    }
                }
            } catch(NumberFormatException e) {
                printer.printException("Wrong value");
            }
        }
        return !correctBeat;
    }

    private boolean opponentTurnPlay() {
        boolean correctBeat = false;
        boolean giveUp = false;
        Card toBeat;
        while (!correctBeat) {
            if (table.getAll().isEmpty()) {
                toBeat = opponent.attack();
            } else {
                try {
                    toBeat = opponent.tossCard(table);
                } catch (IncapableCardException e) {
                    if(!giveUp)
                        correctBeat = true;
                    else {
                        yourHand.takeAll(table.getAll());
                        printer.printTake();
                    }
                    break;
                }
            }
            opponent.getHand().playCard(toBeat);
            table.addCard(toBeat, "Opponent");
            printer.printGame(table, opponent, yourHand, currentDeck, trump);
            while (!giveUp) {
                try {
                    int toPlayIndex = Integer.parseInt(scanner.nextLine());
                    if (toPlayIndex == 0) {
                        giveUp = true;
                    } else {
                        Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                        if (checkBeat(toBeat, playedCard)) {
                            yourHand.playCard(toPlayIndex - 1);
                            table.addCard(playedCard, "Your");
                            break;
                        } else {
                            printer.printException("You've played wrong card");
                        }
                    }
                } catch (Exception e ) {
                    printer.printException("Wrong value");
                }
            }
        }
        return correctBeat;
    }

    private boolean checkFinished() {
        return opponent.getHand().isEmpty() || yourHand.isEmpty();
    }

    private void checkTurn() {
        table.resetTable();
        int youToPull = Math.max((6 - yourHand.getSize()), 0);
        int oppToPull = Math.max(6 - opponent.getHand().getSize(), 0);
        while (!currentDeck.getCards().isEmpty()) {
            if (youToPull > 0 ) {
                yourHand.addCards(currentDeck.pullCards(1));
                youToPull--;
            }
            if (oppToPull > 0 && !currentDeck.getCards().isEmpty()) {
                opponent.getHand().addCards(currentDeck.pullCards(1));
                oppToPull--;
            }
            if (oppToPull == 0 && youToPull == 0)
                break;
        }

    }
    private boolean checkBeat(Card toBeat, Card beating) {
        if(toBeat.getSuit() == beating.getSuit()) {
            return toBeat.getValue().ordinal() < beating.getValue().ordinal();
        } else if (!toBeat.getSuit().getTrump() && beating.getSuit().getTrump()) {
            return true;
        }
        return false;
    }

    private boolean canBeTossed(Card cardToCheck) {
        if(!table.getAll().isEmpty()) {
            for (Card card : table.getAll()) {
                if (cardToCheck.getValue() == card.getValue())
                    return true;
            }
        }
        return false;
    }
}
