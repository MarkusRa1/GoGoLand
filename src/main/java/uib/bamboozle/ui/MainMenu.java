package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.bamboozle.Game;

public class MainMenu extends Menu implements Screen {
    private static final String PLAY = "start.png";
    private static final String BACKGROUND = "bg2.jpg";
    private static final String EXIT = "bs_quit.png";
    private static final String CONNECT = "connect.png";
    private static final String DISCONNECT = "disconnect.png";

    private Table table;
    private Button connectButton;
    private ImageButton.ImageButtonStyle connectedStyle = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle disconnectedStyle = new ImageButton.ImageButtonStyle();

    private Stage stage;
    private Game game;
   
    
    public MainMenu(Game game) {
        this.game = game;
        stage = new Stage(new FitViewport(1920, 1080));
        table = new Table();

        //background image
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
    @Override
    public void exitGame() {
		Gdx.app.exit();
	}
    @Override
    public void newGame() {
        game.setScreen(game.newGame());
    }

    @Override
    public void connect() {
        if(!game.isConnected()) {
            game.connect();
        } else {
            game.disconnect();
        }
    }

    private Stage getStage() {
        return stage;
    }

    private void createButtons() {
        connectButton = createButton(CONNECT, "connect", this);
        connectButton.setStyle(connectedStyle);
        connectedStyle.imageUp = createImage(CONNECT);
        disconnectedStyle.imageUp = createImage(DISCONNECT);

        Array<Button> buttons = new Array<>();

        buttons.add(createButton(PLAY, "newGame", this));
        buttons.add(connectButton);
        buttons.add(createButton(EXIT, "exitGame", this));
        table.center();
        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            table.row();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        
    }

    @Override
    public void render(float delta) {
        if(game.isConnected()) {
            connectButton.setStyle(disconnectedStyle);
        } else {
            connectButton.setStyle(connectedStyle);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        
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
