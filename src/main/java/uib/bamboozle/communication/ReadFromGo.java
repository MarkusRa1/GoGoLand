package uib.bamboozle.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import uib.bamboozle.Game;

public class ReadFromGo implements Runnable {
    private boolean stop = false;
    Socket clientSocket = null;
    DataOutputStream outToServer = null;
    private Game game;
    private static final int MAX_PORT_NUMBER = 65535;
    private static final int MIN_PORT_NUMBER = 0;

    public ReadFromGo(Game game) {
        this.game = game;
    }

    public void run() {
        game.readerIsRunning = true;
        try {
            getData(9001);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            game.readerIsRunning = false;
        }
    }

    public void intepretData(String line) {
        if (line.matches("\\d+")) {
        }
        else if (line.contains("error")) {
            System.out.println(line);
            stop();
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

    public void getData(int port) throws IOException {
        String line;
        clientSocket = new Socket("localhost", port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("Hello Go Beep Boop\n");
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<String> task = inFromServer::readLine;
        Future<String> future = executor.submit(task);
        try {
            if (future.get(5, TimeUnit.SECONDS).compareTo("Hello Java Beep Boop") != 0)
                throw new IOException("Unknown server on port " + port);
        } catch (TimeoutException ex) {
            throw new IOException("Timeout when connected to server on port  " + port);
        } catch (InterruptedException e) {
            throw new IOException("Interrupted when connected to server on port " + port);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new IOException("hmm");
        }
        finally {
            future.cancel(true); // may or may not desire this
        }

        System.out.println("Connected to Go on port " + port);

        if (inFromServer.readLine().compareTo("Hello Java Beep Boop") != 0)
            throw new IOException("Random server på port: " + port);

        while (!stop) {
            line = inFromServer.readLine();
            intepretData(line);
        }
        clientSocket.close();
    }

    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }
        return false;
    }

    public void stop() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stop = true;
        game.setConnected(false);
    }
}
