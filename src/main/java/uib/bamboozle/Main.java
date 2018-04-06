package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uib.bamboozle.communication.ReadFromGo;
import uib.bamboozle.ui.Graphics;

public class Main {
   public static float roll;
   public static float pitch;
   public static float yaw;
   public static ReadFromGo reader;

   public static void main (String[] arg) {
      reader = new ReadFromGo();
      //new Thread(reader).start();

      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "GoGoLand";
      config.width = 1280;
      config.height = 800;
      new LwjglApplication(new Graphics(), config);
   }
}