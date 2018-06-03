package uib.bamboozle.ui;

import javax.swing.text.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class PauseMenuScreen extends Menu implements Screen {
    private final String PAUSE = "buttons/button_paused.png";
    private final String RESUME = "buttons/button_resume.png";
    private final String EXIT = "buttons/button_quit.png";

    private Table table;
    private ImageButton pauseButton;
    private Array<Button> buttons;
    

    
    public PauseMenuScreen(Game game) {
        super(game);

        table = new Table();

        pauseButton = createButton(PAUSE, null);

        buttons = new Array<Button>();
        buttons.add(createButton(RESUME, this::resumeGame));
        buttons.add(createButton(EXIT, this::exitToMainMenu));

        table.add(pauseButton).pad(0, 0, 400, 0);
		table.row();
		table.pad(50);
		for (Button button : buttons) {
			table.add(button).width((float) (button.getWidth() / 2)).height((float) (button.getHeight() / 2)).pad(5);
			table.row();
		}
		table.center();
		table.setFillParent(true);
		getStage().addActor(table);


    }

    @Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			resume();
			return;
		}
		game.batch.begin();
		game.font.setColor(Color.WHITE);
		game.font.draw(game.batch, "Current Level: " + game.getGameScreen().getLevelNumber(), 20, 700);
		game.getGameScreen().getLevelNumber();
		game.batch.end();
		
        stage.act(delta);
        stage.draw();

	}

    public void resumeGame() {
        game.setScreen(game.getGameScreen());

    }
    public void exitToMainMenu() {
        game.setScreen(game.getMainMenuScreen());
    }
}
