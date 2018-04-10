package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.bamboozle.communication.ReadFromGo;
import uib.bamboozle.ui.Graphics;

public class Main extends com.badlogic.gdx.Game {
    private static final String TITLE = "GOGOLAND";
    
    //scenes
    private MainMenu mainMenu;
    private Graphics graphics;
    
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
        new LwjglApplication(new Main(), config);
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
    public Graphics newGame() {
        graphics = new Graphics(this);
        return graphics;
    }
}