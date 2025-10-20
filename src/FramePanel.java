import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
public class FramePanel extends JPanel {
    private BufferedImage cover;
    private BufferedImage[] birdImages = new BufferedImage[170];
    private ArrayList<Bird> birds = new ArrayList<Bird>();
    

    public FramePanel()  {
        try{
            cover = ImageIO.read(FramePanel.class.getResource("/assets/cover_image.png"));
            for (int i=0;i<birdImages.length;i++){
                birdImages[i] = ImageIO.read(FramePanel.class.getResource("/assets/birdPics/bird"+i+".png"));
                // must have bird names be from bird0 -> bird169
            }
            
            

        } catch (Exception e){
            System.out.println("No workie because idk 🤷‍♂️");
        }
    }
    
     @Override
    public void paint(Graphics g) {
        g.drawImage(cover,0,0,1600,900,null);
    }
}
