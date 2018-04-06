package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class ModelFactory implements Disposable {
    private Map<String, ObjectModel> models = new HashMap<>();

    public void add(String name, Model model, btCollisionShape shape) {
        models.put(name, new ObjectModel(model, shape));
    }

    public GameObject get(String name, Vector3 pos, float mass) {
        return new GameObject(models.get(name).model, models.get(name).shape, pos, mass);
    }

    public void dispose() {
        for(ObjectModel om : models.values()) {
            om.model.dispose();
        }
    }

    private class ObjectModel {
        private Model model;
        private btCollisionShape shape;

        private ObjectModel(Model m, btCollisionShape s) {
            this.model = m;
            this.shape = s;
        }
    }
}
