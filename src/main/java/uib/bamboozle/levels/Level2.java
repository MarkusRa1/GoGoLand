package uib.bamboozle.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import uib.bamboozle.ui.GameObject;
import uib.bamboozle.ui.Graphics;

public class Level2 extends Level {
    private GameObject cube;
    private GameObject ball;

    public Level2(Graphics graphics) {
        super(graphics);

        cube = getModelFactory().get("cube", new Vector3(0f, 0f, 0f), 0f);
        cube.getBody().setCollisionFlags(cube.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        addObject(cube);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (ball != null) {
                removeObject(ball);
            }

            ball = getModelFactory().get("ball", new Vector3(0f, 5f, 0f), 0.25f);
            addObject(ball);
        }

        super.render(delta);
    }

    public boolean isFinished() {
        return false;
    }

    public GameObject getCube() {
        return cube;
    }
}