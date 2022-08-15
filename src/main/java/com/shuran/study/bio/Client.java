package com.shuran.study.bio;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = null;
            try {
                input = reader.readLine();
                sendToServer(socket,input);
                if(input.equals("quit")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendToServer(Socket socket,String input) throws IOException {
        if(!socket.isOutputShutdown()){
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(input+"\n");
            writer.flush();
        }
    }
}
