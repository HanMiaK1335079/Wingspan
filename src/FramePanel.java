package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel implements MouseListener, KeyListener {
     private BufferedImage cover;
     private final ProgramState state;
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
             System.out.println(state.CURRENTEVENT);
            switch(state.CURRENTEVENT.getLast()) {
                case "Game Start" -> {
                    
                        state.CURRENTEVENT.add("Process Mouse Click Game Start");
                        
                        this.repaint();
                       
                        
                        
                    break;
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
       
        switch(state.CURRENTEVENT.getLast()) {
            case "Process Mouse Click Game Start" -> {
              
               
                state.CURRENTEVENT.removeLast();
                break;
            }
            case "Game Start" ->{
                g.drawImage(cover, 0, 0, 1600, 900, null);
                 
                break;
                
            }
            default -> {
                
            }
        }
       
    
}
}
     
   

