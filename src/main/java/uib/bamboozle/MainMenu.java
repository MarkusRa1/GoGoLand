package uib.bamboozle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenu implements Screen {
    private static final String PLAY = "play";
    private static final String EXIT = "Exit";
    private static final String CONNECT = "Connect your balls";
    
    
    private Stage stage;
    private Main game;
    private Table menu;
    
    public MainMenu(Main game) {
        stage = new Stage(new FitViewport(1920, 1080));
        this.game = game;
        menu = new Table();
        //make some background?(ref StartMenuScreen cargame)
        
        createButtons();
        
    }
    
    private void createButtons() {
        Array<Button> buttons = new Array<>();
        
        //button which changes scene to the game itself
        // buttons.add(PLAY, this::newGame);
    }
    //TODO Må Endre Graphics slik at den extender Screen
    public void newGame() {
        //game.setScreen(game.newGame());
    }
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
