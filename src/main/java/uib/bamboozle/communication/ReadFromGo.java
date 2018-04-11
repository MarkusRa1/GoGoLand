package uib.bamboozle.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import uib.bamboozle.Game;

public class ReadFromGo implements Runnable {
    public Process pr;
    public BufferedReader in;
    public int pid;
    private Game game;

    public ReadFromGo(Game game) {
        this.game = game;
    }

    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/spheroController.go");
            ps.redirectErrorStream(true);

            pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.matches("\\d+")) {
                    pid = Integer.parseInt(line);
                }
                else if (line.contains("error")) {
                    System.out.println(line);
                    System.out.println(in.readLine());
                    System.out.println(in.readLine());
                    stop();
                    break;
                }
                else if (line.matches("-?\\d+ -?\\d+ -?\\d+")) {
                    String[] coords = line.split(" ");
                    game.roll = Integer.parseInt(coords[0]);
                    game.pitch = Integer.parseInt(coords[1]);
                    game.yaw = Integer.parseInt(coords[2]);
                }
                else {
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop() {
        pr.destroy();
        try{
            if(System.getProperty("os.name").startsWith("Windows"))
                Runtime.getRuntime().exec("taskkill /F /PID " + pid);
            else
                Runtime.getRuntime().exec("kill -9 " + pid);
            in.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
