package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class MainMenuScreen extends Menu implements Screen {
    private static final String PLAY = "start.png";
    private static final String BACKGROUND = "bg2.jpg";
    private static final String EXIT = "bs_quit.png";
    private static final String CONNECT = "connect.png";
    
    //Name for switch case methods
    private final String newGame = "newGame";
    private final String exit = "exitToMainMenu";
    private final String connect = "Insert here Eirik";
    
    private Table table;
    
    public MainMenuScreen(Game game) {
    	super(game);
        table = new Table();

        setupBackground();

        createButtons();
        //For � sjekke rammene til tablet
        //table.setDebug(true);
        table.setFillParent(true);
        getStage().addActor(table);;
        
    }
    
    private void setupBackground() {
        Texture backgroundTexture = new Texture(BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        getStage().addActor(backgroundImage);
    }


    private void createButtons() {
        Array<Button> buttons = new Array<>();

        buttons.add(createButton(PLAY, newGame, this));
        buttons.add(createButton(CONNECT, connect, this));
        buttons.add(createButton(EXIT, exit, this));
        table.center();
        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            table.row();
        }

    }
       
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }



}
