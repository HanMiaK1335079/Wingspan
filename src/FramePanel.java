package src;
import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.System.out;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel implements MouseListener, KeyListener {
    private BufferedImage cover;
    private final ProgramState state;
    private ArrayList<Bird> birds;
    private Map<String, ArrayList<String>> bonusMap;

    public FramePanel(ProgramState state){
        this.state = state;
        addMouseListener(this);
        addKeyListener(this);
         try{
             cover = ImageIO.read(FramePanel.class.getResource("/assets/cover_image.png"));
             System.out.println("Workie");
         } catch (Exception e){
             System.out.println("No workie because idk 🤷‍♂️");
         }
         this.repaint();
    }
        public void addNotify() {
            super.addNotify();
            requestFocus();
            
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            
            switch(state.CURRENTEVENT.getLast()) {
                case "Game Start" -> {
                    synchronized(state.lock) {
                        System.out.println("workie click");
                        state.CURRENTEVENT.add("Process Mouse Click Game Start");
                        System.out.println(state.CURRENTEVENT);
                        this.repaint();
                        System.out.println(state.CURRENTEVENT);
                        state.CURRENTEVENT.removeLast();
                        
                        state.lock.notifyAll();
                    }
                }
                default -> {
                    // No action for other cases
                }
            }
            
        
        
        
        }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    
    @Override
    public void paint(Graphics g) {
         super.paint(g);
        synchronized(state.lock){
            switch(state.CURRENTEVENT.getLast()) {
                case "Process Mouse Click Game Start" -> {
                
                    
                    state.CURRENTEVENT.removeLast();
                    break;
                }
                case "Game Start" ->{
                    g.drawImage(cover, 0, 0, 1600, 900, null);
                    
                    break;
                    
                }
                case "Main Board" -> {
                g.drawImage(board, 0, 0, 1600, 900, null);
                break;
            }
            default -> {
                    
                }
            }
            state.lock.notifyAll();
        }
    }

    public void readCSV(File f){
        try {
            Scanner scan = new Scanner(f);
            ListIterator iter;
            while (scan.hasNextLine()){
                String l = scan.nextLine();
                String[] items = l.split(",");

                Bird b = new Bird(items[0], items[1], items);
                new Bird(l, items, items, WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, FRAMEBITS, WIDTH, l, l, items)
            }
        } catch (Exception e) {
            out.println("Exception: " + e + " csv was not read");
        }
        
        
    }

}
     
   

