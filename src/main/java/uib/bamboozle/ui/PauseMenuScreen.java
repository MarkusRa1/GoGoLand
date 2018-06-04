package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class PauseMenuScreen extends Menu implements Screen {
	private final String PAUSE = "buttons/button_paused.png";
	private final String RESUME = "buttons/button_resume.png";
	private final String EXIT = "buttons/button_quit.png";
	private ImageButton pauseButton;
	private Array<Button> buttons;

	public PauseMenuScreen(Game game) {
		super(game);

		parallaxBackground();
		createButtons();
		getStage().addActor(table);

	}

	public void createButtons() {
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
			table.center();
		}
		table.setFillParent(true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			resume();
			return;
		}
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
