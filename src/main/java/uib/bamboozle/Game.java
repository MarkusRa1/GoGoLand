package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uib.bamboozle.communication.ReadFromGo;

public class Game extends com.badlogic.gdx.Game {
    private static final String TITLE = "GOGOLAND";
    
    //scenes
    private MainMenu mainMenu;
    private GameScreen gameScreen;
    
    public static int roll;
    public static int pitch;
    public static int yaw;
    public static ReadFromGo reader;

    public static void Game(String[] arg) {
        reader = new ReadFromGo();
        new Thread(reader).start();
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "GoGoLand";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Game(), config);
    }
    public void create() {
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }
    public MainMenu getMainMenu() {
        return mainMenu;
    }
    public String getTitle() {
        return TITLE;
    }
    public GameScreen newGame() {
        gameScreen = new GameScreen(this);
        return gameScreen;
    }
}