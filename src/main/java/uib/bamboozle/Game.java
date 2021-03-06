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
    private SettingsScreen settingsScreen;
    private CompleteLevelScreen completeLevelScreen;
    private CompleteGameScreen completeGameScreen;
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

        audioManager.preloadTracks("midlevelmusic.wav", "firstlevelmusic.wav", "finallevelmusic.wav", "mainmenumusic.wav");

        mainMenuScreen = new MainMenuScreen(this);
        pauseMenuScreen = new PauseMenuScreen(this);
        settingsScreen = new SettingsScreen(this);
        completeLevelScreen = new CompleteLevelScreen(this);
        completeGameScreen = new CompleteGameScreen(this);

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
     * Returns the settings screen
     *
     * @return The settings screen
     */
    public SettingsScreen getSettingsScreen() {
        return settingsScreen;
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

         if (reader == null) {
             reader = new ReadFromGo(this);
             readerThread = new Thread(reader);
             readerThread.start();
         } else {
             System.out.println("stopGoProcess()");
             if (readerThread != null) {
                 reader.stop();
                 readerThread.interrupt();
                 reader = new ReadFromGo(this);
                 readerThread = new Thread(reader);
                 readerThread.start();
                 System.out.println("Thread interrupted and started");
             }
         }
    }

    public void startReader(ReadFromGo g) {
        if (readerThread != null && readerThread.isAlive()) {
            readerThread.interrupt();
        }
        reader = g;
        readerThread = new Thread(reader);
        readerThread.start();
        System.out.println("Reader started");
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
        reader.writeToServer("Stop\n");
        disconnect();
        if (gameScreen != null)
            gameScreen.dispose();
        if (mainMenuScreen != null)
            mainMenuScreen.dispose();

    }

    public void levelComplete() {
        if(gameScreen.getLevelNumber() != GameScreen.NUM_LEVELS) {
            setScreen(completeLevelScreen);
        } else {
            newGame();
            setScreen(completeGameScreen);
        }
    }

    public void nextLevel() {
        setScreen(gameScreen);
        gameScreen.nextLevel();
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public ReadFromGo getReader() {
        return reader;
    }
}