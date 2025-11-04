package src;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, WIDTH, HEIGHT);

        JButton play = new JButton("Play Game");
        JButton rules = new JButton("Rules");
        play.setFocusPainted(false);
        rules.setFocusPainted(false);
        play.setForeground(Color.BLUE);
        play.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rules.setBackground(new Color(207, 216, 220));
        rules.setForeground(new Color(38, 50, 56));
        rules.setFont(new Font("Segoe UI", Font.BOLD, 16));
        play.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        rules.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel vbox = new JPanel();
        vbox.setOpaque(false);
        vbox.setLayout(new BoxLayout(vbox, BoxLayout.Y_AXIS));
        vbox.add(play);
        vbox.add(Box.createVerticalStrut(16));
        vbox.add(rules);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 750;
        gbc.gridy = 750;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE;

        overlay.add(vbox, gbc);

        MouseAdapter hoverPlay = new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ 
                play.setBounds(750,750,148,57);
                play.revalidate();
            }
            public void mouseExited(MouseEvent e){ 
                play.setBounds(750,750,148,47); 
                 play.revalidate(); 
            }
        };
        play.addMouseListener(hoverPlay);

        MouseAdapter hoverRules = new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ 
                rules.setPreferredSize(new Dimension(rules.getPreferredSize().width+6, rules.getPreferredSize().height+4)); rules.revalidate(); 
            }
            public void mouseExited(MouseEvent e){ 
                rules.setPreferredSize(null); rules.revalidate(); 
            }
        };
        rules.addMouseListener(hoverRules);

        play.addActionListener(e -> {
            state.CURRENTEVENT.add("Mouse Click for game Start");
            backgroundPanel.repaint();
            state.CURRENTEVENT.removeLast();
            state.CURRENTEVENT.add("Main Board");
            backgroundPanel.repaint();
        });

        layered.add(overlay, JLayeredPane.PALETTE_LAYER);
        add(layered, BorderLayout.CENTER);
        setVisible(true);
        System.out.println(play.getSize());
    }
}