package uib.bamboozle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenu implements Screen {
    private static final String PLAY = "resources/start.png";
    private static final String BACKGROUND = "resources/bg2.jpg";
    private static final String EXIT = "resources/bs_quit.png";
    private static final String CONNECT = "resources/connect.png";
    
    private Table table;
    
    private Stage stage;
    private Main game;
   
    
    public MainMenu(Main game) {
        this.game = game;
        stage = new Stage(new FitViewport(1920, 1080));
        table = new Table();
        
        //background image
        setupBackground();
        
        createButtons();
        //For å sjekke rammene til tablet
        //table.setDebug(true);
        table.setFillParent(true);
        getStage().addActor(table);;
        
    }
    
    private void setupBackground() {
        Texture backgroundTexture = new Texture(BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        getStage().addActor(backgroundImage);
    }
    
    public void exitGame() {
		Gdx.app.exit();
	}
    //TODO Må Endre Graphics slik at den extender Screen
    public void newGame() {
        game.setScreen(game.newGame());
    }



    private Stage getStage() {
        return stage;
    }

    private void createButtons() {
        Array<Button> buttons = new Array<>();
        
        
        buttons.add(createButton(PLAY, this::newGame));
        buttons.add(createButton(CONNECT, this::exitGame));
        buttons.add(createButton(EXIT, this::exitGame));
        table.center();
        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            table.row();
        }

    }
    public ImageButton createButton(String imageString, Runnable lambda) {
        ImageButton button = setupButton(imageString);
        if(lambda != null)
            addButtonListener(button, lambda);
        return button;
    }
    private ImageButton setupButton(String imageString) {
        Texture myTexture = new Texture(Gdx.files.internal(imageString));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        return new ImageButton(myTexRegionDrawable);
    }

    public void addButtonListener(Button button, Runnable lambda) {
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Stage eventStage = event.getTarget().getStage();
                Vector2 mouse = eventStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                if (eventStage.hit(mouse.x, mouse.y, true) == event.getTarget()) {
                    lambda.run();
                }
            }
        });
    }

   
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        
    }

    @Override
    public void render(float delta) {
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
