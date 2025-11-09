package src;
import java.util.*;
public class Feeder {
     private final ProgramState state;
     private ArrayList<String> foodDice=new ArrayList<>();
     public Feeder(ProgramState state){
        this.state = state;
     
    
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
    for(int i=0;i<5;i++){
        int roll=(int)(Math.random()*6);
    switch(roll){
        case 0 -> foodDice.add("Seed");
        case 1 -> foodDice.add("Fruit");
        case 2 -> foodDice.add("Invertebrate");
        case 3 -> foodDice.add("Fish");
        case 4 -> foodDice.add("Rodent");
        case 5 -> foodDice.add("Seed/Invertebrate");
    }
    }
}

    public void takeDie(int index,int player){
    String food=foodDice.get(index);
    switch(player){
        case 1 -> {ArrayList<String> temp = state.playerOne.getFoods();
            temp.add(food);
            state.playerOne.setFoodInHand(temp);
        }
         case 2 -> {ArrayList<String> temp = state.playerTwo.getFoods();
            temp.add(food);
            state.playerTwo.setFoodInHand(temp);
        }
         case 3 -> {ArrayList<String> temp = state.playerThree.getFoods();
            temp.add(food);
            state.playerThree.setFoodInHand(temp);
        }
         case 4 -> {ArrayList<String> temp = state.playerFour.getFoods();
            temp.add(food);
            state.playerFour.setFoodInHand(temp);
        }
    }

}
}
    




