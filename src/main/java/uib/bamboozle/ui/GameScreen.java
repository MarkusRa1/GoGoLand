package uib.bamboozle.ui;

import com.badlogic.gdx.Screen;

import uib.bamboozle.Game;
import uib.bamboozle.Level;
import uib.bamboozle.Level1;

public class GameScreen implements Screen {
    private Graphics graphics;
    private Level level;
    private Game game;

    private float tempRoll = game.roll;
    private float tempPitch = game.pitch;
    private float tempYaw = game.yaw;
    
    public GameScreen(Game game) {
        this.game = game;
        graphics = new Graphics();
        level = new Level1(graphics.getDynamicsWorld(), graphics.getRenderer(), graphics.getModelBatch());
        level.create();
    }

    @Override
    public void render(float delta) {
        graphics.render(delta);

        GameObject cube = level.getCube();

        tempRoll = gradualChangeToRollPitchOrYaw(tempRoll, game.roll);
        tempPitch = gradualChangeToRollPitchOrYaw(tempPitch, game.pitch);
        tempYaw = gradualChangeToRollPitchOrYaw(tempYaw, game.yaw);

        cube.getInstance().transform.setFromEulerAngles(tempYaw, tempPitch, -tempRoll);
        cube.getBody().setWorldTransform(cube.getInstance().transform);

        level.render(delta);
    }

    @Override
    public void dispose() {
        graphics.dispose();
        level.dispose();

        game.reader.stop();
    }

    private float gradualChangeToRollPitchOrYaw(float oldf, float newf) {
        float diff = Math.abs(newf - oldf);
        if (diff > 180)
            return newf > oldf ? oldf - (360 - diff) / 4 : oldf + (360 - diff) / 4;
        else
            return newf > oldf ? oldf + diff / 4: oldf - diff / 4;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
