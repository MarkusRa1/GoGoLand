package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.bamboozle.Game;

/**
 * An abstract class implementing a lot of shared menu screen functionality
 */
public abstract class Menu implements Screen {

    protected Stage stage;
    protected Game game;
    protected Table table;
    
    ParallaxBackground parallaxBackground;
    private static final String BACKGROUND = "buttons/bg4.jpg";

    public Menu(Game game) {
        this.game = game;
        stage = new Stage(new FitViewport(1920, 1080));
        table = new Table();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    /**
     * Creates a new button and adds a listener
     * 
     * @param imageString
     *            The name of the image file
     * @param target
     *            The method to be called on click
     * @return The button
     */
    protected ImageButton createButton(String imageString, Runnable target) {
        ImageButton button = new ImageButton(createImage(imageString));
        if (target != null)
            addButtonListener(button, target);
        return button;
    }

    /**
     * Creates a new button with several different possible images
     * 
     * @param upImage
     *            The name of the normal image
     * @param downImage
     *            The name of the image once the button is clicked.
     * @param target
     *            The method to be called on click
     * @return The button
     */
    protected ImageButton createButton(String upImage, String downImage, String checkedImage, Runnable target) {
        ImageButton button = new ImageButton(createImage(upImage), createImage(downImage), createImage(checkedImage));
        if (target != null)
            addButtonListener(button, target);
        return button;
    }

    protected ImageButton createStatusButton(String im1, String im2, Menu menu) {
        ImageButton button = new ImageButton(createImage(im1), createImage(im2));
        return button;
    }

    /**
     * Loads an image from the resources folder and creates a libgdc image
     */
    protected Drawable createImage(String imageString) {
        Texture myTexture = new Texture(Gdx.files.internal(imageString));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        return new TextureRegionDrawable(myTextureRegion);
    }

    /**
     * Adds a listener to a button
     * 
     * @param button
     *            The button
     * @param target
     *            The function to be called on click
     */
    public void addButtonListener(Button button, Runnable target) {

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
                    target.run();
                }
            }
        });
    }

    public Game getGame() {
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void parallaxBackground() {
        Array<Texture> textures = new Array<Texture>();
        textures.add(new Texture(Gdx.files.internal("Background/bg4.png")));
        textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        parallaxBackground = new ParallaxBackground(textures, stage.getWidth(), stage.getHeight(), 1);
        getStage().addActor(parallaxBackground);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
    }
}
