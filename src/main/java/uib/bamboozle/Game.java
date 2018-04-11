package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uib.bamboozle.communication.ReadFromGo;
import uib.bamboozle.ui.GameScreen;
import uib.bamboozle.ui.MainMenu;

public class Game extends com.badlogic.gdx.Game {
    private static final String TITLE = "GOGOLAND";
    
    //scenes
    private MainMenu mainMenu;
    private GameScreen gameScreen;
    
    public int roll;
    public int pitch;
    public int yaw;
    public ReadFromGo reader;

    public static void main(String[] arg) {
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "GoGoLand";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Game(), config);
    }
    public void create() {
        //connect();
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

    public void connect() {
        reader = new ReadFromGo(this);
        new Thread(reader).start();
    }

    public void disconnect() {
        if(reader != null) reader.stop();
    }

    @Override
    public void dispose() {
        disconnect();
        if(gameScreen != null) gameScreen.dispose();
        if(mainMenu != null)  mainMenu.dispose();
    }
}