package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class PauseMenuScreen extends Menu implements Screen {
    private final String PAUSE = "pause.png";
    private final String RESUME = "connect.png";
    private final String EXIT = "bs_quit.png";
    
    //Name for switch case methods
    private final String resume = "resumeGame";
    private final String exit = "exitToMainMenu";
    
    

    private Table table;
    private ImageButton pauseButton;
    private Array<Button> buttons;
    

    
    public PauseMenuScreen(Game game) {
        super(game);

        table = new Table();

        pauseButton = createButton(PAUSE, null, this);
        //background image
        buttons = new Array<Button>();
        buttons.add(createButton(EXIT, exit, this));
        buttons.add(createButton(RESUME, resume, this));

        table.add(pauseButton).pad(0, 0, 20, 0);
		table.row();
		table.pad(50);
		for (Button button : buttons) {
			table.add(button).width((float) (button.getWidth() / 4)).height((float) (button.getHeight() / 4)).pad(5);
			table.row();
		}

		table.setFillParent(true);
		getStage().addActor(table);


    }

    @Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			resume();
			return;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

	}

}
