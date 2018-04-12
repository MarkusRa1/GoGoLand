package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Menu {
    protected ImageButton createButton(String imageString, String target, Menu menu) {
        ImageButton button = new ImageButton(createImage(imageString));
        if(target != null)
            addButtonListener(button, target, menu);
        return button;
    }

    protected ImageButton createButton(String upImage, String downImage, String checkedImage, String target, Menu menu) {
        ImageButton button = new ImageButton(createImage(upImage), createImage(downImage), createImage(checkedImage));
        if(target != null)
            addButtonListener(button, target, menu);
        return button;
    }

    protected Drawable createImage(String imageString) {
        Texture myTexture = new Texture(Gdx.files.internal(imageString));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        return new TextureRegionDrawable(myTextureRegion);
    }

    public <T> void addButtonListener(Button button, String target, Menu menu) {
        

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Stage eventStage = event.getTarget().getStage();
                Vector2 mouse = eventStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                if (eventStage.hit(mouse.x, mouse.y, true) == event.getTarget()) {
                    switch (target) {
                        case "newGame":
                            menu.newGame();
                            break;
                        case "connect":
                            menu.connect();
                            break;
                        case "disconnect":
                            menu.disconnect();
                            break;
                        case "exitGame":
                            menu.exitGame();
                            break;
                    }
                }
            }
        });
    }
    public void exitGame() {
        //todo
    }

    public void newGame() {
        //todo
    }

    public void connect() {}

    public void disconnect() {}
}
