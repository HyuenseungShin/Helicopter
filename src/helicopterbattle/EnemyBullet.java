package helicopterbattle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Enemy Helicopter machine gun bullet.
 * 
 * @author www.gametutorial.net
 */

public class EnemyBullet {
    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;

    // Moving speed and direction.
    // 숫자 변경하면 적군 총알 속도 변경
    private double movingXspeed = 10;

    // Images of helicopter machine gun bullet. Image is loaded and set in Game class in LoadContent() method.
    public static BufferedImage bulletImg;


    /**
     * Creates new machine gun bullet.
     *
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     */
    public EnemyBullet(int xCoordinate, int yCoordinate)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Checks if the bullet is left the screen.
     * 
     * @return true if the bullet left the screen, false otherwise.
     */
    public boolean isItLeftScreen()
    {
        if(xCoordinate < 0 - bulletImg.getWidth()) // When the entire helicopter is out of the screen.
            return true;
        else
            return false;

    }

    /**
     * Moves the bullet.
     */
    public void Update()
    {
        xCoordinate -= movingXspeed;
    }
    
    
    /**
     * Draws the bullet to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(bulletImg, (int)xCoordinate, (int)yCoordinate, null);
    }
}