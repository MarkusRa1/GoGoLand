package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.bamboozle.communication.ReadFromGo;
import uib.bamboozle.ui.GameScreen;
import uib.bamboozle.ui.MainMenuScreen;
import uib.bamboozle.ui.PauseMenuScreen;

public class Game extends com.badlogic.gdx.Game {
    private static final String TITLE = "GOGOLAND";
    
    //scenes
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private Thread readerThread;
    
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
    public void create() {
        connect();
        batch = new SpriteBatch();
        font = new BitmapFont();
        mainMenuScreen = new MainMenuScreen(this);
        pauseMenuScreen = new PauseMenuScreen(this);

        setScreen(mainMenuScreen);
    }
    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }
    public PauseMenuScreen getPauseMenuScreen() {
    	return pauseMenuScreen;
    }
    public GameScreen getGameScreen() {
    	return gameScreen;
    }
    public String getTitle() {
        return TITLE;
    }
    public GameScreen newGame() {
        gameScreen = new GameScreen(this);
        return gameScreen;
    }

    public void connect() {
        System.out.println("connect()");
        if (reader == null) {
            reader = new ReadFromGo(this);
            readerThread = new Thread(reader);
            readerThread.start();
        } else {
            reader.connect();
            if (readerThread != null) {
                readerThread.interrupt();
                readerThread = new Thread(reader);
                readerThread.start();
                System.out.println("Thread interrupted and started");
            }
        }
    }

    public void disconnect() {
        if(reader != null){
            reader.stop();
        }

    }

    public void setConnected(boolean c) {
        this.connected = c;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void dispose() {
        disconnect();
        if(gameScreen != null) gameScreen.dispose();
        if(mainMenuScreen != null)  mainMenuScreen.dispose();

    }
}