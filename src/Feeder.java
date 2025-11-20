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
                case 0 -> foodDice.add("seed");
                case 1 -> foodDice.add("fish");
                case 2 -> foodDice.add("berry");
                case 3 -> foodDice.add("insect");
                case 4 -> foodDice.add("rat");
                case 5 -> foodDice.add("seed insect");
            }
        }
    }

    public int getImageIndex(int i){
        String k = getDice().get(i);
        switch(k){
            case "seed": return 0;
            case "fish": return 1;
            case "berry": return 2;
            case "insect": return 3;
            case "rat": return 4;
            case "seed insect": return 5;
        }
        return -1;
    }
    public int getOutImageIndex(int i){
        String k = getOutDice().get(i);
        switch(k){
            case "seed": return 0;
            case "fish": return 1;
            case "berry": return 2;
            case "insect": return 3;
            case "rat": return 4;
            case "seed insect": return 5;
        }
        return -1;
    }

    public void takeDice(int f,int player){
        state.players[player].addFood(foodDice.get(f));
        outDice.add(foodDice.get(f));
        foodDice.remove(f);
    }

    public ArrayList<String> getDice() {return foodDice;}
    public ArrayList<String> getOutDice() {return outDice;}

}
    




