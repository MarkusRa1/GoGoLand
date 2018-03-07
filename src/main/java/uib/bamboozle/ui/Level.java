package uib.bamboozle.ui;

import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

import java.util.HashSet;
import java.util.Set;

public abstract class Level {
    private btDiscreteDynamicsWorld dynamicsWorld;
    private Renderer renderer;
    private Set<GameObject> objects = new HashSet<>();

    public Level(btDiscreteDynamicsWorld dynamicsWorld, Renderer renderer) {
        this.dynamicsWorld = dynamicsWorld;
        this.renderer = renderer;
    }

    public void create() {}

    public void render(float delta) {
        renderer.render(objects);
    }

    public void dispose() {
        for(GameObject object : objects) {
            object.dispose();
        }

        renderer.dispose();
    }

    public abstract GameObject getCube();

    protected void addObject(GameObject object) {
        objects.add(object);
        dynamicsWorld.addRigidBody(object.getBody());
    }

    protected void removeObject(GameObject object) {
        objects.remove(object);
        dynamicsWorld.removeRigidBody(object.getBody());
        object.dispose();
    }
}
