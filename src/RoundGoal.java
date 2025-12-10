package src;

import java.util.*;

/**
 * Represents a single end-of-round goal.
 * Evaluates each player's score for that goal and awards placement points.
 */
public class RoundGoal {
    private String description;
    private GoalType type;
    private Object param; // Flexible parameter (e.g., habitat name, nest type, wingspan range)

    public enum GoalType {
        BIRDS_IN_HABITAT,           // Count birds in a specific habitat
        EGGS_IN_HABITAT,            // Count eggs in a specific habitat
        NEST_TYPE_WITH_EGGS,        // Count birds of specific nest type with at least 1 egg
        BIRDS_WITH_WINGSPAN_RANGE,  // Count birds with wingspan in range (e.g., <= 30 cm)
        BIRDS_WITH_FOOD_COST        // Count birds that can be paid with a food type
    }

    public RoundGoal(String description, GoalType type, Object param) {
        this.description = description;
        this.type = type;
        this.param = param;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Evaluate a single player's score for this goal.
     * Returns the count/metric that will be ranked.
     */
    public int evaluatePlayer(Player player) {
        switch (type) {
            case BIRDS_IN_HABITAT:
                return player.getBirdsInHabitat((String) param).size();

            case EGGS_IN_HABITAT:
                int eggs = 0;
                for (Bird b : player.getBirdsInHabitat((String) param)) {
                    eggs += b.getEggCount();
                }
                return eggs;

            case NEST_TYPE_WITH_EGGS:
                int count = 0;
                for (Bird b : player.getAllBirdsOnBoard()) {
                    if (b != null && b.getNest().equals(param) && b.getEggCount() > 0) {
                        count++;
                    }
                }
                return count;

            case BIRDS_WITH_WINGSPAN_RANGE:
                // param = "[op],[value]" e.g., "<=,30" or ">=,75"
                String[] parts = ((String) param).split(",");
                String op = parts[0].trim();
                int wingspanThreshold = Integer.parseInt(parts[1].trim());
                int matched = 0;
                for (Bird b : player.getAllBirdsOnBoard()) {
                    if (b != null) {
                        if (op.equals("<=") && b.getWingspan() <= wingspanThreshold) matched++;
                        else if (op.equals(">=") && b.getWingspan() >= wingspanThreshold) matched++;
                        else if (op.equals("<") && b.getWingspan() < wingspanThreshold) matched++;
                        else if (op.equals(">") && b.getWingspan() > wingspanThreshold) matched++;
                    }
                }
                return matched;

            case BIRDS_WITH_FOOD_COST:
                String foodType = (String) param;
                int foodMatched = 0;
                for (Bird b : player.getAllBirdsOnBoard()) {
                    if (b != null && b.canBePaidWith(foodType)) {
                        foodMatched++;
                    }
                }
                return foodMatched;

            default:
                return 0;
        }
    }

    /**
     * Award placement points to each player.
     * Official scoring (4-player): 1st=5, 2nd=3, 3rd=2, 4th=1
     * Ties split points equally and skip subsequent ranks.
     */
    public Map<Integer, Integer> awardPoints(Player[] players) {
        Map<Integer, Integer> scores = new TreeMap<>();
        int[] placements = {5, 3, 2, 1}; // Standard 4-player official scoring

        // Evaluate all players
        List<Integer> metrics = new ArrayList<>();
        Map<Integer, Integer> playerMetrics = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            int metric = evaluatePlayer(players[i]);
            playerMetrics.put(i, metric);
            metrics.add(metric);
        }

        // Sort in descending order (highest wins)
        metrics.sort((a, b) -> Integer.compare(b, a));

        // Award points with tie handling
        int rank = 0;
        int i = 0;
        while (i < metrics.size() && rank < placements.length) {
            int currentMetric = metrics.get(i);
            List<Integer> tied = new ArrayList<>();
            tied.add(i);

            // Find all tied players
            int j = i + 1;
            while (j < metrics.size() && metrics.get(j) == currentMetric) {
                tied.add(j);
                j++;
            }

            // Split points among tied players
            int totalPoints = 0;
            for (int t = 0; t < tied.size() && rank + t < placements.length; t++) {
                totalPoints += placements[rank + t];
            }
            int pointsPerTied = totalPoints / tied.size();

            for (Integer playerIndex : tied) {
                scores.put(playerIndex, pointsPerTied);
            }

            rank += tied.size();
            i = j;
        }

        return scores;
    }
}
