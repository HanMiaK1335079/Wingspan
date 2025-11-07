package src;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
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
           switch(state.CURRENTEVENT.getLast()) {
            case "Game Start" -> {
                Point p = e.getPoint();
                // Update rect position in case panel was resized
                // Suchit has reached his limit on ChatGPT-4O
                updateStartButtonRect();
                if (startButtonRect.contains(p)) {
                    synchronized(state.lock) {
                        System.out.println("Start button clicked - transitioning to game setup");
                        state.CURRENTEVENT.add("Process Mouse Click Game Start");
                        this.repaint();
                        state.CURRENTEVENT.removeLast();
                        state.lock.notifyAll();
                    }
                }
            }
        }
    }
        public void mousePressed(MouseEvent e) {}
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
              
                
                
                state.CURRENTEVENT.removeLast();
                state.CURRENTEVENT.add("Player one turn");
                GameLogic gameLogic = new GameLogic(this, state);
                gameLogic.setUp();
                break;
            }
            case "Game Start" -> {
                Graphics2D g2 = (Graphics2D) g;
                // Smooth rendering ts was ai
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background image scaled to panel size ts was ai
                int w = getWidth();
                int h = getHeight();
                if (cover != null) g2.drawImage(cover, 0, 0, w, h, null);

                // Dim overlay for readability ts was ai
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(0, 0, w, h);

                // Draw title ts was ai
                g2.setFont(titleFont);
                g2.setColor(new Color(255, 245, 230));
                String title = "Wingspan";
                FontMetrics fmTitle = g2.getFontMetrics();
                int tx = (w - fmTitle.stringWidth(title)) / 2;
                int ty = h / 3;
                g2.drawString(title, tx, ty);

                // Button geometry ts was ai
                updateStartButtonRect();
                RoundRectangle2D.Float rr = new RoundRectangle2D.Float(startButtonRect.x, startButtonRect.y, startButtonRect.width, startButtonRect.height, 24, 24);

                // Shadow you know the drill ts was ai
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Float(startButtonRect.x + 4, startButtonRect.y + 6, startButtonRect.width, startButtonRect.height, 24, 24));

                // Button fill (gradient changes on hover) used AI
                Color top = hover ? new Color(70, 160, 70) : new Color(50, 130, 200);
                Color bottom = hover ? new Color(40, 120, 40) : new Color(20, 80, 160);
                GradientPaint gp = new GradientPaint(startButtonRect.x, startButtonRect.y, top, startButtonRect.x, startButtonRect.y + startButtonRect.height, bottom);
                g2.setPaint(gp);
                g2.fill(rr);

                // Button border quality ts was ai
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(255, 255, 255, 160));
                g2.draw(rr);

                // Button text standard ts was ai
                g2.setFont(buttonFont);
                String label = "Click to Start";
                FontMetrics fm = g2.getFontMetrics();
                int bx = startButtonRect.x + (startButtonRect.width - fm.stringWidth(label)) / 2;
                int by = startButtonRect.y + (startButtonRect.height - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(Color.WHITE);
                g2.drawString(label, bx, by);

                break; //you know the drill ts was ai yield when switch quote done go me lol ts
            }
            default -> {
                
            }
        }
       
    
}
}
}
     
   