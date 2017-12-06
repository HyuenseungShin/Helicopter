package helicopterbattle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * Framework that controls the game (Game.java) that created it, update it and
 * draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {

	/**
	 * Width of the frame.
	 */
	public static int frameWidth;
	/**
	 * Height of the frame.
	 */
	public static int frameHeight;

	/**
	 * Time of one second in nanoseconds. 1 second = 1 000 000 000 nanoseconds
	 */
	public static final long secInNanosec = 1000000000L;

	/**
	 * Time of one millisecond in nanoseconds. 1 millisecond = 1 000 000 nanoseconds
	 */
	public static final long milisecInNanosec = 1000000L;

	/**
	 * FPS - Frames per second How many times per second the game should update?
	 */
	private final int GAME_FPS = 60;
	/**
	 * Pause between updates. It is in nanoseconds.
	 */
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

	/**
	 * Possible states of the game
	 */
	public static enum GameState {
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER,
		DESTROYED, STAGE_CLEAR, NEXT_STAGE, STORE, SHOW_STAGE
	}

	/**
	 * Current state of the game
	 */
	public static GameState gameState;

	/**
	 * Elapsed game time in nanoseconds.
	 */

	public static long gameTime;
	// It is used for calculating elapsed time.
	private long lastTime;

	// The actual game
	static Game game;
	
	private Font font;

	// Images for menu.
	private BufferedImage gameTitleImg;
	private BufferedImage menuBorderImg;
	private BufferedImage skyColorImg;
	private BufferedImage cloudLayer1Img;
	private BufferedImage cloudLayer2Img;
	private BufferedImage storeImg;
	
    Store store;
	Music storeMusic = new Music("storemusic.mp3",true);
	
	public Framework() {
		super();
		CustomCursor();
		gameState = GameState.VISUALIZING;
		// We start game in new thread.
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				GameLoop();
			}
		};
		gameThread.start();
	}

	/**
	 * Set variables and objects. This method is intended to set the variables and
	 * objects for this class, variables and objects for the actual game can be set
	 * in Game.java.
	 */
	private void Initialize() {
		font = new Font("monospaced", Font.BOLD, 28);
	}
	/**
	 * Load files (images). This method is intended to load files for this class,
	 * files for the actual game can be loaded in Game.java.
	 */
	 private void LoadContent() {
         String backgroundPackage = "/helicopterbattle/resources/images/background";
         menuBorderImg = this.drawBufferImage(backgroundPackage+"/menu_border.png");
         skyColorImg = this.drawBufferImage(backgroundPackage+"/sky_color.jpg");
         gameTitleImg = this.drawBufferImage(backgroundPackage+"/helicopter_battle_title.png");
         cloudLayer1Img = this.drawBufferImage(backgroundPackage+"/cloud_layer_1.png");
         cloudLayer2Img = this.drawBufferImage(backgroundPackage+"/cloud_layer_2.png");

         storeImg = this.drawBufferImage("/helicopterbattle/resources/images/store/store.PNG");
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
	 * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated
	 * and then the game is drawn on the screen.
	 */
	private void GameLoop() {
		// This two variables are used in VISUALIZING state of the game. We used them to
		// wait some time so that we get correct frame/window resolution.
		long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

		// This variables are used for calculating the time that defines for how long we
		// should put threat to sleep to meet the GAME_FPS.
		long beginTime, timeTaken, timeLeft;

		while (true) {
			beginTime = System.nanoTime();

			switch (gameState) {
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;
				
				game.UpdateGame(gameTime, mousePosition());

				lastTime = System.nanoTime();
				break;
			case SHOW_STAGE:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case STAGE_CLEAR:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.plusStageNum();
				store = new Store();
				gameState = GameState.STORE;
				break;
			case STORE:
				this.setCursor(null);
				setLayout(null);
				addButton();
				store.UpdateStore();
				break;
			case NEXT_STAGE:
				CustomCursor();
				hideButton();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				restartGame();			
				break;
			case GAMEOVER:
				this.setCursor(null);
				try {
	            	new Rank();
	            	Thread.sleep(100000);
	            }catch(Exception e) {
	            	
	            }
				break;
			case MAIN_MENU:
				if(storeMusic.getState() == Thread.State.NEW) {
				storeMusic.start();
				}
				break;
			case OPTIONS:
				// ...
				break;
			case GAME_CONTENT_LOADING:
				gameState = GameState.SHOW_STAGE;
				break;
			case STARTING:
				// Sets variables and objects.
				Initialize();
				// Load files - images, sounds, ...
				LoadContent();
				// When all things that are called above finished, we change game status to main
				// menu.
				gameState = GameState.MAIN_MENU;
				break;
			case VISUALIZING:
				// On Ubuntu OS (when I tested on my old computer) this.getWidth() method
				// doesn't return the correct value immediately (eg. for frame that should be
				// 800px width, returns 0 than 790 and at last 798px).
				// So we wait one second for the window/frame to be set to its correct size.
				// Just in case we
				// also insert 'this.getWidth() > 1' condition in case when the window/frame
				// size wasn't set in time,
				// so that we although get approximately size.
				if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
					frameWidth = this.getWidth();
					frameHeight = this.getHeight();
					// When we get size of frame we change status.
					gameState = GameState.STARTING;
				} else {
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			}
			// Repaint the screen.
			repaint();
			// Here we calculate the time that defines for how long we should put threat to
			// sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep
			// for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10)
				timeLeft = 10; // set a minimum
			try {
				// Provides the necessary delay and also yields control so that other thread can
				// do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * Draw the game to the screen. It is called through repaint() method in
	 * GameLoop() method.
	 */
	@Override
	public void Draw(Graphics2D g2d) {
		switch (gameState) {

		case PLAYING:
			game.Draw(g2d, mousePosition(), gameTime);
			break;
		case SHOW_STAGE:
			showScreen(g2d, "stage 1");
			break;
		case NEXT_STAGE:
			showScreen(g2d, game.StageNum());
			break;
		case STAGE_CLEAR:
			showScreen(g2d, "STAGE CLEAR");
			break;
		case STORE:
			g2d.drawImage(storeImg, 0, 0,frameWidth, frameHeight, null);
			break;
		case GAMEOVER:
			drawMenuBackground(g2d);
			g2d.setColor(Color.black);
			g2d.drawString("Press ENTER to restart or ESC to exit.", frameWidth / 2 - 113, frameHeight / 4 + 30);
			game.DrawStatistic(g2d, gameTime);
			g2d.setFont(font);
			g2d.drawString("GAME OVER", frameWidth / 2 - 90, frameHeight / 4);
			break;
		case MAIN_MENU:
			drawMenuBackground(g2d);
			g2d.drawImage(gameTitleImg, frameWidth / 2 - gameTitleImg.getWidth() / 2, frameHeight / 4, null);
			g2d.setColor(Color.black);
			g2d.drawString("Use w, a, d or arrow keys to move the helicopter.", frameWidth / 2 - 117,
					frameHeight / 2 - 30);
			g2d.drawString("Use left mouse button to fire bullets and right mouse button to fire rockets.",
					frameWidth / 2 - 180, frameHeight / 2);
			g2d.drawString("Press any key to start the game or ESC to exit.", frameWidth / 2 - 114,
					frameHeight / 2 + 30);
			break;
		case OPTIONS:
			// ...
			break;
		case GAME_CONTENT_LOADING:
			g2d.setColor(Color.white);
			g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
			break;
		}
	}

	public void showScreen(Graphics2D g2d, String sentence)
	{
		drawMenuBackground(g2d);
		g2d.setColor(Color.black);
		g2d.drawString(sentence, frameWidth / 2 - 117, frameHeight / 2 - 30);
		g2d.setFont(font);
	}//추가
	/**
	 * Starts new game.
	 */
	private void newGame() {
		// We set gameTime to zero and lastTime to current time for later calculations.
		gameTime = 0;
		lastTime = System.nanoTime();

		game = new Game();
	}
	/**
	 * Restart game - reset game time and call RestartGame() method of game object
	 * so that reset some variables.
	 */
	private void restartGame() {
		// We set gameTime to zero and lastTime to current time for later calculations.
		gameTime = 0;
		lastTime = System.nanoTime();
		game.RestartGame();
		// We change game status so that the game can start.
		gameState = GameState.PLAYING;
	}
	/**
	 * Returns the position of the mouse pointer in game frame/window. If mouse
	 * position is null than this method return 0,0 coordinate.
	 * 
	 * @return Point of mouse coordinates.
	 */
	private Point mousePosition() {
		try {
			Point mp = this.getMousePosition();

			if (mp != null)
				return this.getMousePosition();
			else
				return new Point(0, 0);
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}
	/**
	 * This method is called when keyboard key is released.
	 * 
	 * @param e
	 *            KeyEvent
	 */
	@Override
	public void keyReleasedFramework(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);

		switch (gameState) {
		case GAMEOVER:
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				gameState = GameState.STARTING;
			break;
		case MAIN_MENU:
			newGame();
			break;
		}
	}
	/**
	 * This method is called when mouse button is clicked.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void CustomCursor()
	{
		BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
        this.setCursor(blankCursor);
	}//추가
	

	public void addButton() {
		add(store.enginePurchaseButton);
		add(store.AMMOPurchaseButton);
		add(store.titaniumArmorPurchaseButton);
		add(store.closeStoreButton);
	}
	public void hideButton() {
		store.enginePurchaseButton.setVisible(false);
		store.titaniumArmorPurchaseButton.setVisible(false);
		store.AMMOPurchaseButton.setVisible(false);
		store.closeStoreButton.setVisible(false);
	}

	private void drawMenuBackground(Graphics2D g2d) {
		g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		g2d.drawImage(cloudLayer1Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		g2d.drawImage(cloudLayer2Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		g2d.drawImage(menuBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		g2d.setColor(Color.white);
		g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
	}
}
