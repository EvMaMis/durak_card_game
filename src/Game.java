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
        getResults();
    }

    private boolean yourTurnPlay() {
        boolean correctBeat = false;
        while (!correctBeat) {
            printer.printGame(opponent, table, trump, yourHand);
            try {
                int toPlayIndex = Integer.parseInt(scanner.nextLine());
                if (toPlayIndex == 0){
                    correctBeat = true;
                } else if(toPlayIndex <= yourHand.getCards().size() && toPlayIndex > 0){
                    Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                    if (table.getCardsAttack().isEmpty() || canBeTossed(playedCard)) {
                        yourHand.playCard(toPlayIndex - 1);
                        table.addCard(playedCard, "Your");
                        try {
                            Card beat = opponent.beat(playedCard);
                            opponent.getHand().playCard(opponent.getHand().getCards().indexOf(beat));
                            table.addCard(beat, "Opponent");
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

    private boolean opponentTurnPlay() {
        boolean correctBeat = false;
        Card toBeat = opponent.attack();
        opponent.getHand().getCards().remove(toBeat);
        table.addCard(toBeat, "Opponent");
        printer.printGame(opponent, table, trump, yourHand);
        while (!correctBeat) {
            try {
                int toPlayIndex = Integer.parseInt(scanner.nextLine());
                if (toPlayIndex == 0) {
                    yourHand.takeAll(table.getAll());
                    break;
                } else {
                    Card playedCard = yourHand.getCards().get(toPlayIndex - 1);
                    if (checkBeat(toBeat, playedCard)) {
                        correctBeat = true;
                        yourHand.playCard(toPlayIndex - 1);
                        table.addCard(playedCard, "Your");
                    } else {
                        System.out.println("You have played wrong card");
                    }
                }
                table.resetTable();
            } catch (Exception e) {
                System.out.println("Wrong value");
            }
        }
        return correctBeat;
    }

    private boolean checkFinished() {
        return opponent.getHand().isEmpty() || yourHand.isEmpty();
    }

    private void checkTurn() {
        table.resetTable();
        int youToPull = Math.max((6 - yourHand.getCards().size()), 0);
        int oppToPull = Math.max(6 - opponent.getHand().getCards().size(), 0);
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

    private void getResults() {
        if(yourHand.isEmpty() && opponent.getHand().isEmpty()) {
            System.out.println("DRAW!");
        } else if (yourHand.isEmpty() && !opponent.getHand().isEmpty()) {
            System.out.println("YOU WON!");
        } else {
            System.out.println("YOU LOSE!");
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
