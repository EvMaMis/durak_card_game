public enum Suit {
    DIAMONDS(false), HEARTS(false), CLUBS(false), SPADES(false);

    private boolean trump;

    public boolean getTrump() {
        return trump;
    }

    public void setTrump(boolean trump) {
        this.trump = trump;
    }

    Suit(boolean trump) {
        this.trump = trump;
    }

    public String toString() {
        return name();
    }
}
