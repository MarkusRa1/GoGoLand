package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.Set;

public class Renderer {
    private Camera cam;
    private Environment environment;
    private ModelBatch modelBatch;

    public Renderer(Camera cam, Environment environment, ModelBatch modelBatch) {
        this.cam = cam;
        this.environment = environment;
        this.modelBatch = modelBatch;
    }

    /**
     * Draws the game objects on the screen
     */
    public void render(Set<GameObject> objects) {
        modelBatch.begin(cam);
        for(GameObject object : objects) {
            modelBatch.render(object.getInstance(), environment);
        }
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
    }

    public void setCameraPosition(Vector3 newPos) {
        cam.position.set(newPos);
    }
}
