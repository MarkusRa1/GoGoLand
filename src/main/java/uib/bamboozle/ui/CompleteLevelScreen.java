package uib.bamboozle.ui;

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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;


import uib.bamboozle.Game;

public class CompleteLevelScreen extends Menu {
    private static final String BACKGROUND = "Background/bg4.png";
    private static final String COMPLETE = "buttons/button_level-complete.png";
    private static final String NEXT = "buttons/button_next-level.png";

    private Table table;

    public CompleteLevelScreen(Game game) {
        super(game);
        table = new Table();

        setupBackground();

        createButtons();

        table.setFillParent(true);
        getStage().addActor(table);;
    }

    private void setupBackground() {
        Texture backgroundTexture = new Texture(BACKGROUND);
        Image backgroundImage = new Image(backgroundTexture);
        getStage().addActor(backgroundImage);
    }

    private void createButtons() {
        Button completeSign = createButton(COMPLETE, null);
        Array<Button> buttons = new Array<>();

        buttons.add(createButton(NEXT, this::nextLevel));

        table.add(completeSign).pad(0, 0, 200, 0);
        table.center();
        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 2)).height((float) (but.getHeight() / 2)).pad(5);
            table.row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void nextLevel() {
        game.nextLevel();
    }

    public String getTrackName() {
        return "mainmenumusic.wav";
    }
}
