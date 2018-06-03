package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class SettingsScreen extends Menu implements Screen {

    private final String BLUE = "buttons/blue.png";
    private final String RED = "buttons/red.png";
    private final String GREEN = "buttons/green.png";
    private final String PURPLE = "buttons/purple.png";
    private final String RETURN = "buttons/button_return.png";
    

    private Array<Button> buttons;

    public SettingsScreen(Game game) {
        super(game);
        parallaxBackground();
        createButtons();
        stage.addActor(table);

    }
    public void createButtons() {


        buttons = new Array<Button>();
        buttons.add(createButton(RETURN, this::returnToMainMenu));

        table.pad(50);
        for (Button button : buttons) {
            table.add(button).width((float) (button.getWidth() / 2)).height((float) (button.getHeight() / 2)).pad(5);
            table.row();
            table.center();
            table.setFillParent(true);
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    public void returnToMainMenu() {
        game.setScreen(game.getMainMenuScreen());
    }

    public void setRed() {

    }

    public void setBlue() {

    }

    public void setGreen() {

    }

    public void setPurple() {

    }

}
