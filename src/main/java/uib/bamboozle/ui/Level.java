package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

import java.util.HashSet;
import java.util.Set;

public abstract class Level {
    private btDiscreteDynamicsWorld dynamicsWorld;
    private Camera cam;
    private Environment environment;
    private ModelBatch modelBatch;
    private Set<GameObject> objects = new HashSet<>();

    public Level(btDiscreteDynamicsWorld dynamicsWorld, Camera cam, Environment environment, ModelBatch modelBatch) {
        this.dynamicsWorld = dynamicsWorld;
        this.cam = cam;
        this.environment = environment;
        this.modelBatch = modelBatch;
    }

    public void create() {}

    public void render(float delta) {
        modelBatch.begin(cam);
        for(GameObject object : this.objects) {
            modelBatch.render(object.getInstance(), environment);
        }
        modelBatch.end();
    }

    public void dispose() {
        for(GameObject object : objects) {
            object.dispose();
        }

        modelBatch.dispose();
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
