package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class GameObject implements Disposable {
    private ModelInstance instance;
    private btRigidBody body;
    private Vector3 localInertia = new Vector3();

    GameObject(Model model, btCollisionShape shape, Vector3 pos, float mass) {
        instance = new ModelInstance(model, pos);
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);

        body = new btRigidBody(new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia));
        body.setCollisionShape(shape);
        body.setWorldTransform(instance.transform);
        body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    }

    public ModelInstance getInstance() {
        return instance;
    }

    public btRigidBody getBody() {
        return body;
    }

    public void dispose() {
        body.dispose();
    }
}