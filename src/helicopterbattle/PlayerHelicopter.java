package helicopterbattle;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Helicopter which is managed by player.
 * 
 * @author www.gametutorial.net
 */

public class PlayerHelicopter {
    
	
    
    
    // Position of the helicopter on the screen.
    public int xCoordinate;
    public int yCoordinate;
    
    // Moving speed and also direction.
    private double movingXspeed;
    public double movingYspeed;
    private double acceleratingXspeed;
    private double acceleratingYspeed;
    private double stoppingXspeed;
    private double stoppingYspeed;
    
    public double getacceleratingXspeed() {
    	return acceleratingXspeed;
    }
    
    public void setacceleratingXspeed(double a) {
    	acceleratingXspeed = a;
    }
    
    public double getacceleratingYspeed() {
    	return acceleratingYspeed;
    }
    
    public void setacceleratingYspeed(double a) {
    	acceleratingYspeed = a;
    }
    // Helicopter rockets.
    public int numberOfRockets = 80;
    // Helicopter machinegun ammo.
    public int numberOfAmmo= 1400;
    public int health=100;
    public static int coins = 0; // 수정
    public void InitializeAmmo() {
    	numberOfRockets = 80;
    	numberOfAmmo= 1400;
    }
   
    // Images of helicopter and its propellers.
    public BufferedImage helicopterBodyImg;
    
    public BufferedImage helicopterFrontPropellerAnimImg;
    public BufferedImage helicopterRearPropellerAnimImg;
    
    // Animation of the helicopter propeller.
    private Animation helicopterFrontPropellerAnim;
    private Animation helicopterRearPropellerAnim;
    // Offset for the propeler. We add offset to the position of the position of helicopter.
    private int offsetXFrontPropeller;
    private int offsetYFrontPropeller;
    private int offsetXRearPropeller;
    private int offsetYRearPropeller;
    
    // Offset of the helicopter rocket holder.
    private int offsetXRocketHolder;
    private int offsetYRocketHolder;
    // Position on the frame/window of the helicopter rocket holder.
    public int rocketHolderXcoordinate;
    public int rocketHolderYcoordinate;
    
    // Offset of the helicopter machine gun. We add offset to the position of the position of helicopter.
    private int offsetXMachineGun;
    private int offsetYMachineGun;
    // Position on the frame/window of the helicopter machine gun.
    public int machineGunXcoordinate;
    public int machineGunYcoordinate;
    
    public BufferedImage machinegunImg;
    
    // Draw Body ,Machinegun and propeller
    public int helicopterBodyWidth = 129;
    public int helicopterBodylength = 27;
    public int MachinegunWidth = 54;
    public int Machinegunlength = 38;
    public int FrontPropellerWidth = 102;
    public int FrontPropellerHeight = 16;
    public int FrontPropellerNumberOfFrames = 3;
    public int RearPropellerWidth = 26;
    public int RearPropellerHeight = 26;
    public int RearPropellerNumberOfFrames = 4;
    public int PropellerFrameTime = 20;
    
    
    /**
     * Creates object of player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     */
    public PlayerHelicopter(int xCoordinate, int yCoordinate)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        LoadContent();
        Initialize();
    }
    //이거 추가
    public PlayerHelicopter(int xCoordinate, int yCoordinate, int x , int y)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        LoadContent();
        Initialize();
    }
    
    
    
    /**
     * Set variables and objects for this class.
     */
    private void Initialize()
    {
        
        
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
        this.acceleratingXspeed = 0.2;
        this.acceleratingYspeed = 0.2;
        this.stoppingXspeed = 0.1;
        this.stoppingYspeed = 0.1;

        this.offsetXFrontPropeller = 35;
        this.offsetYFrontPropeller = -10;        
        this.offsetXRearPropeller = -12;
        this.offsetYRearPropeller = -14;
        
        this.offsetXRocketHolder = 138;
        this.offsetYRocketHolder = 30;
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        this.offsetXMachineGun = helicopterBodyImg.getWidth() - 80;
        this.offsetYMachineGun = helicopterBodyImg.getHeight() - 20;
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
        
 //       this.machinegunImg = 
        
    }
    
    
    /**
     * Load files for this class.
     */
    private void LoadContent()
    {
        machinegunImg = this.drawBufferImage("/helicopterbattle/resources/images/weapon/machine_gun.png");

        String helicopterPackage = "/helicopterbattle/resources/images/helicopter";
        helicopterBodyImg = this.drawBufferImage(helicopterPackage+"/1_helicopter_body.png");
        helicopterFrontPropellerAnimImg = this.drawBufferImage(helicopterPackage+"/1_front_propeller_anim.png");
        helicopterRearPropellerAnimImg = this.drawBufferImage(helicopterPackage+"/1_rear_propeller_anim_blur.png");

        // Now that we have images of propeller animation we initialize animation object.
        helicopterFrontPropellerAnim = new Animation(helicopterFrontPropellerAnimImg, FrontPropellerWidth, 
        FrontPropellerHeight, FrontPropellerNumberOfFrames, PropellerFrameTime, true, xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller, 0);
        helicopterRearPropellerAnim = new Animation(helicopterRearPropellerAnimImg, RearPropellerWidth, 
        RearPropellerHeight, RearPropellerNumberOfFrames, PropellerFrameTime, true, xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller, 0);
        
    }

    private BufferedImage drawBufferImage(String targetImgLocation){
        URL targetImgUrl = this.getClass().getResource(targetImgLocation);
        BufferedImage resultBufferImage = null;
        try{
            resultBufferImage =  ImageIO.read(targetImgUrl);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultBufferImage;
    }
    
    /**
     * Resets the player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     */
    public void Reset(int xCoordinate, int yCoordinate)
    {
                       
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
    }
    
    
    /**
     * Checks if player is shooting. It also checks if player can 
     * shoot (time between bullets, does a player have any bullet left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is shooting.
     */
    public boolean isShooting(long gameTime)
    {
        // Checks if left mouse button is down && if it is the time for a new bullet.
        if( Canvas.mouseButtonState(MouseEvent.BUTTON1) && 
            ((gameTime - Bullet.timeOfLastCreatedBullet) >= Bullet.timeBetweenNewBullets) &&
            this.numberOfAmmo > 0) 
        {
            return true;
        } else
            return false;
    }
    
    
    /**
     * Checks if player is fired a rocket. It also checks if player can 
     * fire a rocket (time between rockets, does a player have any rocket left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is fired a rocket.
     */
    public boolean isFiredRocket(long gameTime)
    {
        // Checks if right mouse button is down && if it is the time for new rocket && if he has any rocket left.
        if( Canvas.mouseButtonState(MouseEvent.BUTTON3) && 
            ((gameTime - Rocket.timeOfLastCreatedRocket) >= Rocket.timeBetweenNewRockets) && 
            this.numberOfRockets > 0 ) 
        {
            return true;
        } else
            return false;
    }
    
    
    /**
     * Checks if player moving helicopter and sets its moving speed if player is moving.
     */
    public void isMoving()
    {
        // Moving on the x coordinate.
        if(Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
            movingXspeed += acceleratingXspeed;
        else if(Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
            movingXspeed -= acceleratingXspeed;
        else    // Stoping
            if(movingXspeed < 0)
                movingXspeed += stoppingXspeed;
            else if(movingXspeed > 0)
                movingXspeed -= stoppingXspeed;
        
        // Moving on the y coordinate.
        if(Canvas.keyboardKeyState(KeyEvent.VK_W) || Canvas.keyboardKeyState(KeyEvent.VK_UP))
            movingYspeed -= acceleratingYspeed;
        else if(Canvas.keyboardKeyState(KeyEvent.VK_S) || Canvas.keyboardKeyState(KeyEvent.VK_DOWN))
            movingYspeed += acceleratingYspeed;
        else    // Stoping
            if(movingYspeed < 0)
                movingYspeed += stoppingYspeed;
            else if(movingYspeed > 0)
                movingYspeed -= stoppingYspeed;
    }
    
    
    /**
     * Updates position of helicopter, animations.
     */
    public void Update()
    {
        // Move helicopter and its propellers.
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
        helicopterFrontPropellerAnim.changeCoordinates(xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller);
        helicopterRearPropellerAnim.changeCoordinates(xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller);
        
        // Change position of the rocket holder.
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        // Move the machine gun with helicopter.
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
    }
    
    
    /**
     * Draws helicopter to the screen.
     * 
     * @param g2d Graphics2D
     */
   
    public void Draw(Graphics2D g2d)
    {
        helicopterFrontPropellerAnim.Draw(g2d);
        helicopterRearPropellerAnim.Draw(g2d);
        g2d.drawImage(helicopterBodyImg, xCoordinate, yCoordinate, helicopterBodyWidth, helicopterBodylength,  null);
        g2d.drawImage(machinegunImg, xCoordinate + 80, yCoordinate, MachinegunWidth, Machinegunlength, null);
    }
    
}