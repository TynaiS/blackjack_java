import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This plays the Blackjack card game that we wrote throughout
 * the videos in this lesson.
 * 
 * Try to play the game and test it out. As you play it, can you think
 * of ways to improve the game? Can you think of ways to improve the code
 * or organize the code?
 * 
 * @author jkeesh
 * 
 */
public class Blackjack {

    private static final int HEARTS = 0;
    private static final int DIAMONDS = 1;
    private static final int SPADES = 2;
    private static final int CLUBS = 3;

    private static final int JACK = 11;
    private static final int QUEEN = 12;
    private static final int KING = 13;
    private static final int ACE = 14;

    // The starting bankroll for the player.
    private static final int STARTING_BANKROLL = 100;
    private static int numOfPlayers;
    private static ArrayList<Player> players = new ArrayList<Player>();;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Ask the player for a move, hit or stand.
     * 
     * @return A lowercase string of "hit" or "stand"
     *         to indicate the player's move.
     */
    private String getPlayerMove(String playerName) {
        while (true) {
            System.out.print(playerName + ", enter your move (hit/stand): ");
            String move = scanner.nextLine().toLowerCase();

            if (move.equals("hit") || move.equals("stand")) {
                return move;
            } else {
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Play the dealer's turn.
     * 
     * The dealer must hit if the value of the hand is less
     * than 17.
     * 
     * @param dealer The hand for the dealer.
     * @param deck   The deck.
     */
    private void dealerTurn(Hand dealer, Deck deck) {
        while (true) {
            System.out.println("Dealer's hand");
            System.out.println(dealer);

            int value = dealer.getValue();
            System.out.println("Dealer's hand has value " + value);

            System.out.println("Enter to continue...");
            scanner.nextLine();

            if (value < 17) {
                System.out.println("Dealer hits");
                Card c = deck.deal();
                dealer.addCard(c);

                System.out.println("Dealer card was " + c);

                if (dealer.busted()) {
                    System.out.println("Dealer busted!");
                    break;
                }
            } else {
                System.out.println("Dealer stands.");
                break;
            }
        }
    }

    /**
     * Play a player turn by asking the player to hit
     * or stand.
     * 
     * Return whether or not the player busted.
     */
    private boolean playerTurn(Player player, Deck deck) {

        boolean wantsToHit = true;
        boolean result = false;

        while (wantsToHit) {
            String move = getPlayerMove(player.getName());
            if (move.equals("hit")) {
                Card c = deck.deal();
                System.out.println("Your card was: " + c);
                player.addCard(c);
                System.out.println(player.getName() + "'s hand");
                System.out.println(player);

                if (player.busted()) {
                    result = true;
                    wantsToHit = false;
                }
            } else if (move.equals("stand")) {
                wantsToHit = false;
            }
        }

        return result;

    }

    /**
     * Determine if the player wins.
     * 
     * If the player busted, they lose. If the player did
     * not bust but the dealer busted, the player wins.
     * 
     * Then check the values of the hands.
     * 
     * @param player The player hand.
     * @param dealer The dealer hand.
     * @return
     */
    private boolean playerWins(Hand player, Hand dealer) {
        if (player.busted()) {
            return false;
        }

        if (dealer.busted()) {
            return true;
        }

        return player.getValue() > dealer.getValue();
    }

    /**
     * Check if there was a push, which means the player and
     * dealer tied.
     * 
     * @param player The player hand.
     * @param dealer The dealer hand.
     * @return
     */
    private boolean push(Hand player, Hand dealer) {
        return player.getValue() == dealer.getValue();
    }

    /**
     * Find the winner between the player hand and dealer
     * hand. Return how much was won or lost.
     */
    private void findWinner(Hand dealer) {
        for (Player player : players) {
            double bankrollChange = 0;

            if (playerWins(player, dealer)) {
                System.out.println(player.getName() + " wins!");

                if (player.hasBlackjack()) {
                    bankrollChange = 1.5 * player.getBet();
                } else {
                    bankrollChange = player.getBet();
                }

                System.out.println(player.getName() + "'s bankroll: " + (player.getBankroll() + bankrollChange));
                System.out.println();
            } else if (push(player, dealer)) {
                System.out.println(player.getName() + ", you push");
                System.out.println(player.getName() + "'s bankroll: " + (player.getBankroll() + bankrollChange));
                System.out.println();
            } else {
                System.out.println("Dealer wins you, " + player.getName());
                bankrollChange = -player.getBet();
                System.out.println(player.getName() + "'s bankroll: " + (player.getBankroll() + bankrollChange));
                System.out.println();
            }

            player.setBankroll(player.getBankroll() + bankrollChange);
            player.clearHand();
        }

    }

    /**
     * This plays a round of blackjack which includes:
     * - Creating a deck
     * - Creating the hands
     * - Dealing the round
     * - Playing the player turn
     * - Playing the dealer turn
     * - Finding the winner
     * 
     * @param bankroll
     * @return The new bankroll for the player.
     */
    private void playRound() {
        for (Player player : players) {
            System.out.print(player.getName() + ", what is your bet? ");
            player.setBet(scanner.nextInt());
            scanner.nextLine();
        }

        Deck deck = new Deck();
        deck.shuffle();

        Hand dealer = new Hand();

        // --- Dealing cards to all players and the dealer ---

        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                player.addCard(deck.deal());
            }

            dealer.addCard(deck.deal());
        }

        // -------------------------------------------------

        for (Player player : players) {
            System.out.println();
            System.out.println(player.getName() + "'s hand");
            System.out.println(player);

            System.out.println("Dealer's hand");
            dealer.printDealerHand();

            if (player.hasBlackjack()) {
                System.out.println(player.getName() + " wins!");
            } else {
                boolean playerBusted = playerTurn(player, deck);
                if (playerBusted) {
                    System.out.println("You busted :(");
                }
            }

        }

        System.out.println("Enter for dealer turn...");
        scanner.nextLine();
        dealerTurn(dealer, deck);

        findWinner(dealer);
    }

    /**
     * Play the blackjack game. Initialize the bankroll and keep
     * playing roudns as long as the user wants to.
     */
    public void run() {
        System.out.print("How many players will there be? (1-6) ");
        numOfPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numOfPlayers; i++) {
            Player newPlayer = new Player("player" + i, STARTING_BANKROLL);
            players.add(newPlayer);
        }
        System.out.println("Starting bankroll for all players: " + STARTING_BANKROLL);

        while (players.size() > 0) {
            playRound();

            List<Player> playersToRemove = new ArrayList<>();
            for (Player player : players) {
                System.out.print(player.getName() + ", would you like to play again? (Y/N) ");
                String playAgain = scanner.nextLine();
                if (playAgain.equalsIgnoreCase("N")) {
                    playersToRemove.add(player);
                }
            }

            players.removeAll(playersToRemove);

        }

        System.out.println("Thanks for playing!");
    }

    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.run();
    }

}
