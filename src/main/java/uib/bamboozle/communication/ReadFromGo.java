package uib.bamboozle.communication;

import uib.bamboozle.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Eirik on 15.02.2018.
 */
public class ReadFromGo implements Runnable {
    public Process pr;
    public BufferedReader in;
    public int pid;

    public void run() {
        try {
        		String s = "";
        		if(System.getProperty("os.name").toLowerCase().startsWith("mac")) {
        			Process p = Runtime.getRuntime().exec("./findspheromac.sh");
        			s = new BufferedReader(new InputStreamReader(p.getInputStream())).readLine();
        		}
        			
            ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/spheroController.go", s);
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
                    Main.roll = Integer.parseInt(coords[0]);
                    Main.pitch = Integer.parseInt(coords[1]);
                    Main.yaw = Integer.parseInt(coords[2]);
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
