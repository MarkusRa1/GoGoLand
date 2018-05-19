package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.bamboozle.Game;

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
    TextureAtlas spriteSheet;
    private Array<Sprite> bb8;
    ParallaxBackground parallaxBackground;
    private Table table;
    
    private Button connectButton;
    private ImageButton.ImageButtonStyle connectedStyle = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle disconnectedStyle = new ImageButton.ImageButtonStyle();
    
    public MainMenuScreen(Game game) {
    	super(game);
        table = new Table();
        
        //BB8 animation
        spriteSheet = new TextureAtlas("bbm8.txt");
        bb8 = spriteSheet.createSprites("BB8");
        
        //parallax background
        Array<Texture> textures = new Array<Texture>();
        for(int i = 4; i <=4;i++){
          textures.add(new Texture(Gdx.files.internal("Background/bg"+i+".png")));
          textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        parallaxBackground = new ParallaxBackground(textures, stage.getWidth(), stage.getHeight(), 1);
        getStage().addActor(parallaxBackground);
        
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



}
