package uib.bamboozle.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import org.lwjgl.Sys;
import uib.bamboozle.Main;

import java.util.ArrayList;
import java.util.List;

public class Graphics implements ApplicationListener {
    private PerspectiveCamera cam;
    private Environment environment;
    private ModelBatch modelBatch;
    private Model cubeModel;
    private Model ballModel;
    private GameObject cube;
    private GameObject ball;

    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private boolean start = false;
    private ModelBuilder modelBuilder;

    @Override
    public void create () {
        Bullet.init();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        btSequentialImpulseConstraintSolver constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 5f, -10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        modelBuilder = new ModelBuilder();

        cubeModel = modelBuilder.createBox(10f, 1f, 10f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        cube = new GameObject(cubeModel, new btBoxShape(new Vector3(5f, 0.5f, 5f)), new Vector3(0f, 0f, 0f), 0f);
        dynamicsWorld.addRigidBody(cube.getBody());
    }

    @Override
    public void render () {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

        cube.getInstance().transform.setFromEulerAngles(Main.yaw, Main.pitch, -Main.roll);
        cube.getBody().setWorldTransform(cube.getInstance().transform);
        if(ball != null)
            ball.getBody().getWorldTransform(ball.getInstance().transform);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            ballModel = modelBuilder.createSphere(2f, 2f, 2f, 24, 24,
                    new Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                    Usage.Position | Usage.Normal);
            ball = new GameObject(ballModel, new btSphereShape(1f), new Vector3(0f, 5f, 0f), 0.25f);
            dynamicsWorld.addRigidBody(ball.getBody());
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        if(ball != null)
            modelBatch.render(ball.getInstance(), environment);
        modelBatch.render(cube.getInstance(), environment);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        dispatcher.dispose();
        collisionConfig.dispose();

        modelBatch.dispose();
        cubeModel.dispose();
        if(ballModel != null)
            ballModel.dispose();
        cube.dispose();
        if(ball != null)
            ball.dispose();
        Main.reader.stop();
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }
}