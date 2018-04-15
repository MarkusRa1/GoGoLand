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
            e.printStackTrace();
            System.out.println("gggg");
        } finally {
            game.readerIsRunning = false;
        }
    }

    public void intepretData(String line) {
        if (line.matches("\\d+")) {
        } else if (line.contains("error")) {
            System.out.println(line);
            stop();
        } else if (line.matches("-?\\d+ -?\\d+ -?\\d+")) {
            String[] coords = line.split(" ");
            game.roll = Integer.parseInt(coords[0]);
            game.pitch = Integer.parseInt(coords[1]);
            game.yaw = Integer.parseInt(coords[2]);
        } else {
            System.out.println(line);
        }
    }

    public void getData(int port) throws IOException {
        String line;
        BufferedReader inFromServer = connectTo(port);

        while (!stop) {
            line = inFromServer.readLine();
            intepretData(line);
        }
        clientSocket.close();
    }

    public BufferedReader connectTo(int preferredPort) {
        boolean connected = false;
        BufferedReader inFromServer = null;
        Future<String> future = null;
        Thread t;

        while (!connected) {
            try {
                if (available(preferredPort)) {
                    ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/tcpServer.go", "" + preferredPort);
                    ps.redirectErrorStream(true);
                    Process pr = ps.start();
                    System.out.println("Started Go");
                    BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    Runnable task1 = new Runnable(){

                        @Override
                        public void run(){
                            try {
                                String line;
                                while ((line = in.readLine()) != null) {
                                    System.out.println(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("dafuq");
                            } finally {
                                System.out.println("closing :S");
                            }

                        }
                    };
                    t = new Thread(task1);
                    t.start();
                } else {
                    System.out.println("busy port " + preferredPort);
                }

                TimeUnit.SECONDS.sleep(1);
                clientSocket = new Socket("localhost", preferredPort);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes("Hello Go Beep Boop\n");
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                ExecutorService executor = Executors.newCachedThreadPool();
                future = executor.submit(inFromServer::readLine);
                if (future.get(5, TimeUnit.SECONDS).compareTo("Hello Java Beep Boop") != 0) {
                    System.out.println("hmm");
                    throw new IOException("Unknown server on port " + preferredPort);
                } else {
                    connected = true;
                    System.out.println("Connected to Go on port " + preferredPort);
                }
            } catch (IOException e) {
                connected = false;
                System.out.println("wut io drit");
            } catch (TimeoutException ex) {
                System.out.println("Timeout when connected to server on port  " + preferredPort);
                connected = false;
            } catch (InterruptedException e) {
                System.out.println("Interrupted when connected to server on port " + preferredPort);
                connected = false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                System.out.println("wut");
                connected = false;
            } finally {
                if (future != null)
                    future.cancel(true); // may or may not desire this
                if (!connected)
                    preferredPort = Math.max((preferredPort + 1) % 65535, 1024);
            }
        }
        return inFromServer;
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
                System.out.println("fkmas");
            }
        }
        stop = true;
        game.setConnected(false);
    }
}
