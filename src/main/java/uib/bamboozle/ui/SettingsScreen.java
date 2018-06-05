package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

public class SettingsScreen extends Menu implements Screen {

	private final String COLOR = "buttons/button_choose-ball-color.png";
    private final String BLUE = "buttons/blue.png";
    private final String RED = "buttons/red.png";
    private final String GREEN = "buttons/green.png";
    private final String PURPLE = "buttons/purple.png";
    private final String RETURN = "buttons/button_return.png";
    private final String UP = "buttons/up.png";
    private final String DOWN = "buttons/down.png";
    private final String LIGHT = "buttons/button_ball-light-intensity.png";
    private final String TOGGLELED = "buttons/button_toggle-led.png";
    private final String CALIBRATE = "buttons/button_reset-heading.png";

    
    Table colorButtons;
    Table lightButtons;
    Table calibrateButtons;

    private Array<Button> buttons;
    private Array<Button> clrButtons;
    private Array<Button> litButtons;
    private Array<Button> calButtons;

    public SettingsScreen(Game game) {
        super(game);
        parallaxBackground();
        
        colorButtons = new Table();
        colorButtons.setFillParent(true);
        
        lightButtons = new Table();
        lightButtons.setFillParent(true);

        calibrateButtons = new Table();
        calibrateButtons.setFillParent(true);

        createButtons();
        stage.addActor(table);
        stage.addActor(colorButtons);
        stage.addActor(lightButtons);
        stage.addActor(calibrateButtons);

    }
    public void createButtons() {
    	clrButtons = new Array<Button>();
        buttons = new Array<Button>();
        litButtons = new Array<Button>();
        calButtons = new Array<Button>();
        
        buttons.add(createButton(RETURN, this::returnToMainMenu));
        table.pad(50);
        for (Button button : buttons) {
            table.add(button).width((float) (button.getWidth() / 2)).height((float) (button.getHeight() / 2)).pad(5);
            table.row();
            table.setFillParent(true);
        }
        
        colorButtons.pad(500);
        colorButtons.add(createButton(COLOR, null));
        colorButtons.row();
        clrButtons.add(createButton(BLUE, this::setBlue));
        clrButtons.add(createButton(RED, this::setRed));
        clrButtons.add(createButton(GREEN, this::setGreen));
        clrButtons.add(createButton(PURPLE, this::setPurple));
        for(Button button : clrButtons) {
        	colorButtons.add(button).width((float) (100)).height(100).pad(5);
        	colorButtons.row();
        }

        lightButtons.pad(50);
        lightButtons.add(createButton(LIGHT, null));
        lightButtons.row();
        litButtons.add(createButton(UP, this::increaseLight));
        litButtons.add(createButton(DOWN, this::decreaseLight));
        for(Button button : litButtons) {
            lightButtons.add(button).width(150).height(150);
            lightButtons.row();
        }

        calibrateButtons.pad(50);
        calButtons.add(createButton(TOGGLELED, this::toggleLED));
        calButtons.add(createButton(CALIBRATE, this::calibrate));
        for(Button button : calButtons) {
            calibrateButtons.add(button).width((float) (button.getWidth() / 2)).height((float) (button.getHeight() / 2)).pad(5);
            calibrateButtons.row();
        }
        
        table.setPosition(0, -350);
        colorButtons.setPosition(-400, 0);
        lightButtons.setPosition(400, 65);
        calibrateButtons.setPosition(0, 0);
        
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
    	game.getReader().setRGB(255, 0, 0);
    }

    public void setBlue() {
    	game.getReader().setRGB(0, 0, 255);
    }

    public void setGreen() {
    	game.getReader().setRGB(0, 255, 0);
    }

    public void setPurple() {
    	game.getReader().setRGB(255, 0, 255);
    }
    public void increaseLight() {
    	game.getReader().increaseBrightness();
    }
    public void decreaseLight() {
    	game.getReader().decreaseBrightness();
    }
    public void toggleLED() {
        game.getReader().toggleLED();
    }
    public void calibrate() {
        game.getReader().calibrate();
    }

    public String getTrackName() {
        return "mainmenumusic.wav";
    }
}
