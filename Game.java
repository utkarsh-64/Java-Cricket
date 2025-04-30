package src;

public class Game {
    private Player player;
    private Bot bot;
    private int totalBalls;
    private int overs;

    private boolean statsUpdated = false;

    public Game(int overs) {
        this.overs = overs;
        this.totalBalls = overs * 6;
        this.player = new Player();
        this.bot = new Bot(overs);
    }

    public Player getPlayer() {
        return player;
    }

    public Bot getBot() {
        return bot;
    }

    public int getTotalBalls() {
        return totalBalls;
    }

    public boolean isGameOver() {
        return player.getBallsFaced() >= totalBalls || player.getWickets() >= 10 || player.getScore() > bot.getScore();
    }

    public String getResult(String playerName) {
        int playerScore = player.getScore();
        int botScore = bot.getScore();
        String result = "";

        try {
            if (playerScore > botScore) {
                result = "Congratulations, " + playerName + "! You won the match!";
                DatabaseManager.updatePlayerStats(playerName, true);
            } else if (playerScore == botScore) {
                result = "It's a tie!";
            } else {
                result = "Bot wins! Better luck next time, " + playerName + ".";
                DatabaseManager.updatePlayerStats(playerName, false);
            }
        } catch (DatabaseException e) {
            result += "\nError updating stats: " + e.getMessage();
        }
        return result;
    }
}
