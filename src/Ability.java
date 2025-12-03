package src;

import java.util.Locale;

/**
 * Lightweight, extensible Ability representation parsed from CSV ability text.
 * This class intentionally keeps logic simple and delegates game-specific effects
 * to callers via the perform(...) method which has access to `ProgramState` and `Player`.
 */
public class Ability {
    public enum Trigger { BROWN, WHITE, PINK, NONE }

    public final Trigger trigger;
    public final String rawText;

    public Ability(Trigger trigger, String rawText){
        this.trigger = trigger;
        this.rawText = rawText == null ? "" : rawText;
    }

    /**
     * Very small set of heuristic helpers for executing common ability patterns.
     * Game-specific effects should be wired in GameLogic; this provides helpers
     * that callers can use (e.g., detect if ability tucks a card, draws a card, etc.).
     */
    public boolean mentionsTuck(){
        return rawText.toLowerCase(Locale.ROOT).contains("tuck");
    }

    public boolean mentionsDraw(){
        return rawText.toLowerCase(Locale.ROOT).contains("draw");
    }

    public boolean mentionsLayEgg(){
        return rawText.toLowerCase(Locale.ROOT).contains("lay 1 [egg]") || rawText.toLowerCase(Locale.ROOT).contains("lay 1 egg");
    }

    public boolean mentionsCache(){
        return rawText.toLowerCase(Locale.ROOT).contains("cache");
    }

    public boolean isGainFoodAbility() {
        String lowerText = rawText.toLowerCase(Locale.ROOT);
        return lowerText.contains("gain") && 
               (lowerText.contains("[seed]") || 
                lowerText.contains("[fish]") || 
                lowerText.contains("[berry]") || 
                lowerText.contains("[insect]") || 
                lowerText.contains("[rat]") ||
                lowerText.contains("[invertebrate]") || 
                lowerText.contains("[fruit]") ||
                lowerText.contains("[wild]"));
    }
    public String getTrigger(){
        switch (trigger){
            case (Trigger.BROWN) -> {return "OA";}
            case (Trigger.WHITE) -> {return "WP";}
            case (Trigger.PINK) -> {return "OBT";}
            case (Trigger.NONE) -> {return "none";}
        }
        return null;
    }
    
    public boolean mentionsPredator(){
        return rawText.toLowerCase(Locale.ROOT).contains("[predator]");
    }
    
    public boolean mentionsHabitat(String habitat){
        return rawText.toLowerCase(Locale.ROOT).contains("[" + habitat.toLowerCase() + "]");
    }
    
    public boolean mentionsNestType(String nestType){
        return rawText.toLowerCase(Locale.ROOT).contains("[" + nestType.toLowerCase() + "] nest");
    }
    
    public boolean isWhenOtherPlayerLaysEggs(){
        return rawText.toLowerCase(Locale.ROOT).contains("when another player takes the \"lay eggs\" action");
    }
    
    public boolean isWhenOtherPlayerGainsFood(){
        return rawText.toLowerCase(Locale.ROOT).contains("when another player takes the \"gain food\" action");
    }
    
    public boolean isWhenOtherPlayerPlaysBird(){
        return rawText.toLowerCase(Locale.ROOT).contains("when another player plays a bird");
    }
    
    public boolean isAllPlayersAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("all players");
    }
    
    public boolean isPlayerWithFewestAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("player(s) with the fewest");
    }
    
    public boolean isRightmostBirdAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("if this bird is to the right of all other birds");
    }
    
    public boolean isRepeatAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("repeat") || 
               rawText.toLowerCase(Locale.ROOT).contains("activate again");
    }
    
    public boolean isTradeAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("trade");
    }
    
    public boolean isLookAtCardsAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("look at a [card]");
    }
    
    public boolean isDiscardEggAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("discard") && rawText.toLowerCase(Locale.ROOT).contains("[egg]");
    }
    
    public boolean isRollDiceAbility(){
        return rawText.toLowerCase(Locale.ROOT).contains("roll all dice");
    }
    
    public int getFoodCount(String foodType){
        String foodNameMap = switch(foodType) {
            case "s" -> "seed";
            case "f" -> "fish";
            case "b" -> "berry";
            case "i" -> "insect";
            case "r" -> "rat";
            case "a" -> "wild";
            default -> foodType;
        };
        
        String lowerText = rawText.toLowerCase(Locale.ROOT);
        if (lowerText.contains("gain")) {
            int gainIndex = lowerText.indexOf("gain");
            int bracketIndex = lowerText.indexOf("[" + foodNameMap + "]", gainIndex);
            if (bracketIndex > gainIndex) {
                String substring = lowerText.substring(gainIndex + 4, bracketIndex).trim();
                StringBuilder numberBuilder = new StringBuilder();
                for (int i = 0; i < substring.length(); i++) {
                    if (Character.isDigit(substring.charAt(i))) {
                        numberBuilder.append(substring.charAt(i));
                    }
                }
                if (numberBuilder.length() > 0) {
                    try {
                        return Integer.parseInt(numberBuilder.toString());
                    } catch (NumberFormatException e) {
                        return 1;
                    }
                }
            }
        }
        return 1; 
    }
    
    @Override
    public String toString(){
        return "Ability{"+trigger+": '"+rawText+"'}";
    }
}
