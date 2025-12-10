import java.util.LinkedList;
import java.util.Random;

public class Deck {

    private LinkedList<Card> cards;       // Linked list for the deck
    private LinkedList<Card> discardPile; // Stack-like discard pile

    private String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private int[] values  = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10}; // Ace = 11 by default

    public Deck() {
        cards = new LinkedList<>();
        discardPile = new LinkedList<>();
        buildDeck();
        shuffle();
    }

    /**
     * Builds a standard 52-card deck
     */
    private void buildDeck() {
        cards.clear();
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                cards.add(new Card(suit, ranks[i], values[i]));
            }
        }
    }

    /**
     * Fisher-Yates shuffle algorithm (O(n))
     */
    public void shuffle() {
        Random rand = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Swap cards[i] and cards[j]
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    /**
     * Draws the top card from the deck.
     * If the deck is empty, rebuild it from the discard pile.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            reshuffleFromDiscard();
        }
        return cards.removeFirst();
    }

    /**
     * Adds a card to the discard pile (LIFO behavior)
     */
    public void discard(Card card) {
        discardPile.addFirst(card);
    }

    /**
     * Rebuilds the deck from the discard pile and shuffles it again.
     * This is used when the deck runs out.
     */
    private void reshuffleFromDiscard() {
        System.out.println("Deck empty — reshuffling discard pile...");
        cards.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }

    /**
     * Returns size of deck (useful for debugging)
     */
    public int size() {
        return cards.size();
    }
}