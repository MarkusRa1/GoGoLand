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

    private static final String CONNECTED = "button_connected.png";
    private static final String NOTCONNECTED = "button_not-connected.png";
	private static final String PLAY = "buttons/button_play.png";
    private static final String BACKGROUND = "buttons/bg4.jpg";
    private static final String EXIT = "buttons/button_quit.png";
    private static final String CONNECT = "buttons/button_connect.png";
    private static final String DISCONNECT = "buttons/button_disconnect.png";
    private static final String SETTINGS = "buttons/button_settings.png";
    
    //Graphics for mainmenu
    ParallaxBackground parallaxBackground;
    private Table table;
    private Table topRightTable;

    private Button connectedButton;
    private ImageButton.ImageButtonStyle cStyle = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle ncStyle = new ImageButton.ImageButtonStyle();

    private Button connectButton;
    private ImageButton.ImageButtonStyle connectedStyle = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle disconnectedStyle = new ImageButton.ImageButtonStyle();
    
    public MainMenuScreen(Game game) {
    	super(game);
        table = new Table();
        topRightTable = new Table();
        
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
        //topRightTable.setDebug(true);
        topRightTable.setFillParent(true);
        getStage().addActor(table);
        getStage().addActor(topRightTable);
    }

    
    private void createButtons() {
        connectButton = createButton(CONNECT, this::connect);
        connectButton.setStyle(connectedStyle);
        connectedStyle.imageUp = createImage(CONNECT);
        disconnectedStyle.imageUp = createImage(DISCONNECT);

        Array<Button> buttons = new Array<>();

        buttons.add(createButton(PLAY, this::newGame));
        buttons.add(connectButton);
        buttons.add(createButton(EXIT, this::exitGame));

        table.setPosition(stage.getWidth()/10 - 200, stage.getHeight()/10 +200);;

        table.row();
        for (Button but : buttons) {
            table.add(but).width((float) (but.getWidth() / 2)).height((float) (but.getHeight() / 2)).pad(5);
            table.row();
        }


        topRightTable.top().right();
        topRightTable.row();
        cStyle.imageUp = createImage(CONNECTED);
        ncStyle.imageUp = createImage(NOTCONNECTED);
        connectedButton = createButton(NOTCONNECTED, null);
        topRightTable.add(connectedButton).width((float) (connectedButton.getWidth())).height((float) (connectedButton.getHeight()));
    }
    
    @Override
    public void render(float delta) {
        if(getGame().isConnected()) {
            connectButton.setStyle(disconnectedStyle);
        } else {
            connectButton.setStyle(connectedStyle);
        }

        if (game.isConnected()) {
            connectedButton.setStyle(cStyle);
        } else {
            connectedButton.setStyle(ncStyle);
        }


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void exitGame() {
        Gdx.app.exit();
    }
    public void newGame() {
        game.setScreen(game.newGame());
    }

    public void connect() {
        if(!game.isConnected()) {
            game.connect();
        } else {
            game.disconnect();
        }
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
