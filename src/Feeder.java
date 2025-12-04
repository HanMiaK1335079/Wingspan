package src;
import java.util.*;
public class Feeder {
     private final ProgramState state;
     private ArrayList<String> foodDice=new ArrayList<>();
     private ArrayList<String> outDice=new ArrayList<>();
     public Feeder(ProgramState state){
        this.state = state;
        reRoll();
    }

    public boolean canReroll(){
        if(foodDice.size()>1){
            String one=foodDice.get(0);
            for(int i=0;i<foodDice.size();i++){
                if(!foodDice.get(i).equals(one)){
                    return false;
                }
            }
        }
        return true;
    }

    public void reRoll(){
        foodDice.clear();
        outDice.clear();
        for(int i=0;i<5;i++){
            int roll=(int)(Math.random()*6);
            switch(roll){
                case 0 -> foodDice.add("s");
                case 1 -> foodDice.add("f");
                case 2 -> foodDice.add("b");
                case 3 -> foodDice.add("i");
                case 4 -> foodDice.add("r");
                case 5 -> foodDice.add("a");
            }
        }
    }

    public int getImageIndex(int i){
        String k = getDice().get(i);
        switch(k){
            case "s": return 0;
            case "f": return 1;
            case "b": return 2;
            case "i": return 3;
            case "r": return 4;
            case "a": return 5;
        }
        return -1;
    }
    public int getOutImageIndex(int i){
        String k = getOutDice().get(i);
        switch(k){
            case "s": return 0;
            case "f": return 1;
            case "b": return 2;
            case "i": return 3;
            case "r": return 4;
            case "a": return 5;
        }
        return -1;
    }
    
    public void takeDice(int f,int player){
        if (f >= 0 && f < foodDice.size()) {
            state.players[player].addFood(foodDice.get(f), 1);
            outDice.add(foodDice.get(f));
            foodDice.remove(f);
        }
    }
    
    public void takeDice(int f, int subIndex, int player) {
        if (f >= 0 && f < foodDice.size()) {
            String diceValue = foodDice.get(f);
            if (diceValue.equals("a")) {
                if (subIndex == 0) {
                    state.players[player].addFood("s", 1);
                    outDice.add("s");
                } else if (subIndex == 1) {
                    state.players[player].addFood("i", 1);
                    outDice.add("i");
                }
                foodDice.remove(f);
            } else {
                takeDice(f, player);
            }
        }
    }
    
    public boolean isDualDie(int index) {
        if (index >= 0 && index < foodDice.size()) {
            return foodDice.get(index).equals("a");
        }
        return false;
    }
    
    public String[] getDualDieOptions(int index) {
        if (isDualDie(index)) {
            return new String[]{"s", "i"};
        }
        return new String[]{};
    }

    public String removeDie(int i){
        return foodDice.remove(i);
    }
    public boolean isEmpty() {
        return foodDice.isEmpty();
    }
    public ArrayList<String> getDice(){
        return foodDice;
    }
    public ArrayList<String> getOutDice() {return outDice;}
    public void removeFood(String food) {
        foodDice.remove(food);
    }

    public String takeRandomFood() {
        if (foodDice.isEmpty()) {
            return null;
        }
        return foodDice.remove((int) (Math.random() * foodDice.size()));
    }

    public int takeAll(String food) {
        int count = 0;
        String foodType = switch(food.toLowerCase()) {
            case "seed" -> "s";
            case "fish" -> "f";
            case "berry" -> "b";
            case "insect" -> "i";
            case "invertebrate" -> "i";
            case "rat" -> "r";
            case "wild" -> "a";
            default -> food;
        };
        
        for (int i = foodDice.size() - 1; i >= 0; i--) {
            if (foodDice.get(i).equals(foodType)) {
                foodDice.remove(i);
                count++;
            }
        }
        return count;
    }
    public void rollOutDice(){
        int length = outDice.size();
        outDice.clear();
        for(int i=0;i<length;i++){
            int roll=(int)(Math.random()*6);
            switch(roll){
                case 0 -> outDice.add("s");
                case 1 -> outDice.add("f");
                case 2 -> outDice.add("b");
                case 3 -> outDice.add("i");
                case 4 -> outDice.add("r");
                case 5 -> outDice.add("a");
            }
        }
    }

}
