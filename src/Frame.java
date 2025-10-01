package src;
import javax.swing.*;

public class Frame extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    public Frame(String n){
        super(n);
        setSize(WIDTH, HEIGHT);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new FramePanel());
        setVisible(true);
    }


}
