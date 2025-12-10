import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;

public class BlackjackGame {

    private Deck deck;
    private Player player;
    private Player dealer;

    // Queue for logging messages in order
    private Queue<String> messageLog;

    // Stack for discarding cards at the end of a round
    private Stack<Card> discardPile;

    private Scanner scanner;

    public BlackjackGame() {
        deck = new Deck();
        player = new Player("Player", false);
        dealer = new Player("Dealer", true);
        messageLog = new LinkedList<>();
        discardPile = new Stack<>();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.run();
    }

    /**
     * Main game loop: lets you play multiple rounds until you quit.
     */
    public void run() {
        System.out.println("=== Welcome to Blackjack ===");

        boolean keepPlaying = true;

        while (keepPlaying) {
            playRound();
            System.out.print("\nPlay another round? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y")) {
                keepPlaying = false;
            }
        }

        System.out.println("Thanks for playing!");
    }

    /**
     * Play a single round of Blackjack.
     */
    private void playRound() {
        // Clear hands and discard pile for the new round
        player.clearHand();
        dealer.clearHand();
        discardPile.clear();
        messageLog.clear();

        // Deal initial cards
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());

        logState("Initial deal:");
        logHands(true); // hide dealer's first card

        // Player turn
        playerTurn();

        if (player.isBusted()) {
            messageLog.add("Player busted! Dealer wins.");
            endRound();
            return;
        }

        // Dealer turn (recursive)
        dealerTurnRecursive();

        if (dealer.isBusted()) {
            messageLog.add("Dealer busted! Player wins.");
            endRound();
            return;
        }

        // Determine winner
        determineWinner();
        endRound();
    }

    /**
     * Player's turn: loop until stand or bust.
     */
    private void playerTurn() {
        boolean done = false;

        while (!done) {
            int playerScore = player.calculateScore();
            System.out.println("\nYour hand: " + player.handToString(false));
            System.out.println("Your score: " + playerScore);
            System.out.println("Dealer's visible card(s): " + dealer.handToString(true));

            if (player.isBusted()) {
                messageLog.add("Player busted with " + playerScore);
                done = true;
                break;
            }

            System.out.print("Hit or stand? (h/s): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("h")) {
                Card newCard = deck.drawCard();
                player.addCard(newCard);
                messageLog.add("Player hits and draws: " + newCard);
            } else if (choice.equals("s")) {
                messageLog.add("Player stands with " + playerScore);
                done = true;
            } else {
                System.out.println("Invalid input, please type 'h' or 's'.");
            }
        }
    }

    /**
     * Algorithm #3: Recursive dealer turn.
     * Dealer hits until score is at least 17.
     */
    private void dealerTurnRecursive() {
        int dealerScore = dealer.calculateScore();

        // Base case: dealer stands
        if (dealerScore >= 17) {
            messageLog.add("Dealer stands with " + dealerScore);
            return;
        }

        // Recursive case: dealer hits
        Card newCard = deck.drawCard();
        dealer.addCard(newCard);
        messageLog.add("Dealer hits and draws: " + newCard + " (score now " + dealer.calculateScore() + ")");

        // Recursive call
        dealerTurnRecursive();
    }

    /**
     * Compare final scores and decide winner.
     */
    private void determineWinner() {
        int playerScore = player.calculateScore();
        int dealerScore = dealer.calculateScore();

        messageLog.add("Final Player score: " + playerScore);
        messageLog.add("Final Dealer score: " + dealerScore);

        if (playerScore > dealerScore) {
            messageLog.add("Player wins!");
        } else if (dealerScore > playerScore) {
            messageLog.add("Dealer wins!");
        } else {
            messageLog.add("It's a tie!");
        }
    }

    /**
     * End the round: show all logged messages and move cards to discard pile.
     */
    private void endRound() {
        System.out.println("\n=== Round Results ===");
        logHands(false); // show all cards now

        while (!messageLog.isEmpty()) {
            System.out.println(messageLog.remove());
        }

        // Move cards to discard pile (Stack)
        for (Card c : player.getHand()) {
            discardPile.push(c);
        }
        for (Card c : dealer.getHand()) {
            discardPile.push(c);
        }

        System.out.println("Cards moved to discard pile: " + discardPile.size());
    }

    /**
     * Helper: log a text state marker.
     */
    private void logState(String state) {
        messageLog.add("\n--- " + state + " ---");
    }

    /**
     * Helper: log hands into message log.
     */
    private void logHands(boolean hideDealerFirst) {
        messageLog.add("Player hand: " + player.handToString(false)
                + " (score " + player.calculateScore() + ")");
        messageLog.add("Dealer hand: " + dealer.handToString(hideDealerFirst)
                + (hideDealerFirst ? "" : " (score " + dealer.calculateScore() + ")"));
    }
}
