public class Card {
    private Value value;
    private Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public void setValue(Value value) {
        this.value = value;
    }
    public Value getValue() {
        return value;
    }
    public void setSuit(Suit suit) {
        this.suit = suit;
    }
    public Suit getSuit() {
        return suit;
    }

    public String toString() {
        return new String(suit + " " + value);
    }
}