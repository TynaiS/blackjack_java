public class Player extends Hand {
    private double bankroll;
    private double bet;
    private String playerName;

    public Player(String name, double startingBankroll) {
        playerName = name;
        bankroll = startingBankroll;
    }

    public String getName() {
        return playerName;
    }

    public double getBankroll() {
        return bankroll;
    }

    public double getBet() {
        return bet;
    }

    public void setBankroll(double newBankroll) {
        bankroll = newBankroll;
    }

    public void setBet(double newBet) {
        bet = newBet;
    }

}
