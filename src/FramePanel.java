package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.System.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage cover, infoButton, bg;
    private final ProgramState state;
    private Rectangle startButtonRect = new Rectangle(700, 700, 200, 100);
    private boolean hover = false;
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 64);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 28);
    private final ArrayList<Bird> birds = new ArrayList<Bird>();
;
    private Map<String, ArrayList<String>> bonusMap = new HashMap<String, ArrayList<String>>();
    private String[] bonuses = {"Anatomist", "Cartographer", "Historian", "Photographer", "Backyard Birder", "Bird Bander", "Bird Counter", "Bird Feeder", "Diet Specialist", "Enclosure Builder", "Species Protector", "Falconer", "Fishery Manager", "Food Web Expert", "Forester", "Large Bird Specialist", "Nest Box Builder", "Omnivore Expert", "Passerine Specialist", "Platform Builder", "Prairie Manager", "Rodentologist", "Small Clutch Specialist", "Viticulturalist", "Wetland Scientist", "Wildlife Gardener"};
    private ArrayList<Bonus> bonusArr = new ArrayList<Bonus>();
    /*Gamestate variables */

    boolean[] startSelections; //1-5 for birds, 6-10 for foods, 11-12 for bonus
    Bird[] startOptions;
    Bonus[] bonusOptions;

    public FramePanel(ProgramState state){
        this.state = state;
        addMouseListener(this);
        readCSV(new File("assets/birdInfo.csv"));
        setUpBirdPics();
        mockSetup();
        setUpBonus();
        
        

        //Add all buffered images here 
        try{
            cover = ImageIO.read(FramePanel.class.getResource("/assets/cover_image.png"));
            infoButton = ImageIO.read(FramePanel.class.getResource("/assets/info picture.png"));
            bg = ImageIO.read(FramePanel.class.getResource("/assets/table_bg.png"));
        } catch (Exception e){
            System.out.println("No workie because idk 🤷‍♂️");
            System.out.println(e);
        }

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
        int x = e.getX();
        int y = e.getY();
        out.println("("+x+","+y+")");
        // Only respond to clicks when we are on the start screen
        if ("Game Start".equals(state.CURRENTEVENT.getLast())) {
            Point p = e.getPoint();
            // Update rect position in case panel was resized
            updateStartButtonRect();
            if (startButtonRect.contains(p)) {
                synchronized(state.lock) {
                    System.out.println("Start button clicked");
                    state.CURRENTEVENT.add("Select Screen");
                    setUpSelection();
                    this.repaint();
                    
                    state.lock.notifyAll();
                    GameLogic gameLogic = new GameLogic(this, state);
                    //  gameLogic.setUp(); //this has yet to be set up.
                }
            }
        }else if ("Select Screen".equals(state.CURRENTEVENT.getLast())){
            if (y>=120 && y<=420){
                for (int i=0;i<5;i++){
                    if (x>=30+i*220 && x<=230+i*220){
                        startSelections[i] = !startSelections[i];
                        out.println("Swapped");
                        
                    }
                } //30+i*220, 120, 200, 300
            }else if (x>=140 && x<=340 && y>=600 && y<=680){
                out.println("clicked box");
                if (canContinue()){
                    out.println("Continuing");
                    //state.players[state.playing].addCard(birds[0])

                    if (state.playing == 3) {/*start game */ state.playing = 0;}
                    else {
                        state.playing ++;
                        setUpSelection();
                        setUpBonus();
                    }
                }
            
            }else if (y>=500 && y<=800){
                if (x>=700 && x<=900){
                    startSelections[10] = !startSelections[10];
                }else if (x>=950 && x<=1150){
                    startSelections[11] = !startSelections[11];
                }
            }
            this.repaint();
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        // highlight the button when hovered
        /*updateStartButtonRect();
        boolean nowHover = startButtonRect.contains(e.getPoint());
        if (nowHover != hover) {
            hover = nowHover;
            repaint();
        }*/
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
                case "Game Start" -> {
                    paintStart(g);
                    break;
                }
                case "Select Screen" -> {
                    paintSelection(g);
                    break;
                }
                
                default -> {
                
                }
            }
            state.lock.notifyAll();
        }
    }

    public void paintStart(Graphics g){
        /*Graphics2D g2 = (Graphics2D) g;
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
        g2.drawString(label, bx, by);*/
    }

    public void paintSelection(Graphics g){
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        g.setFont(new Font("Arial", 1, 50));
        g.drawString("Player: " + (1+state.playing), 60, 80);
        g.setFont(new Font("Arial", 1, 30));
        g.drawString("Select 5", 1000, 80);
        g.drawLine(40, getHeight()/2, getWidth()-40, getHeight()/2);
        for (int i=0;i<5;i++){
            if (startSelections[i]) g.fillRect(25+i*220, 115, 210, 310);
            g.drawImage(startOptions[i].getImage(), 30+i*220, 120, 200, 300,null);
        }

        g.drawImage(infoButton, 1200, 120, 100, 100, null); //fish
        g.drawImage(infoButton, 1200, 330, 100, 100, null); //seed
        g.drawImage(infoButton, 1430, 120, 100, 100, null); //fruit
        g.drawImage(infoButton, 1430, 330, 100, 100, null); //rat
        g.drawImage(infoButton, 1315, 225, 100, 100, null); //insect

        if (startSelections[10]) g.fillRect(695, 495, 210, 310);
        if (startSelections[11]) g.fillRect(945, 495, 210, 310);
        g.drawImage(bonusOptions[0].getImage(), 700, 500, 200, 300, null);
        g.drawImage(bonusOptions[1].getImage(), 950, 500, 200, 300, null);
        
        g.fillRect(140, 600, 200, 80);
        //g.fillRect(140, 700, 200, 80);
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
}
     
   
