package src;
import java.util.Random;
import java.time.LocalTime;

public class Player {
    private int score;
    private int wickets;
    private int ballsFaced;
    private final int maxWickets = 10;

    public Player() {
        score = 0;
        wickets = 0;
        ballsFaced = 0;
    }

    public String bat() throws InvalidBattingActionException {
        if (wickets >= maxWickets) {
            throw new InvalidBattingActionException("You cannot bat. All wickets are down!");
        }

        ballsFaced++;
        int runs = calculateRuns();

        Random rand = new Random();
        boolean isWicket = rand.nextInt(25) == 0; // 4% chance of wicket

        if (isWicket) {
            wickets++;
            return "WICKET! Total Wickets: " + wickets;
        } else {
            score += runs;
            return "You scored " + runs + " run(s). Total Score: " + score + "/" + wickets;
        }
    }

    private int calculateRuns() {
        int milliUnit = LocalTime.now().getNano() / 1_000_000 % 10;

        switch (milliUnit) {
            case 0: case 9:
                return 1;
            case 2: case 7:
                return 2;
            case 4: case 5:
                return 3;
            case 6: case 3:
                return 4;
            case 8: case 1:
                return 6;
            default:
                return 0;
        }
    }

    public int getScore() {
        return score;
    }

    public int getWickets() {
        return wickets;
    }

    public int getBallsFaced() {
        return ballsFaced;
    }
} 
