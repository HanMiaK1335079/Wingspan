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

    @Override
    public String toString(){
        return "Ability{"+trigger+": '"+rawText+"'}";
    }
}
