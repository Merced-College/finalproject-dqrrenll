import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<Card> hand;
    private boolean isDealer;

    public Player(String name, boolean isDealer) {
        this.name = name;
        this.isDealer = isDealer;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Algorithm #2: hand score calculation (O(n))
     * Aces start as 11, then are reduced to 1 if the total is > 21.
     */
    public int calculateScore() {
        int total = 0;
        int aceCount = 0;

        for (Card card : hand) {
            total += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }

        // If we are over 21 and have aces counted as 11, drop them to 1
        while (total > 21 && aceCount > 0) {
            total -= 10; // convert one Ace from 11 to 1
            aceCount--;
        }

        return total;
    }

    public boolean isBusted() {
        return calculateScore() > 21;
    }

    public String handToString(boolean hideFirstCard) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hand.size(); i++) {
            if (hideFirstCard && isDealer && i == 0) {
                sb.append("[Hidden Card]");
            } else {
                sb.append(hand.get(i).toString());
            }
            if (i < hand.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}