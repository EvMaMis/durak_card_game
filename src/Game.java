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
    private static Card trump;


    private Game(int numberOfShuffle) {
        table = new Table();
        currentDeck = new Deck();
        currentDeck.shuffleCards(numberOfShuffle);
        yourHand = new Hand(currentDeck.pullCards(6));
        opponent = Opponent.getInstance(new Hand(currentDeck.pullCards(6)));
        yourTurn = true;
        scanner = new Scanner(System.in);
        printer = new Printer();
    }

    public static void main(String[] args) {
        Game game = new Game(100);
        startGame(game);
    }

    private static void startGame(Game game) {

        trump = game.currentDeck.pullCards(1).get(0);
        trump.getSuit().setTrump(true);

        game.currentDeck.pushCard(trump);

        while (!game.yourHand.getCards().isEmpty() && !game.opponent.getHand().getCards().isEmpty()) {

            if (game.yourTurn) {
                game.yourTurn = yourTurnPlay(game);
            } else {
                game.yourTurn = opponentTurnPlay(game);
            }
            checkTurn(game);
        }
        getResults(game.yourHand, game.opponent.getHand());
    }

    private static boolean yourTurnPlay(Game game) {
        boolean correctBeat = false;
        while (!correctBeat) {
            game.printer.printHand(game.opponent.getHand(), "Opponent's");
            game.printer.printTable(game.table, trump);
            game.printer.printHand(game.yourHand, "Your");
            System.out.println("Choose what to play");

            try {
                int toPlayIndex = Integer.parseInt(game.scanner.nextLine());
                if (toPlayIndex == 0){
                    correctBeat = true;
                } else if(toPlayIndex <= game.yourHand.getCards().size() && toPlayIndex > 0){
                    Card playedCard = game.yourHand.getCards().get(toPlayIndex - 1);
                    if (game.table.getCardsAttack().isEmpty() || canBeTossed(playedCard, game.table)) {
                        game.yourHand.playCard(toPlayIndex - 1);
                        game.table.addCard(playedCard, "Your");
                        try {
                            Card beat = game.opponent.beat(playedCard);
                            game.opponent.getHand().playCard(game.opponent.getHand().getCards().indexOf(beat));
                            game.table.addCard(beat, "Opponent");
                        } catch (IncapableCardException e) {
                            game.opponent.getHand().takeAll(game.table.getAll());
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

    private static boolean opponentTurnPlay(Game game) {
        boolean correctBeat = false;
        Card toBeat = game.opponent.attack();
        game.opponent.getHand().getCards().remove(toBeat);
        game.table.addCard(toBeat, "Opponent");

        game.printer.printHand(game.opponent.getHand(), "Opponent's");
        game.printer.printTable(game.table, trump);
        game.printer.printHand(game.yourHand, "Your");

        while (!correctBeat) {
            try {
                int toPlayIndex = Integer.parseInt(game.scanner.nextLine());
                if (toPlayIndex == 0) {
                    game.yourHand.takeAll(game.table.getAll());
                    break;
                } else {
                    Card playedCard = game.yourHand.getCards().get(toPlayIndex - 1);
                    if (checkBeat(toBeat, playedCard)) {
                        correctBeat = true;
                        game.yourHand.playCard(toPlayIndex - 1);
                        game.table.addCard(playedCard, "Your");
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

    private static void checkTurn(Game game) {
        game.table.resetTable();
        int youToPull = 6 - game.yourHand.getCards().size();
        int oppToPull = 6 - game.opponent.getHand().getCards().size();
        while (!game.currentDeck.getCards().isEmpty() && youToPull > 0 || oppToPull > 0) {
            if (youToPull > 0) {
                game.yourHand.addCards(game.currentDeck.pullCards(1));
                youToPull--;
            }
            if (oppToPull > 0 && !game.currentDeck.getCards().isEmpty()) {
                game.opponent.getHand().addCards(game.currentDeck.pullCards(1));
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

    private static boolean canBeTossed(Card cardToCheck, Table table) {
        if(!table.getAll().isEmpty()) {
            for (Card card : table.getAll()) {
                if (cardToCheck.getValue() == card.getValue())
                    return true;
            }
        }
        return false;
    }
}
