package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.System.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage cover, infoButton, bg, exitPic, leftArrow, rightArrow, birdBack, wheatToken, invertebrateToken, fishToken, fruitToken, rodentToken, Continue_Button, feederPic, Action_Button, Score_By_Round;
    private BufferedImage[] dicePics = new BufferedImage[6];
    private final ProgramState state;
    private Rectangle startButtonRect = new Rectangle(700, 700, 200, 100);
    private boolean hover = false;
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 64);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 28);
    private final ArrayList<Bird> birds = new ArrayList<Bird>();
    private BufferedImage ingameBg;
    private Feeder feeder;

    private ArrayList<Integer> roundGoals = new ArrayList<Integer>();
    private BufferedImage[] roundPics = new BufferedImage[4];
    private boolean setUp = false;

    private Map<String, ArrayList<String>> bonusMap = new HashMap<String, ArrayList<String>>();
    private String[] bonuses = {"Anatomist", "Cartographer", "Historian", "Photographer", "Backyard Birder", "Bird Bander", "Bird Counter", "Bird Feeder", "Diet Specialist", "Enclosure Builder", "Species Protector", "Falconer", "Fishery Manager", "Food Web Expert", "Forester", "Large Bird Specialist", "Nest Box Builder", "Omnivore Expert", "Passerine Specialist", "Platform Builder", "Prairie Manager", "Rodentologist", "Small Clutch Specialist", "Viticulturalist", "Wetland Scientist", "Wildlife Gardener"};
    private ArrayList<Bonus> bonusArr = new ArrayList<Bonus>();
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
            Action_Button = ImageIO.read(FramePanel.class.getResource("/assets/Action_Button.png"));
            Score_By_Round = ImageIO.read(FramePanel.class.getResource("/assets/score_by_round.png"));

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
    public void mousePressed(MouseEvent e) {
          int x = e.getX();
        int y = e.getY();
        out.println("("+x+","+y+")");
        // Only respond to clicks when we are on the start screen

        switch(state.CURRENTEVENT.getLast()) {
        
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
                    GameLogic gameLogic = new GameLogic(this, state);
                    //  gameLogic.setUp(); //this has yet to be set up.
                }
            }
        }
        case "Select Screen" -> {
            //food click
             
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
                if(numberOfItemsSelected<5&&!startSelections[6]){
                    startSelections[6] = !startSelections[6];
                    numberOfItemsSelected++;
                }else if(startSelections[6]){
                    startSelections[6] = !startSelections[6];
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
                if(numberOfItemsSelected<5&&!startSelections[8]){
                    startSelections[8] = !startSelections[8];
                    numberOfItemsSelected++;
                }else if(startSelections[8]){
                    startSelections[8] = !startSelections[8];
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
                //continue click
            }else if (x>=140 && x<=440 && y>=600 && y<=720){
                out.println("clicked box");
                if (canContinue()){
                    out.println("Continuing");
                    numberOfItemsSelected = 0;
                    numberOfBonusesSelected = 0;
                    for (int i=0;i<5;i++){
                        if (startSelections[i]) state.players[state.playing].addCardToHand(startOptions[i]);
                    }
                    if (startSelections[5]) state.players[state.playing].addFood("fish");
                    if (startSelections[6]) state.players[state.playing].addFood("seed");
                    if (startSelections[7]) state.players[state.playing].addFood("insect");
                    if (startSelections[8]) state.players[state.playing].addFood("berry");
                    if (startSelections[9]) state.players[state.playing].addFood("rat");

                    if (startSelections[10]) state.players[state.playing].addBonus(bonusOptions[0]);
                    else state.players[state.playing].addBonus(bonusOptions[1]);

                    if (state.playing == 3) {state.CURRENTEVENT.add("Game"); state.playing = 0;}
                    else {
                        state.playing ++;
                        setUpSelection();
                        setUpBonus();
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
        //     g.drawImage(Action_Button, 425, 200, 50,50,null);
        // g.drawImage(Action_Button, 425, 450, 50,50,null);
        // g.drawImage(Action_Button, 350, 725, 50,50,null);
        //g.drawImage(Action_Button, 480, 120, 50 ,50, null);
            if (x>=184 && x<=231 && y>=180 && y<=222) state.CURRENTEVENT.add("View Birds");
            else if (x>=190 && x<=235 && y>=440 && y<=484) state.CURRENTEVENT.add("View Bonus");
            else if (x>=37 && x<=83 && y>=683 && y<=726) state.CURRENTEVENT.add("View Feeder");
            else if (x>=508 && x<=589 && y>=22 && y<=86) state.CURRENTEVENT.add("Info");
            else if (x>=1375 && x<=1425 && y>=615 && y<=665) state.CURRENTEVENT.add("View Draw Birds");
            else if (x>=425 && x<=475 && y>=200 && y<=250) state.CURRENTEVENT.add("Get Food");
            else if (x>=425 && x<=475 && y>=450 && y<=500) state.CURRENTEVENT.add("Lay Eggs");
            else if (x>=350 && x<=400 && y>=725 && y<=775) state.CURRENTEVENT.add("Draw Birds");
            else if (x>=480 && x<=530 && y>=120 && y<=170) state.CURRENTEVENT.add("Play Bird");
            repaint();
        }case "View Birds" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
            else if (x>=1400 && y>=590 && x<=1460 && y<=650 && currentShowing != state.players[state.playing].getCardsInHand().size()%showing)
                currentShowing++;
            else if (x>=50 && x<=110 && y>=590 && y<=650 && currentShowing != 0) currentShowing--;
            //50, 590, 60, 60

            repaint();
        }case "View Bonus" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
            repaint();
        
        }case "View Draw Birds" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
            
            repaint();

        }case "View Feeder" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450) state.CURRENTEVENT.removeLast();
            repaint();

        }case "Info" -> {
            if (x>=30 && x<=120 && y>=30 && y<=120) state.CURRENTEVENT.removeLast();
            repaint();
        }
        case "Draw Birds" -> {
            if (x>=20 && x<=70 && y>=400 && y<=450){ state.CURRENTEVENT.removeLast();repaint();return;}
            if(y>=470 && y<=825){//This is the Y level of the cards in the draw tray
                    if(x>=475 && x<=720){//This would be the first card
                        out.println("Clicked first card");
                
                    } else if ( x >= 745 && x <= 990 ) {// This would be the second card
                        out.println("Clicked Second card"); //475+270*i, 470, 245, 355, (xy,wh)
                    } else if ( x >= 1015 && x <= 1260 ) {//This would be the third card
                        out.println("Clicked Third card");
                    }
            }
            if(x>=120&&x<=335&&y>=515&&y<=775){//120, 515, 215, 260,
                out.println("Clicked the bird deck");    
                }
            }
        case "Play Bird" -> {
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
        
            repaint();
        }
    }
}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
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
                case "View Birds" -> paintViewBirds(g);
                case "View Bonus" -> paintViewBonus(g);
                case "Info" -> paintInfo(g);
                case "View Draw Birds" -> paintViewDrawBird(g);
                case "View Feeder" -> paintViewFeeder(g);
                case "Draw Birds" -> paintDrawBirds(g);
                case "Score Round" -> paintScoreRound(g);
                case "Play Bird" -> paintPlayBird(g);
                default -> {
                
                }
            }
            state.lock.notifyAll();
        }
    }

    public void paintScoreRound(Graphics g){
        g.drawImage(Score_By_Round, 0, 0, getWidth(), getHeight(), null);
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
        if (startSelections[6]) g.fillOval(1195, 325, 110, 110);
        if (startSelections[7]) g.fillOval(1310, 220, 110, 110);
        if (startSelections[8]) g.fillOval(1425, 115, 110, 110);
        if (startSelections[9]) g.fillOval(1425, 325, 110, 110);
        g.drawImage(fishToken, 1200, 120, 100, 100, null); //fish
        g.drawImage(wheatToken, 1200, 330, 100, 100, null); //seed
        g.drawImage(fruitToken, 1430, 120, 100, 100, null); //fruit
        g.drawImage(rodentToken, 1430, 330, 100, 100, null); //rat
        g.drawImage(invertebrateToken, 1315, 225, 100, 100, null); //insect

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
        //425 200, 425 450, 350 725
       
        g.drawImage(ingameBg, 0, 0, 1540, 863,null);
        //480 150
        //Draw possible actions
        g.drawImage(Action_Button, 480, 120, 50 ,50, null);
        g.drawImage(Action_Button, 425, 200, 50,50,null);
        g.drawImage(Action_Button, 425, 450, 50,50,null);
        g.drawImage(Action_Button, 350, 725, 50,50,null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 55));
        g.drawString(state.playing+1+"", 238, 67);

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
        for (int i=0;i<5;i++)
            g.drawString(""+state.players[state.playing].getFoods().get(i), 1416, 55+78*i);
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
    
    int currentShowing = 0;
    int showing = 4;
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
    public void paintViewBirds(Graphics g){
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
            birdArrSplit.getLast().add(b);
        }

        for (int i=0;i<birdArrSplit.get(currentShowing).size();i++){
            g.drawImage(birdArrSplit.get(currentShowing).get(i).getImage(), 250 + 250*i, 500, 240, 325,null);
        }
        //BUG::: rightarrow still shows up even though there are only 4 birbs (max is 4)

        if (currentShowing != 0) g.drawImage(leftArrow, 50, 590, 60, 60, null);
        if (currentShowing != (state.players[state.playing].getCardsInHand().size()-1)/4) g.drawImage(rightArrow, 1400, 590, 60, 60, null);
    }

    public void paintDrawBirds(Graphics g){
        paintGame(g);
        g.drawImage(bg, 0, 380, getWidth(), getHeight(), null);
        g.drawImage(exitPic, 20, 400, 50, 50, null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Draw Birds", 600, 458);
        
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
        
        if (state.players[state.playing].getBonuses().size()==0) return;
        Bonus b;
        for (int i=0;i<state.players[state.playing].getBonuses().size();i++){
            b = state.players[state.playing].getBonuses().get(i);
            g.drawImage(b.getImage(), 60 + 250*i, 500, 240, 320, null);
        }
    }
    
    public void paintViewDrawBird(Graphics g){
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
        
    }

    public void paintInfo(Graphics g){
        g.drawImage(exitPic, 30, 30, 90, 90, null);
        g.setFont(new Font("Arial", Font.BOLD, 65));
        g.drawString("This is a PLACEHOLDER for Info (I'm lazy :))", 100, 300);
        BufferedImage birb;
        try{
            birb = ImageIO.read(FramePanel.class.getResource("/assets/yes.jpg"));
            g.drawImage(birb, 300, 400, 700, 320, null);
        }catch(Exception e){
            out.println("oops");
        }
        
    }
    

    public void startSetUp(){
        try {
            for (int i=0;i<6;i++){
                dicePics[i] = ImageIO.read(FramePanel.class.getResource("/assets/dice/"+i+".png"));
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
        mockSetup();
        setUpBonus();
        setRgoals();
        updateTray();

    }
    //read in birdinfo
    public void readCSV(File f){
        for (String b: bonuses) bonusMap.put(b, new ArrayList<String>());
        try {
            //out.println("Will activate scanner");
            Scanner scan = new Scanner(f);
            //out.println("Read the scanner");
            Bird b;
            String[] items;
            int readAmt = 1;
            while (scan.hasNextLine() && readAmt>0){
                String l = scan.nextLine();
                //out.println("line: " + l);
                if (l.contains("\"")){
                    l = l.replace("\"\"", "");
                    String[] quoteSplit = l.split("\"");
                    //out.println("quotesplit: "+Arrays.toString(quoteSplit));
                    ArrayList<String> supportSplit = new ArrayList<String>();
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
                ArrayList<String[]> foodArr = new ArrayList<String[]>();
                ArrayList<String> foods = new ArrayList<String>();
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
                    ArrayList<String> foo = new ArrayList<String>();
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
                ArrayList<String> habitats = new ArrayList<String>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X")) habitats.add("p");
                if (items[12].equals("X")) habitats.add("w");
                //out.println("Items: " + items[11]+items[12]+items[13]);
                //out.println("Habitats: "+habitats);

                //out.println("Got to birdmaking stuff");
                
                b = new Bird(items[0], abilityActivate, items[2], abilityType, Integer.parseInt(items[6]), items[7], Integer.parseInt(items[8]), Integer.parseInt(items[9]), habitats, foodArr);
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

}
     
   