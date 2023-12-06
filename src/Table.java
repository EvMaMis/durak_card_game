public class Table {
    public static void main(String[] args) {
        Deck testDeck = new Deck();
        testDeck.shuffleCards(100);
        Hand yourHand = new Hand(testDeck.pullCards(6));
        Hand opponentsHead = new Hand(testDeck.pullCards(6));

        yourHand.printHand();

    }
}
