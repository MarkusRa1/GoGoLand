package uib.bamboozle.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class tcpClient {
    public static void main(String[] args) throws Exception {
        //getData(9001);
    }

//    public static void getData(int port) throws IOException {
//        String sentence;
//        String modifiedSentence;
//        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//        Socket clientSocket = new Socket("localhost", port);
//        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//
//        sentence = inFromUser.readLine();
//        outToServer.writeBytes(sentence + '\n');
//        while (true) {
//            modifiedSentence = inFromServer.readLine();
//            System.out.println(modifiedSentence);
//
//        }
//    }
}

