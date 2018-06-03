package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedActor extends Image
{
    protected Animation<TextureRegion> animation = null;
    private float stateTime = 1;

    public AnimatedActor(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    public void act(float delta)
    {
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime+=delta, true));
        super.act(delta);
    }
}