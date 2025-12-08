package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import static java.lang.System.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private BufferedImage cover, infoButton, bg, exitPic, leftArrow, rightArrow, birdBack, wheatToken, invertebrateToken, fishToken, fruitToken, rodentToken, Continue_Button, feederPic, Action_Button, Score_By_Round, Reroll_Button, skip, Clear_Button;
    private BufferedImage[] dicePics = new BufferedImage[6];
    private BufferedImage[] rulePics = new BufferedImage[12];
    private final ProgramState state;
    private Rectangle startButtonRect = new Rectangle(700, 700, 200, 100);
    private boolean hover = false;
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 64);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 28);
    private final ArrayList<Bird> birds = new ArrayList<>();
    private BufferedImage ingameBg;
    private Feeder feeder;

    private ArrayList<Integer> roundGoals = new ArrayList<>();
    private BufferedImage[] roundPics = new BufferedImage[4];
    private boolean setUp = false;

    private Map<String, ArrayList<String>> bonusMap = new HashMap<>();
    private String[] bonuses = {"Anatomist", "Cartographer", "Historian", "Photographer", "Backyard Birder", "Bird Bander", "Bird Counter", "Bird Feeder", "Diet Specialist", "Enclosure Builder", "Species Protector", "Falconer", "Fishery Manager", "Food Web Expert", "Forester", "Large Bird Specialist", "Nest Box Builder", "Omnivore Expert", "Passerine Specialist", "Platform Builder", "Prairie Manager", "Rodentologist", "Small Clutch Specialist", "Viticulturalist", "Wetland Scientist", "Wildlife Gardener"};
    private ArrayList<Bonus> bonusArr = new ArrayList<>();
    private int[][] diceLocMap = new int[5][2];
    /*Gamestate variables */

    //Startselection variables
    boolean[] startSelections; //1-5 for birds, 6-10 for foods, 11-12 for bonus
    private int numberOfItemsSelected = 0;
    private int numberOfBonusesSelected = 0;
    Bird[] startOptions;
    Bonus[] bonusOptions;

    //Game run variables
    

    public FramePanel(ProgramState state){
        
        this.state = state;
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        feeder = new Feeder(state);
        

        for (int i=0;i<4;i++){
            state.players[i] = new Player();
            }
         
        
        //Add all buffered images here 
        try{
            cover = loadImage("/assets/cover_image.png", "assets/cover_image.png");
            infoButton = loadImage("/assets/info picture.png", "assets/info picture.png");
            bg = loadImage("/assets/table_bg.png", "assets/table_bg.png");
            Reroll_Button = loadImage("/assets/Reroll.png", "assets/Reroll.png");
            skip = loadImage("/assets/skip.png", "assets/skip.png");
            Clear_Button = loadImage("/assets/fghjk-Picsart-BackgroundRemover.jpeg", "assets/fghjk-Picsart-BackgroundRemover.jpeg");

        } catch (Exception e){
            System.out.println("No workie because idk 🤷‍♂️");
            System.out.println(e);
        }
        //readCSV(new File("src/birdInfo.csv"));
        this.repaint();
    }
    
    // Helper method to load images with fallback to file system
    private BufferedImage loadImage(String resourcePath, String filePath) throws Exception {
        try {
            URL resource = FramePanel.class.getResource(resourcePath);
            if (resource != null) {
                return ImageIO.read(resource);
            }
        } catch (Exception e1) {
            System.err.println("Failed to load from classpath: " + resourcePath);
        }
        // Try file system fallback
        return ImageIO.read(new File(filePath));
    }
    
    // update button geometry based on current panel size
    private void updateStartButtonRect() {
        int w = getWidth() > 0 ? getWidth() : 1600;
        int h = getHeight() > 0 ? getHeight() : 900;
        int btnW = Math.max(200, w / 6);
        int btnH = Math.max(60, h / 12);
        int x = (w - btnW) / 2;
        int y = h - btnH - Math.max(60, h / 12);
        startButtonRect.setBounds(x, y, btnW, btnH);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
            
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
      

    }
    Bird currentBird;
    boolean selectingBool;
    boolean selectingFood;
    int[] yesCrds = {510, 790, 385, 515};
    int[] noCrds = {810, 1090, 385, 515};
    String nestType;
    int runCount = 0;
    int playingHolder;
    int currentBirdNum;
    //int habitatActCount;
    String runningHabitat;
    boolean traded = false;
    boolean canTrade = false;
    Bird highlighted;
    @Override
    public void mousePressed(MouseEvent e) {
          int x = e.getX();
        int y = e.getY();
        out.println("("+x+","+y+")");
        //out.println(state.CURRENTEVENT);
        // Only respond to clicks when we are on the start screen
        switch(state.CURRENTEVENT.getLast()){

            case "Game Start" -> {
                Point p = e.getPoint();
                // Update rect position in case panel was resized
                updateStartButtonRect();
                if (startButtonRect.contains(p)) {
                    synchronized(state.lock) {
                        System.out.println("Start button clicked");
                        state.CURRENTEVENT.add("Select Screen");
                        startSetUp();
                        setUpSelection();
                        this.repaint();
                        
                        state.lock.notifyAll();
                        new GameLogic(this, state);
                        //  gameLogic.setUp(); //this has yet to be set up.
                    }
                }
            }
            //food click
            case "Select Screen" ->{
                if (x>=1200 && x<=1300 && y>=130 && y<=220){
                    if(numberOfItemsSelected<5&&!startSelections[5]){
                        startSelections[5] = !startSelections[5];
                        numberOfItemsSelected++;
                    }else if(startSelections[5]){
                        startSelections[5] = !startSelections[5];
                        numberOfItemsSelected--;
                    }
                }
            
                else if (x>=1200 && x<=1300 && y>=340 && y<=430){
                    if(numberOfItemsSelected<5&&!startSelections[8]){
                        startSelections[8] = !startSelections[8];
                        numberOfItemsSelected++;
                    }else if(startSelections[8]){
                        startSelections[8] = !startSelections[8];
                        numberOfItemsSelected--;
                    }
                }
                else if (x>=1320 && x<=1410 && y>=230 && y<=320){
                    if(numberOfItemsSelected<5&&!startSelections[7]){
                        startSelections[7] = !startSelections[7];
                        numberOfItemsSelected++;
                    }else if(startSelections[7]){
                        startSelections[7] = !startSelections[7];
                        numberOfItemsSelected--;
                    }
                }
                else if (x>=1440 && x<=1530 && y>=130 && y<=220){
                    if(numberOfItemsSelected<5&&!startSelections[6]){
                        startSelections[6] = !startSelections[6];
                        numberOfItemsSelected++;
                    }else if(startSelections[6]){
                        startSelections[6] = !startSelections[6];
                        numberOfItemsSelected--;
                    }
                }
                else if (x>=1440 && x<=1530 && y>=340 && y<=430){
                    if(numberOfItemsSelected<5&&!startSelections[9]){
                        startSelections[9] = !startSelections[9];
                        numberOfItemsSelected++;
                    }else if(startSelections[9]){
                        startSelections[9] = !startSelections[9];
                        numberOfItemsSelected--;
                    }
                }
                
                
                // //debug click
                else if (x>=140 && y>=700 && x<=290 && y<=780){
                    for (int i=0;i<5;i++) startSelections[i] = true;
                    startSelections[10] = true;
                }
                //birb click
                else if (y>=120 && y<=420){
                    for (int i=0;i<5;i++){
                        if (x>=30+i*220 && x<=230+i*220){
                            if(numberOfItemsSelected<5&&!startSelections[i]){
                                startSelections[i] = !startSelections[i];
                                numberOfItemsSelected++;
                            }else if(startSelections[i]){
                                startSelections[i] = !startSelections[i];
                                numberOfItemsSelected--;
                            }
                        }
                    } //30+i*220, 120, 200, 300
                }
                //continue click
                else if (x>=140 && x<=440 && y>=600 && y<=720){
                    out.println("clicked box");
                    if (canContinue()){
                        out.println("Continuing");
                        numberOfItemsSelected = 0;
                        numberOfBonusesSelected = 0;
                        for (int i=0;i<5;i++){
                            if (startSelections[i]) state.players[state.playing].addCardToHand(startOptions[i]);
                        }//SFBIR  CHAGE BACK LATER
                        if (startSelections[5]) state.players[state.playing].addFood("s", 5);
                        if (startSelections[6]) state.players[state.playing].addFood("f", 5);
                        if (startSelections[7]) state.players[state.playing].addFood("b", 5);
                        if (startSelections[8]) state.players[state.playing].addFood("i", 5);
                        if (startSelections[9]) state.players[state.playing].addFood("r", 5);
                        if (startSelections[10]) state.players[state.playing].addBonus(bonusOptions[0]);
                        else state.players[state.playing].addBonus(bonusOptions[1]);
                        if (state.playing == 3) {state.CURRENTEVENT.add("Game"); state.playing = 0;}
                        else {
                            state.playing ++;
                            setUpSelection();
                        }
                    }
                
                    //bonus click
                }else if (y>=500 && y<=800){
                    if (x>=550 && x<=750){
                        if(numberOfBonusesSelected<1){
                            startSelections[10] = !startSelections[10];
                            numberOfBonusesSelected++;
                        }else if(!startSelections[10]){ 
                            startSelections[10] = !startSelections[10];
                            startSelections[11] = !startSelections[11];
                            
                        }
                    }else if (x>=800 && x<=1000){
                        if(numberOfBonusesSelected<1){
                            startSelections[11] = !startSelections[11];
                            numberOfBonusesSelected++;
                        }else if(!startSelections[11]){
                            startSelections[10] = !startSelections[10];
                            startSelections[11] = !startSelections[11];
                        }
                    }
                }
            this.repaint();
        
            }case "Game" -> {
                // g.drawImage(Action_Button, 480, 120, 50 ,50, null);
        //         g.drawImage(Action_Button, 480, 120, 50 ,50, null);
        // g.drawImage(Action_Button, 425, 200, 50,50,null);
        // g.drawImage(Action_Button, 425, 450, 50,50,null);
        // g.drawImage(Action_Button, 350, 725, 50,50,null);
                if (x>=184 && x<=231 && y>=180 && y<=222) state.CURRENTEVENT.add("View Birds");
                else if (x>=190 && x<=235 && y>=440 && y<=484) state.CURRENTEVENT.add("View Bonus");
                else if (x>=37 && x<=83 && y>=683 && y<=726) state.CURRENTEVENT.add("View Feeder");
                else if (x>=508 && x<=589 && y>=22 && y<=86) state.CURRENTEVENT.add("Info");
                else if (x>=1375 && x<=1425 && y>=615 && y<=665) state.CURRENTEVENT.add("View Draw Birds");
                else if (x>=480 && x<=530 && y>=120 && y<=170) {state.CURRENTEVENT.add("Play Bird");
                    state.specificBirdToPlay=null;
                    state.birdFoodsForPlayingBird=new int[5];
                }
                else if (x>=440 && x<=463 && y>=214 && y<=238){
                    if (state.players[state.playing].getBirdsInHabitat("forest").size()!=0)
                        state.CURRENTEVENT.add("On Activate Ability");
                    //state.CURRENTEVENT.add("Select Food");
                    runningHabitat = "forest";
                    currentBirdNum = state.players[state.playing].getBoard().getBirdsInHabitat("forest").size()-1;
                    if (currentBirdNum!=-1)currentBird = state.players[state.playing].getBirdsInHabitat("forest").get(currentBirdNum);
                    /*add counter for habitat action */
                    switch (state.players[state.playing].getBoard().getBirdsInHabitat("forest").size()){
                        case 5 -> {
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 4 -> {
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                        }
                        case 3 -> {
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 2 -> {
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Select Food");
                        }
                        case 1 -> {
                            state.CURRENTEVENT.add("Select Food");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 0 -> {state.CURRENTEVENT.add("Select Food");}
                    }
                }else if (x>=439 && x<=463 && y>=462 && y<=488 && state.players[state.playing].canLayEggs()){
                    if (state.players[state.playing].getBirdsInHabitat("grasslands").size()!=0)
                        state.CURRENTEVENT.add("On Activate Ability");
                    //state.CURRENTEVENT.add("Lay Eggs");
                    runningHabitat = "grasslands";
                    currentBirdNum = state.players[state.playing].getBoard().getBirdsInHabitat("grasslands").size()-1;
                    if (currentBirdNum!=-1)currentBird = state.players[state.playing].getBirdsInHabitat("grasslands").get(currentBirdNum);
                    switch (state.players[state.playing].getBoard().getBirdsInHabitat("grasslands").size()){
                        case 5 -> {
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 4 -> {
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                        }
                        case 3 -> {
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 2 -> {
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                        }
                        case 1 -> {
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Lay Eggs");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 0 -> {state.CURRENTEVENT.add("Lay Eggs");state.CURRENTEVENT.add("Lay Eggs");}
                        
                    }
                }else if (x>=366 && x<=387 && y>=736 && y<=759){
                    if (state.players[state.playing].getBirdsInHabitat("wetlands").size()!=0)
                        state.CURRENTEVENT.add("On Activate Ability");
                    //state.CURRENTEVENT.add("Draw Birds");
                    runningHabitat = "wetlands";
                    currentBirdNum = state.players[state.playing].getBoard().getBirdsInHabitat("wetlands").size()-1;
                    if (currentBirdNum!=-1)currentBird = state.players[state.playing].getBirdsInHabitat("wetlands").get(currentBirdNum);
                    switch (state.players[state.playing].getBoard().getBirdsInHabitat("wetlands").size()){
                        case 5 -> {
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 4 -> {
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                        }
                        case 3 -> {
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 2 -> {
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Draw Birds");
                        }
                        case 1 -> {
                            state.CURRENTEVENT.add("Draw Birds");
                            state.CURRENTEVENT.add("Trade");
                        }
                        case 0 -> {state.CURRENTEVENT.add("Draw Birds");}
                    }
                }
                tradeStarted = false;
                repaint();
            }case "Trade" -> {
                if (!tradeStarted){
                    if(state.players[state.playing].getCardsInHand().isEmpty()){state.CURRENTEVENT.removeLast(); repaint(); return;}

                        canTrade = true;
                    if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]){
                        if (runningHabitat.equals("grasslands") && !state.players[state.playing].hasFood()){
                            state.CURRENTEVENT.removeLast();
                            state.CURRENTEVENT.add("No Food");
                        }
                        else{
                            tradeStarted = true;
                            if (runningHabitat.equals("wetlands")){
                                state.CURRENTEVENT.removeLast();
                                state.CURRENTEVENT.add("Draw Birds");
                                state.CURRENTEVENT.add("Remove Egg");
                            }
                        }
                    }else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]){
                        state.CURRENTEVENT.removeLast();
                    }
                }
                else if (runningHabitat.equals("forest")){
                    if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()/showing)
                        currentShowing++;
                    else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;

                    for (int i=0;i<4;i++){
                        if (x>=250+250*i && y>=500 && x<=490+250*i && y<=825){
                            if (currentShowing*4+i<state.players[state.playing].getCardsInHand().size()){
                                state.players[state.playing].getCardsInHand().remove(currentShowing*4+i);
                                state.CURRENTEVENT.removeLast();
                                state.CURRENTEVENT.add("Select Food");
                            }
                        }
                    }

                }
                else if (runningHabitat.equals("grasslands")){
                    String[] fo = {"s", "f", "b", "i", "r"};
                    for (int i=0;i<5;i++){
                        if (x>=250+100*i && x<=350+100*i && y>=550 && y<=650){
                            if (state.players[state.playing].hasFoodType(fo[i])){
                                state.players[state.playing].removeFood(fo[i], 1);
                                state.CURRENTEVENT.removeLast();
                                state.CURRENTEVENT.add("Lay Eggs");
                            }
                        }
                    }
                    
                }
                repaint();
            }case "View Birds" -> {
                if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
                else if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()/showing)
                    currentShowing++;
                else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;
                //50, 590, 60, 60

                repaint();
            }case "View Bonus" -> {
                if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
                repaint();
            
            }case "View Draw Birds" ->{
                if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
                endTurn(ProgramState.PlayerAction.DRAW_CARDS);
                repaint();

            }case "View Feeder" -> {
                if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
                repaint();

            }case "Info" ->{
                if (x>=30 && x<=120 && y>=30 && y<=120) state.CURRENTEVENT.removeLast();
                else if (x>=400 && x<=600 && y>=400 && y<=500) state.CURRENTEVENT.add("Rules");       // g.fillRect(400, 400, 200, 100);
                repaint();
            
            }case "Rules" ->{
                if (x>=30 && x<=120 && y>=30 && y<=120) state.CURRENTEVENT.removeLast();
                else if (x>=1400 && x<=1470 && y>=450 && y<=520 && rulePage<11) rulePage ++;
                else if (x>=100 && x<=170 && y>=450 && y<=520 && rulePage>0) rulePage --;
                repaint();
            }case "Select Food" -> {
               
                if (selectingBool&&x>=500 && x<=1100 && y>=250 && y<=525){
                    if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]) {state.players[state.playing].addFood("s", 1);state.CURRENTEVENT.removeLast();}
                    else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]) {state.players[state.playing].addFood("i", 1);state.CURRENTEVENT.removeLast();}
                    selectingBool = false;
                }
            
                else{
                    if (feeder.canReroll() && x>=1185 && x<=1300 && y>=480 && y<=605&&!selectingBool) feeder.reRoll();
                    for (int i=0;i<5;i++){
                        if (x>=diceLocMap[i][0] && x<=diceLocMap[i][0]+90 && y>=diceLocMap[i][1] && y<=diceLocMap[i][1]+90){
                            if (feeder.getDice().get(i).equals("a")) selectingBool = true;
                            else {state.players[state.playing].addFood(feeder.getDice().get(i), 1);state.CURRENTEVENT.removeLast();}
                            feeder.getOutDice().add(feeder.getDice().remove(i));
                        }
                    }
                    
                }
            
                repaint();
            }case "Remove Egg" ->{
                Bird[][] birdBoard = state.players[state.playing].getPlayerBoard();
                for (int i=0;i<3;i++){
                    for (int j=0;j<5;j++){
                        if (x>=470+170*j && x<=644+170*j && y>=155+247*i && y<= 375+247*i){
                            if (birdBoard[i][j]!=null){
                                if (birdBoard[i][j].getEggCount()>0){
                                    birdBoard[i][j].removeEggs(1);
                                    state.CURRENTEVENT.removeLast();
                                }
                            }
                        }
                    }
                }
                //out.println("Removing eggz");
                repaint();
            }case "Lay Eggs" ->{
                if(!state.players[state.playing].canLayEggs()) {
                    state.CURRENTEVENT.removeLast();
                    repaint();
                    return;
                }
                Bird[][] birdBoard = state.players[state.playing].getPlayerBoard();
                for (int i=0;i<3;i++){
                    for (int j=0;j<5;j++){
                        if (x>=470+170*j && x<=644+170*j && y>=155+247*i && y<= 375+247*i){
                            if (birdBoard[i][j]!=null){
                                if (birdBoard[i][j].getEggCount()<birdBoard[i][j].getMaxEggs()){
                                    birdBoard[i][j].addEggs(1);
                                    state.CURRENTEVENT.removeLast();
                                }
                            }
                        }
                    }
                }
                //out.println("Removing eggz");
                repaint();
            }
            case "Draw Birds" -> {
                if (x>=20 && x<=70 && y>=400 && y<=450){ 
                    state.CURRENTEVENT.removeLast();
                    repaint();
                    return;
                }
                if (y>=470 && y<=825){//This is the Y level of the cards in the draw tray
                    if(x>=475 && x<=720){//This would be the first card
                        if(state.cardTray[0]!=null){
                            state.players[state.playing].addCardToHand(state.cardTray[0]);
                            state.cardTray[0] = null;
                            state.CURRENTEVENT.removeLast(); 
                        }
                    } else if ( x >= 745 && x <= 990 ) {// This would be the second card
                        if(state.cardTray[1]!=null){
                            state.players[state.playing].addCardToHand(state.cardTray[1]);
                            state.cardTray[1] = null;
                            state.CURRENTEVENT.removeLast(); 
                        }
                    } else if ( x >= 1015 && x <= 1260 ) {//This would be the third card
                        if(state.cardTray[2]!=null){
                            state.players[state.playing].addCardToHand(state.cardTray[2]);
                            state.cardTray[2] = null;
                            state.CURRENTEVENT.removeLast(); 
                        }
                    }
                }
                if(x>=120&&x<=335&&y>=515&&y<=775){//120, 515, 215, 2 60,
                    out.println("Clicked the bird deck");
                    state.players[state.playing].addCardToHand(birds.removeFirst());
                    state.CURRENTEVENT.removeLast(); 
                }
                repaint();
            }case "Play Bird" -> {
                   //     g2.drawRect(470, 155, 628-470, 392-155);
            // g2.drawRect(469, 403, 627-469, 637-403);
            // g2.drawRect(470,650,626-470,866-650);

            // g2.drawRect(644, 155, 800-644, 392-155);
            // g2.drawRect(644, 403, 800-644, 637-403);
            // g2.drawRect(644,650,800-644,866-650);

            // g2.drawRect(815, 155, 969-815, 392-155);
            // g2.drawRect(815, 403, 969-815, 637-403);
            // g2.drawRect(815,650,969-815,866-650);

            // g2.drawRect(985, 155, 1138-985, 392-155);
            // g2.drawRect(985, 403, 1138-985, 637-403);
            // g2.drawRect(985,650,1138-985,866-650);

            // g2.drawRect(1152, 155, 1302-1152, 392-155);
            // g2.drawRect(1152, 403, 1302-1152, 637-403);
            // g2.drawRect(1152,650,1302-1152,866-650);
            if(x>=470 && x<=628 && y>=155 && y<=392){ state.squaresClickedToPlayBird[0][0] = !state.squaresClickedToPlayBird[0][0]; state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="forest";} 
            if(x>=469 && x<=627 && y>=403 && y<=637) {state.squaresClickedToPlayBird[1][0] = !state.squaresClickedToPlayBird[1][0]; state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="plains";}
            if(x>=470 && x<=626 && y>=650 && y<=866){ state.squaresClickedToPlayBird[2][0] = !state.squaresClickedToPlayBird[2][0];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="wetlands";} 

            if(x>=644 && x<=800 && y>=155 && y<=392) {state.squaresClickedToPlayBird[0][1] = !state.squaresClickedToPlayBird[0][1];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="forest";} 
            if(x>=644 && x<=800 && y>=403 && y<=637) {state.squaresClickedToPlayBird[1][1] = !state.squaresClickedToPlayBird[1][1];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="plains";} 
            if(x>=644 && x<=800 && y>=650 && y<=866) {state.squaresClickedToPlayBird[2][1] = !state.squaresClickedToPlayBird[2][1];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="wetlands";} 

        if(x>=815 && x<=969 && y>=155 && y<=392){ state.squaresClickedToPlayBird[0][2] = !state.squaresClickedToPlayBird[0][2];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="forest";} 
        if(x>=815 && x<=969 && y>=403 && y<=637){ state.squaresClickedToPlayBird[1][2] = !state.squaresClickedToPlayBird[1][2];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="plains";} 
        if(x>=815 && x<=969 && y>=650 && y<=866){ state.squaresClickedToPlayBird[2][2] = !state.squaresClickedToPlayBird[2][2];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="wetlands";} 
        
        if(x>=985 && x<=1138 && y>=155 && y<=392){ state.squaresClickedToPlayBird[0][3] = !state.squaresClickedToPlayBird[0][3];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="forest";} 
        if(x>=985 && x<=1138 && y>=403 && y<=637) {state.squaresClickedToPlayBird[1][3] = !state.squaresClickedToPlayBird[1][3];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="plains";} 
        if(x>=985 && x<=1138 && y>=650 && y<=866) {state.squaresClickedToPlayBird[2][3] = !state.squaresClickedToPlayBird[2][3];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="wetlands";} 
        
        if(x>=1152 && x<=1302 && y>=155 && y<=392) {state.squaresClickedToPlayBird[0][4] = !state.squaresClickedToPlayBird[0][4];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="forest";} 
        if(x>=1152 && x<=1302 && y>=403 && y<=637){ state.squaresClickedToPlayBird[1][4] = !state.squaresClickedToPlayBird[1][4];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="plains";} 
        if(x>=1152 && x<=1302 && y>=650 && y<=866){ state.squaresClickedToPlayBird[2][4] = !state.squaresClickedToPlayBird[2][4];state.CURRENTEVENT.removeLast();state.CURRENTEVENT.add("Play Specific Bird");state.habitatToPlayBird="wetlands";} 
        repaint();
            
        }
        case "Wait For Second Part Play Specific Bird" -> {
          if (x>=20 && x<=70 && y>=400 && y<=450){ state.CURRENTEVENT.removeLast(); state.CURRENTEVENT.removeLast();state.specificBirdToPlay=null;currentShowing=0;
                for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
            }
                else if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != (state.players[state.playing].getCardsInHand().size()-1)/4)
                    currentShowing++;
                else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;
                //(253,504)->(489,825)
                //(504,504)->(740,825)
                //(755,504)->(991,825)
                //(100,504)->(1237,825)
                int position=0;
                for(int i=0;i<3;i++){
                    for(int j=0;j<5;j++){
                        if(state.squaresClickedToPlayBird[i][j]){
                            position=j;
                            for(int a=j;a>-1;a--){
                                if(state.players[state.playing].getBoard().getBoard()[i][a]==null){
                                    position=a;
                            }
                        }
                        }
                    }
                }
                out.println(state.players[state.playing].getCardsInHand().get(currentShowing*4+0));
                if(x>=253 && x<=489 && y>=504 && y<=825&&state.players[state.playing].getCardsInHand().get(currentShowing*4+0).canLiveInHabitat(state.habitatToPlayBird)) {
                    // out.println("Clicked first card to play");
                    // if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+0),state.habitatToPlayBird,position ))
                    //     state.players[state.playing].getCardsInHand().remove(currentShowing*showing+0);
                    // for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    // state.CURRENTEVENT.removeLast();
                    state.specificBirdToPlay=state.players[state.playing].getCardsInHand().get(currentShowing*4+0);
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.add("Pick Food For Specific Bird");
                } else if ( x >= 504 && x <= 740 && y >= 504 && y <= 825&&state.players[state.playing].getCardsInHand().get(currentShowing*4+1).canLiveInHabitat(state.habitatToPlayBird) ) {
                    out.println("Clicked Second card to play");
                //    if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+1),state.habitatToPlayBird, position))
                //         state.players[state.playing].getCardsInHand().remove(currentShowing*showing+1);
                //     for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                //     state.CURRENTEVENT.removeLast();
                    state.specificBirdToPlay=state.players[state.playing].getCardsInHand().get(currentShowing*4+1);
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.add("Pick Food For Specific Bird");
                } else if ( x >= 755 && x <= 991 && y >= 504 && y <= 825&&state.players[state.playing].getCardsInHand().get(currentShowing*4+2).canLiveInHabitat(state.habitatToPlayBird) ) {
                    out.println("Clicked Third card to play");
                    // if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+2),state.habitatToPlayBird, position))
                    //     state.players[state.playing].getCardsInHand().remove(currentShowing*showing+2);
                    // for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    // state.CURRENTEVENT.removeLast();
                    state.specificBirdToPlay=state.players[state.playing].getCardsInHand().get(currentShowing*4+2);
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.add("Pick Food For Specific Bird");
                } else if ( x >= 1000 && x <= 1237 && y >= 504 && y <= 825&&state.players[state.playing].getCardsInHand().get(currentShowing*4+3).canLiveInHabitat(state.habitatToPlayBird) ) {
                //     out.println("Clicked Fourth card to play");
                //    if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+3),state.habitatToPlayBird, position))
                //         state.players[state.playing].getCardsInHand().remove(currentShowing*showing+3);
                //     for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                //      state.CURRENTEVENT.removeLast();
                    state.specificBirdToPlay=state.players[state.playing].getCardsInHand().get(currentShowing*4+3);
                     state.CURRENTEVENT.removeLast();
                     state.CURRENTEVENT.add("Pick Food For Specific Bird");
                   
                }
                
                    
                 
                

                    
                
                repaint();
        }
        case "Choose Bird" -> {
            
            }case "When Played Ability" ->{
                //out.println(state.CURRENTEVENT);
                out.println("WHEN PLACED");
                if (x>=1000 && y>=550 && x<=1300 && y<=630) {state.CURRENTEVENT.removeLast();repaint();return;}
                out.println("Ability: "+currentBird.getAbility().getTrigger());
                if (currentBird.getAbility().getTrigger().equals("WP")){
                    out.println("activated when placed");
                    String abilityText = currentBird.getAbilityText();
                    Player p = state.players[state.playing];
                    if (abilityText.contains("a second bird")) {
                        /*OFFER A CHOICE */
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.add("Play Bird");
                    } else if (abilityText.contains("Gain 3 [seed] from the supply.")) {
                        p.addFood("s", 3);
                        state.CURRENTEVENT.removeLast();
                    } else if (abilityText.contains("Gain 3 [fish] from the supply.")) {
                        p.addFood("f", 3);
                        state.CURRENTEVENT.removeLast();
                    } else if (abilityText.contains("Gain all [fish] that are in the birdfeeder.")) {
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.add("View Feeder");
                        int fishGained = feeder.takeAll("fish");
                        p.addFood("f", fishGained);
                    } else if (abilityText.contains("Gain all [invertebrate] that are in the birdfeeder.")) {
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.add("View Feeder");
                        int invertebratesGained = feeder.takeAll("insect");
                        p.addFood("i", invertebratesGained);
                    } else if (abilityText.contains("Draw [card] equal to the number of players +1")) {
                        int numToDraw = state.players.length + 1;
                        loopDrawing = state.playing;
                        for (int i=0;i<5;i++) loopOptions.add(birds.remove(0));
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.add("Loop Draw");
                    } else if (abilityText.contains("Draw 2 [card]")) {
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.add("Draw Birds");
                        state.CURRENTEVENT.add("Draw Birds");
                    } else if (abilityText.contains("Lay 1 [egg] on each of your birds ")) {
                        String nestType = "";
                        if (abilityText.contains("cavity")) nestType = "cavity";
                        else if (abilityText.contains("ground")) nestType = "ground";
                        else if (abilityText.contains("platform")) nestType = "platform";
                        else if (abilityText.contains("bowl")) nestType = "bowl";
                        p.layEggsInNestType(nestType);
                        state.CURRENTEVENT.removeLast();
                    }
                     else if (abilityText.contains("Lay 1 [egg] on this bird.")) {
                        currentBird.addEggs(1); //IDK IF IT CHANGES THE BIRD THAT THE PLAYER HAS (IDK MUTATING RULS LOL)
                        state.CURRENTEVENT.removeLast();
                    } else if (abilityText.contains("Draw 2 new bonus cards and keep 1.")) {
                        state.CURRENTEVENT.removeLast();
                        bonusSelectOptions[0] = bonusArr.remove(0);
                        bonusSelectOptions[1] = bonusArr.remove(1);
                        state.CURRENTEVENT.add("Choose Bonus");
                    }
                }else{
                    state.CURRENTEVENT.removeLast();
                }
                out.println(state.CURRENTEVENT);
                repaint();
            }case "Choose Bonus" -> {
                if (x>=400 && x<=645 && y>=470 && y<=825) {state.players[state.playing].addBonus(bonusSelectOptions[0]);state.CURRENTEVENT.removeLast();}
                if (x>=800 && x<=1045 && y>=470 && y<=825) {state.players[state.playing].addBonus(bonusSelectOptions[1]);state.CURRENTEVENT.removeLast();}
                repaint();
            }case "Loop Draw" -> {
                for (int i=0;i<loopOptions.size();i++){
                    if (x>=120+230*i && x<=335+230*i && y>=515 && y<=775){
                        state.players[loopDrawing].addCardToHand(loopOptions.remove(i));
                        loopDrawing = (loopDrawing+1)%4;
                    }
                    //g.drawImage(loopOptions.get(i).getImage(), 120+230*i, 515, 215, 260, null);
                }
                
                if (loopOptions.size()<1) state.CURRENTEVENT.removeLast();
                repaint();
            }case "On Activate Ability" -> {
                String ability = currentBird.getAbilityText();
                out.println("Current Bird NUm: " + currentBirdNum);
                if (currentBirdNum>-1){
                    currentBird = state.players[state.playing].getBirdsInHabitat(runningHabitat).get(currentBirdNum);
                    ability = currentBird.getAbilityText();
                }else {currentBird = null;state.CURRENTEVENT.removeLast();}
                
                
                out.println("ON ACTIVATE");
                out.println("Ability: "+ability);
                if (currentBird!=null && currentBird.getAbility().getTrigger().equals("OA")){
                    if (x>=1260 && y>=16 && x<=1480 && y<=121){
                        currentBirdNum--;
                    }
                    else if (ability.contains("Gain 1 [seed] from the birdfeeder")){
                        if (feeder.getDice().contains("s")){
                            if (selectingBool){
                                if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]) {
                                    currentBird.cacheFood();
                                    currentBirdNum--;
                                    feeder.removeDie("s");
                                    selectingBool = false;
                                }
                                else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]) {
                                    state.players[state.playing].addFood("s",1);
                                    currentBirdNum--;
                                    feeder.removeDie("s");
                                    selectingBool = false;
                                }
                            }else{
                                state.CURRENTEVENT.add("View Feeder");
                                selectingBool = true;
                            }
                        }
                    }
                    else if (ability.contains("Tuck 1 [card]")){
                        if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()/showing)
                        currentShowing++;
                        else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;

                        for (int i=0;i<4;i++){
                            if (x>=250+250*i && y>=500 && x<=490+250*i && y<=825){
                                if (currentShowing*4+i<state.players[state.playing].getCardsInHand().size()){
                                    state.players[state.playing].getCardsInHand().remove(currentShowing*4+i);
                                    currentBird.tuckCard();
                                    currentBirdNum--;
                                    if (ability.contains("draw 1 [card]"))
                                        state.CURRENTEVENT.add("Draw Birds");
                                    else if (ability.contains("lay 1 [egg]"))
                                        currentBird.addEggs(1);
                                    else if (ability.contains("gain 1 [fruit]"))
                                        state.players[state.playing].addFood("b", 1);
                                    else if (ability.contains("gain 1 [seed]"))
                                        state.players[state.playing].addFood("s",1);
                                    else if (ability.contains("gain 1 [invertebrate] or [seed]"))
                                        out.println("I'm a lazy bum: Nuthatch ability");
                                }
                            }
                        }
                    }
                    else if (ability.contains("Discard 1 [egg] from any")){
                        state.CURRENTEVENT.add("Add Food");
                        if (ability.contains("2")) state.CURRENTEVENT.add("Add Food");
                        state.CURRENTEVENT.add("Remove Egg");
                    }
                    else if (ability.contains("Roll all dice not in birdfeeder")){
                        feeder.rollOutDice();
                        String check="";
                        if (ability.contains("[rodent]"))
                            check = "r";
                        else if (ability.contains("[fish]"))
                            check = "f";
                        if (feeder.getOutDice().contains(check)){
                            currentBird.cacheFood();
                        }
                        currentBirdNum--;
                        state.CURRENTEVENT.add("View Feeder");
                    }
                    else if (ability.contains("Each player gains 1 [die]")){
                        if (x>=1300 && y>=400 && x<=1400 && y<= 500) {
                            state.CURRENTEVENT.add("View Feeder");
                        }else if (y>=550 && y<=630){
                            for (int i=0;i<4;i++){
                                if (x>=250 + 110*i && x<=350 + 110*i){
                                    playingHolder = state.playing;
                                    state.playing = i;
                                    //add the event lol
                                    state.CURRENTEVENT.add("Loop Gain Food");
                                    currentBirdNum--;
                                }

                            }
                        }
                        
                    }
                    else if (ability.contains("Lay 1 [egg] on any bird")){
                        currentBirdNum--;
                        state.CURRENTEVENT.add("Lay Eggs");
                    }
                    else if (ability.contains("All players gain 1")){
                        String check = "";
                        if (ability.contains("[fruit]")) check = "b";
                        else if (ability.contains("[seed]")) check = "s";
                        else if (ability.contains("[invertebrate]")) check = "i";
                        else if (ability.contains("[fish]")) check = "f";

                        currentBirdNum--;
                        for (Player p: state.players) p.addFood(check, 1);
                    }
                    else if (ability.contains("Look at a [card] from the deck")){
                        int span = 0;
                        if (ability.contains("50")) span = 50;
                        else if (ability.contains("75")) span = 75;
                        else span = 100;

                        highlighted = birds.remove(0);
                        if (highlighted.getWingspan()<span) currentBird.tuckCard();
                        currentBirdNum--;
                        state.CURRENTEVENT.add("View Highlight Bird");
                    }
                    else if (ability.contains("If this bird is to the right")){
                        if (state.players[state.playing].getBirdsInHabitat(runningHabitat).getLast().equals(currentBird)){
                            if (x>=247 && x<=466 && y>=154 && y<=394){
                                out.println("Fly to forest");
                                state.players[state.playing].getBoard().moveBird(currentBird, "forest");
                                currentBirdNum--;
                            }else if (x>=246 && x<=467 && y>=398 && y<=646){
                                out.println("Fly to grasslands");
                                state.players[state.playing].getBoard().moveBird(currentBird, "grasslands");    
                                currentBirdNum--;
                            }else if (x>=245 && x<=463 && y>=649 && y<=860){
                                out.println("Fly to grasslands");
                                state.players[state.playing].getBoard().moveBird(currentBird, "wetlands");
                                currentBirdNum--;
                            }
                        }else currentBirdNum--;
                    }
                    else if (ability.contains("If you do, discard")){
                        state.CURRENTEVENT.add(state.CURRENTEVENT.indexOf("Game")+1, "Remove Bird");
                        currentBirdNum--;
                        if (ability.contains("2")) state.CURRENTEVENT.add("Draw Birds");
                        state.CURRENTEVENT.add("Draw Birds");
                    }
                    else if (ability.contains("to tuck 2")){
                        String check = ability.contains("fish")? "f":"s";
                        if (state.players[state.playing].hasFoodType(check)){
                            state.players[state.playing].removeFood(check, 1);
                            state.CURRENTEVENT.add("Tuck Card");
                            state.CURRENTEVENT.add("Tuck Card");
                        }
                        currentBirdNum--;
                    }
                    else if (ability.contains("Lay 1 [egg] on this bird")){
                        currentBird.addEggs(1);
                        currentBirdNum--;
                    }
                    else if (ability.contains("Cache 1 [seed]")){
                        currentBird.cacheFood();
                        currentBirdNum--;
                    }
                    else if (ability.contains("Discard 1 [egg] to draw 2 [card]")){
                        currentBirdNum--;
                        state.CURRENTEVENT.add("Draw Birds");
                        state.CURRENTEVENT.add("Draw Birds");
                        state.CURRENTEVENT.add("Remove Egg");
                    }
                    else if (ability.contains("Trade 1 [wild] for any")){
                        if (state.players[state.playing].hasFood()){
                            state.CURRENTEVENT.add("Remove Food");
                            state.CURRENTEVENT.add("Add Food");
                            state.CURRENTEVENT.add("Add Food");
                        }else{
                            state.CURRENTEVENT.add("No Food");
                            currentBirdNum--;
                        }
                    }
                    else if (ability.contains("Repeat 1 [predator] power")){
                        int j = 0;
                        ArrayList<Bird> birdArr = state.players[state.playing].getBirdsInHabitat(runningHabitat);
                        if (runningHabitat.equals("grasslands")) j++;
                        if (runningHabitat.equals("wetlands")) j+=2;
                        for (int i=0;i<5;i++){
                            if (x>=470+170*i && x<=644+170*i && y>=155+247*i && y<= 375+247*i){
                                String a = birdArr.get(i).getAbilityText();
                                if (a.contains("Roll all dice not in birdfeeder")){
                                    feeder.rollOutDice();
                                    String check="";
                                    if (a.contains("[rodent]"))
                                        check = "r";
                                    else if (a.contains("[fish]"))
                                        check = "f";
                                    if (feeder.getOutDice().contains(check)){
                                        currentBird.cacheFood();
                                    }
                                    currentBirdNum--;
                                    state.CURRENTEVENT.add("View Feeder");
                                }else if (a.contains("Look at a [card] from the deck")){
                                    int span = 0;
                                    if (a.contains("50")) span = 50;
                                    else if (a.contains("75")) span = 75;
                                    else span = 100;

                                    highlighted = birds.remove(0);
                                    if (highlighted.getWingspan()<span) currentBird.tuckCard();
                                    currentBirdNum--;
                                    state.CURRENTEVENT.add("View Highlight Bird");
                                }
                            }
                            currentBirdNum--;
                        }
                    }
                    else if (ability.contains("All players lay 1 [egg] on any 1")){
                        playingHolder = state.playing;
                        state.CURRENTEVENT.add("Loop Lay Eggs");
                        currentBirdNum--;
                    }
                    else if (currentBird.getName().equals("Mallard")){
                        currentBirdNum--;
                        state.CURRENTEVENT.add("Draw Birds");
                    }
                    else if (ability.contains("All players draw 1 [card]")){
                        for (int i=0;i<4;i++){
                            state.players[state.playing].addCardToHand(birds.removeFirst());
                            state.playing = (state.playing+1)%4;
                        }
                        currentBirdNum--;
                    }
                    else if (ability.contains("or [fruit] from the birdfeeder, if available")){
                        // paint viewfeeder for this
                        if (feeder.getDice().contains("b") && (feeder.getDice().contains("i")||feeder.getDice().contains("a"))){
                            if (selectingBool){
                                if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]) {
                                    state.players[state.playing].addFood("b", 1);
                                    feeder.removeDie("b");
                                    currentBirdNum--;
                                    selectingBool = false;
                                }
                                else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]) {
                                    if (feeder.getDice().contains("i")){
                                        feeder.removeDie("i");
                                    }else{
                                        feeder.removeDie("a");
                                    }
                                    state.players[state.playing].addFood("s",1);
                                    currentBirdNum--;
                                    selectingBool = false;
                                }
                            }else{
                                state.CURRENTEVENT.add("View Feeder");
                                selectingBool = true;
                            }
                        }else{
                            if (feeder.getDice().contains("b")){
                                state.players[state.playing].addFood("b", 1);
                                feeder.removeDie("b");
                            }else if (feeder.getDice().contains("i")){
                                state.players[state.playing].addFood("i", 1);
                                feeder.removeDie("i");
                            }else if (feeder.getDice().contains("a")){
                                state.players[state.playing].addFood("i", 1);
                                feeder.removeDie("a");
                            }

                            state.CURRENTEVENT.add("View Feeder");
                            currentBirdNum--;
                        }
                    }
                    else if (ability.contains("Gain 1 [invertebrate] from the birdfeeder")){
                        if (feeder.getDice().contains("i")){
                            state.players[state.playing].addFood("i", 1);
                            feeder.removeDie("i");
                        }else if (feeder.getDice().contains("a")){
                            state.players[state.playing].addFood("i", 1);
                            feeder.removeDie("a");
                        }
                        state.CURRENTEVENT.add("View Feeder");
                        currentBirdNum--;
                    }
                }else {currentBirdNum--;}
                
                
                repaint();
            }case "Tuck Card" -> {
                if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()/showing)
                    currentShowing++;
                else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;

                for (int i=0;i<4;i++){
                    if (x>=250+250*i && y>=500 && x<=490+250*i && y<=825){
                        if (currentShowing*4+i<state.players[state.playing].getCardsInHand().size()){
                            state.players[state.playing].getCardsInHand().remove(currentShowing*4+i);
                            currentBird.tuckCard();
                            state.CURRENTEVENT.removeLast();
                        }
                    }
                }
                repaint();
            }
            case "Remove Bird" -> {
                if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()/showing)
                    currentShowing++;
                else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;

                for (int i=0;i<4;i++){
                    if (x>=250+250*i && y>=500 && x<=490+250*i && y<=825){
                        if (currentShowing*4+i<state.players[state.playing].getCardsInHand().size()){
                            state.players[state.playing].getCardsInHand().remove(currentShowing*4+i);
                            state.CURRENTEVENT.removeLast();
                        }
                    }
                }
                repaint();
            }case "Remove Food" -> {
                String[] fo = {"s", "f", "b", "i", "r"};
                for (int i=0;i<5;i++){
                    if (x>=250+100*i && x<=350+100*i && y>=550 && y<=650){
                        if (state.players[state.playing].hasFoodType(fo[i])){
                            state.players[state.playing].removeFood(fo[i], 1);
                            state.CURRENTEVENT.removeLast();
                        }
                    }
                }
                repaint();
                }case "Add Food" -> {
                    String[] fo = {"s", "f", "b", "i", "r"};
                    for (int i=0;i<5;i++){
                        if (x>=250+100*i && x<=350+100*i && y>=550 && y<=650){
                            state.players[state.playing].addFood(fo[i], 1);
                            state.CURRENTEVENT.removeLast();
                        }
                    }
                    repaint();
                }
            default ->{
                state.CURRENTEVENT.removeLast();
                repaint();
            }
            case "Loop Lay Eggs" -> {
                Bird[][] birdBoard = state.players[state.playing].getPlayerBoard();
                for (int i=0;i<3;i++){
                    for (int j=0;j<5;j++){
                        if (x>=470+170*j && x<=644+170*j && y>=155+247*i && y<= 375+247*i){
                            if (birdBoard[i][j]!=null){
                                if (birdBoard[i][j].getEggCount()<birdBoard[i][j].getMaxEggs()){
                                    if (birdBoard[i][j].getNest().equals(nestType) || birdBoard[i][j].getNest().equals("wild")){
                                        birdBoard[i][j].addEggs(1);
                                        state.playing++;
                                        runCount++;
                                    }
                                    
                                }
                            }
                        }
                    }
                }
                if (runCount==5){
                    state.playing = playingHolder;
                    runCount = 0;
                    state.CURRENTEVENT.removeLast();
                }
                //out.println("Removing eggz");
                repaint();
            }
            case "Loop Gain Food" ->{
                if (selectingBool){
                    if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]) {
                        state.players[state.playing].addFood("s", 1);
                        runCount++;
                        state.playing = (state.playing+1)%4;
                        if (runCount==4){
                            state.playing = playingHolder;
                            runCount = 0;
                            state.CURRENTEVENT.removeLast();
                        }
                        selectingBool = false;
                    }
                    else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]) {
                        state.players[state.playing].addFood("i", 1);
                        runCount++;
                        state.playing = (state.playing+1)%4;
                        if (runCount==4){
                            state.playing = playingHolder;
                            runCount = 0;
                            state.CURRENTEVENT.removeLast();
                        }
                        selectingBool = false;
                    }
                    
                }
                else{
                    if (feeder.canReroll() && x>=1185 && x<=1300 && y>=480 && y<=605) feeder.reRoll();
                    for (int i=0;i<5;i++){
                        if (x>=diceLocMap[i][0] && x<=diceLocMap[i][0]+90 && y>=diceLocMap[i][1] && y<=diceLocMap[i][1]+90){
                            if (feeder.getDice().get(i).equals("a")) {
                                selectingBool = true;
                            }else {
                                state.players[state.playing].addFood(feeder.getDice().get(i), 1);
                                runCount++;
                                state.playing = (state.playing+1)%4;
                                if (runCount==4){
                                    state.playing = playingHolder;
                                    runCount = 0;
                                    state.CURRENTEVENT.removeLast();
                                }
                                
                            }
                            feeder.getOutDice().add(feeder.getDice().remove(i));
                        }
                    }
                    
                }
                repaint();
            }case "Pick Food For Specific Bird" -> {
        //         g.drawImage(wheatToken, 250, 550, 100, 100, null);
        // g.drawImage(fishToken, 350, 550, 100, 100, null);
        // g.drawImage(fruitToken, 450, 550, 100, 100, null);
        // g.drawImage(invertebrateToken, 550, 550, 100, 100, null);
        // g.drawImage(rodentToken, 650, 550, 100, 100, null);
        // g.drawImage(Clear_Button, 100, 400, 250, 100, null);
        //             if(){
        //                 g.drawImage(Continue_Button, 100, 750, 250, 100, null);
        //             }

        
            if(x>=100 && x<=350 && y>=400 && y<=500){
                for (int i=0;i<5;i++) state.birdFoodsForPlayingBird[i]=0;
            }
            else
            if(x>=250 && x<=350 && y>=550 && y<=650){
                state.birdFoodsForPlayingBird[0]++;
            }
            else if (x>=350 && x<=450 && y>=550 && y<=650){
               state.birdFoodsForPlayingBird[1]++;
            }
            else if (x>=450 && x<=550 && y>=550 && y<=650){
               state.birdFoodsForPlayingBird[2]++;
            }
            else if (x>=550 && x<=650 && y>=550 && y<=650){
                state.birdFoodsForPlayingBird[3]++;
            }
            else if (x>=650 && x<=750 && y>=550 && y<=650){
                state.birdFoodsForPlayingBird[4]++;
                
            }else if (x>=100 && x<=350 && y>=750 && y<=850&&state.players[state.playing].canAffordBirdWithChosenFoods(state.specificBirdToPlay, state.birdFoodsForPlayingBird)){
                state.CURRENTEVENT.removeLast();
                state.CURRENTEVENT.add("Remove Eggs For Bird");
                state.eggsNeededToSpendForPlayingBird = 0;
                for(int i=0;i<3;i++){
                    for(int j=0;j<5;j++){
                        if(state.squaresClickedToPlayBird[i][j]){
                            state.eggsNeededToSpendForPlayingBird = (int)Math.ceil(j/2.0);
                        }
                    }
                }
            }
            //     int position=0;
            //     for(int i=0;i<3;i++){
            //         for(int j=0;j<5;j++){
            //             if(state.squaresClickedToPlayBird[i][j]){
            //                 position=j;
            //                 for(int a=j;a>-1;a--){
            //                     if(state.players[state.playing].getBoard().getBoard()[i][a]==null){
            //                         position=a;
            //                 }
            //             }
            //             }
            //         }
            //     }
            //     // if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+0),state.habitatToPlayBird,position ))
            //         //     state.players[state.playing].getCardsInHand().remove(currentShowing*showing+0);
            //         // 
            //         // state.CURRENTEVENT.removeLast();
            //     //attempt to play bird
            //     if(state.players[state.playing].canAffordBirdWithChosenFoods(state.specificBirdToPlay, state.birdFoodsForPlayingBird)){
            //         state.players[state.playing].playBird(state.specificBirdToPlay, state.habitatToPlayBird, position, state.birdFoodsForPlayingBird);
            //         for (int i=0;i<5;i++) state.birdFoodsForPlayingBird[i]=0;
            //         state.CURRENTEVENT.removeLast();
            //         state.CURRENTEVENT.removeLast();
            //         for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
            //     }
            // }
            
        
            repaint();
            }
            case "Remove Eggs For Bird" -> {
                // if(state.players[state.playing].getBoard().getBoard()[0][0]!=null)g2.drawRect(470, 155, 628-470, 392-155);
                //     if(state.players[state.playing].getBoard().getBoard()[1][0]!=null)g2.drawRect(469, 403, 627-469, 637-403);
                //     if(state.players[state.playing].getBoard().getBoard()[2][0]!=null)g2.drawRect(470,650,626-470,866-650);

                //     if(state.players[state.playing].getBoard().getBoard()[0][1]!=null)g2.drawRect(644, 155, 800-644, 392-155);
                //     if(state.players[state.playing].getBoard().getBoard()[1][1]!=null)g2.drawRect(644, 403, 800-644, 637-403);
                //     if(state.players[state.playing].getBoard().getBoard()[2][1]!=null)g2.drawRect(644,650,800-644,866-650);

                //     if(state.players[state.playing].getBoard().getBoard()[0][2]!=null)g2.drawRect(815, 155, 969-815, 392-155);
                //     if(state.players[state.playing].getBoard().getBoard()[1][2]!=null)g2.drawRect(815, 403, 969-815, 637-403);
                //     if(state.players[state.playing].getBoard().getBoard()[2][2]!=null)g2.drawRect(815,650,969-815,866-650);

                //     if(state.players[state.playing].getBoard().getBoard()[0][3]!=null) g2.drawRect(985, 155, 1138-985, 392-155);
                //     if(state.players[state.playing].getBoard().getBoard()[1][3]!=null) g2.drawRect(985, 403, 1138-985, 637-403);
                //     if(state.players[state.playing].getBoard().getBoard()[2][3]!=null) g2.drawRect(985,650,1138-985,866-650);

                //     if(state.players[state.playing].getBoard().getBoard()[0][4]!=null) g2.drawRect(1152, 155, 1302-1152, 392-155);
                //     if(state.players[state.playing].getBoard().getBoard()[1][4]!=null) g2.drawRect(1152, 403, 1302-1152, 637-403);
                //     if(state.players[state.playing].getBoard().getBoard()[2][4]!=null) g2.drawRect(1152,650,1302-1152,866-650);
                if(x>=470 && x<=644 && y>=155 && y<=392){
                    if(state.players[state.playing].getBoard().getBoard()[0][0]!=null){
                    if(state.players[state.playing].getBoard().getBoard()[0][0].getEggCount()>0){
                        state.players[state.playing].getBoard().getBoard()[0][0].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                        }
                    }
            
        }
        else if(x>=469 && x<=627 && y>=403 && y<=637){
            if(state.players[state.playing].getBoard().getBoard()[1][0]!=null){
                if(state.players[state.playing].getBoard().getBoard()[1][0].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[1][0].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=470 && x<=626 && y>=650 && y<=866){
            if(state.players[state.playing].getBoard().getBoard()[2][0]!=null){
                if(state.players[state.playing].getBoard().getBoard()[2][0].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[2][0].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=644 && x<=800 && y>=155 && y<=392){
            if(state.players[state.playing].getBoard().getBoard()[0][1]!=null){
                if(state.players[state.playing].getBoard().getBoard()[0][1].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[0][1].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=644 && x<=800 && y>=403 && y<=637){
            if(state.players[state.playing].getBoard().getBoard()[1][1]!=null){
                if(state.players[state.playing].getBoard().getBoard()[1][1].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[1][1].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=644 && x<=800 && y>=650 && y<=866){
            if(state.players[state.playing].getBoard().getBoard()[2][1]!=null){
                if(state.players[state.playing].getBoard().getBoard()[2][1].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[2][1].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=815 && x<=969 && y>=155 && y<=392){
            if(state.players[state.playing].getBoard().getBoard()[0][2]!=null){
                if(state.players[state.playing].getBoard().getBoard()[0][2].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[0][2].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=815 && x<=969 && y>=403 && y<=637){
            if(state.players[state.playing].getBoard().getBoard()[1][2]!=null){
                if(state.players[state.playing].getBoard().getBoard()[1][2].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[1][2].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=815 && x<=969 && y>=650 && y<=866){
            if(state.players[state.playing].getBoard().getBoard()[2][2]!=null){
                if(state.players[state.playing].getBoard().getBoard()[2][2].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[2][2].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=985 && x<=1138 && y>=155 && y<=392){
            if(state.players[state.playing].getBoard().getBoard()[0][3]!=null){
                if(state.players[state.playing].getBoard().getBoard()[0][3].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[0][3].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=985 && x<=1138 && y>=403 && y<=637){
            if(state.players[state.playing].getBoard().getBoard()[1][3]!=null   ){
                if(state.players[state.playing].getBoard().getBoard()[1][3].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[1][3].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=985 && x<=1138 && y>=650 && y<=866){
            if(state.players[state.playing].getBoard().getBoard()[2][3]!=null){
                if(state.players[state.playing].getBoard().getBoard()[2][3].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[2][3].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=1152 && x<=1302 && y>=155 && y<=392){
            if(state.players[state.playing].getBoard().getBoard()[0][4]!=null){
                if(state.players[state.playing].getBoard().getBoard()[0][4].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[0][4].removeEggs(1);state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=1152 && x<=1302 && y>=403 && y<=637){
            if(state.players[state.playing].getBoard().getBoard()[1][4]!=null){
                if(state.players[state.playing].getBoard().getBoard()[1][4].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[1][4].removeEggs(1);
                    state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        else if(x>=1152 && x<=1302 && y>=650 && y<=866){
            if(state.players[state.playing].getBoard().getBoard()[2][4]!=null){
                if(state.players[state.playing].getBoard().getBoard()[2][4].getEggCount()>0){
                    state.players[state.playing].getBoard().getBoard()[2][4].removeEggs(1);
                    state.eggsNeededToSpendForPlayingBird--;
                    }
                }
        }
        
            repaint();
    }
}
    
}
            
        
    
       
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {
        // highlight the button when hovered
        updateStartButtonRect();
        boolean nowHover = startButtonRect.contains(e.getPoint());
        if (nowHover != hover) {
            hover = nowHover;
            repaint();
        }
    }
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'e') {
           out.println(state.CURRENTEVENT);
        }
    }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        synchronized(state.lock){
            switch(state.CURRENTEVENT.getLast()) {
                case "Process Mouse Click Game Start" -> {
                
                    g.drawImage(infoButton, 1420, 720, 90, 90, null);
                    
                    state.CURRENTEVENT.removeLast();
                    break;
                }
                case "Game Start" -> paintStart(g);
                case "Select Screen" -> paintSelection(g);
                case "Game" -> paintGame(g);
                case "Trade" -> paintTrade(g);
                case "View Birds" -> paintViewBirds(g);
                case "View Bonus" -> paintViewBonus(g);
                case "Info" -> paintInfo(g);
                case "View Draw Birds" -> paintViewDrawBirds(g);
                case "View Feeder" -> paintViewFeeder(g);
                case "Draw Birds" -> paintDrawBirds(g);
                case "Score Round" -> paintScoreRound(g);
                case "Play Bird" -> paintPlayBird(g);
                case "Play Specific Bird" -> paintPlaySpecificBird(g);
                case "Rules" -> paintRules(g);
                case "Wait For Second Part Play Specific Bird" -> paintPlaySpecificBirdSecondPart(g);
                case "When Played Ability" -> paintWPAbility(g);
                case "Choose Bonus" -> paintBonusDraw(g);
                case "Loop Draw" -> paintLoopDraw(g);
                case "On Activate Ability" -> paintOAAbility(g);
                case "Remove Bird" -> {
                    paintViewBirds(g);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.drawString("Remove", 449, 456);
                }
                case "Tuck Card" -> paintViewBirds(g);
                case "Remove Food" -> paintViewFoods(g);
                case "Add Food" -> paintViewFoods(g);
                case "Loop Gain Food" -> {
                    paintViewFeeder(g);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.drawString("PLayer " + (state.playing+1) + " pick a food", 300, 200);
                    if (selectingBool) paintBoolean(g, "Select Food", "Seed","Insect");
                }
                case "No Food" -> {
                    paintGame(g);
                    g.setColor(Color.CYAN);
                    g.fillRect(600, 400, 300, 100);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.setColor(Color.BLACK);
                    g.drawString("Oops you have no food :(", 650, 420);
                }
                case "View Highlight Bird" -> {
                    paintGame(g);
                    g.drawImage(highlighted.getImage(), 200, 200, 352, 600, null);
                }
                case "Lay Eggs" -> {
                    paintGame(g);Graphics2D g2 = (Graphics2D)g;
                    g2.setStroke(new BasicStroke(5.0f));
                    g2.setColor(Color.BLUE);

                    if(state.players[state.playing].getBoard().getBoard()[0][0]!=null)g2.drawRect(470, 155, 628-470, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][0]!=null)g2.drawRect(469, 403, 627-469, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][0]!=null)g2.drawRect(470,650,626-470,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][1]!=null)g2.drawRect(644, 155, 800-644, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][1]!=null)g2.drawRect(644, 403, 800-644, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][1]!=null)g2.drawRect(644,650,800-644,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][2]!=null)g2.drawRect(815, 155, 969-815, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][2]!=null)g2.drawRect(815, 403, 969-815, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][2]!=null)g2.drawRect(815,650,969-815,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][3]!=null) g2.drawRect(985, 155, 1138-985, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][3]!=null) g2.drawRect(985, 403, 1138-985, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][3]!=null) g2.drawRect(985,650,1138-985,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][4]!=null) g2.drawRect(1152, 155, 1302-1152, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][4]!=null) g2.drawRect(1152, 403, 1302-1152, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][4]!=null) g2.drawRect(1152,650,1302-1152,866-650);
                    g2.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.drawString("Click a bird to add an egg", 300, 100);
                    int count = 0;
                    for (String s: state.CURRENTEVENT) if (s.equals("Lay Eggs")) count ++;
                    g.drawString("Add " + count + " more eggs", 300, 160);
                }
                case "Remove Egg" -> {
                    paintGame(g);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.drawString("Click a bird to remove an egg", 300, 100);
                }
                case "Select Food" -> {
                    paintViewFeeder(g);
                    if (selectingBool) paintBoolean(g, "Select Food", "Seed","Insect");
                }
                case "Pick Food For Specific Bird" -> {
                    paintGame(g);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    
                    out.println(state.specificBirdToPlay.getFoods());
                    g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
                    //g.drawImage(exitPic, 20, 400, 50, 50, null);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("Click foods to play bird with", 600, 458);

                    for (int i=0;i<5;i++){  
                        g.drawString(""+state.birdFoodsForPlayingBird[i], 285+100*i, 700);
                    }
                    g.drawImage(state.specificBirdToPlay.getImage(), 1137, 523, 1345-1137,777-523, null);
                    g.drawImage(wheatToken, 250, 550, 100, 100, null);
                    g.drawImage(fishToken, 350, 550, 100, 100, null);
                    g.drawImage(fruitToken, 450, 550, 100, 100, null);
                    g.drawImage(invertebrateToken, 550, 550, 100, 100, null);
                    g.drawImage(rodentToken, 650, 550, 100, 100, null);
                    g.drawImage(Clear_Button, 100, 400, 250, 100, null);
                    if(state.players[state.playing].canAffordBirdWithChosenFoods(state.specificBirdToPlay, state.birdFoodsForPlayingBird)){
                        g.drawImage(Continue_Button, 100, 750, 250, 100, null);
                    }

                    state.lock.notifyAll();
                }
                case "Remove Eggs For Bird" -> {
                    if(state.eggsNeededToSpendForPlayingBird==0){
                        int position=0;
                for(int i=0;i<3;i++){
                    for(int j=0;j<5;j++){
                        if(state.squaresClickedToPlayBird[i][j]){
                            position=j;
                            for(int a=j;a>-1;a--){
                                if(state.players[state.playing].getBoard().getBoard()[i][a]==null){
                                    position=a;
                            }
                        }
                        }
                    }
                }
                // if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+0),state.habitatToPlayBird,position ))
                    //     state.players[state.playing].getCardsInHand().remove(currentShowing*showing+0);
                    // 
                    // state.CURRENTEVENT.removeLast();
                //attempt to play bird
                if(state.players[state.playing].canAffordBirdWithChosenFoods(state.specificBirdToPlay, state.birdFoodsForPlayingBird)){
                    if(state.players[state.playing].playBird(state.specificBirdToPlay, state.habitatToPlayBird, position, state.birdFoodsForPlayingBird))
                    state.players[state.playing].getCardsInHand().remove(state.specificBirdToPlay);
                    for (int i=0;i<5;i++) state.birdFoodsForPlayingBird[i]=0;
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.removeLast();
                    for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    repaint();
                    return;
                }
            }
                    
                    if(state.players[state.playing].getEggCount()<state.eggsNeededToSpendForPlayingBird){
                        state.CURRENTEVENT.removeLast();
                        state.CURRENTEVENT.removeLast();
                        repaint();
                        break;
                    }
                    paintGame(g);Graphics2D g2 = (Graphics2D)g;
                    g2.setStroke(new BasicStroke(5.0f));
                    g2.setColor(Color.BLUE);

                    if(state.players[state.playing].getBoard().getBoard()[0][0]!=null)g2.drawRect(470, 155, 628-470, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][0]!=null)g2.drawRect(469, 403, 627-469, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][0]!=null)g2.drawRect(470,650,626-470,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][1]!=null)g2.drawRect(644, 155, 800-644, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][1]!=null)g2.drawRect(644, 403, 800-644, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][1]!=null)g2.drawRect(644,650,800-644,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][2]!=null)g2.drawRect(815, 155, 969-815, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][2]!=null)g2.drawRect(815, 403, 969-815, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][2]!=null)g2.drawRect(815,650,969-815,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][3]!=null) g2.drawRect(985, 155, 1138-985, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][3]!=null) g2.drawRect(985, 403, 1138-985, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][3]!=null) g2.drawRect(985,650,1138-985,866-650);

                    if(state.players[state.playing].getBoard().getBoard()[0][4]!=null) g2.drawRect(1152, 155, 1302-1152, 392-155);
                    if(state.players[state.playing].getBoard().getBoard()[1][4]!=null) g2.drawRect(1152, 403, 1302-1152, 637-403);
                    if(state.players[state.playing].getBoard().getBoard()[2][4]!=null) g2.drawRect(1152,650,1302-1152,866-650);
                    g2.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 55));
                    g.drawString("Click eggs to spend. Eggs needed: "+state.eggsNeededToSpendForPlayingBird, 300, 100);
                   
                    
            }
        }
        }
    }

    public void paintOAAbility(Graphics g){
        String ability = currentBird.getAbilityText();
        if (currentBirdNum<0){
            paintGame(g);
            g.setFont(new Font("Arial", Font.BOLD, 55));
            g.drawString("CLICK to continue", 200, 200);
            return;
        }
        else if (ability.contains("Gain 1 [seed] from the birdfeeder")){
            g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
            if (selectingBool) paintBoolean(g, "Cache seed?", "Yes", "No");
        }
        else if (ability.contains("or [fruit] from the birdfeeder, if available")){
            g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
            if (selectingBool) paintBoolean(g, "Gain which one?", "Berry", "Insect");
        }
        else if (ability.contains("Tuck 1 [card]")){
            paintViewBirds(g);
        }
        else if (ability.contains("Each player gains 1 [die]")){
            g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
            g.drawImage(cover, 1300, 400, 100, 100, null); //feeder placeholder
            g.setFont(new Font("Arial", Font.BOLD, 55));
            g.drawString("Click a player to start first. You're player " + (state.playing+1), 400, 200);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            for (int i=0;i<4;i++){
                g.setColor(Color.CYAN);
                g.drawRect(250 + 110*i, 550, 100, 80);
                g.setColor(Color.BLACK);
                g.drawString(""+(i+1), 260 + 110*i, 610);

            }
            
        }
        else if (ability.contains("If this bird is to the right")){
            paintGame(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5.5f));
            g2.setColor(Color.CYAN);
            g2.drawRect(247, 154, 466-247, 394-154);
            g2.drawRect(246, 398, 467-246, 646-398);
            g2.drawRect(245, 649, 463-245, 860-649);
        }
        else if (ability.contains("Discard 1 [egg] from any")){
            if (selectingFood){
                g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
                g.setFont(new Font("Arial", Font.BOLD, 55));
                g.drawString("Click a food to take", 400, 200);
                g.drawImage(wheatToken, 250, 550, 100, 100, null);
                g.drawImage(fishToken, 350, 550, 100, 100, null);
                g.drawImage(fruitToken, 450, 550, 100, 100, null);
                g.drawImage(invertebrateToken, 550, 550, 100, 100, null);
                g.drawImage(rodentToken, 650, 550, 100, 100, null);
            }else paintGame(g);
        }
        else paintGame(g);
        g.setColor(Color.CYAN);
        g.fillRect(630, 10, 300, (int)(200*1.4)+40);
        g.drawImage(state.players[state.playing].getBirdsInHabitat(runningHabitat).get(currentBirdNum).getImage(), 650, 30, 260, (int)(200*1.4), null);
        g.drawImage(skip, 1260, 16, 220, 105, null);
    }

    public void paintScoreRound(Graphics g){
	// Draw score sheet background
	if (Score_By_Round != null) g.drawImage(Score_By_Round, 0, 0, getWidth(), getHeight(), null);
	else {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(),getHeight());
	}

	Player[] players = state.players;
	if (players == null || players.length == 0) return;

	// Layout: 4 columns (approx) — tuned for the score_by_round.png layout
	int startX = Math.max(180, getWidth()/6);
	int colW = Math.min(260, Math.max(180, getWidth()/8));
	int[] colX = new int[] {startX, startX + colW, startX + colW*2, startX + colW*3};
	int titleY = 160;

	Font bold = new Font("SansSerif", Font.BOLD, 28);
	Font regular = new Font("SansSerif", Font.PLAIN, 20);
	Font small = new Font("SansSerif", Font.PLAIN, 16);
	g.setFont(regular);
	g.setColor(Color.BLACK);

	// Rows: Birds, Bonus, End-of-Round Goals (display sum) + per-round breakdown, Eggs, Cached food, Tucked cards, TOTAL
	int rowStart = 250;
	int rowGap = 62;

	// Column headings (player names)
	g.setFont(bold);
	for (int i = 0; i < Math.min(players.length, 4); i++) {
		String name = players[i] != null && players[i].getName() != null ? players[i].getName() : ("Player " + (i+1));
		g.drawString(name, colX[i] + 10, titleY);
	}

	// Draw round icons across the top-right area (if available)
	int roundIconX = Math.min(getWidth()-420, startX + colW*4 + 10);
	int roundIconY = 110;
	for (int r = 0; r < 4; r++) {
		if (roundPics != null && roundPics.length > r && roundPics[r] != null) {
			g.drawImage(roundPics[r], roundIconX + r*82, roundIconY, 72, 72, null);
		} else {
			g.setFont(small);
			g.drawString("R"+(r+1), roundIconX + r*82 + 18, roundIconY + 46);
		}
	}

	// draw rows labels on the left side
	g.setFont(bold);
	int labelX = startX - 150;
	g.drawString("Birds", labelX, rowStart + rowGap * 0);
	g.drawString("Bonus Cards", labelX, rowStart + rowGap * 1);
	g.drawString("End-of-Round Goals (total)", labelX, rowStart + rowGap * 2);
	// small per-round label group placed under the goal row
	g.setFont(small);
	g.drawString("Round breakdown (R1..R4)", labelX + 10, rowStart + rowGap * 2 + 26);

	g.setFont(bold);
	g.drawString("Eggs", labelX, rowStart + rowGap * 3);
	g.drawString("Food in Cache", labelX, rowStart + rowGap * 4);
	g.drawString("Tucked Cards", labelX, rowStart + rowGap * 5);
	g.setFont(bold);
	g.drawString("TOTAL", labelX, rowStart + rowGap * 6);

	// Draw each player's values
	g.setFont(regular);
	for (int i = 0; i < Math.min(players.length, 4); i++) {
		Player p = players[i];
		if (p == null) continue;

		ScoreBreakdown base = p.calculateScore();
		if (base == null) base = new ScoreBreakdown(0,0,0,0,0,0,0);

		int printed = base.printedPoints;
		int bonusPoints = 0;
		for (Bonus b : p.getBonuses()) {
			try { bonusPoints += b.getBonusPoints(p); } catch (Exception ignored) {}
		}

		// Sum all round awards (display as total)
		int roundPointsTotal = 0;
		for (int r = 0; r < 4; r++) roundPointsTotal += state.getPlayerRoundScore(i, r);

		int eggs = base.eggs;
		int cached = base.cachedFood;
		int tucked = base.tuckedCards;
		int flocked = base.flocked;

		int total = printed + bonusPoints + roundPointsTotal + eggs + cached + tucked + flocked;

		// Draw category values
		int x = colX[i] + 10;
		g.drawString(String.valueOf(printed), x, rowStart + rowGap * 0);   // Birds
		g.drawString(String.valueOf(bonusPoints), x, rowStart + rowGap * 1); // Bonus
		g.drawString(String.valueOf(roundPointsTotal), x, rowStart + rowGap * 2); // Goals total
		g.drawString(String.valueOf(eggs), x, rowStart + rowGap * 3); // Eggs
		g.drawString(String.valueOf(cached), x, rowStart + rowGap * 4); // Cached food
		g.drawString(String.valueOf(tucked), x, rowStart + rowGap * 5); // Tucked
		g.setFont(bold);
		g.drawString(String.valueOf(total), x, rowStart + rowGap * 6); // Total
		g.setFont(regular);

		// per-round breakdown (small digits in a compact column below the Goals row)
		g.setFont(small);
		for (int r = 0; r < 4; r++) {
			int smallY = rowStart + rowGap * 2 + 28 + r * 16;
			int roundValue = state.getPlayerRoundScore(i, r);
			g.drawString("R" + (r+1) + ": " + roundValue, x, smallY);
		}
	}

	// small footer: brief help about scoring order
	g.setFont(new Font("SansSerif", Font.ITALIC, 12));
	g.setColor(new Color(0,0,0,140));
	g.drawString("Scoring order: Birds → Bonus → Round Goals → Eggs → Tucked → Cached food", startX, rowStart + rowGap * 8 + 8);
}
}



