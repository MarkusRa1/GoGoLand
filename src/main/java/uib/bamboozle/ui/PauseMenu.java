package uib.bamboozle.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.bamboozle.Game;

public class PauseMenu extends Menu implements Screen {
    private Game game;
    private Table table;
    private ImageButton pauseButton;
    private Array<Button> buttons;
    

    
    public PauseMenu(Game game) {
        this.game = game;
        
        table = new Table();

        //background image
        setupBackground();

        createButtons();
        table.setFillParent(true);
        getStage().addActor(table);;
        
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
