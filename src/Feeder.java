package src;
import java.util.*;
public class Feeder {
     private final ProgramState state;
     private ArrayList<Food.FoodType> foodDice=new ArrayList<>();
     private ArrayList<Food.FoodType> outDice=new ArrayList<>();
     public Feeder(ProgramState state){
        this.state = state;
        reRoll();
    }

    public boolean canReroll(){
        if(foodDice.size()>1){
            Food.FoodType one=foodDice.get(0);
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
                case 0 -> foodDice.add(Food.FoodType.SEED);
                case 1 -> foodDice.add(Food.FoodType.FISH);
                case 2 -> foodDice.add(Food.FoodType.BERRY);
                case 3 -> foodDice.add(Food.FoodType.INSECT);
                case 4 -> foodDice.add(Food.FoodType.RAT);
                case 5 -> foodDice.add(Food.FoodType.SEED_INSECT);
            }
        }
    }

    public int getImageIndex(int i){
        Food.FoodType k = getDice().get(i);
        switch(k){
            case SEED: return 0;
            case FISH: return 1;
            case BERRY: return 2;
            case INSECT: return 3;
            case RAT: return 4;
            case SEED_INSECT: return 5;
        }
        return -1;
    }
    public int getOutImageIndex(int i){
        Food.FoodType k = getOutDice().get(i);
        switch(k){
            case SEED: return 0;
            case FISH: return 1;
            case BERRY: return 2;
            case INSECT: return 3;
            case RAT: return 4;
            case SEED_INSECT: return 5;
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
            Food.FoodType diceValue = foodDice.get(f);
            if (diceValue.equals(Food.FoodType.SEED_INSECT)) {
                if (subIndex == 0) {
                    state.players[player].addFood(Food.FoodType.SEED, 1);
                    outDice.add(Food.FoodType.SEED);
                } else if (subIndex == 1) {
                    state.players[player].addFood(Food.FoodType.INSECT, 1);
                    outDice.add(Food.FoodType.INSECT);
                }
                foodDice.remove(f);
            } else {
                takeDice(f, player);
            }
        }
    }
    
    public boolean isDualDie(int index) {
        if (index >= 0 && index < foodDice.size()) {
            return foodDice.get(index).equals(Food.FoodType.SEED_INSECT);
        }
        return false;
    }
    
    public Food.FoodType[] getDualDieOptions(int index) {
        if (isDualDie(index)) {
            return new Food.FoodType[]{Food.FoodType.SEED, Food.FoodType.INSECT};
        }
        return new Food.FoodType[]{};
    }

    public void removeDie(int i){
        foodDice.remove(i);
    }
    public boolean isEmpty() {
        return foodDice.isEmpty();
    }
    public ArrayList<Food.FoodType> getDice(){
        return foodDice;
    }
    public ArrayList<Food.FoodType> getOutDice() {return outDice;}
    public void removeFood(Food.FoodType food) {
        foodDice.remove(food);
    }

    public Food.FoodType takeRandomFood() {
        if (foodDice.isEmpty()) {
            return null;
        }
        return foodDice.remove((int) (Math.random() * foodDice.size()));
    }

    public int takeAll(String food) {
        int count = 0;
        Food.FoodType foodType = Food.FoodType.fromString(food);
        if (foodType != null) {
            for (int i = foodDice.size() - 1; i >= 0; i--) {
                if (foodDice.get(i) == foodType) {
                    foodDice.remove(i);
                    count++;
                }
            }
        }
        return count;
    }
}
    




