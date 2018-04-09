package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class ModelFactory implements Disposable {
    private Map<String, ObjectModel> models = new HashMap<>();

    public void add(String name, Model model, btCollisionShape shape) {
        if(shape == null) {
            shape = createShape(model);
        }

        models.put(name, new ObjectModel(model, shape));
    }

    public GameObject get(String name, Vector3 pos, float mass) {
        ObjectModel om = models.get(name);
        return new GameObject(om.model, om.shape, pos, mass);
    }

    public void dispose() {
        for(ObjectModel om : models.values()) {
            om.model.dispose();
        }
    }

    private btCollisionShape createShape(Model model) {
        btCompoundShape shape = new btCompoundShape();

        for (Node node : model.nodes) {
            Array<MeshPart> meshParts = new Array<>();

            for (NodePart nodepart : node.parts) {
                meshParts.add(nodepart.meshPart);
            }

            shape.addChildShape(new Matrix4(node.translation, new Quaternion(), new Vector3().add(1)),
                    new btBvhTriangleMeshShape(meshParts));
        }

        return shape;
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
