package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

/**
 * The main menu screen shown when starting the game
 */
public class MainMenuScreen extends Menu implements Screen {
    //Name of Pictures
	private static final String PLAY = "Play2.png";
    private static final String BACKGROUND = "bg2.jpg";
    private static final String EXIT = "bs_quit.png";
    private static final String CONNECT = "connect.png";
    private static final String DISCONNECT = "disconnect.png";

    //Name for switch case methods
    private final String newGame = "newGame";
    private final String exit = "exitGame";
    private final String connect = "connect";
    
    //Graphics for mainmenu
    ParallaxBackground parallaxBackground;
    private Table table;
   
    
    private Button connectButton;
    private ImageButton.ImageButtonStyle connectedStyle = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle disconnectedStyle = new ImageButton.ImageButtonStyle();
    
    public MainMenuScreen(Game game) {
    	super(game);
        table = new Table();
        
        //parallax background, for more advanced add pictures to Background and loop them
        Array<Texture> textures = new Array<Texture>();
          textures.add(new Texture(Gdx.files.internal("Background/bg4.png")));
          textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        parallaxBackground = new ParallaxBackground(textures, stage.getWidth(), stage.getHeight(), 1);
        getStage().addActor(parallaxBackground);
        
        //BB8 animation
        Array<TextureRegion> bb8footage = new Array<TextureRegion>();
        for(int i = 0; i<=22 ; i ++) {
        	bb8footage.add(new TextureRegion(new Texture(Gdx.files.internal("bb8/" + i +".png"))));
        }
        System.out.println(bb8footage.size);
        Animation<TextureRegion> bb8Animate = new Animation<TextureRegion>(0.0001f , bb8footage, PlayMode.LOOP);
        AnimatedActor bb8 = new AnimatedActor(bb8Animate);
        bb8.setPosition(350, 150);
        getStage().addActor(bb8);
        
        createButtons();
        //table.setDebug(true);
        table.setFillParent(true);
        getStage().addActor(table);

    }

    
    private void createButtons() {
        connectButton = createButton(CONNECT, connect, this);
        connectButton.setStyle(connectedStyle);
        connectedStyle.imageUp = createImage(CONNECT);
        disconnectedStyle.imageUp = createImage(DISCONNECT);

        Array<Button> buttons = new Array<>();

        buttons.add(createButton(PLAY, newGame, this));
        buttons.add(connectButton);
        buttons.add(createButton(EXIT, exit, this));

        table.center();

        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            table.row();
        }

    }
    
    @Override
    public void render(float delta) {
        if(getGame().isConnected()) {
            connectButton.setStyle(disconnectedStyle);
        } else {
            connectButton.setStyle(connectedStyle);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
