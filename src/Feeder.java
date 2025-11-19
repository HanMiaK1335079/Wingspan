package src;
import java.util.*;
public class Feeder {
     private final ProgramState state;
     private ArrayList<String> foodDice=new ArrayList<>();
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
        for(int i=0;i<5;i++){
            int roll=(int)(Math.random()*6);
            switch(roll){
                case 0 -> foodDice.add("seed");
                case 1 -> foodDice.add("fish");
                case 2 -> foodDice.add("berry");
                case 3 -> foodDice.add("insect");
                case 4 -> foodDice.add("rat");
                case 5 -> foodDice.add("seed/insect");
            }
        }
    }

    public void takeDie(String f,int player){
        switch(player){
            case 0 -> state.players[0].addFood(f);
            case 1 -> state.players[1].addFood(f);
            case 2 -> state.players[2].addFood(f);
            case 3 -> state.players[3].addFood(f);
        }

    }

    public ArrayList<String> getDice() {return foodDice;}
}
    




