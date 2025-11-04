package src;
import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;

    public Frame(String n){
        super(n);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ProgramState state = new ProgramState();
        state.CURRENTEVENT.add("BASE");
        state.CURRENTEVENT.add("Game Start");

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        FramePanel backgroundPanel = new FramePanel(state);
        backgroundPanel.setBounds(0, 0, WIDTH, HEIGHT);
        layered.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        
        add(layered, BorderLayout.CENTER);
        setVisible(true);
    }
}