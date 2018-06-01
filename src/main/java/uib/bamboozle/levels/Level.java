package uib.bamboozle.levels;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Disposable;
import uib.bamboozle.ui.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Level implements Disposable {
    private final AudioManager audioManager;
    private btDiscreteDynamicsWorld dynamicsWorld;
    private Renderer renderer;
    private Set<GameObject> objects = new HashSet<>();
    private ModelFactory factory;

    public Level(Graphics graphics) {
        this.dynamicsWorld = graphics.getDynamicsWorld();
        this.renderer = graphics.getRenderer();
        this.factory = graphics.getModelFactory();
        this.audioManager = graphics.getAudioManager();
    }

    public void render(float delta) {
        renderer.render(objects);
    }

    public void dispose() {
        Iterator<GameObject> iter = objects.iterator();

        while(iter.hasNext()) {
            GameObject obj = iter.next();
            dynamicsWorld.removeRigidBody(obj.getBody());
            obj.dispose();
            iter.remove();
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

    protected void playTrack(String name) {
        audioManager.playTrack(name);
    }

    protected void setCameraPosition(Vector3 newPos) {
        renderer.setCameraPosition(newPos);
    }

    public abstract boolean isFinished();
}
