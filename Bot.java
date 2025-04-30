package src;
import java.util.Random;

public class Bot{
    private int score;
    private int wickets;

    public Bot(int overs) {
        Random rand = new Random();
        int minScore = overs * 13;
        int maxScore = overs * 20;

        this.score = rand.nextInt(maxScore - minScore + 1) + minScore;
        this.wickets = (overs < 10) ? rand.nextInt(overs) : rand.nextInt(7); // up to 6 wickets
    }

    public int getScore() {
        return score;
    }

    public int getWickets() {
        return wickets;
    }

    public String getSummary(int overs) {
        return "Bot's Score: " + score + "/" + wickets + " in " + overs + " overs.";
    }
}
