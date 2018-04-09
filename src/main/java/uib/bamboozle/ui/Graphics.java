package uib.bamboozle.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import uib.bamboozle.Level;
import uib.bamboozle.Level1;
import uib.bamboozle.Main;

public class Graphics implements ApplicationListener {
    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;
    private btDiscreteDynamicsWorld dynamicsWorld;
    private ModelFactory modelFactory;

    private Level level;

    @Override
    public void create () {
        Bullet.init();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        btSequentialImpulseConstraintSolver constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));

        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBatch modelBatch = new ModelBatch();
        PerspectiveCamera cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 5f, -10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        modelFactory = createModels();

        Renderer renderer = new Renderer(cam, environment, modelBatch);

        level = new Level1(dynamicsWorld, renderer, modelFactory);
        level.create();
    }

    @Override
    public void render () {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        GameObject cube = level.getCube();

        cube.getInstance().transform.setFromEulerAngles(Main.yaw, Main.pitch, -Main.roll);
        cube.getBody().setWorldTransform(cube.getInstance().transform);

        level.render(delta);
    }

    @Override
    public void dispose () {
        dispatcher.dispose();
        collisionConfig.dispose();

        level.dispose();
        modelFactory.dispose();

        Main.reader.stop();
    }

    private ModelFactory createModels() {
        ModelBuilder modelBuilder = new ModelBuilder();
        ModelFactory factory = new ModelFactory();
        ModelLoader loader = new ObjLoader();

        btCompoundShape cubeShape = new btCompoundShape();
        cubeShape.addChildShape(new Matrix4(), new btBoxShape(new Vector3(5f, 0.5f, 5f)));
        cubeShape.addChildShape(new Matrix4(new Vector3(3, 1.5f, -3), new Quaternion(), new Vector3().add(1)), new btConeShape(1, 2));
        cubeShape.addChildShape(new Matrix4(new Vector3(-3, 1.5f, -3), new Quaternion(), new Vector3().add(1)), new btBoxShape(new Vector3(0.3f, 2, 0.3f)));

        modelBuilder.begin();
        modelBuilder.node().translation.set(3, 1.5f, -3);
        modelBuilder.part("cone", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                .cone(2f,2f,2f,16);

        modelBuilder.node().translation.set(0, 0, 0);
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(10f,1f,10f);

        modelBuilder.node().translation.set(-3, 0.5f, -3);
        Model treeModel = loader.loadModel(Gdx.files.internal("LowPolyTree.obj"));
        modelBuilder.part("tree", treeModel.meshes.first(), GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BLUE)));

        factory.add("cube", modelBuilder.end(), cubeShape);

        Model ballModel = modelBuilder.createSphere(2f, 2f, 2f, 24, 24,
                new Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        factory.add("ball", ballModel, new btSphereShape(1f));

        return factory;
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