package src;
import java.io.IOException;

import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class Main {
     public static void main(String[]args) throws UnsupportedAudioFileException, IOException{
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