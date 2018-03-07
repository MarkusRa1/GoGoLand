package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

public class Level1 extends Level {
    private Model cubeModel;
    private Model ballModel;
    private GameObject cube;
    private GameObject ball;

    public Level1(btDiscreteDynamicsWorld dynamicsWorld, Renderer renderer) {
        super(dynamicsWorld, renderer);
    }

    @Override
    public void create() {
        ModelBuilder modelBuilder = new ModelBuilder();

        cubeModel = modelBuilder.createBox(10f, 1f, 10f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        cube = new GameObject(cubeModel, new btBoxShape(new Vector3(5f, 0.5f, 5f)), new Vector3(0f, 0f, 0f), 0f);
        cube.getBody().setCollisionFlags(cube.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        addObject(cube);

        ballModel = modelBuilder.createSphere(2f, 2f, 2f, 24, 24,
                new Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (ball != null) {
                removeObject(ball);
            }

            ball = new GameObject(ballModel, new btSphereShape(1f), new Vector3(0f, 5f, 0f), 0.25f);
            addObject(ball);
        }

        super.render(delta);
    }

    public GameObject getCube() {
        return cube;
    }
}
