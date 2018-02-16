package uib.bamboozle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
   public static int roll;
   public static int pitch;
   public static int yaw;
   public static ReadFromGo reader;

   public static void main (String[] arg) {
      reader = new ReadFromGo();
      new Thread(reader).start();

      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "GoGoLand";
      config.width = 1280;
      config.height = 800;
      new LwjglApplication(new Graphics(), config);
   }
}