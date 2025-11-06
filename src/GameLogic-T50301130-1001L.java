package src;

public class GameLogic implements Runnable {
     private final FramePanel panel;
     private final ProgramState state;
      public GameLogic(FramePanel panel, ProgramState state) {
        this.panel = panel;
        this.state = state;
    }
    @Override
    public void run() {
        
    }
    
    
    
    public void setUp(){
      
      //Instantiate deck and shuffle To be implemented later

      //Deal 5 cards to each player
      for(int i=0;i<5;i++){
        state.playerOne.addNewCardToHand(state.deckOfCards.remove(state.deckOfCards.size()-1));
      }
       for(int i=0;i<5;i++){
        state.playerTwo.addNewCardToHand(state.deckOfCards.remove(state.deckOfCards.size()-1));
      }  
       for(int i=0;i<5;i++){
        state.playerThree.addNewCardToHand(state.deckOfCards.remove(state.deckOfCards.size()-1));
      }  
       for(int i=0;i<5;i++){
        state.playerFour.addNewCardToHand(state.deckOfCards.remove(state.deckOfCards.size()-1));
      }
      //This iterates though each of the players and gives them the cards that they can have.
      //Gives player ONE 1 of each food type
      state.playerOne.addFoodToHand("Seed"); 
      state.playerOne.addFoodToHand("Fruit");
      state.playerOne.addFoodToHand("Invertebrate");
      state.playerOne.addFoodToHand("Fish");
      state.playerOne.addFoodToHand("Rodent");

      //Gives player TWO 1 of each food type
      state.playerTwo.addFoodToHand("Seed");
      state.playerTwo.addFoodToHand("Fruit");
      state.playerTwo.addFoodToHand("Invertebrate");
      state.playerTwo.addFoodToHand("Fish");
      state.playerTwo.addFoodToHand("Rodent");

      //Gives player THREE 1 of each food type
      state.playerThree.addFoodToHand("Seed");
      state.playerThree.addFoodToHand("Fruit");
      state.playerThree.addFoodToHand("Invertebrate");
      state.playerThree.addFoodToHand("Fish");
      state.playerThree.addFoodToHand("Rodent");

      //Gives player FOUR 1 of each food type
      state.playerFour.addFoodToHand("Seed");
      state.playerFour.addFoodToHand("Fruit");
      state.playerFour.addFoodToHand("Invertebrate");
      state.playerFour.addFoodToHand("Fish");
      state.playerFour.addFoodToHand("Rodent");

      state.CURRENTEVENT.add("Decide Hand And Food Player One");

    }

}
