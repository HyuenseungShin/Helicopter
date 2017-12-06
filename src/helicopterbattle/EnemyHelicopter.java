package helicopterbattle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Enemy helicopter.
 * 
 * @author www.gametutorial.net
 */

public class EnemyHelicopter {
    
    // For creating new enemies.
    private static final long timeBetweenNewEnemiesInit = Framework.secInNanosec * 3;
    public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
    public static long timeOfLastCreatedEnemy = 0;
    
    // Health of the helicopter.
    public int health;
    
    // Position of the helicopter on the screen.
    public int xCoordinate;
    public int yCoordinate;
    
    // Moving speed and direction.
    private static final double movingXspeedInit = -4;
    private static double movingXspeed = movingXspeedInit;
    
    // Images of enemy helicopter. Images are loaded and set in Game class in LoadContent() method.
    public static BufferedImage helicopterBodyImg;
    public static BufferedImage helicopterFrontPropellerAnimImg;
    public static BufferedImage helicopterRearPropellerAnimImg;
    
    // Animation of the helicopter propeller.
    private Animation helicopterFrontPropellerAnim;
    private Animation helicopterRearPropellerAnim;
    // Offset for the propeler. We add offset to the position of the position of helicopter.
    // 이거 변경
    private static int offsetXFrontPropeller = 3;
    private static int offsetYFrontPropeller = -3;
    private static int offsetXRearPropeller = 109;
    private static int offsetYRearPropeller = -9;
    private int position;
    private int moveCount;

    // Draw Body and propeller
    public int helicopterBodyWidth = 124;
    public int helicopterBodylength = 27;
    public int FrontPropellerWidth = 79;
    public int FrontPropellerHeight = 8;
    public int FrontPropellerNumberOfFrames = 3;
    public int FrontPropellerFrameTime = 20;
    public int RearPropellerWidth = 23;
    public int RearPropellerHeight = 24;
    public int RearPropellerNumberOfFrames = 10;
    public int RearPropellerFrameTime = 10;

    /**
     * Initialize enemy helicopter.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     * @param helicopterBodyImg Image of helicopter body.
     * @param helicopterFrontPropellerAnimImg Image of front helicopter propeller.
     * @param helicopterRearPropellerAnimImg Image of rear helicopter propeller.
     */
    // 여기 변경
    public void BulletInitialize(int xCoordinate, int yCoordinate, int position)
    {
        health = 100;
        moveCount = 0;
        
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.position = position;
        
        // Initialize animation object.
        helicopterFrontPropellerAnim = new Animation(helicopterFrontPropellerAnimImg, FrontPropellerWidth, 
              FrontPropellerHeight, FrontPropellerNumberOfFrames, FrontPropellerFrameTime, true, xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller, 0);
        helicopterRearPropellerAnim = new Animation(helicopterRearPropellerAnimImg, RearPropellerWidth, 
              RearPropellerHeight, RearPropellerNumberOfFrames, RearPropellerFrameTime, true, xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller, 0);
       
        // Moving speed and direction of enemy.
        EnemyHelicopter.movingXspeed = -4;
    }
    public void Initialize(int xCoordinate, int yCoordinate, int x, int y)
    {
        health = 100;
        moveCount = 0;
        
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        // Initialize animation object.
        helicopterFrontPropellerAnim = new Animation(helicopterFrontPropellerAnimImg, FrontPropellerWidth, 
              FrontPropellerHeight, FrontPropellerNumberOfFrames, FrontPropellerFrameTime, true, xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller, 0);
        helicopterRearPropellerAnim = new Animation(helicopterRearPropellerAnimImg, RearPropellerWidth, 
              RearPropellerHeight, RearPropellerNumberOfFrames, RearPropellerFrameTime, true, xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller, 0);
       
        // Moving speed and direction of enemy.
        EnemyHelicopter.movingXspeed = -4;
    }
    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public static void restartEnemy(){
        EnemyHelicopter.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        EnemyHelicopter.timeOfLastCreatedEnemy = 0;
        EnemyHelicopter.movingXspeed = movingXspeedInit;
    }
    /**
     * It increase enemy speed and decrease time between new enemies.
     */
    public static void speedUp(){
        if(EnemyHelicopter.timeBetweenNewEnemies > Framework.secInNanosec)
            EnemyHelicopter.timeBetweenNewEnemies -= Framework.secInNanosec / 100;
        EnemyHelicopter.movingXspeed -= 0.25;
    }
    /**
     * Checks if the enemy is left the screen.
     * 
     * @return true if the enemy is left the screen, false otherwise.
     */
    public boolean isLeftScreen()
    {
        if(xCoordinate < 0 - helicopterBodyImg.getWidth()) // When the entire helicopter is out of the screen.
            return true;
        else
            return false;
    }
    /**
     * Updates position of helicopter, animations.
     */
    public void Update()
    {
       moveCount++;
        if(moveCount == 100) {
            moveCount = 0;
        }
        // Move enemy on x coordinate.
        xCoordinate += movingXspeed;
        if(position == 0) {
            yCoordinate -= 2;
        } else if(position == 1) {
            yCoordinate += 2;
        }
        // Moves helicoper propeler animations with helicopter.
        helicopterFrontPropellerAnim.changeCoordinates(xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller);
        helicopterRearPropellerAnim.changeCoordinates(xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller);
    }
    /**
     * 적헬기가 총알을 쏠 타이밍인지 확인 
     *
     * @return
     */
    public boolean isShootBullet() {
       //숫자 50 -> 0으로 바꿈(총알 속도 조절)
        if(moveCount == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Draws helicopter to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    { 
        helicopterFrontPropellerAnim.Draw(g2d);
        g2d.drawImage(helicopterBodyImg, xCoordinate, yCoordinate, helicopterBodyWidth, helicopterBodylength, null);
        helicopterRearPropellerAnim.Draw(g2d);
    }
    
}