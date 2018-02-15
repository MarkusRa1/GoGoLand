package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
   public static void main (String[] arg) {
      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "GoGoLand";
      config.width = 1280;
      config.height = 800;
      new LwjglApplication(new Basic3DTest(), config);
   }
}