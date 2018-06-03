package uib.bamboozle.levels;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Disposable;
import uib.bamboozle.ui.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An abstract class that provides functionality for the levels
 */
public abstract class Level implements Disposable {
    private final AudioManager audioManager;
    private btDiscreteDynamicsWorld dynamicsWorld;
    private Renderer renderer;
    private Set<GameObject> objects = new HashSet<>();
    private ModelFactory factory;

    public Level(Graphics graphics, AudioManager audioManager) {
        this.dynamicsWorld = graphics.getDynamicsWorld();
        this.renderer = graphics.getRenderer();
        this.factory = graphics.getModelFactory();
        this.audioManager = audioManager;

        audioManager.loopTrack(getTrackName());
    }

    /**
     * Renders the level
     *
     * @param delta Time since last frame
     */
    public void render(float delta) {
        renderer.render(objects);
    }

    /**
     * Disposes the level
     */
    public void dispose() {
        Iterator<GameObject> iter = objects.iterator();

        while(iter.hasNext()) {
            GameObject obj = iter.next();
            dynamicsWorld.removeRigidBody(obj.getBody());
            obj.dispose();
            iter.remove();
        }

        audioManager.stopTrack(getTrackName());
    }

    /**
     * @return The cube that is being controlled by the sphero
     */
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

    protected void setCameraPosition(Vector3 newPos) {
        renderer.setCameraPosition(newPos);
    }

    /**
     * @return True if the game has just been completed
     */
    public abstract boolean isFinished();

    public abstract String getTrackName();
}
