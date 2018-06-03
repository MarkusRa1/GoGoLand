package uib.bamboozle.ui;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

/**
 * Represents an object in the 3d space
 */
public class GameObject implements Disposable {
    private ModelInstance instance;
    private btRigidBody body;

    /**
     * Creates an object
     *
     * @param model The libgdx model
     * @param shape The jBullet shape
     * @param pos Initial osition
     * @param mass Mass
     */
    GameObject(Model model, btCollisionShape shape, Vector3 pos, float mass) {
        instance = new ModelInstance(model, pos);

        Vector3 localInertia = new Vector3();
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);

        body = new btRigidBody(new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia));
        body.setCollisionShape(shape);
        body.setMotionState(new StandardMotionState(instance.transform));
        body.proceedToTransform(instance.transform);
    }

    /**
     * @return The libgdx ModelInstance
     */
    public ModelInstance getInstance() {
        return instance;
    }

    /**
     * @return The jBullet rigid body
     */
    public btRigidBody getBody() {
        return body;
    }

    public void dispose() {
        body.dispose();
    }
}

/**
 * Called by jBullet when the position of the body changes. Updates the libgdx instance.
 */
class StandardMotionState extends btMotionState {
    private Matrix4 transform;

    public StandardMotionState(Matrix4 transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform (Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform (Matrix4 worldTrans) {
        transform.set(worldTrans);
    }
}