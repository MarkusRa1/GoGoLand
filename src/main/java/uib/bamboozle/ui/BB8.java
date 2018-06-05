package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BB8 {
    public static AnimatedActor make() {
        Array<TextureRegion> bb8footage = new Array<TextureRegion>();
        for (int i = 0; i <= 22; i++) {
            bb8footage.add(new TextureRegion(new Texture(Gdx.files.internal("bb8/" + i + ".png"))));
        }
        return new AnimatedActor(new Animation<TextureRegion>(0.0001f, bb8footage, Animation.PlayMode.LOOP));
    }
}
