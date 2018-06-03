package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.bamboozle.communication.ReadFromGo;
import uib.bamboozle.ui.*;

public class Game extends com.badlogic.gdx.Game {
    // scenes
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private Thread readerThread;

    private AudioManager audioManager = new AudioManager();
    
    public int roll;
    public int pitch;
    public int yaw;
    public boolean readerIsRunning = false;
    public SpriteBatch batch;
    public BitmapFont font;



    private ReadFromGo reader;
    private boolean connected = false;

    public static void main(String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "GoGoLand";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Game(), config);
    }

    /**
     * Creates a new game
     */
    public void create() {
        connect();
        batch = new SpriteBatch();
        font = new BitmapFont();

        audioManager.preloadTracks("level1music.wav", "level2music.wav");

        mainMenuScreen = new MainMenuScreen(this);
        pauseMenuScreen = new PauseMenuScreen(this);

        setScreen(mainMenuScreen);
    }

    /**
     * Returns the main menu screen
     * 
     * @return The main menu screen
     */
    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    /**
     * Returns the main menu screen
     *
     * @return The main menu screen
     */
    public PauseMenuScreen getPauseMenuScreen() {
        return pauseMenuScreen;
    }

    /**
     * Returns the pause menu screen
     *
     * @return The pause menu screen
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Starts a new game
     *
     * @return The main menu screen
     */
    public GameScreen newGame() {
        gameScreen = new GameScreen(this);
        return gameScreen;
    }

    /**
     * Connects to the go server
     */
    public void connect() {
        // System.out.println("connect()");
        // if (reader == null) {
        // reader = new ReadFromGo(this);
        // readerThread = new Thread(reader);
        // readerThread.start();
        // } else {
        // reader.connect();
        // if (readerThread != null) {
        // readerThread.interrupt();
        // readerThread = new Thread(reader);
        // readerThread.start();
        // System.out.println("Thread interrupted and started");
        // }
        // }
    }

    /**
     * Disconnects from the go server
     */
    public void disconnect() {
        if (reader != null) {
            reader.stop();
        }

    }

    /**
     * Sets the connected flag
     *
     * @param c
     *            The flag
     */
    public void setConnected(boolean c) {
        this.connected = c;
    }

    /**
     * Returns the connected flag
     *
     * @return The flag
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disposes the whole game
     */
    @Override
    public void dispose() {
        disconnect();
        if (gameScreen != null)
            gameScreen.dispose();
        if (mainMenuScreen != null)
            mainMenuScreen.dispose();

    }

    public void levelComplete() {
        // if(gameScreen.getLevelNumber() != GameScreen.NUM_LEVELS) {
        setScreen(new CompleteLevelScreen(this));
        // } else {

        // }
    }

    public void nextLevel() {
        setScreen(gameScreen);
        gameScreen.nextLevel();
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
}