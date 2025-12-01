package src;
import javax.swing.*;

public class Main {
     public static void main(String[]args){
       ProgramState state = new ProgramState();
       FramePanel panel = new FramePanel(state);
       state.CURRENTEVENT.add("Game Start");
       Frame wingSpanFrame = new Frame("Wingspan");
     
        wingSpanFrame.setSize(1600, 900);        
        wingSpanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wingSpanFrame.add(panel);
        wingSpanFrame.setVisible(true);
        wingSpanFrame.setResizable(false);
        Thread gameThread = new Thread(new GameLogic(panel, state));
        gameThread.start();
     }
     
    
}