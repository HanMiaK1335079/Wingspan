package src;
 import javax.swing.*;
public class Main {
     public static void main(String[]args){
       ProgramState state = new ProgramState();
       FramePanel panel = new FramePanel(state);
       state.CURRENTEVENT.add("BASE");
       state.CURRENTEVENT.add("Game Start");
       JFrame wingSpanFrame = new JFrame("Wingspan");
     
        wingSpanFrame.setSize(1600, 900);        
        wingSpanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wingSpanFrame.add(panel);
        wingSpanFrame.setVisible(true);
        Thread gameThread = new Thread(new GameLogic(panel, state));
        gameThread.start();
     }
     
    
}
