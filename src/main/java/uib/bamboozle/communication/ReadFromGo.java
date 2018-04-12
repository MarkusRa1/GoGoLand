package uib.bamboozle.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import uib.bamboozle.Game;

public class ReadFromGo implements Runnable {
    public Process pr;
    public BufferedReader in;
    public int pid;

    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/spheroController.go");
            ps.redirectErrorStream(true);

            pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            getData(9001);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean intepretData(String line) throws IOException {
        if (line.matches("\\d+")) {
            pid = Integer.parseInt(line);
        }
        else if (line.contains("error")) {
            System.out.println(line);
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            stop();
            return true;
        }
        else if (line.matches("-?\\d+ -?\\d+ -?\\d+")) {
            String[] coords = line.split(" ");
            Game.roll = Integer.parseInt(coords[0]);
            Game.pitch = Integer.parseInt(coords[1]);
            Game.yaw = Integer.parseInt(coords[2]);
        }
        else {
            System.out.println(line);
        }
        return false;
    }

    public void getData(int port) throws IOException {
        String sentence;
        String line;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        while (true) {
            line = inFromServer.readLine();
            if (intepretData(line)) break;

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
