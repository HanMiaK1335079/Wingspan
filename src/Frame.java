import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;

    public Frame(String n) {
        super(n);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        ProgramState state = new ProgramState();
        state.setCurrentEvent(ProgramState.GameEvent.GAME_START);

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        add(layered, BorderLayout.CENTER);
        setVisible(true);
    }
}