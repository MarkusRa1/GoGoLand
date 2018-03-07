package uib.bamboozle;

import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Disposable;
import uib.bamboozle.ui.GameObject;
import uib.bamboozle.ui.ModelFactory;
import uib.bamboozle.ui.Renderer;

import java.util.HashSet;
import java.util.Set;

public abstract class Level implements Disposable {
    private btDiscreteDynamicsWorld dynamicsWorld;
    private Renderer renderer;
    private Set<GameObject> objects = new HashSet<>();
    private ModelFactory factory;

    public Level(btDiscreteDynamicsWorld dynamicsWorld, Renderer renderer, ModelFactory factory) {
        this.dynamicsWorld = dynamicsWorld;
        this.renderer = renderer;
        this.factory = factory;
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
        if(object == null) throw new IllegalArgumentException();
        objects.remove(object);
        dynamicsWorld.removeRigidBody(object.getBody());
        object.dispose();
    }

    protected ModelFactory getModelFactory() {
        return factory;
    }
}
