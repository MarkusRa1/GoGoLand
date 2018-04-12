package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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
    
    public static int roll;
    public static int pitch;
    public static int yaw;
    public static ReadFromGo reader;

    public static void main(String[] arg) {
        reader = new ReadFromGo();
        new Thread(reader).start();
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "GoGoLand";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Game(), config);
    }
    public void create() {
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
}