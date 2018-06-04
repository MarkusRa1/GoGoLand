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
    
    
    Table colorButtons;
    Table lightButtons;

    private Array<Button> buttons;
    private Array<Button> clrButtons;
    private Array<Button> litButtons;

    public SettingsScreen(Game game) {
        super(game);
        parallaxBackground();
        
        colorButtons = new Table();
        colorButtons.setFillParent(true);
        
        lightButtons = new Table();
        lightButtons.setFillParent(true);
        
        createButtons();
        stage.addActor(table);
        stage.addActor(colorButtons);
        stage.addActor(lightButtons);


    }
    public void createButtons() {
    	clrButtons = new Array<Button>();
        buttons = new Array<Button>();
        litButtons = new Array<Button>();
        
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
        
        table.center();
        colorButtons.setPosition(-400, 0);
        lightButtons.setPosition(400, 65);
        
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
    	//TODO inserted by Sturle
    }

    public void setBlue() {
    	//TODO inserted by Sturle
    }

    public void setGreen() {
    	//TODO inserted by Sturle
    }

    public void setPurple() {
    	//TODO inserted by Sturle
    }
    public void increaseLight() {
    	//TODO inserted by Sturle
    }
    public void decreaseLight() {
    	//TODO inserted by Sturle
    }
    @Override
    public void show() {
        game.getAudioManager().loopTrack("level2music.wav");
        super.show();
    }

    @Override
    public void hide() {
        game.getAudioManager().stopTrack("level2music.wav");
        super.hide();
    }

}
