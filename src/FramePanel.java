package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class FramePanel extends JPanel {

    private BufferedImage cover;

    public FramePanel() {
        try {
            cover = ImageIO.read(FramePanel.class.getResource("/assets/cover_image.png"));
        } catch (Exception e) {
            System.out.println("No workie ");
        }
    }

    public void paint(Graphics g) {
        g.drawImage(cover, 0, 0, 1600, 900, null);
    }
}
