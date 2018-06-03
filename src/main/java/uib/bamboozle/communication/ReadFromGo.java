package uib.bamboozle.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import uib.bamboozle.Game;

public class ReadFromGo implements Runnable {
    private boolean stop = false;
    private boolean isMonitoring = false;

    Socket clientSocket = null;
    DatagramSocket udpSocket = null;
    byte[] receiveData = new byte[1024];
    DatagramPacket receivePacket = null;
    DataOutputStream outToServer = null;
    private Game game;
    private static final int MAX_PORT_NUMBER = 65535;
    private static final int MIN_PORT_NUMBER = 0;

    private boolean goServerRunning = false;

    public ReadFromGo(Game game) {
        this.game = game;
    }

    public void run() {
        game.readerIsRunning = true;
        try {
            getData(9001);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            game.readerIsRunning = false;
        }
    }

    /**
     * Takes data sendt by Go server and inteprets them. The data is available in the variables game.roll, game.pitch and game.yaw.
     * @param line the data from the Go server.
     */
    public void intepretData(String line) {
        if (!isMonitoring) {
            isMonitoring = true;
            game.setConnected(true);
        }
        line = line.split("\n")[0];
        if (line.matches("\\d+")) {
        } else if (line.contains("error")) {
            System.out.println(line);
            stop();
        } else if (Pattern.compile("-?\\d+ -?\\d+ -?\\d+", Pattern.MULTILINE).matcher(line).matches()) {
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
            intepretData(udpReceive());
        }
        clientSocket.close();
    }

    /**
     * Connects to Go server, if Go server is not online, a subprocess of the Go server will be started on an available port.
     * @param preferredPort The port that Go server exists or where we would prefer to communicate with the Go server.
     * @return The buffered reader to read sphero data.
     */
    public BufferedReader connectTo(int preferredPort) {
        boolean connected = false;
        BufferedReader inFromServer = null;
        Future<String> future = null;
        Thread t;

        while (!connected) {
            try {
                if (available(preferredPort)) {
                    ProcessBuilder ps = new ProcessBuilder("go", "run", "src/main/go/spheroController.go", "" + preferredPort);
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
                                    if (line.compareTo("Launching server...") == 0)
                                        goServerRunning = true;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t = new Thread(task1);
                    t.start();
                } else {
                    System.out.println("busy port " + preferredPort);
                    goServerRunning = true;
                }

                while(!goServerRunning) {
                    Thread.sleep(200);
                }

                udpSocket = new DatagramSocket(preferredPort);
                receivePacket = new DatagramPacket(receiveData, receiveData.length);


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
                e.printStackTrace();
                System.out.println("Retrying...");
            } catch (TimeoutException ex) {
                System.out.println("Timeout when connected to server on port  " + preferredPort);
                connected = false;
            } catch (InterruptedException e) {
                System.out.println("Interrupted when connected to server on port " + preferredPort);
                connected = false;
            } catch (ExecutionException e) {
                e.printStackTrace();
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

    /**
     * Checks if there is a server on the port
     * @param port the port number to be checked
     * @return true if the port is busy
     */
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

    /**
     * Listens for data on udpsocket. Make sure it is initialized, because it won't be checked for preformance purposes.
     * @return the data receive from Go.
     */
    private String udpReceive() throws IOException {
        udpSocket.receive(receivePacket);
        return new String(receivePacket.getData());
    }

    private void feedback(BufferedReader fromServer) {

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

    /**
     * Changes the COM port with which Gobot connects to sphero
     * @param port The port, including COM
     */
    public void setComPort(String port) {
        try {
            if(outToServer != null)
                outToServer.writeBytes(port + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            outToServer.writeBytes("Connect\n");
            System.out.println("stop");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
