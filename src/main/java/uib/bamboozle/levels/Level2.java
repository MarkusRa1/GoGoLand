package uib.bamboozle.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import uib.bamboozle.Game;
import uib.bamboozle.ui.AudioManager;
import uib.bamboozle.ui.GameObject;
import uib.bamboozle.ui.Graphics;

public class Level2 extends Level {
    private GameObject cube;
    private GameObject ball;
    private GameObject goal;

    public Level2(Graphics graphics, AudioManager audioManager) {
        super(graphics, audioManager);

        cube = getModelFactory().get("level2", new Vector3(0f, 0f, 0f), 0f);
        cube.getBody().setCollisionFlags(cube.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        addObject(cube);

        goal = getModelFactory().get("goal", new Vector3(8.7f, -1.5f, 1f), 0f);
        addObject(goal);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (ball != null) {
                removeObject(ball);
            }

            ball = getModelFactory().get("ball", new Vector3(-4f, 5f, 0f), 0.25f);
            addObject(ball);
        }

        super.render(delta);
    }

    public boolean isFinished() {
        if(ball != null) {
            Vector3 dist = goal.getInstance().transform.getTranslation(new Vector3()).sub(ball.getInstance().transform.getTranslation(new Vector3()));
            return dist.len() < 1.5;
        } else {
            return false;
        }
    }

    public GameObject getCube() {
        return cube;
    }

    public String getTrackName() {
        return "midlevelmusic.wav";
    }
}
