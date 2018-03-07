package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

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
}
