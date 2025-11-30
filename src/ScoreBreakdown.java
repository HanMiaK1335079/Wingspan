package src;

public class ScoreBreakdown {
    public final int printedPoints;
    public final int eggs;
    public final int cachedFood;
    public final int tuckedCards;
    public final int flocked;

    public ScoreBreakdown(int printedPoints, int eggs, int cachedFood, int tuckedCards, int flocked){
        this.printedPoints = printedPoints;
        this.eggs = eggs;
        this.cachedFood = cachedFood;
        this.tuckedCards = tuckedCards;
        this.flocked = flocked;
    }

    public int total(){
        return printedPoints + eggs + cachedFood + tuckedCards + flocked;
    }
}
