package src;
import java.util.*;

public class Game {
    private final ProgramState state;
    private final Feeder feeder;
    private int round = 1;
    private int currentPlayer = 0;
    private boolean over = false;
    private ArrayList<Bird> birds;

    // Round goal points storage - new structure to persist awarded points
    private int[][] roundGoalPoints;

    private RoundGoal[] roundGoals;

    public enum GamePhase {
        SETUP,
        INITIAL_SELECTION,
        PLAYER_TURN,
        END_OF_ROUND,
        GAME_OVER
    }

    public Game(ProgramState s) {
        this.state = s;
        this.feeder = new Feeder(s);
        init();
    }

    private void init() {
        // ensure round-goal points storage is allocated to avoid NPEs later
        roundGoalPoints = new int[state.players.length][4];
        for (int i = 0; i < roundGoalPoints.length; i++) Arrays.fill(roundGoalPoints[i], 0);

        for (int i = 0; i < state.players.length; i++) {
            state.players[i] = new Player("Player " + (i + 1), new ArrayList<>(), new ArrayList<>(), i);
        }

        // ProgramState.playerRoundScores will hold per-player per-round awards

        state.makeDeckOfCards();
        dealInitialCards();
        dealInitialBonusCards();
        setupRoundGoals();

        state.currentPhase = ProgramState.GamePhase.INITIAL_SELECTION;
    }

    private void setupRoundGoals() {
        int[] goalIds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        for (int i = 0; i < goalIds.length; i++) {
            int randomIndex = (int)(Math.random() * goalIds.length);
            int temp = goalIds[i];
            goalIds[i] = goalIds[randomIndex];
            goalIds[randomIndex] = temp;
        }

        for (int i = 0; i < 4; i++) {
            state.setRoundGoal(i, goalIds[i]);
        }
    }

    private void dealInitialCards() {
        for (int i = 0; i < 5; i++) {
            for (Player p : state.players) {
                if (!state.deckOfCards.isEmpty()) {
                    Bird card = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                    p.addCardToHand(card);
                }
            }
        }
    }

    private void dealInitialBonusCards() {
        for (int i = 0; i < 2; i++) {
            for (Player p : state.players) {
                if (!state.bonusDeck.isEmpty()) {
                    Bonus card = state.bonusDeck.remove(state.bonusDeck.size() - 1);
                    p.addBonusCard(card);
                }
            }
        }
    }



    public void next(ProgramState.PlayerAction action) {
        currentPlayer = (currentPlayer + 1) % 4;
        // activatePinkPowers(state.players[currentPlayer], action); // Handled in FramePanel now
        
        if (currentPlayer == (state.firstPlayerToken - 1)) {
            nextRound();
        }

        state.players[currentPlayer].setPlayerScore(state.players[currentPlayer].calculateScore().total());
    }

    public void activatePinkPowers(Player p, ProgramState.PlayerAction action) {
        for (Player other : state.players) {
            if (other == p) continue;
            ArrayList<Bird> birdsList = other.getAllBirdsOnBoard();
            for (Bird b : birdsList) {
                if (!b.isPinkPowerUsed() && b.triggersOnAction(action)) {
                    handlePinkPower(other, b, action);
                    b.setPinkPowerUsed(true);
                }
            }
        }
    }

    public void nextRound() {
        determineRoundWinner(round);
        round++;

        if (round > 4) {
            state.currentPhase = ProgramState.GamePhase.GAME_OVER;
            over = true;
            calculateFinalScores();
            return;
        }

        for (Player p : state.players) {
            p.nextRound();
            p.resetPinkPowers();
        }

        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
    }

    private void determineRoundWinner(int roundNumber) {
        if (roundNumber < 1 || roundNumber > 4) return;

        int goalId = state.getRoundGoal(roundNumber - 1);
        int nPlayers = state.players.length;
        int[] values = new int[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            values[i] = getRoundGoalCount(goalId, state.players[i]);
        }

        // Prepare sorted indices by value (descending)
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < nPlayers; i++) indices.add(i);
        indices.sort((a, b) -> Integer.compare(values[b], values[a])); // descending

        // Scoring scheme - standard 4-player (first..fourth = 5,3,2,1).
        // For projects that assume 4 players, this is most appropriate.
        int[] placePoints;
        if (nPlayers == 4) {
            placePoints = new int[]{5, 3, 2, 1};
        } else if (nPlayers == 3) {
            placePoints = new int[]{6, 4, 2}; // reasonable fallback
        } else if (nPlayers == 2) {
            placePoints = new int[]{7, 4}; // reasonable fallback
        } else {
            // Generic fallback: descending starting at 5
            placePoints = new int[nPlayers];
            int start = 5;
            for (int i = 0; i < nPlayers; i++) {
                placePoints[i] = Math.max(start - i, 0);
            }
        }

        // Walk through sorted indices and allocate points for ties
        boolean[] awarded = new boolean[nPlayers];
        int rank = 0; // how many positions have been processed
        while (rank < nPlayers) {
            // find group of tied players at this rank
            int idx = indices.get(rank);
            int val = values[idx];

            ArrayList<Integer> tiedPlayers = new ArrayList<>();
            int j = rank;
            while (j < nPlayers && values[indices.get(j)] == val) {
                tiedPlayers.add(indices.get(j));
                j++;
            }

            // determine positions covered by this tie: startPos .. endPos inclusive
            int startPos = rank; // 0-based
            int endPos = j - 1;

            // sum the placePoints for each tied position that exists
            int sumPoints = 0;
            for (int pos = startPos; pos <= endPos; pos++) {
                if (pos < placePoints.length) sumPoints += placePoints[pos];
            }

            int awardEach = tiedPlayers.size() > 0 ? (sumPoints / tiedPlayers.size()) : 0;

            // assign points to each tied player (store & add)
            for (int pIndex : tiedPlayers) {
                state.players[pIndex].addPoints(awardEach);
                // persist into round table (roundNumber-1)
                roundGoalPoints[pIndex][roundNumber - 1] = awardEach;
                // also write into Player object for easy access elsewhere
                state.players[pIndex].setRoundGoalPoints(roundNumber - 1, awardEach);
            }

            // advance rank to after the tied group
            rank = j;
        }
    }

    private int getRoundGoalCount(int goalId, Player p) {
        return switch (goalId) {
            case 0 -> p.getBirdsInHabitat("forest").size();
            case 1 -> p.getBirdsInHabitat("plains").size();
            case 2 -> p.getBirdsInHabitat("wetlands").size();
            case 3 -> p.getEggCount();
            case 4 -> getCachedFoodCount(p);
            case 5 -> getBirdsWithEggsCount(p);
            case 6 -> p.getFoodTokens().size();
            case 7 -> p.getBoard().getLongestRow();
            case 8 -> getMaxBirdsInSingleHabitat(p);
            case 9 -> getTuckedCardCount(p);
            case 10 -> getDietTypes(p);
            case 11 -> p.getBirdsInHabitat("forest").size() + p.getBirdsInHabitat("plains").size();
            case 12 -> p.getBirdsInHabitat("forest").size() + p.getBirdsInHabitat("wetlands").size();
            case 13 -> p.getBirdsInHabitat("plains").size() + p.getBirdsInHabitat("wetlands").size();
            case 14 -> getBirdsWithNoEggsCount(p);
            case 15 -> p.getBirdCount();
            default -> 0;
        };
    }

    private int getCachedFoodCount(Player p) {
        ScoreBreakdown sb = p.calculateScore();
        return sb == null ? 0 : sb.cachedFood;
    }

    private int getBirdsWithEggsCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            if (b.getEggCount() > 0) {
                count++;
            }
        }
        return count;
    }

    private int getMaxBirdsInSingleHabitat(Player p) {
        return Math.max(p.getBirdsInHabitat("forest").size(),
            Math.max(p.getBirdsInHabitat("plains").size(), p.getBirdsInHabitat("wetlands").size()));
    }

    private int getTuckedCardCount(Player p) {
        ScoreBreakdown sb = p.calculateScore();
        return sb == null ? 0 : sb.tuckedCards;
    }

    private int getDietTypes(Player p) {
        Set<String> dietTypes = new HashSet<>();
        for (Bird b : p.getAllBirdsOnBoard()) {
            for (String[] foodOptions : b.getFoods()) {
                for (String food : foodOptions) {
                    dietTypes.add(food);
                }
            }
        }
        return dietTypes.size();
    }

    private int getBirdsWithNoEggsCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            if (b.getEggCount() == 0) {
                count++;
            }
        }
        return count;
    }

    private void calculateFinalScores() {
        for (int playerIndex = 0; playerIndex < state.players.length; playerIndex++) {
            Player p = state.players[playerIndex];
            ScoreBreakdown baseScore = p.calculateScore();

            ArrayList<Bonus> bonuses = p.getBonuses();
            int bonusPoints = 0;
            for (Bonus b : bonuses) {
                bonusPoints += b.getBonusPoints(p);
            }

            // Sum all round goal points recorded for this player.
            int roundPointsTotal = 0;
            for (int r = 0; r < roundGoalPoints[playerIndex].length; r++) {
                roundPointsTotal += roundGoalPoints[playerIndex][r];
            }

            // Combine bonusPoints and roundPointsTotal into the single 'bonus' parameter
            int combinedBonus = bonusPoints + roundPointsTotal;

            // Create final breakdown using the 6-arg constructor:
            ScoreBreakdown finalScore = new ScoreBreakdown(
                baseScore.printedPoints,
                baseScore.eggs,
                baseScore.cachedFood,
                baseScore.tuckedCards,
                combinedBonus,
                baseScore.flocked
            );

            p.setFinalScore(finalScore);
            p.setPlayerScore(finalScore.total());
        }
    }

    public boolean isOver() {
        return over;
    }

    public ArrayList<Bird> getBirds() {
        return birds;
    }

    public void setBirds(ArrayList<Bird> birds) {
        this.birds = birds;
    }

    public int getPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayer;
    }

    public int getRound() {
        return round;
    }

    public Player getPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < state.players.length) {
            return state.players[playerIndex];
        }
        return null;
    }

    public boolean doAction(ProgramState.PlayerAction action, Object... params) {
        Player p = state.players[currentPlayer];
        if (p.getActionsRemaining(round) <= 0) return false;

        boolean success = switch (action) {
            case PLAY_BIRD -> play(p, params);
            case GAIN_FOOD -> food(p, params);
            case LAY_EGGS -> eggs(p, params);
            case DRAW_CARDS -> {
                String[] drawParams = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    drawParams[i] = (String) params[i];
                }
                yield draw(p, drawParams);
            }
            default -> false;
        };

        if (success) {
            //p.useAction(round);
            triggerPinkPowers(action, p);
        }

        return success;
    }

    private void triggerPinkPowers(ProgramState.PlayerAction action, Object... params) {
        for (int i = 0; i < state.players.length; i++) {
            if (i == currentPlayer) continue;
            Player other = state.players[i];
            ArrayList<Bird> birdsList = other.getAllBirdsOnBoard();
            for (Bird b : birdsList) {
                if (!b.isPinkPowerUsed() && b.triggersOnAction(action)) {
                    handlePinkPower(other, b, action, params);
                    b.setPinkPowerUsed(true);
                }
            }
        }
    }

    public boolean play(Player p, Object... params) {
        if (params.length < 2) return false;

        if (!(params[0] instanceof Bird) || !(params[1] instanceof String)) {
            return false;
        }

        Bird b = (Bird) params[0];
        String h = (String) params[1];

        if (!p.getCardsInHand().contains(b)) {
            return false;
        }

        if (!p.canAffordBird(b)) {
            return false;
        }

        if (!b.canLiveInHabitat(h)) {
            return false;
        }

        if (p.isHabitatFull(h)) {
            return false;
        }
        
        Object[] playParams = new Object[params.length - 2];
        int[] playParam = new int[params.length-2];

        // convert remaining params into an Object[] if present (was referencing undefined playParams)
        Object[] remainingParams = new Object[0];
        if (params.length > 2) {
            remainingParams = new Object[params.length - 2];
            System.arraycopy(params, 2, remainingParams, 0, params.length - 2);
        }
        int[] tim = {};
        boolean played = p.playBird(b, h, -1, tim, playParams);
        if (played) {
            p.removeCardFromHand(b);

            if (b.isWhenPlayedAbility()) {
                activateWhenPlayedAbility(p, b, params);
            }
        }

        return played;
    }

    private boolean food(Player p, Object... params) {
        if (params.length == 0) {
            return false;
        }

        int numToGain = 1 + (p.getBirdsInHabitat("forest") != null ? p.getBirdsInHabitat("forest").size() : 0);
        if (params[0] instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<String> foodChoices = (ArrayList<String>) params[0];
            if (foodChoices.size() > numToGain) {
                return false;
            }

            for (String food : foodChoices) {
                if (feeder.getDice().contains(food)) {
                    p.addFood(food, 1);
                    feeder.removeDie(feeder.getDice().indexOf(food));
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

        if (feeder.getDice().isEmpty() || feeder.canReroll()) {
            feeder.reRoll();
        }

        return true;
    }

    private boolean eggs(Player p, Object... params) {
        if (params.length == 0) {
            return false;
        }

        int numToLay = 2 + (p.getBirdsInHabitat("plains") != null ? p.getBirdsInHabitat("plains").size() : 0);
        if (params[0] instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Bird, Integer> eggsToLay = (Map<Bird, Integer>) params[0];
            int totalEggs = 0;
            for (int eggsCount : eggsToLay.values()) {
                totalEggs += eggsCount;
            }

            if (totalEggs > numToLay) {
                return false;
            }

            for (Map.Entry<Bird, Integer> entry : eggsToLay.entrySet()) {
                Bird b = entry.getKey();
                int eggCount = entry.getValue();
                if (!p.layEggsOnBird(b, eggCount)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public void activateWhenPlayedAbility(Player p, Bird b, Object... params) {
        String abilityText = b.getAbilityText();

        if (abilityText.contains("Play a second bird")) {
        } else if (abilityText.contains("Gain 3 [seed] from the supply.")) {
            p.addFood("s", 3);
        } else if (abilityText.contains("Gain 3 [fish] from the supply.")) {
            p.addFood("f", 3);
        } else if (abilityText.contains("Gain all [fish] that are in the birdfeeder.")) {
            int fishGained = feeder.takeAll("fish");
            p.addFood("f", fishGained);
        } else if (abilityText.contains("Gain all [invertebrate] that are in the birdfeeder.")) {
            int invertebratesGained = feeder.takeAll("insect");
            p.addFood("i", invertebratesGained);
        } else if (abilityText.contains("Draw [card] equal to the number of players +1")) {
            int numToDraw = state.players.length + 1;
            ArrayList<Bird> drawnCards = new ArrayList<>();
            for (int i = 0; i < numToDraw && !state.deckOfCards.isEmpty(); i++) {
                drawnCards.add(state.deckOfCards.remove(state.deckOfCards.size() - 1));
            }
            if (!drawnCards.isEmpty()) {
                p.addCardToHand(drawnCards.remove(0));
            }
            for (int i = 0; i < state.players.length - 1; i++) {
                if (!drawnCards.isEmpty()) {
                    state.players[(currentPlayer + 1 + i) % state.players.length].addCardToHand(drawnCards.remove(0));
                }
            }
        } else if (abilityText.contains("Draw 3 [card] from the deck.")) {
            for (int i = 0; i < 3 && !state.deckOfCards.isEmpty(); i++) {
                p.addCardToHand(state.deckOfCards.remove(state.deckOfCards.size() - 1));
            }
        } else if (abilityText.contains("Lay 1 [egg] on each of your birds with a [cavity] nest.")) {
            p.layEggsInNestType("cavity");
        } else if (abilityText.contains("Lay 1 [egg] on this bird.")) {
            b.addEggs(1);
        } else if (abilityText.contains("Draw 2 new bonus cards and keep 1.")) {
            if (p.canDrawBonusCard()) {
                ArrayList<Bonus> drawnCards = new ArrayList<>();
                for (int i = 0; i < 2 && !state.bonusDeck.isEmpty(); i++) {
                    drawnCards.add(state.bonusDeck.remove(state.bonusDeck.size() - 1));
                }
                if (params.length > 0 && params[0] instanceof Bonus) {
                    Bonus cardToKeep = (Bonus) params[0];
                    if (drawnCards.contains(cardToKeep)) {
                        p.addBonusCard(cardToKeep);
                        drawnCards.remove(cardToKeep);
                    }
                    state.bonusDeck.addAll(drawnCards);
                } else if (!drawnCards.isEmpty()) {
                    p.addBonusCard(drawnCards.remove(0));
                    state.bonusDeck.addAll(drawnCards);
                }
            }
        }
    }

    private void handlePinkPower(Player p, Bird b, ProgramState.PlayerAction action, Object... params) {
        String abilityText = b.getAbilityText();
        switch (action) {
            case LAY_EGGS:
                if (abilityText.contains("When another player takes the \"lay eggs\" action")) {
                    if (abilityText.contains("[ground] nest")) {
                        p.layEggsInNestType("ground");
                    } else if (abilityText.contains("[cavity] nest")) {
                        p.layEggsInNestType("cavity");
                    } else if (abilityText.contains("[bowl] nest")) {
                        p.layEggsInNestType("bowl");
                    }
                }
                break;
            case PLAY_BIRD:
                if (abilityText.contains("When another player plays a bird in their [wetland]")) {
                    p.addFood("f", 1);
                } else if (abilityText.contains("When another player plays a bird in their [forest]")) {
                    p.addFood("i", 1);
                } else if (abilityText.contains("When another player plays a bird in their [grassland]")) {
                    if (!p.getCardsInHand().isEmpty()) {
                        Bird cardToTuck = p.getCardsInHand().get(0);
                        p.removeCardFromHand(cardToTuck);
                        b.tuckCard();
                    }
                }
                break;
            case GAIN_FOOD:
                if (abilityText.contains("if they gain any number of [rodent]")) {
                    if (params.length > 0 && params[0] instanceof ArrayList) {
                        @SuppressWarnings("unchecked")
                        ArrayList<String> foodGained = (ArrayList<String>) params[0];
                        if (foodGained.contains("rodent")) {
                            b.cacheFood();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean draw(Player p, String[] params) {
        int numToDraw = 1 + p.getBirdsInHabitat("wetlands").size();
        if (p.getActionsRemaining(getRound()) > 0) {
            if (params[0].equals("0")) {
                for (int i = 0; i < numToDraw; i++) {
                    p.addCardToHand(state.deckOfCards.remove(state.deckOfCards.size() - 1));
                }
                //p.useAction(getRound());
                return true;
            } else {
                int index = Integer.parseInt(params[1]);
                if (state.cardTray[index] != null) {
                    p.addCardToHand(state.cardTray[index]);
                    state.cardTray[index] = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                    //p.useAction(getRound());
                    return true;
                }
            }
        }
        return false;
    }

    public void rerollFeeder() {
        feeder.reRoll();
    }

    public void updateCardTray() {
        for (int i = 0; i < state.cardTray.length; i++) {
            if (state.cardTray[i] == null) {
                if (!state.deckOfCards.isEmpty()) {
                    state.cardTray[i] = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                }
            }
        }
    }

    public int getActionsRemaining() {
        return state.players[currentPlayer].getActionsRemaining(round);
    }

    public ProgramState.GamePhase getCurrentPhase() {
        return state.currentPhase;
    }

    public Feeder getFeeder() {
        return feeder;
    }

    public Player[] getPlayers() {
        return state.players;
    }

    // new getter so UI / other systems can ask the Game for stored awarded round points
    public int getPlayerRoundScore(int playerIndex, int roundIndex) {
        if (roundGoalPoints == null) return 0;
        if (playerIndex < 0 || playerIndex >= roundGoalPoints.length) return 0;
        if (roundIndex < 0 || roundIndex >= roundGoalPoints[playerIndex].length) return 0;
        return roundGoalPoints[playerIndex][roundIndex];
    }

    /**
     * Initialize 4 round goals for the game.
     * These are randomly selected or pre-defined based on game setup.
     */
    private void initializeRoundGoals() {
        roundGoals = new RoundGoal[4];
        // Example round goals (can be randomized from a pool)
        roundGoals[0] = new RoundGoal("Most birds in forest", RoundGoal.GoalType.BIRDS_IN_HABITAT, "forest");
        roundGoals[1] = new RoundGoal("Most eggs in grasslands", RoundGoal.GoalType.EGGS_IN_HABITAT, "grasslands");
        roundGoals[2] = new RoundGoal("Most small birds (wingspan <= 30)", RoundGoal.GoalType.BIRDS_WITH_WINGSPAN_RANGE, "<=,30");
        roundGoals[3] = new RoundGoal("Most cavity nesters with eggs", RoundGoal.GoalType.NEST_TYPE_WITH_EGGS, "cavity");
    }

    /**
     * Called at the end of each round.
     * Evaluates the round goal and awards placement points to each player.
     */
    public void scoreRoundGoal(int roundIndex) {
        if (roundIndex < 0 || roundIndex >= 4 || roundGoals[roundIndex] == null) return;

        RoundGoal goal = roundGoals[roundIndex];
        Map<Integer, Integer> pointsAwarded = goal.awardPoints(state.players);
        
        for (int playerIndex = 0; playerIndex < state.players.length; playerIndex++) {
            int points = pointsAwarded.getOrDefault(playerIndex, 0);
            state.players[playerIndex].setRoundGoalPoints(roundIndex, points);
        }

        for (Player player : state.players) {
            player.calculateFinalScore();
        }
        // TODO: Call UI to display final score sheet (will be implemented in FramePanel)
    }

    public RoundGoal getRoundGoal(int roundIndex) {
        if (roundIndex >= 0 && roundIndex < 4) {
            return roundGoals[roundIndex];
        }
        return null;
    }
}
