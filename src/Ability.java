package src;

import java.util.Locale;



public class Ability {

    
    public void printstatement() {
        System.out.println("testing");
    }

    public enum Trigger { BROWN, WHITE, PINK, NONE }

    public final Trigger trigger;
    public final String rawText;

    public Ability(Trigger trigger, String rawText){
        this.trigger = trigger;
        this.rawText = rawText == null ? "" : rawText;
    }

 
    public boolean mentionsTuck(){
        printstatement();
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
