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
    private static final String PLAY = "resources/play1.png";
    private static final String BACKGROUND = "resources/background.png";
    private static final String EXIT = "Exit";
    private static final String CONNECT = "Connect your balls";
    
    
    private Stage stage;
    private Main game;
    private Table menu;
    
    public MainMenu(Main game) {
        stage = new Stage(new FitViewport(1920, 1080));
        this.game = game;
        menu = new Table();
        
        
        setupBackground();
        createButtons();
        menu.setFillParent(true);
        getStage().addActor(menu);;
        
    }
    
    private void setupBackground() {
        Texture backgroundTexture = new Texture(BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        getStage().addActor(backgroundImage);
    }


    private Stage getStage() {
        return stage;
    }

    private void createButtons() {
        Array<Button> buttons = new Array<>();
        
        
        buttons.add(createButton(PLAY, this::newGame));
        menu.pad(450, 1100, 0, 0);
        menu.row();
        for (Button but : buttons) {
            menu.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            menu.row();
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

    //TODO Må Endre Graphics slik at den extender Screen
    public void newGame() {
        //game.setScreen(game.newGame());
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
