package uib.bamboozle.ui;

import com.badlogic.gdx.Screen;

import uib.bamboozle.Game;
import uib.bamboozle.levels.Level;
import uib.bamboozle.levels.Level1;
import uib.bamboozle.levels.Level2;

public class GameScreen implements Screen {
    private Graphics graphics;
    private Level level;
    private Game game;

    private int levelNum = 0;

    private float tempRoll;
    private float tempPitch;
    private float tempYaw;
    
    public GameScreen(Game game) {
        this.game = game;
        tempRoll = game.roll;
        tempPitch = game.pitch;
        tempYaw = game.yaw;

        graphics = new Graphics();
        nextLevel();
    }

    @Override
    public void render(float delta) {
        graphics.render(delta);

        if(level.isFinished()) {
            nextLevel();
        }

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
    }

    private float gradualChangeToRollPitchOrYaw(float oldf, float newf) {
        float diff = Math.abs(newf - oldf);
        if (diff > 180)
            return newf > oldf ? oldf - (360 - diff) / 4 : oldf + (360 - diff) / 4;
        else
            return newf > oldf ? oldf + diff / 4: oldf - diff / 4;
    }

    private void nextLevel() {
        level = getLevel(++levelNum);
    }

    private Level getLevel(int num) {
        switch (num) {
            case 1:
                return new Level1(graphics);
            case 2:
                return new Level2(graphics);
            default:
                return null;
        }
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
