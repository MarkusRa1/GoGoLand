package uib.bamboozle.communication;

import org.lwjgl.openal.AL;

import uib.bamboozle.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Eirik on 15.02.2018.
 */
public class ReadFromGo implements Runnable {
    public Process pr;
    public BufferedReader in;

    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/spheroController.go");
            ps.redirectErrorStream(true);

            pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("error")) {
                    pr.destroy();
                    in.close();
                    break;
                }
                else if (line.matches("-?\\d+ -?\\d+ -\\d+")) {
                    String[] coords = line.split(" ");
                    Main.roll = Integer.parseInt(coords[0]);
                    Main.pitch = Integer.parseInt(coords[1]);
                    Main.yaw = Integer.parseInt(coords[2]);

                    //System.out.printf("%d # %d # %d\n", Main.roll, Main.pitch, Main.yaw);
                }
                else {
                    System.out.println(line);
                }
            }
            System.out.println("end");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop() {
        pr.destroy();
        try{
            in.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
