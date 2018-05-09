package uib.bamboozle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;

import org.lwjgl.Sys;
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

        checkForPauseRequest();

        if(level.isFinished()) {
            nextLevel();
        }

        GameObject cube = level.getCube();

        cheat();

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
    private boolean checkForPauseRequest() {
		final boolean pause = Gdx.input.isKeyJustPressed(Keys.ESCAPE);
		if (pause) {
			game.setScreen(game.getPauseMenuScreen());
		}
		return pause;
	}


    private void nextLevel() {
        if(level != null) {
            level.dispose();
        }
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
    public int getLevelNumber() {
    	return levelNum;
    }

    private void cheat() {
        if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            game.roll += 1;
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            game.roll -= 1;
        }
        if(Gdx.input.isKeyPressed(Keys.UP)) {
            game.pitch += 1;
        }
        if(Gdx.input.isKeyPressed(Keys.DOWN)) {
            game.pitch -= 1;
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
