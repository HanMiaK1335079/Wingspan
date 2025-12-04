package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.System.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import src.Bird;
import src.Bonus;
import src.Feeder;
import src.GameLogic;
import src.Player;
import src.ProgramState;
public class FramePanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage Reroll_Button,cover, infoButton, bg, exitPic, leftArrow, rightArrow, birdBack, wheatToken, invertebrateToken, fishToken, fruitToken, rodentToken, Continue_Button, feederPic, Action_Button, Score_By_Round, skip;
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
        addMouseMotionListener(this);
        feeder = new Feeder(state);
        

        for (int i=0;i<4;i++){
            state.players[i] = new Player();
        }

        //Add all buffered images here 
        try{
            cover = ImageIO.read(FramePanel.class.getResource("/assets/cover_image.png"));
            infoButton = ImageIO.read(FramePanel.class.getResource("/assets/info picture.png"));
            bg = ImageIO.read(FramePanel.class.getResource("/assets/table_bg.png"));

        } catch (Exception e){
            System.out.println("No workie because idk 🤷‍♂️");
            System.out.println(e);
        }
        //readCSV(new File("src/birdInfo.csv"));
        this.repaint();
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
                else if (x>=480 && x<=530 && y>=120 && y<=170) state.CURRENTEVENT.add("Play Bird");
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
                if (selectingBool){
                    if (x>=yesCrds[0] && x<=yesCrds[1] && y>=yesCrds[2] && y<=yesCrds[3]) {state.players[state.playing].addFood("s", 1);state.CURRENTEVENT.removeLast();}
                    else if (x>=noCrds[0] && x<=noCrds[1] && y>=noCrds[2] && y<=noCrds[3]) {state.players[state.playing].addFood("i", 1);state.CURRENTEVENT.removeLast();}
                    selectingBool = false;
                }
                else{
                    if (feeder.canReroll() && x>=1185 && x<=1300 && y>=480 && y<=605) feeder.reRoll();
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
            state.CURRENTEVENT.removeLast();
            paintPlaySpecificBirdSecondPart(this.getGraphics());
        }
        case "Choose Bird" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450){ state.CURRENTEVENT.removeLast(); state.CURRENTEVENT.removeLast();
                for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
            }
                else if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()%showing)
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
                            
                        }
                    }
                }
                if(x>=253 && x<=489 && y>=504 && y<=825){
                    out.println("Clicked first card to play");
                    if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+0),state.habitatToPlayBird,position ))
                        state.players[state.playing].getCardsInHand().remove(currentShowing*showing+0);
                    for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.removeLast();
                } else if ( x >= 504 && x <= 740 && y >= 504 && y <= 825 ) {
                    out.println("Clicked Second card to play");
                   if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+1),state.habitatToPlayBird, position))
                        state.players[state.playing].getCardsInHand().remove(currentShowing*showing+1);
                    for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.removeLast();
                } else if ( x >= 755 && x <= 991 && y >= 504 && y <= 825 ) {
                    out.println("Clicked Third card to play");
                    if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+2),state.habitatToPlayBird, position))
                        state.players[state.playing].getCardsInHand().remove(currentShowing*showing+2);
                    for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                    state.CURRENTEVENT.removeLast();
                    state.CURRENTEVENT.removeLast();
                } else if ( x >= 1000 && x <= 1237 && y >= 504 && y <= 825 ) {
                    out.println("Clicked Fourth card to play");
                   if(state.players[state.playing].playBird(state.players[state.playing].getCardsInHand().get(currentShowing*4+3),state.habitatToPlayBird, position))
                        state.players[state.playing].getCardsInHand().remove(currentShowing*showing+3);
                    for (int i=0;i<3;i++) for (int j=0;j<5;j++) state.squaresClickedToPlayBird[i][j] = false;
                     state.CURRENTEVENT.removeLast();
                     state.CURRENTEVENT.removeLast();
                    endTurn(ProgramState.PlayerAction.PLAY_BIRD);
                }
                    
                    state.habitatToPlayBird = "";
                

                    
                
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
    public void keyTyped(KeyEvent e) {}
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
                case "Choose Bird" -> paintPlaySpecificBirdSecondPart(g);
            }
            state.lock.notifyAll();
        }
    }

    public void paintOAAbility(Graphics g){
        String ability = currentBird.getAbilityText();
        if (ability.contains("Gain 1 [seed] from the birdfeeder")){
            if (selectingBool) paintBoolean(g, "Cache seed?", "Yes", "No");
        }
        else if (ability.contains("Tuck 1 [card]")){
            paintViewBirds(g);
        }
        else if (ability.contains("Discard 1 [egg] from any")){
            if (selectingFood){
                g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
                g.drawString("Click a food to take", 400, 200);
                g.drawImage(wheatToken, 250, 550, 100, 100, null);
                g.drawImage(fishToken, 350, 550, 100, 100, null);
                g.drawImage(fruitToken, 450, 550, 100, 100, null);
                g.drawImage(invertebrateToken, 550, 550, 100, 100, null);
                g.drawImage(rodentToken, 650, 550, 100, 100, null);
            }else paintGame(g);
        }
    }

    public void paintScoreRound(Graphics g){
        g.drawImage(Score_By_Round, 0, 0, getWidth(), getHeight(), null);
    }

    public void paintPlaySpecificBird(Graphics g){
       
        paintGame(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g.setColor(new Color(173, 216, 230));
        if(state.squaresClickedToPlayBird[0][0]) g2.drawRect(470, 155, 628-470, 392-155);
        if(state.squaresClickedToPlayBird[1][0]) g2.drawRect(469, 403, 627-469, 637-403);
        if(state.squaresClickedToPlayBird[2][0]) g2.drawRect(470,650,626-470,866-650);
        if(state.squaresClickedToPlayBird[0][1]) g2.drawRect(644, 155, 800-644, 392-155);
        if(state.squaresClickedToPlayBird[1][1]) g2.drawRect(644, 403, 800-644, 637-403);
        if(state.squaresClickedToPlayBird[2][1]) g2.drawRect(644,650,800-644,866-650);
        if(state.squaresClickedToPlayBird[0][2]) g2.drawRect(815, 155, 969-815, 392-155);
        if(state.squaresClickedToPlayBird[1][2]) g2.drawRect(815, 403, 969-815, 637-403);
        if(state.squaresClickedToPlayBird[2][2]) g2.drawRect(815,650,969-815,866-650);
        if(state.squaresClickedToPlayBird[0][3]) g2.drawRect(985, 155, 1138-985, 392-155);
        if(state.squaresClickedToPlayBird[1][3]) g2.drawRect(985, 403, 1138-985, 637-403);
        if(state.squaresClickedToPlayBird[2][3]) g2.drawRect(985,650,1138-985,866-650);
        if(state.squaresClickedToPlayBird[0][4]) g2.drawRect(1152, 155, 1302-1152, 392-155);
        if(state.squaresClickedToPlayBird[1][4]) g2.drawRect(1152, 403, 1302-1152, 637-403);
        if(state.squaresClickedToPlayBird[2][4]) g2.drawRect(1152,650,1302-1152,866-650);
        currentShowing=0;
        state.CURRENTEVENT.add("Wait For Second Part Play Specific Bird");
        

    }

    public void paintPlaySpecificBirdSecondPart(Graphics g){
        paintGame(g);
        out.println("painting sepcific ibird");
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g.setColor(new Color(173, 216, 230));
        if(state.squaresClickedToPlayBird[0][0]) g2.drawRect(470, 155, 628-470, 392-155);
        if(state.squaresClickedToPlayBird[1][0]) g2.drawRect(469, 403, 627-469, 637-403);
        if(state.squaresClickedToPlayBird[2][0]) g2.drawRect(470,650,626-470,866-650);
        if(state.squaresClickedToPlayBird[0][1]) g2.drawRect(644, 155, 800-644, 392-155);
        if(state.squaresClickedToPlayBird[1][1]) g2.drawRect(644, 403, 800-644, 637-403);
        if(state.squaresClickedToPlayBird[2][1]) g2.drawRect(644,650,800-644,866-650);
        if(state.squaresClickedToPlayBird[0][2]) g2.drawRect(815, 155, 969-815, 392-155);
        if(state.squaresClickedToPlayBird[1][2]) g2.drawRect(815, 403, 969-815, 637-403);
        if(state.squaresClickedToPlayBird[2][2]) g2.drawRect(815,650,969-815,866-650);
        if(state.squaresClickedToPlayBird[0][3]) g2.drawRect(985, 155, 1138-985, 392-155);
        if(state.squaresClickedToPlayBird[1][3]) g2.drawRect(985, 403, 1138-985, 637-403);
        if(state.squaresClickedToPlayBird[2][3]) g2.drawRect(985,650,1138-985,866-650);
        if(state.squaresClickedToPlayBird[0][4]) g2.drawRect(1152, 155, 1302-1152, 392-155);
        if(state.squaresClickedToPlayBird[1][4]) g2.drawRect(1152, 403, 1302-1152, 637-403);
        if(state.squaresClickedToPlayBird[2][4]) g2.drawRect(1152,650,1302-1152,866-650);
        if (state.CURRENTEVENT.indexOf("Choose Bird")<0)state.CURRENTEVENT.add("Choose Bird");
        g.setColor(new Color(0, 0, 0));
        g.drawImage(bg, 0, 380, this.getWidth(), this.getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Bird Card", 600, 458);
        g.drawString(""+state.players[state.playing].getCardsInHand().size(), 1400, 460);
        ArrayList<ArrayList<Bird>> birdArrSplit = new ArrayList<>();
        int counter = 0;
        for (Bird b: state.players[state.playing].getCardsInHand()){
            if (counter %showing == 0) 
                birdArrSplit.add(new ArrayList<>());
            counter ++;
            birdArrSplit.get(birdArrSplit.size()-1).add(b);
        }
        if (state.players[state.playing].getCardsInHand().size()==0) return;

       
         for (int i=0;i<birdArrSplit.get(currentShowing).size();i++){
             g.drawImage(birdArrSplit.get(currentShowing).get(i).getImage(), 250 + 250*i, 500, 240, 325,null);
         }
        //BUG::: rightarrow still shows up even though there are only 4 birbs (max is 4)

        if (currentShowing != 0) g.drawImage(leftArrow, 50, 590, 60, 60, null);
        if (currentShowing != (state.players[state.playing].getCardsInHand().size()-1)/4) g.drawImage(rightArrow, 1400, 590, 60, 60, null);
        
    }

    public void paintStart(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        // Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Drsaw background image scaled to panel size
        int w = getWidth();
        int h = getHeight();
        if (cover != null) g2.drawImage(cover, 0, 0, w, h, null);

        // Dim overlay for readability
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, w, h);

        // Draw title
        g2.setFont(titleFont);
        g2.setColor(new Color(255, 245, 230));
        String title = "Wingspan";
        FontMetrics fmTitle = g2.getFontMetrics();
        int tx = (w - fmTitle.stringWidth(title)) / 2;
        int ty = h / 3;
        g2.drawString(title, tx, ty);

        // Button geometry
        updateStartButtonRect();
        RoundRectangle2D.Float rr = new RoundRectangle2D.Float(startButtonRect.x, startButtonRect.y, startButtonRect.width, startButtonRect.height, 24, 24);



        // Shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fill(new RoundRectangle2D.Float(startButtonRect.x + 4, startButtonRect.y + 6, startButtonRect.width, startButtonRect.height, 24, 24));

        // Button fill (gradient changes on hover)
        Color top = hover ? new Color(70, 160, 70) : new Color(50, 130, 200);
        Color bottom = hover ? new Color(40, 120, 40) : new Color(20, 80, 160);
        GradientPaint gp = new GradientPaint(startButtonRect.x, startButtonRect.y, top, startButtonRect.x, startButtonRect.y + startButtonRect.height, bottom);
        g2.setPaint(gp);
        g2.fill(rr);

        // Button border
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 160));
        g2.draw(rr);

        // Button text
        g2.setFont(buttonFont);
        String label = "Click to Start";
        FontMetrics fm = g2.getFontMetrics();
        int bx = startButtonRect.x + (startButtonRect.width - fm.stringWidth(label)) / 2;
        int by = startButtonRect.y + (startButtonRect.height - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(label, bx, by);

       
    }

    public void paintBoolean(Graphics g, String s, String a, String b){
        switch (state.CURRENTEVENT.getLast()){
            case "Game" -> paintGame(g);
            case "Select Food" -> paintViewFeeder(g);
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.5f));
        g2.setColor(new Color(8, 130, 161)); //dark blue
        g2.fillRect(500, 375, 600, 150);
        g2.setColor(new Color(58, 197, 164)); //light blue
        g2.drawRect(510, 385, 280, 130);
        g2.drawRect(810, 385, 280, 130);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        g.drawString(s, 800-10*s.length(), 355);
        g.drawString(a, 600, 480);
        g.drawString(b, 900, 480);
    }

    public void paintSelection(Graphics g){
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        
        g.setFont(new Font("Arial", 1, 50));
        g.drawString("Player: " + (1+state.playing), 60, 80);
        g.setFont(new Font("Arial", 1, 30));
        g.drawString("Select 5", 1000, 80);
        g.drawLine(40, getHeight()/2, getWidth()-40, getHeight()/2);
         g.setColor(new Color(173, 216, 230));
        for (int i=0;i<5;i++){
            if (startSelections[i]) g.fillRect(25+i*220, 115, 210, 310);
            g.drawImage(startOptions[i].getImage(), 30+i*220, 120, 200, 300,null);
        }
       
        if (startSelections[5]) g.fillOval(1195, 115, 100, 100);
        if (startSelections[8]) g.fillOval(1195, 325, 110, 110);
        if (startSelections[7]) g.fillOval(1310, 220, 110, 110);
        if (startSelections[6]) g.fillOval(1425, 115, 110, 110);
        if (startSelections[9]) g.fillOval(1425, 325, 110, 110);
        g.drawImage(wheatToken, 1200, 120, 100, 100, null);
        g.drawImage(fishToken, 1430, 120, 100, 100, null);
        g.drawImage(fruitToken, 1315, 225, 100, 100, null);  
        g.drawImage(invertebrateToken, 1200, 330, 100, 100, null);
        g.drawImage(rodentToken, 1430, 330, 100, 100, null); 
     


        if (startSelections[10]) g.fillRect(545, 495, 210, 310);
        if (startSelections[11]) g.fillRect(795, 495, 210, 310);
        g.drawImage(bonusOptions[0].getImage(), 550, 500, 200, 300, null);
        g.drawImage(bonusOptions[1].getImage(), 800, 500, 200, 300, null);
        
        //draw roundPics
        g.drawImage(roundPics[0], 1180, 530, 110, 110, null);
        g.drawImage(roundPics[1], 1180, 660, 110, 110, null);
        g.drawImage(roundPics[2], 1310, 530, 110, 110, null);
        g.drawImage(roundPics[3], 1310, 660, 110, 110, null);

        g.drawImage(Continue_Button,140, 600, 300, 120,null);
        
        //DEBUG RECT: click to instantly select 5 birb cards and first bonus (only cuz im too lazy to individually select)
        g.fillRect(140, 700, 150, 80);
        
        /*if (canContinue()) {
            out.println("Drawing cont. box");
            g.setColor(Color.BLUE);
            g.fillRect(1410, 590, 180, 60);
        }*/
        //g.fillRect(140, 700, 200, 80);
    }
    
    public void paintGame(Graphics g){
        g.drawImage(ingameBg, 0, 0, 1540, 863,null);
        g.drawImage(Action_Button, 480, 120, 50 ,50, null);
        g.drawImage(Action_Button, 425, 200, 50,50,null);
        g.drawImage(Action_Button, 425, 450, 50,50,null);
        g.drawImage(Action_Button, 350, 725, 50,50,null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 55));
        g.drawString(state.playing+1+"", 238, 67);
        for (int i=0;i<3;i++){
           for(int j=0;j<5;j++){
            try{
               switch(i){
                   case 0 -> {
                       //forest
                       switch(j){
                           case 0 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 470, 155, 158, 237, null);
                           case 1 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 644, 155, 156, 237, null);
                           case 2 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 815, 155, 154, 237, null);
                           case 3 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 985, 155, 153, 237, null);
                           case 4 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 1152, 155, 150, 237, null);
                       }
                   }
                   case 1 -> {
                       //plains
                       switch(j){
                           case 0 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 469, 403, 158, 234, null);
                           case 1 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 644, 403, 156, 234, null);
                           case 2 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 815, 403, 154, 234, null);
                           case 3 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 985, 403, 153, 234, null);
                           case 4 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 1152, 403, 150, 234, null);
                       }
                   }
                   case 2 -> {
                       //wetlands
                       switch(j){
                           case 0 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 470, 650, 156, 216, null);
                           case 1 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 644, 650, 156, 216, null);
                           case 2 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 815, 650, 154, 216, null);
                           case 3 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 985, 650, 153, 216, null);
                           case 4 -> g.drawImage(state.players[state.playing].getBoard().getBoard()[i][j].getImage(), 1152, 650, 150, 216, null);
                       }
                    }
               }
            } catch (Exception NullPointerException){/*out.println(i+ " " +j);*/ }
           }
        }
        int counterIndex = 0;
        int counter = 0;
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(new Color(70, 85, 207));
        for (Bird[] birdArr: state.players[state.playing].getPlayerBoard()){
            for (Bird b: birdArr){
                //g.drawString()
                if (b!=null){
                    g.drawString("E: "+b.getEggCount(), 476, 330+230*counter);
                    if (b.getTuckedCards()>0){
                        g.drawString("T: "+b.getTuckedCards(), 594, 220 + 230*counter);
                    }else if (b.getCachedFood()>0) g.drawString("C: "+b.getCachedFood(), 594, 220 + 230*counter);
                }
                counterIndex++;
                    //out.println("Birbing");
            }
            counter++;
            counterIndex = 0;
        }

        //draw the goals
        g.setColor(Color.CYAN);
        g.fillRect(965+100*state.round, 15, 90, 90);
        g.fillRect(1374, 615, 50, 50); //PLACEHOLDER FOR MAG GLASS
        g.setColor(Color.BLACK);
        for (int i=0;i<4;i++){
            g.drawImage(roundPics[i], 970+100*i, 20, 80, 80, null);
        }
        
        //draw the food token nums
        g.setFont(new Font("Arial", Font.BOLD, 35));
        //out.println("Player foods: "+state.players[state.playing].getFoods());
        for (int i=0;i<5;i++){
            String foodTypeForToken="";
            switch(i){
                case 0 -> foodTypeForToken="s";
                case 1 -> foodTypeForToken="f";
                case 2 -> foodTypeForToken="b";
                case 3 -> foodTypeForToken="i";
                case 4 -> foodTypeForToken="r";
            }
        g.drawString(""+state.players[state.playing].getFoodCount(foodTypeForToken), 1416, 55+78*i);
        }
        //g.drawString(state.players[state.playing].getFoods)

        //birdpics
        BufferedImage pic;
        for (int i=0;i<state.players[state.playing].getCardsInHand().size();i++){
            pic = state.players[state.playing].getCardsInHand().get(i).getImage();
            g.drawImage(pic, 17+(102/state.players[state.playing].getCardsInHand().size())*i, 239, 130, 185, null);
        }

        //bonuspics
        for (int i=0;i<state.players[state.playing].getBonuses().size();i++){
            pic = state.players[state.playing].getBonuses().get(i).getImage();
            g.drawImage(pic, 17+(102/state.players[state.playing].getBonuses().size())*i, 490, 120, 170, null);
        }

        //cardTray
        if (state.cardTray[0] != null) g.drawImage(state.cardTray[0].getImage(), 1356, 450, 85, 120, null);
        if (state.cardTray[1] != null) g.drawImage(state.cardTray[1].getImage(), 1446, 450, 85, 120, null);
        if (state.cardTray[2] != null) g.drawImage(state.cardTray[2].getImage(), 1446, 577, 85, 120, null);
        
    }
    
    boolean tradeStarted = false;
    //selectingBool exists too!
    public void paintTrade(Graphics g){
        //out.println("PRINTING TRADE");
        //paintGame(g);
        if (!tradeStarted){
            paintGame(g);
            switch (runningHabitat){
                case "forest" -> {paintBoolean(g, "Trade bird for food?", "Yes", "No");}
                case "grasslands" -> {paintBoolean(g, "Trade food for egg?", "Yes", "No");}
                case "wetlands" -> {paintBoolean(g, "Trade egg for bird?", "Yes", "No");}
            }
        }else{
            g.setFont(new Font("Arial", Font.BOLD, 55));
            if (runningHabitat.equals("forest")){
                paintViewBirds(g);
                g.drawString("Click a card to trade", 400, 200);
            }else if (runningHabitat.equals("grasslands")){
                g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
                g.drawString("Click a food to trade", 400, 200);
                for (int i=0;i<5;i++){
                    String foodTypeForToken="";
                    switch(i){
                        case 0 -> foodTypeForToken="s";
                        case 1 -> foodTypeForToken="f";
                        case 2 -> foodTypeForToken="b";
                        case 3 -> foodTypeForToken="i";
                        case 4 -> foodTypeForToken="r";
                    }
                    g.drawString(""+state.players[state.playing].getFoodCount(foodTypeForToken), 285+100*i, 700);
                }
                g.drawImage(wheatToken, 250, 550, 100, 100, null);
                g.drawImage(fishToken, 350, 550, 100, 100, null);
                g.drawImage(fruitToken, 450, 550, 100, 100, null);
                g.drawImage(invertebrateToken, 550, 550, 100, 100, null);
                g.drawImage(rodentToken, 650, 550, 100, 100, null);
            }else if (runningHabitat.equals("wetlands")){
                g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
                g.drawString("Click to trade an egg", 400, 200);
            }
            //g.drawImage(skip, 1200, 350, 300, 80, null);
        }
    }

    ArrayList<ArrayList<Bird>> birdArrSplit = new ArrayList<ArrayList<Bird>>();
    int currentShowing = 0;
    int showing = 4;
    public void paintViewBirds(Graphics g){
        out.println("Painting view birds");
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Bird Cards", 600, 458);
        g.drawString(""+state.players[state.playing].getCardsInHand().size(), 1400, 460);
        if (state.players[state.playing].getCardsInHand().size()==0) return;

        ArrayList<ArrayList<Bird>> birdArrSplit = new ArrayList<ArrayList<Bird>>();
        int counter = 0;
        for (Bird b: state.players[state.playing].getCardsInHand()){
            if (counter %showing == 0) 
                birdArrSplit.add(new ArrayList<Bird>());
            counter ++;
            birdArrSplit.get(birdArrSplit.size()-1).add(b);
        }

        for (int i=0;i<birdArrSplit.get(currentShowing).size();i++){
            g.drawImage(birdArrSplit.get(currentShowing).get(i).getImage(), 250 + 250*i, 500, 240, 325,null);
        }
        //BUG::: rightarrow still shows up even though there are only 4 birbs (max is 4)

        if (currentShowing != 0) g.drawImage(leftArrow, 50, 590, 60, 60, null);
        if (currentShowing != (state.players[state.playing].getCardsInHand().size()-1)/4) g.drawImage(rightArrow, 1400, 590, 60, 60, null);
    }

    public void paintPlayBird(Graphics g){
        
        paintGame(g);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Select Slot to place bird", 600, 130);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));

        g2.drawRect(470, 155, 628-470, 392-155);
        g2.drawRect(469, 403, 627-469, 637-403);
        g2.drawRect(470,650,626-470,866-650);

        g2.drawRect(644, 155, 800-644, 392-155);
        g2.drawRect(644, 403, 800-644, 637-403);
        g2.drawRect(644,650,800-644,866-650);
       
        g2.drawRect(815, 155, 969-815, 392-155);
        g2.drawRect(815, 403, 969-815, 637-403);
        g2.drawRect(815,650,969-815,866-650);

        g2.drawRect(985, 155, 1138-985, 392-155);
        g2.drawRect(985, 403, 1138-985, 637-403);
        g2.drawRect(985,650,1138-985,866-650);

        g2.drawRect(1152, 155, 1302-1152, 392-155);
        g2.drawRect(1152, 403, 1302-1152, 637-403);
        g2.drawRect(1152,650,1302-1152,866-650);
        
    }

    //Ability stuffs
    public void paintWPAbility(Graphics g){
        g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
        g.setFont(new Font("Arial", Font.BOLD, 55));
        g.drawString("Click anywhere to continue (that isn't skip)", 400, 200);
        g.drawString("Any When Placed abilities will be auto-activated", 300, 300);
        g.drawImage(currentBird.getImage(), 500, 400, 340, 470, null);
        g.drawImage(skip, 1000, 550, 300, 80, null);
        
    }
    
    Bonus[] bonusSelectOptions = new Bonus[2];
    public void paintBonusDraw(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Draw Bonus: Click to choose the one you want", 300, 458);
        g.drawImage(bonusSelectOptions[0].getImage(), 400, 470, 245, 355, null);
        g.drawImage(bonusSelectOptions[1].getImage(), 800, 470, 245, 355, null);
    }
    int loopDrawing = 0;
    ArrayList<Bird> loopOptions = new ArrayList<Bird>();
    public void paintLoopDraw(Graphics g){
        g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
        g.setFont(new Font("Arial", Font.BOLD, 55));
        g.setColor(Color.BLACK);
        g.drawString("Player " + (loopDrawing+1) + " can select a card", 400, 200);
        
        for (int i=0;i<loopOptions.size();i++){
            g.drawImage(loopOptions.get(i).getImage(), 120+230*i, 515, 215, 260, null);
        }
    }

    public void paintDrawBirds(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));

        int count = 0;
        for (String s: state.CURRENTEVENT) if (s.equals("Draw Birds")) count++;
        g.drawString("Draw Birds: " + count, 600, 458);
        
        g.drawImage(birdBack, 120, 515, 215, 260, null);
        for (int i=0;i<3;i++)
            if (state.cardTray[i]!=null) g.drawImage(state.cardTray[i].getImage(), 475+270*i, 470, 245, 355, null);
        
    }

    public void paintViewBonus(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Bonus Cards", 600, 458);
        g.drawString(""+state.players[state.playing].getBonuses().size(), 1400, 460);
        
                if (state.players[state.playing].getBonuses().isEmpty()) return;
        Bonus b;
        for (int i=0;i<state.players[state.playing].getBonuses().size();i++){
            b = state.players[state.playing].getBonuses().get(i);
            g.drawImage(b.getImage(), 60 + 250*i, 500, 240, 320, null);
        }
    }
    
    public void paintViewDrawBirds(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Draw Birds", 600, 458);
        
        g.drawImage(birdBack, 120, 515, 215, 260, null);
        for (int i=0;i<3;i++)
            if (state.cardTray[i]!=null) g.drawImage(state.cardTray[i].getImage(), 475+270*i, 470, 245, 355, null);
        
    }

    public void paintViewFeeder(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Feeder", 600, 458);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        g.drawString("In", 900, 525);
        g.drawString("Out", 360, 525);

        g.drawImage(feederPic, 1150, 430, 350, 395, null);

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.drawRect(730, 494, 425, 298);
        g2.drawRect(207, 494, 425, 298);

        for (int i=0;i<feeder.getDice().size();i++){
            g.drawImage(dicePics[feeder.getImageIndex(i)], diceLocMap[i][0], diceLocMap[i][1], 90, 90, null);
        }
        for (int i=0;i<feeder.getOutDice().size();i++){
            g.drawImage(dicePics[feeder.getOutImageIndex(i)], diceLocMap[i][0]-520, diceLocMap[i][1], 90, 90, null);
        }
        if (feeder.canReroll()) g.drawImage(cover, 1185, 480, 115, 115, null);
        
    }

    public void paintInfo(Graphics g){
        g.drawImage(bg, 0,0, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 30, 30, 90, 90, null);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        //g.drawString("This is a PLACEHOLDER for Info (I'm lazy :))", 100, 300);
        BufferedImage birb;
        g.setColor(Color.CYAN);
        g.fillRect(400, 400, 200, 100);
        g.setColor(Color.BLACK);
        g.drawString("Rules", 400, 480);
        
        /*try{
            birb = ImageIO.read(FramePanel.class.getResource("/assets/yes.jpg"));
            g.drawImage(birb, 300, 400, 700, 320, null);
        }catch(Exception e){
            out.println("oops");
        }*/
    }

    private int rulePage = 0;
    public void paintRules(Graphics g){
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 30, 30, 90, 90, null);
        g.drawImage(rulePics[rulePage], getWidth()/2-350, 5, 700, getHeight()-50, null);
        if (rulePage != 0) g.drawImage(leftArrow, 100, getHeight()/2, 70, 70, null);
        if (rulePage != 11) g.drawImage(rightArrow, 1400, getHeight()/2, 70, 70, null);
       
    }
    
    public void startSetUp(){
        try {
            for (int i=0;i<6;i++){
                dicePics[i] = ImageIO.read(FramePanel.class.getResource("/assets/dice/"+i+".png"));
            }
            for (int i=0;i<12;i++){
                rulePics[i] = ImageIO.read(FramePanel.class.getResource("/assets/rules/"+i+".png"));
            }
            ingameBg = ImageIO.read(FramePanel.class.getResource("/assets/ingamebg.png"));
            wheatToken = ImageIO.read(FramePanel.class.getResource("/assets/Wheat_Token.png"));
            invertebrateToken = ImageIO.read(FramePanel.class.getResource("/assets/Invertebrate_Token.png"));
            fishToken = ImageIO.read(FramePanel.class.getResource("/assets/Fish_Token.png"));
            fruitToken = ImageIO.read(FramePanel.class.getResource("/assets/Fruit_Token.png"));
            rodentToken = ImageIO.read(FramePanel.class.getResource("/assets/Rodent_Token.png"));
            exitPic = ImageIO.read(FramePanel.class.getResource("/assets/Exit_Button.png")); 
            leftArrow = ImageIO.read(FramePanel.class.getResource("/assets/Left_Arrow.png")); 
            rightArrow = ImageIO.read(FramePanel.class.getResource("/assets/Right_Arrow.png")); 
            birdBack = ImageIO.read(FramePanel.class.getResource("/assets/blue_back.png"));
            feederPic = ImageIO.read(FramePanel.class.getResource("/assets/feeder.png"));
            Continue_Button = ImageIO.read(FramePanel.class.getResource("/assets/Continue_Button.png"));
            Action_Button = ImageIO.read(FramePanel.class.getResource("/assets/Action_Button.png"));
            Score_By_Round = ImageIO.read(FramePanel.class.getResource("/assets/score_by_round.png"));
        } catch (Exception e) {
            out.println("Exception: "+e);
            out.println("Oops pics dont load");
        }
        diceLocMap[0][0] = 769;diceLocMap[0][1] = 550;
        diceLocMap[1][0] = 925;diceLocMap[1][1] = 550;
        diceLocMap[2][0] = 1046;diceLocMap[2][1] = 550;
        diceLocMap[3][0] = 827;diceLocMap[3][1] = 670;
        diceLocMap[4][0] = 989;diceLocMap[4][1] = 670;
        readCSV(new File("src/birdInfo.csv"));
        setUpBirdPics();
        //mockSetup();
        setUpBonus();
        setRgoals();
        updateTray();
        out.println(birds.size());
        Collections.shuffle(birds);

    }
    //read in birdinfo
    public void readCSV(File f){
        for (String b: bonuses) bonusMap.put(b, new ArrayList<String>());
        try (Scanner scan = new Scanner(f)) {
            Bird b;
            String[] items;
            int readAmt = 100;
            while (scan.hasNextLine() && readAmt>0){
                String l = scan.nextLine();
                //out.println("line: " + l);
                if (l.contains("\"")){
                    l = l.replace("\"\"", "");
                    String[] quoteSplit = l.split("\"");
                    //out.println("quotesplit: "+Arrays.toString(quoteSplit));
                    ArrayList<String> supportSplit = new ArrayList<>();
                    supportSplit.addAll(Arrays.asList(quoteSplit[0].split(",")));
                    supportSplit.add(quoteSplit[1]);
                    supportSplit.addAll(Arrays.asList(quoteSplit[2].split(",")));
                    supportSplit.remove(3);
                    items = new String[supportSplit.size()];
                    //out.println("Support: "+supportSplit);
                    for (int i=0;i<items.length;i++) items[i] = supportSplit.get(i);
                    
                    //for (int i=0;i<items.length;i++) out.print(i+": "+items[i]+"        ");
                }else{
                    items = l.split(",");
                }


                // Doing all the food parsing yey
                Map<Integer, String> foodMap = new HashMap<Integer, String>();
                String[] foodtypes = {"i", "s", "f", "b", "r", "","a"};
                for (int i=13;i<20;i++) foodMap.put(i, foodtypes[i-13]);
                ArrayList<String[]> foodArr = new ArrayList<>();
                ArrayList<String> foods = new ArrayList<>();
                //out.println("Food stuff instantiated");
                if (items[20].equals("/")){
                    //out.println("Activated splitfoods");
                    for (int i=13;i<18;i++)
                        if (!items[i].equals("")) foods.add(foodMap.get(i));

                    for (String fo: foods){
                        String[] foo = {fo};
                        foodArr.add(foo);
                    }
                    
                }else{
                    //out.println("Activated setfoods");
                    ArrayList<String> foo = new ArrayList<>();
                    for (int i=13;i<20;i++){
                        if (!items[i].equals("")){
                            //out.println("Adding");
                            for (int j=0;j<Integer.parseInt(items[i]);j++){
                                foo.add(foodMap.get(i));
                                //out.println("Added food to foodMap");
                            }
                        }
                    }
                    String[] foox = new String[foo.size()];
                    for (int i=0;i<foo.size();i++) foox[i] = foo.get(i);
                    //out.println(Arrays.toString(foox));
                    foodArr.add(foox);
                }

                // Ability type stuff
                //out.println("Got to abilityType stuff");
                String abilityType;
                if (items[3].equals("X")) abilityType = "predator";
                else if (items[4].equals("X")) abilityType = "flocking";
                else if (items[5].equals("X")) abilityType = "bonus";
                else abilityType = "";

                //ablityActivate stuff
                //out.println("Got to abilityActivate stuff");
                String abilityActivate;
                abilityActivate = switch (items[1]) {
                    case "brown" -> "OA";
                    case "white" -> "WP";
                    case "pink" -> "OBT";
                    default -> "N";
                };

                //habitat stuff
                //out.println("Got to Habitat stuff");
                ArrayList<String> habitats = new ArrayList<>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X")) habitats.add("p");
                if (items[12].equals("X")) habitats.add("w");
                //out.println("Items: " + items[11]+items[12]+items[13]);
                //out.println("Habitats: "+habitats);

                //out.println("Got to birdmaking stuff");
                
                b = Bird.create(items[0], abilityActivate, items[2], abilityType, Integer.parseInt(items[6]), items[7], Integer.parseInt(items[8]), Integer.parseInt(items[9]), habitats, foodArr);
                birds.add(b);
                //out.println("Finished birdmaking stuff");

                int i=22;
                while (i<22+bonuses.length && i<items.length){
                    if (items[i].equals("X")){
                        bonusMap.get(bonuses[i-22]).add(items[0]);
                    }
                    i++;
                }
                readAmt -= 1;
            }
        } catch (Exception e) {
            out.println("Exception: " + e + "\ncsv reading ran into issue");
        }
        
        
    }
    //load birdpics
    public void setUpBirdPics(){
        try{
            for (Bird b: birds){
                String name = b.getName().toLowerCase().replace("-","_").replace("'","").replace(" ","_");
                //out.println(name);
                b.setImage(ImageIO.read(Tester.class.getResource("/assets/birds/"+name+".png")));
                
            }
        }catch(Exception e){
            out.println("Exception: "+e);
            out.println("Images couldn't be loaded.");
        }
    }
    
    //update card tray
    public void updateTray(){
        for (int i=0;i<3;i++)
            if (state.cardTray[i] == null) state.cardTray[i] = birds.removeFirst();
        out.println("Updated tray");
        
    }

    //select round goals
    public void setRgoals(){
        //out.println("Ran");
        //if (setUp == true) return;
        //else setUp = true;
        roundGoals.clear();
        int randNum;
        for (int i=0;i<4;i++){
            randNum = (int)(Math.random()*16);
            while (roundGoals.contains(randNum))
                randNum = (int)(Math.random()*16);
            roundGoals.add(randNum);
            
        }

        // YES I KNOW EGG IN CAVITY IS UPSIDE DOWN IDFC
        for (int i=0;i<4;i++){
            try {
                roundPics[i] = ImageIO.read(FramePanel.class.getResource("/assets/round/"+roundGoals.get(i)+".png"));
            } catch (Exception e) { 
                out.println("Exception: "+e);
                out.println("Round goal pics unloaded");
            }
        }
        //out.println(roundGoals);
    } 
    //start Selection methods
    public void mockSetup(){
        Bird b = birds.get(0);
        for (int i=0;i<169;i++){
            birds.add(b);
        }
    }
    public void setUpBonus(){
        for(int i=0;i<bonuses.length;i++){

            bonusArr.add(new Bonus(bonuses[i], bonusMap.get(bonuses[i])));
            if (i==5||i==8||i==10||i==22) continue;
            try{bonusArr.get(i).setImage(ImageIO.read(Tester.class.getResource("/assets/bonus/"+bonuses[i]+".png")));}
            catch(Exception e){out.println("Sreejit sold for " + bonuses[i]);}
        }
        bonusArr.remove(22);
        bonusArr.remove(10);
        bonusArr.remove(8);
        bonusArr.remove(5);
    }
    public boolean canContinue(){
        int sum = 0;
        for (int i=0;i<10;i++){
            if (startSelections[i]) sum++;
        }

        int sum2 = startSelections[10]?1:0;
        sum2 += startSelections[11]?1:0;

        return sum==5 && sum2==1;
        
    }
    public void setUpSelection(){
        startSelections = new boolean[12]; //1-5 for birds, 6-10 for foods, 11-12 for bonus
        startOptions = new Bird[5];
        for (int i=0;i<5;i++){
            startOptions[i] = birds.remove(0);
        }
        bonusOptions = new Bonus[2];
        bonusOptions[0] = bonusArr.remove(0);
        bonusOptions[1] = bonusArr.remove(0);
        
    }

    public void endTurn(ProgramState.PlayerAction action) {
        //Todo: state needs a game portion
        state.game.next(action);
    }




    public void paintGetFoodFromFeeder(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Get Food from Feeder", 600, 458);

        g.drawImage(feederPic, 1150, 430, 350, 395, null);
        if(feeder.canReroll()){
            g.drawImage(Reroll_Button, 50, 700, 100, 100, null);
            
        }

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.drawRect(730, 494, 425, 298);
        g.drawString(""+state.numberOfRemovableDice, 1400, 460);
        for (int i=0;i<feeder.getDice().size();i++){
            g.drawImage(dicePics[feeder.getImageIndex(i)], diceLocMap[i][0], diceLocMap[i][1], 90, 90, null);
        }
       
    }
    
    public void paintTradeCardForFood(Graphics g){
        if (state.players[state.playing].getCardsInHand().size()==0){ state.CURRENTEVENT.removeLast();repaint();return;}
        paintGetFoodFromFeeder(g);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Select a bird card to trade for food, or press the x to decline", 300, 150);
        
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        
        
        g.drawString(""+state.players[state.playing].getCardsInHand().size(), 1400, 460);
      

        ArrayList<ArrayList<Bird>> birdArrSplit = new ArrayList<ArrayList<Bird>>();
        int counter = 0;
        for (Bird b: state.players[state.playing].getCardsInHand()){
            if (counter %showing == 0) 
                birdArrSplit.add(new ArrayList<Bird>());
            counter ++;
            birdArrSplit.get(birdArrSplit.size()-1).add(b);
        }

        for (int i=0;i<birdArrSplit.get(currentShowing).size();i++){
            g.drawImage(birdArrSplit.get(currentShowing).get(i).getImage(), 250 + 250*i, 500, 240, 325,null);
        }
     

        if (currentShowing != 0) g.drawImage(leftArrow, 50, 590, 60, 60, null);
        if (currentShowing != (state.players[state.playing].getCardsInHand().size()-1)/4) g.drawImage(rightArrow, 1400, 590, 60, 60, null);
    }
    public void paintLayEggs(Graphics g){
      paintGame(g);
      Graphics2D g2 = (Graphics2D)g;
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

}
}
     
   