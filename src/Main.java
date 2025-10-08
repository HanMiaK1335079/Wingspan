package src;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Wingspan");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(0);
        frame.add(new FramePanel());
        frame.setVisible(true);

    }

}
